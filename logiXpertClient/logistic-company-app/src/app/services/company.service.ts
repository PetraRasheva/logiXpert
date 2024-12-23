import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../types/еmployee';
import { environment } from '../../environments/environment';
import { EmployeeDetails } from '../types/employeeDetails';
import { Office } from '../types/office';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http: HttpClient) {}

   getAllOffices(): Observable<Office[]> {
    return this.http.get<Office[]>(`${environment.apiUrl}/office/all`, {
      withCredentials: true,
    });
  }

  addOffice(office: Office): Observable<Office> {
    return this.http.post<Office>(`${environment.apiUrl}/add`, office);
  }

  updateOffice(office: Office): Observable<Office> {
    return this.http.put<Office>(`${environment.apiUrl}/update`, office);
  }

  deleteOffice(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/delete/${id}`);
  }

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${environment.apiUrl}/company/employees`, {
      withCredentials: true, 
    });
  }

  getEmployeeDetails(id: number): Observable<EmployeeDetails> {
    return this.http.get<EmployeeDetails>(`${environment.apiUrl}/employee/find/${id}`, {
      withCredentials: true,
    });
  }

  updateEmployeeDetails(employee: EmployeeDetails): Observable<any> {
    return this.http.put(`${environment.apiUrl}/employee/admin/update`, employee, {
      withCredentials: true,
    });
  }

}
