import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Credentials } from '../types/credentials';
import { environment } from '../../environments/environment';
import { User } from '../types/user';
import { SignUp } from '../types/signup';

@Injectable({
  providedIn: 'root'
})

export class UserService {
  USER_KEY = '[user]';
  user: User | undefined;

  constructor(private http: HttpClient) {
    this.user = JSON.parse(localStorage.getItem(this.USER_KEY) || 'null');
  }

  login(credentials: Credentials): Observable<User> {
    return new Observable((observer) => {
      this.http.post<User>(`${environment.apiUrl}/api/auth/login`, credentials, { withCredentials: true }).subscribe({
        next: (user) => {
          this.user = user; 
          localStorage.setItem(this.USER_KEY, JSON.stringify(user)); 
          observer.next(user);
          observer.complete();
        },
        error: (err) => {
          observer.error(err);
        },
      });
    });
  }
  
  signUp(signUpDto: SignUp): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/api/auth/register`, signUpDto).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMsg = 'An error occurred during registration.';
        if (error.error instanceof ErrorEvent) {
          errorMsg = `Client Error: ${error.error.message}`;
        } else {
          errorMsg = `Server Error: ${error.status} - ${error.message}`;
        }
        console.error('Registration failed:', errorMsg);
        return throwError(() => new Error(errorMsg));
      })
    );
  }


  logout(): Observable<void> {
    return this.http.post<void>(`${environment.apiUrl}/api/auth/logout`, null, {
      withCredentials: true,
      responseType: 'text' as 'json',
    }).pipe(
      tap(() => {
        this.user = undefined; 
        localStorage.removeItem(this.USER_KEY); 
        console.log('User logged out successfully');
      }),
      catchError((err) => {
        console.error('Logout failed:', err);
        return throwError(() => new Error('Failed to logout.'));
      })
    );
  }

  get isLogged(): boolean {
    return !!this.user; 
  }


  get currentUser(): User | null {
    const userJson = localStorage.getItem(this.USER_KEY);
    return userJson ? JSON.parse(userJson) : null;
  }
}