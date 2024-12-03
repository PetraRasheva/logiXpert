import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const password = control.value;

    if (!password || password.trim() === '') {
      return { backend: 'Password cannot be null or empty' };
    }

    const errors: string[] = [];

    if (password.length < 8) {
      errors.push('at least 8 characters');
    }

    if (!/[A-Z]/.test(password)) {
      errors.push('an uppercase letter');
    }
    if (!/\d/.test(password)) {
      errors.push('a digit');
    }

    if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
      errors.push('a special character');
    }

    if (errors.length > 0) {
      return { backend: `Password must contain ${errors.join(', ')}` };
    }

    return null;
  };
}