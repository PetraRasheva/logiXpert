import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Message {
  text: string;
  type: 'success' | 'error';
}

@Injectable({ providedIn: 'root' })
export class MessageService {
  private message = new BehaviorSubject<Message | null>(null);

  setMessage(text: string, type: 'success' | 'error' = 'success') {
    this.message.next({ text, type });
    setTimeout(() => this.message.next(null), 4000); 
  }

  getMessage(): Observable<Message | null> {
    return this.message.asObservable();
  }
}