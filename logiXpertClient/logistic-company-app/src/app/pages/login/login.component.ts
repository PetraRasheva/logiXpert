import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Credentials } from '../../types/credentials';
import { User } from '../../types/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  credentials: Credentials = {
    email: '',
    password: '',
  };

  errorMessage: string | null = null; 

  constructor(private userService: UserService, private router: Router) {}

  login() {
    this.userService.login(this.credentials).subscribe({
      next: (response: User) => {
        localStorage.setItem('userEmail', response.email);
        localStorage.setItem('userRoles', JSON.stringify(response.roles)); 
  
        console.log('Logged in as:', response.email);
        console.log('Roles:', response.roles);
  
        if (response.roles.includes('ADMIN')) {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
      },
    });
  }

}