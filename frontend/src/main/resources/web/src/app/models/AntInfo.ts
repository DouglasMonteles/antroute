import { Node } from "./GraphNode";

export interface AntInfo {
  label: string;
  actualNode: Node | null;
  initialNode: Node;
  nextNode: Node | null;
  pathFound: Node[];
  tabuList: Node[];
};