import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Shipment } from '../types/shipment';
import { environment } from '../../environments/environment';
import { EmployeeDetails } from '../types/employeeDetails';

@Injectable({
  providedIn: 'root'
})
export class OfficeEmployeeService {

  constructor(private http: HttpClient) {}

  addEmployee(employee: EmployeeDetails): Observable<EmployeeDetails> {
    return this.http.post<EmployeeDetails>(`${environment.apiUrl}/employee/add`, employee, {
      withCredentials: true,
    });
  }

  getShipmentsByEmployeeId(employeeId: number): Observable<Shipment[]> {
    return this.http.get<Shipment[]>(`${environment.apiUrl}/shipment/employee/${employeeId}/shipments`, {
      withCredentials: true,
    });
  }

  deleteEmployeeById(employeeId: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/employee/delete/${employeeId}`, {
      withCredentials: true,
    });
  }
}