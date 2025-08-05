import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { AntInfo } from 'app/models/AntInfo';
import { Node } from 'app/models/GraphNode';
import { GraphResult } from 'app/models/GraphResult';
import { GraphEdge, GraphService } from 'app/services/graph.service';
import { WebSocketService } from 'app/services/web-socket.service';
import { randHexColor } from 'app/utils/RandomUtils';
import cytoscape from 'cytoscape';
import { environment } from 'environments/environment.development';
import { Message } from 'stompjs';
import { GraphResultPipe } from './graph-result-pipe';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-ant-graph',
  imports: [
    MatCardModule,
    GraphResultPipe,
    DecimalPipe,
  ],
  templateUrl: './ant-graph.html',
  styleUrl: './ant-graph.css'
})
export class AntGraph implements OnInit {

  private ONE_SECOND_MILLISECONDS: number = 1000;

  private cy: cytoscape.Core | null = null;

  result: GraphResult = {
    shortestPath: [],
    shortestDistance: 0
  };

  constructor(
    private _graphService: GraphService,
    private _wsService: WebSocketService,
    private _route: ActivatedRoute,
  ) { }

  ngOnInit() {
    const antSpeed = this._route.snapshot.queryParams["antSpeed"];

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

        this.cy = this._graphService.generateGraph(container, nodes, edges);

        const url = `${environment.wsUrl}/ant-route-updates`;
        const headers = {};

        this._wsService.handleConnection(url, headers, {
          destination: "/topic/ants/updates",
          callback: (message: Message) => {
            const body: AntInfo = JSON.parse(message.body) as AntInfo;

            const {
              label,
              initialNode,
              pathFound,
            } = body;

            if (this.cy == null) return;

            const graphInitialNode = this.cy.getElementById(`${initialNode.name}`);

            console.log("graphInitialNode")
            console.log(graphInitialNode)

            const ant = this.cy.getElementById(`${label}`);

            if (ant.length <= 0) {
              const antNode = this.cy.add({
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
                  x: graphInitialNode.position().x,
                  y: graphInitialNode.position().y,
                }
              });

              this._moveAnt(antNode, pathFound, antSpeed);
            } else {
              this._moveAnt(ant, pathFound, antSpeed);
            }

          },
        }, (error) => {
          console.log("Web socket error:");
          console.log(error);
        });

        this._wsService.handleConnection(url, headers, {
          destination: "/topic/graph/result",
          callback: (message: Message) => {
            const body: GraphResult = JSON.parse(message.body) as GraphResult;
            this.result = body;
          },
        }, (error) => {
          console.log("Web socket error:");
          console.log(error);
        });
      }
    });
  }

  private _moveAnt(antNode: cytoscape.CollectionReturnValue, path: Node[], antSpeed: number): void {
    path.forEach((node, index) => {
      const targetNode = this.cy?.getElementById(`${node.name}`);

      if (targetNode) {
        // Animation for ant move in the line for x seconds
        antNode.animate({
          position: {
            x: targetNode.position().x,
            y: targetNode.position().y,
          },
          // In the fist node path, ant already starts in a node position, don't need to wait for 15s
          duration: index == 0 ? 0 : this.ONE_SECOND_MILLISECONDS * antSpeed,
          easing: "linear",
        });

        // Update the position of ant at each target node from the path founded.
        antNode.position({
          x: targetNode.position().x,
          y: targetNode.position().y,
        });
      }
    });
  }

}
