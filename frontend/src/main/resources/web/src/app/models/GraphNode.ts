interface Position {
  x: number;
  y: number;
}

interface Node {
  name: string;
  position: Position;
}

export default interface GraphNode {
  node: Node;
  edges: string[];
};