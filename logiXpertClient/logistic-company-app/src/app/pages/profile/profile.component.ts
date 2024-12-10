import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { MessageService } from '../../services/message.service';
import { slideFade } from '../../animations/animations';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  animations: [slideFade]
})

export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  role: string = '';
  userId!: number;
  initials: string = '';
  showElement = false;

  constructor(private fb: FormBuilder, private http: HttpClient,  private messageService: MessageService) {}

  ngOnInit(): void {
    this.showElement = true;
    const userJson = localStorage.getItem('[user]');
    if (!userJson) {
      this.messageService.setMessage('User data not found. Please log in again.', 'error');
      console.error('User data not found in localStorage');
      return;
    }

    const user = JSON.parse(userJson); 
    this.role = user.roles?.[0] || ''; 
    this.userId = user.id;

    // Изчисляване на инициалите
  if (user?.name) {
    const nameParts = user.name.split(' ');
    this.initials = nameParts.length >= 2
      ? `${nameParts[0][0]}${nameParts[1][0]}`.toUpperCase()
      : nameParts[0]?.[0]?.toUpperCase() || '';
  } else {
    this.initials = ''; 
  }

    this.initForm({
      name: user.name || '',
      email: user.email || '',
      phone: user.phone || '',
    });
  }

  initForm(userData: { name: string; email: string; phone: string }): void {
    this.profileForm = this.fb.group({
      name: [userData.name, [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: [userData.email, [Validators.required, Validators.email]],
      phone: [userData.phone, [Validators.required, Validators.pattern(/^\d{10,15}$/)]],
    });
  }

  updateProfile(): void {
    if (this.profileForm.invalid) {
      this.messageService.setMessage('Form is invalid. Please correct the errors.', 'error');
      return;
    }

    const apiUrl = `${environment.apiUrl}${this.getApiUrl()}/update`;

    const userJson = localStorage.getItem('[user]');
    if (!userJson) {
      this.messageService.setMessage('User data not found. Please log in again.', 'error');
      return;
    }

    const user = JSON.parse(userJson);
    const updateData = { ...this.profileForm.value, id: user.id };

    this.http.put(apiUrl, updateData, { withCredentials: true }).subscribe({
      next: () => {
         this.messageService.setMessage('Profile updated successfully!', 'success');

        const updatedUser = { ...user, ...this.profileForm.value };
        localStorage.setItem('[user]', JSON.stringify(updatedUser));

        const nameParts = this.profileForm.value.name.split(' ');
        this.initials = nameParts.length >= 2
          ? `${nameParts[0][0]}${nameParts[1][0]}`.toUpperCase()
          : nameParts[0]?.[0]?.toUpperCase() || '';
  
      },
      error: (error) => {
        console.error('Error updating profile:', error);
        const errorMessage = error?.error?.message || 'An unexpected error occurred.';
        this.messageService.setMessage(errorMessage, 'error');
      },
    });
  }

  private getApiUrl(): string {
    switch (this.role) {
      case 'CLIENT':
        return '/client';
      case 'OFFICE_EMPLOYEE':
        return '/employee';
      case 'ADMIN':
        return '/admin';
      default:
        throw new Error('Invalid role!');
    }
  }
}