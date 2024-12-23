import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EmployeeDetails } from '../types/employeeDetails';

@Injectable({
  providedIn: 'root'
})
export class CourierService {

    constructor(private http: HttpClient) {}
    
    getCourierDetails(id: number): Observable<EmployeeDetails> {
      return this.http.get<EmployeeDetails>(`${environment.apiUrl}/courier/find/${id}`, {
        withCredentials: true,
      });
    }
    
    updateCourierDetails(courier: any): Observable<any> {
      return this.http.put(`${environment.apiUrl}/courier/update-admin`, courier, {
        withCredentials: true,
      });
    }

}
