import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { AntInfo } from 'app/models/AntInfo';
import { GraphEdge, GraphService } from 'app/services/graph.service';
import { WebSocketService } from 'app/services/web-socket.service';
import { randHexColor } from 'app/utils/RandomUtils';
import cytoscape from 'cytoscape';
import { environment } from 'environments/environment.development';
import { Message } from 'stompjs';

@Component({
  selector: 'app-ant-graph',
  imports: [],
  templateUrl: './ant-graph.html',
  styleUrl: './ant-graph.css'
})
export class AntGraph implements OnInit {

  constructor(
    private _graphService: GraphService,
    private _wsService: WebSocketService,
  ) { }

  ngOnInit() {
    const container = document.getElementById("cy");
    const nodes: string[] = [];
    const edges: GraphEdge[] = [];

    this._graphService.graphNodes().subscribe({
      next: (graphNodes) => {
        graphNodes.forEach(graphNode => {
          const { name } = graphNode.node;
          nodes.push(name);

          graphNode.edges.forEach(edge => {
            edges.push({
              source: name,
              target: edge,
            });
          });
        });

        const cy = this._graphService.generateGraph(container, nodes, edges);

        const url = `${environment.wsUrl}/ant-route-updates`;
        const headers = {};

        this._wsService.handleConnection(url, headers, {
          destination: "/topic/ants/updates",
          callback: (message: Message) => {
            const body: AntInfo = JSON.parse(message.body) as AntInfo;

            const {
              label,
              initialNode,
              nextNode,
            } = body;

            if (!nextNode) return;

            console.log("NODES:")
            console.log(cy.nodes().map(it => it.id()))

            const initialNodeProps = cy.getElementById(initialNode.name);

            if (cy.getElementById(`${label}`).length === 0) {
              console.log("INSERINDO");
              
              const { x, y } = initialNodeProps.position();

              cy.add({
                group: 'nodes',
                data: {
                  id: `${label}`,
                },
                style: {
                  "width": 10,
                  "height": 10,
                  "font-size": "8pt",
                  "background-color": randHexColor()
                },
                position: {
                  x,
                  y,
                },
              });
            } else {
              console.log("ATUALIZANDO");
              
              const ant = cy.getElementById(`${label}`);
              const node = cy.getElementById(nextNode.name);
              const { x, y } = node.position();

              ant.position({
                x,
                y,
              });
            }
          },
        }, (error) => {
          console.log("Web socket error:");
          console.log(error);
        });
      }
    });
  }

}
