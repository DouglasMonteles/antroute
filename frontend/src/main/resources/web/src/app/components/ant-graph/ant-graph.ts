import { Component, OnInit } from '@angular/core';
import { GraphEdge, GraphService } from 'app/services/graph.service';

@Component({
  selector: 'app-ant-graph',
  imports: [],
  templateUrl: './ant-graph.html',
  styleUrl: './ant-graph.css'
})
export class AntGraph implements OnInit {

  constructor(
    private _graphService: GraphService
  ) {}

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

        this._graphService.generateGraph(container, nodes, edges);
      }
    });
  }

  public getNodes(): void {
    
  }

}
