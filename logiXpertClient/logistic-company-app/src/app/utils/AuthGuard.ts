import { inject } from '@angular/core';
import {CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
  
    const userEmail = localStorage.getItem('userEmail'); 
    if (!userEmail) {
      router.navigate(['/login']); 
      return false;
    }
    return true; 
  };