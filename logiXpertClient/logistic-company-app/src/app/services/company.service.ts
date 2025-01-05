import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../types/еmployee';
import { environment } from '../../environments/environment';
import { EmployeeDetails } from '../types/employeeDetails';
import { Office } from '../types/office';
import { Company } from '../types/company';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http: HttpClient) {}

  getCompanyById(id: number): Observable<Company> {
    return this.http.get<Company>(`${environment.apiUrl}/company/find/${id}`, {
      withCredentials: true,
    });
  }

  updateCompany(company: Company): Observable<Company> {
    return this.http.put<Company>(`${environment.apiUrl}/company/update`, company, {
      withCredentials: true,
    });
  }

  getDateRevenue(companyId: number, startDate: string, endDate: string): Observable<number> {
    return this.http.get<number>(
      `${environment.apiUrl}/shipment/revenueByDateRange?companyId=${companyId}&startDate=${startDate}&endDate=${endDate}`, {
        withCredentials: true,
      }
    );
  }

  getTotalRevenue(companyId: number): Observable<number> {
    return this.http.get<number>(`${environment.apiUrl}/shipment/totalRevenue?companyId=${companyId}`, {
      withCredentials: true,
    }
    );
  }

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

  updateEmployeeDetails(employee: EmployeeDetails): Observable<EmployeeDetails> {
    return this.http.put<EmployeeDetails>(`${environment.apiUrl}/employee/admin/update`, employee, {
      withCredentials: true,
    });
  }

}
