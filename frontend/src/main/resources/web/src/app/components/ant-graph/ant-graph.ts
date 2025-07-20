import { AfterViewInit, Component, OnInit } from '@angular/core';
import cytoscape from 'cytoscape'; 
import euler from 'cytoscape-euler';

cytoscape.use(euler);

@Component({
  selector: 'app-ant-graph',
  imports: [],
  templateUrl: './ant-graph.html',
  styleUrl: './ant-graph.css'
})
export class AntGraph implements OnInit {

  constructor() {
    
  }

  ngOnInit(): void {
    cytoscape({
      container: document.getElementById('cy'), // container com id 'cy'

      elements: [
        // Node list
        { data: { id: 'a' }, position: { x: Math.random() * 500, y: Math.random() * 500 } },
        { data: { id: 'b' }, position: { x: Math.random() * 500, y: Math.random() * 500 } },
        { data: { id: 'c' }, position: { x: Math.random() * 500, y: Math.random() * 500 } },
        { data: { id: 'd' }, position: { x: Math.random() * 500, y: Math.random() * 500 } },
        // Node connections
        { data: { id: 'ab', source: 'a', target: 'b' } },
        { data: { id: 'ac', source: 'a', target: 'c' } },
        { data: { id: 'ad', source: 'a', target: 'd' } },
        { data: { id: 'bc', source: 'b', target: 'c' } }
      ],

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
            'width': 2,
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

}
