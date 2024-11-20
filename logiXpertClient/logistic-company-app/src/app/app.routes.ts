import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CalculatePriceComponent } from './pages/calculate-price/calculate-price.component';
import { CreateParcelLabelComponent } from './pages/create-parcel-label/create-parcel-label.component';

export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: '/home' }, 
    { path: 'home', component: HomeComponent }, 
    {path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)},
    {path: 'register', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)},
    {path: 'profile', loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent)},
    {path: 'calculate-price', component: CalculatePriceComponent},
    {path: 'create-parcel-label', component: CreateParcelLabelComponent}
];
