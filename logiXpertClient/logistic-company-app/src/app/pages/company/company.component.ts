import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../types/company';
import { CommonModule } from '@angular/common';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-company',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})

export class CompanyComponent implements OnInit {
  companyForm: FormGroup;
  revenue: number | null = null;
  startDate: string | null = null;
  endDate: string | null = null;
  role: string | null = null;

  constructor(private fb: FormBuilder, private companyService: CompanyService, private messageService: MessageService) {
    this.companyForm = this.fb.group({
      id: [{ value: null, disabled: true }],
      name: ['', Validators.required],
      baseCapital: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    const userJson = localStorage.getItem('[user]');
    if (!userJson) {
      console.error('User data not found in localStorage');
      return;
    }
  
    const user = JSON.parse(userJson);
    const roles = user.roles || [];
  
    this.role = roles.includes('ADMIN') ? 'ADMIN' : roles.includes('CLIENT') ? 'CLIENT' : null;
    console.log('Role:', this.role);

    if (this.role === 'ADMIN') {
      this.loadCompany();
    this.loadTotalRevenue();
    }  
  }

  loadCompany(): void {
    this.companyService.getCompanyById(1).subscribe((company: Company) => {
      this.companyForm.patchValue(company);
    });
  }

  loadTotalRevenue(): void {
    this.companyService.getTotalRevenue(1).subscribe((revenue: number) => {
      this.revenue = revenue;
    });
  }

  loadRevenueByDateRange(): void {
    if (this.startDate && this.endDate) {
      this.companyService.getDateRevenue(1, this.startDate, this.endDate).subscribe((revenue: number) => {
        this.revenue = revenue;
      });
    }
  }

  resetRevenue(): void {
    this.startDate = null;
    this.endDate = null;
    this.loadTotalRevenue();
  
    const startDateInput = document.querySelector('input[type="date"][id="start-date"]') as HTMLInputElement;
    const endDateInput = document.querySelector('input[type="date"][id="end-date"]') as HTMLInputElement;
  
    if (startDateInput) startDateInput.value = '';
    if (endDateInput) endDateInput.value = '';
  }

  updateStartDate(event: Event): void {
    const input = event.target as HTMLInputElement;
    const rawDate = input?.value || null;
  
    this.startDate = rawDate ? `${rawDate} 00:00:00` : null; 
    if (this.startDate && this.endDate) {
      this.loadRevenueByDateRange();
    }
  }

  updateEndDate(event: Event): void {
    const input = event.target as HTMLInputElement;
    const rawDate = input?.value || null;
  
    this.endDate = rawDate ? `${rawDate} 23:59:59` : null; 
    if (this.startDate && this.endDate) {
      this.loadRevenueByDateRange();
    }
  }

  updateCompany(): void {
    if (this.companyForm.valid) {
      const updatedCompany: Company = this.companyForm.getRawValue();
      this.companyService.updateCompany(updatedCompany).subscribe(() => {
        this.messageService.setMessage('Company updated successfully!', 'success');
      });
    }
  }
}