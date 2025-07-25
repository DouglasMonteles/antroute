export interface Position {
  x: number;
  y: number;
}

export interface Node {
  name: string;
  position: Position;
}

export interface GraphNode {
  node: Node;
  edges: string[];
};