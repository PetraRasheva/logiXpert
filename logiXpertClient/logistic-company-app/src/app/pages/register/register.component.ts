import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { SignUp } from '../../types/signup';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  signUpData: SignUp = {
    email: '',
    password: '',
    name: '',
    phone: '', 
  };

  constructor(private userService: UserService, private router: Router) {}

  register() {
    this.userService.signUp(this.signUpData).subscribe({
      next: () => {
        console.log('Successfully registered!');
        this.router.navigate(['/login']); 
      },
      error: (error) => {
        console.error('Registration error:', error);
      },
    });
  }
}