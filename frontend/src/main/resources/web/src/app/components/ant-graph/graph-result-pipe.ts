import { Pipe, PipeTransform } from '@angular/core';
import { Node } from 'app/models/GraphNode';

@Pipe({
  name: 'graphResult'
})
export class GraphResultPipe implements PipeTransform {

  transform(value: Node[], ...args: unknown[]): string {
    return value
      .map(node => node.name)
      .toString()
      .replaceAll(",", " -> ");
  }

}
