import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../types/client';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) {}

  updateClient(client: Client): Observable<Client> {
    return this.http.put<Client>(`${environment.apiUrl}/client/update`, client);
  }
}