import { Injectable } from '@angular/core';
import cytoscape from 'cytoscape'; 
import { randNumber } from "app/utils/RandomUtils";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment';
import GraphNode from 'app/models/GraphNode';

interface GraphEdge {
  source: string;
  target: string;
}

@Injectable({
  providedIn: 'root'
})
export class GraphService {

  constructor(
    private _httpClient: HttpClient
  ) {}

  public graphNodes(): Observable<GraphNode[]> {
    return this._httpClient.get<GraphNode[]>(`http://localhost:8080/graph`);
  }
  
  public generateGraph(container: HTMLElement | null, nodes: string[], connections: GraphEdge[]): void {
    cytoscape({
      container: container,

      elements: this._handleElements(nodes, connections),

      style: [
        {
          selector: 'node',
          style: {
            'background-color': '#0074D9',
            'label': 'data(id)'
          }
        },
        {
          selector: 'edge',
          style: {
            'width': 6,
            'line-color': '#ccc',
            'target-arrow-color': '#ccc',
            'target-arrow-shape': 'triangle'
          }
        }
      ],

      layout: { 
        name: 'preset',
        fit: true,
      },
    });
  }

  private _handleElements(nodes: string[], connections: GraphEdge[]): cytoscape.ElementsDefinition | cytoscape.ElementDefinition[] | Promise<cytoscape.ElementsDefinition> | Promise<cytoscape.ElementDefinition[]> | undefined {
    const graphNodes: cytoscape.NodeDefinition[] = nodes.map(node => {
      const nodeDef: cytoscape.NodeDefinition = {
        data: {
          id: `${node}`,
        },
        position: {
          x: randNumber(), 
          y: randNumber(),
        },
        group: "nodes",
      };

      return nodeDef;
    });

    console.log(graphNodes)

    const graphEdges: cytoscape.EdgeDefinition[] = connections.map(edge => {
      const { source, target } = edge;
      
      const edgeDef: cytoscape.EdgeDefinition = {
        data: {
          id: `${source}${target}`,
          source,
          target,
        },
        group: "edges",
      };

      return edgeDef;
    });

    return [
      // Node list
      ...graphNodes,
      // Node connections
      ...graphEdges
    ];
  }

}
