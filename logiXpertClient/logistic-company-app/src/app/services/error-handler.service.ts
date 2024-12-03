import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor() { }
  
  assignBackendErrors(errors: { [key: string]: string }, form: FormGroup): void {
    Object.keys(errors).forEach((key) => {
      const control = form.get(key);
      if (control) {
        control.setErrors({ backend: errors[key] });
      }
    });
  }

  getErrorMessage(controlName: string, form: FormGroup): string | null {
    const control = form.get(controlName);
    if (control && control.errors) {
      if (control.errors['backend']) {
        return control.errors['backend']; 
      } else if (control.errors['required']) {
        return 'This field is required.';
      } else if (control.errors['minlength']) {
        return `Minimum length is ${control.errors['minlength'].requiredLength} characters.`;
      }
    }
  
    return null;
  }

  extractErrorMessage(fullMessage: string): string {
    const separator = ': ';
    if (fullMessage.includes(separator)) {
      return fullMessage.split(separator)[1].trim(); 
    }
    return fullMessage; 
  }
}
