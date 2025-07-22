import { Component, OnInit } from '@angular/core';
import { GraphService } from 'app/services/graph.service';

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

  ngOnInit(): void {
    this.getNodes();
    const container = document.getElementById("cy");
    this._graphService.generateGraph(container, ["a", "b", "c", "d"], [{source: "a", target: "b"}, {source: "a", target: "c"}, {source: "a", target: "d"}, {source: "b", target: "d"}]);
  }

  public getNodes(): void {
    this._graphService.graphNodes().subscribe({
      next: (data) => {
        console.log(data)
      }
    });
  }

}
