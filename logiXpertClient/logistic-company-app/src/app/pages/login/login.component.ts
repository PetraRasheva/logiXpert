import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ErrorHandlerService } from '../../services/error-handler.service';
import { CommonModule } from '@angular/common';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup;
  formSubmitted = false;
  showPassword = false; 

  errorMessage: string | null = null; 

  constructor(
    private formBuilder: FormBuilder, private userService: UserService, private router: Router, private errorHandler: ErrorHandlerService, private messageService: MessageService) {

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]], 
      password: ['', [Validators.required]], 
    });
  }

  login(): void {
    this.formSubmitted = true;
  
    if (this.loginForm.valid) {
      const credentials = this.loginForm.value;
  
      this.userService.login(credentials).subscribe({
        next: () => {
          const user = this.userService.currentUser;
          console.log('Logged in as:', user?.email);
          console.log('Roles:', user?.roles);

          if (user){
            const userName = user.name || 'User'; 
            this.messageService.setMessage(`Welcome, ${userName}!`, 'success'); 
          }
          
          if (user?.roles.includes('ADMIN')) {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/home']);
          }
        },

        error: (errorResponse) => {
          if (errorResponse.error && errorResponse.error.message) {
            const cleanMessage =  this.errorHandler.extractErrorMessage(errorResponse.error.message);
            this.messageService.setMessage(cleanMessage, 'error');
  
            this.errorHandler.assignBackendErrors(
              { password: cleanMessage }, 
              this.loginForm
            );
          } else {
            console.error('Unexpected error:', errorResponse);
            this.messageService.setMessage('An unexpected error occurred.', 'error');
          }
        },
      });
    } else {
      console.warn('Login form is invalid');
      this.messageService.setMessage('Please fill out the form correctly.', 'error');
    }
  }


  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

}