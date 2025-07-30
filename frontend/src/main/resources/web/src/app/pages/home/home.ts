import { Component } from '@angular/core';
import { NavigationComponent } from "app/components/navigation/navigation.component";

@Component({
  selector: 'app-home',
  imports: [
    NavigationComponent
],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  constructor() {}

}
