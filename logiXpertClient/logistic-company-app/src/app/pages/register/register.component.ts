import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { ErrorHandlerService } from '../../services/error-handler.service';
import { emailValidator } from '../../utils/email-validator';
import { passwordValidator } from '../../utils/password-validator';
import { MessageService } from '../../services/message.service';
import { slideFade, slideInFromLeft } from '../../animations/animations';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  animations: [slideFade, slideInFromLeft]
})
export class RegisterComponent {
  registerForm: FormGroup;
  formSubmitted = false;
  showPassword = false; 
  showElement = false;

  ngOnInit(): void {
    this.showElement = true;
}
  
  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router, private errorHandler: ErrorHandlerService, private messageService: MessageService) {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email, emailValidator()]],
      password: ['', [Validators.required, passwordValidator()]],
      name: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10,15}$/)]], 
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  register(): void {
    this.formSubmitted = true;
  
    if (this.registerForm.valid) {
      const { email, password, name, phone } = this.registerForm.value;
  
      this.userService.signUp({ email, password, name, phone }).subscribe({
        next: () => {
          this.messageService.setMessage('Registration successful! Please log in.', 'success');
          this.router.navigate(['/login']);
        },
        error: (backendErrors) => {
          const errorMessage = backendErrors.error?.message || 'An unexpected error occurred.';
          this.messageService.setMessage(errorMessage, 'error');
          this.errorHandler.assignBackendErrors(backendErrors.error, this.registerForm);
        },
      });
    } else {
      console.warn('Form is invalid');
      this.messageService.setMessage('Please fill out the form correctly.', 'error');
    }
  }

  getErrorMessage(controlName: string): string | null {
    return this.errorHandler.getErrorMessage(controlName, this.registerForm);
  }

}