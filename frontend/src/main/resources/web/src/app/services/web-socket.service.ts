import { Injectable } from '@angular/core';
import { Client, Frame, Message, over } from "stompjs";
import SockJsClient from "sockjs-client";

interface Header {
  login?: string;
  passcode?: string;
  host?: string | undefined;
};

interface Topic {
  destination: string,
  callback?: (message: Message) => void,
  headers?: {}
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  public handleConnection(
    url: string,
    headers: Header | Object,
    topicCallback: Topic,
    errorCallback?: (error: Frame | string) => void
  ): Client {
    const stompClient = this._connect(url);
    
    return stompClient.connect(headers, (frame?: Frame) => {
      console.log(frame)
      const { destination, callback, headers } = topicCallback;
      stompClient.subscribe(destination, callback, headers);
    }, errorCallback);
  }

  private _connect(url: string): Client {
    const ws = new SockJsClient(url);
    return over(ws);
  }

}
