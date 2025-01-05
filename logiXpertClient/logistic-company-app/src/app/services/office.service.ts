import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Office } from '../types/office';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  constructor(private http: HttpClient) { }

    addOffice(office: Office): Observable<Office> {
      return this.http.post<Office>(`${environment.apiUrl}/office/add`, office, {
        withCredentials: true,
      });
    }
  
    updateOffice(office: Office): Observable<Office> {
      return this.http.put<Office>(`${environment.apiUrl}/office/update`, office, {
        withCredentials: true,
      });
    }
  
    deleteOffice(id: number): Observable<void> {
      return this.http.delete<void>(`${environment.apiUrl}/office/delete/${id}`, {
        withCredentials: true,
      });
    }
}
