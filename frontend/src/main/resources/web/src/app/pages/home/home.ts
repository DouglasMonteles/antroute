import { Component } from '@angular/core';
import { NavigationComponent } from "app/components/navigation/navigation.component";
import { AntSimulationFormComponent } from "app/components/ant-simulation-form/ant-simulation-form.component";

@Component({
  selector: 'app-home',
  imports: [
    AntSimulationFormComponent
],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  constructor() {}

}
