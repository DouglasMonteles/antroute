import { Component } from '@angular/core';
import { AntGraph } from "app/components/ant-graph/ant-graph";

@Component({
  selector: 'app-simulation',
  imports: [AntGraph],
  templateUrl: './simulation.html',
  styleUrl: './simulation.css'
})
export class Simulation {

}
