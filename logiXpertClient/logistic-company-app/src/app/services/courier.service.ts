import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EmployeeDetails } from '../types/employeeDetails';
import { Courier } from '../types/courier';

@Injectable({
  providedIn: 'root'
})
export class CourierService {

    constructor(private http: HttpClient) {}

    addCourier(courier: EmployeeDetails): Observable<EmployeeDetails> {
      return this.http.post<EmployeeDetails>(`${environment.apiUrl}/courier/add`, courier, {
        withCredentials: true,
      });
    }
    
    getCourierDetails(id: number): Observable<EmployeeDetails> {
      return this.http.get<EmployeeDetails>(`${environment.apiUrl}/courier/find/${id}`, {
        withCredentials: true,
      });
    }

    getAllCouriers(): Observable<Courier[]> {
      return this.http.get<Courier[]>(`${environment.apiUrl}/courier/all`, {
        withCredentials: true,
      });
    }

    updateCourierDetails(courier: Courier): Observable<Courier> {
      return this.http.put<Courier>(`${environment.apiUrl}/courier/update-admin`, courier, {
        withCredentials: true,
      });
    }

    deleteCourierById(courierId: number): Observable<void> {
      return this.http.delete<void>(`${environment.apiUrl}/courier/delete/${courierId}`, {
        withCredentials: true,
      });
    }

}
