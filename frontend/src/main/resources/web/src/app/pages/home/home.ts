import { Component } from '@angular/core';
import { Message } from 'stompjs';
import { JsonPipe } from '@angular/common';
import { environment } from 'environments/environment';
import { WebSocketService } from 'app/services/web-socket.service';
import { AntGraph } from 'app/components/ant-graph/ant-graph';

@Component({
  selector: 'app-home',
  imports: [
    JsonPipe,
    AntGraph,
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  message: string[] = [];
  
  constructor(
    private _wsService: WebSocketService,
  ) {
    const url = `${environment.wsUrl}/ant-route-updates`;
    const headers = {};

    this._wsService.handleConnection(url, headers, {
      destination: "/topic/ants/updates",
    callback: (message: Message) => {
        this.message.push(message.body);
      },
    }, (error) => {
      console.log("Web socket error:");
      console.log(error);
    });
  }

}
