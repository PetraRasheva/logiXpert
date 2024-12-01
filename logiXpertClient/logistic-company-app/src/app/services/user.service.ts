import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credentials } from '../types/credentials';
import { environment } from '../../environments/environment';
import { User } from '../types/user';
import { SignUp } from '../types/signup';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) {}

  login(credentials: Credentials): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/api/auth/login`, credentials, {
      withCredentials: true, 
    });
  }

  signUp(signUpDto: SignUp): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/api/auth/register`, signUpDto);
  }

  logout(): Observable<string> {
    return this.http.post(`${environment.apiUrl}/api/auth/logout`, null, {
      withCredentials: true,
      responseType: 'text', 
    });
  }
}