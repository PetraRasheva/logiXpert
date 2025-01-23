import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { OfficeService } from '../../services/office.service';
import { Office } from '../../types/office';
import { CommonModule } from '@angular/common';
import { CompanyService } from '../../services/company.service';
import { MessageService } from '../../services/message.service';
import { fade, slideFade } from '../../animations/animations';


@Component({
  selector: 'app-office',
  templateUrl: './office.component.html',
  styleUrls: ['./office.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  animations: [fade, slideFade]
})
export class OfficeComponent implements OnInit {
  offices: Office[] = [];
  isAddOfficeModalOpen: boolean = false;
  isEditOfficeModalOpen: boolean = false;
  officeForm: FormGroup;
  selectedOfficeId: number | null = null; 
  isDeleteModalOpen: boolean = false;
  selectedOffice: Office | null = null;
  role: string | null = null;

  constructor(private officeService: OfficeService, private companyService: CompanyService, private messageService: MessageService, private fb: FormBuilder) {
    this.officeForm = this.fb.group({
      name: ['', [Validators.required]],
      address: ['', [Validators.required]],
      companyName: [{ value: 'logiXpert', disabled: true }]
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
     this.loadOffices();
    }
  }
 
  loadOffices(): void {
    this.companyService.getAllOffices().subscribe((offices) => {
      this.offices = offices;
    });
  }

  openAddOfficeModal(): void {
    this.isAddOfficeModalOpen = true;
    this.officeForm.reset();
    this.officeForm.patchValue({ companyName: 'logiXpert' }); 
  }

  openEditOfficeModal(office: Office): void {
    this.selectedOfficeId = office.id; 
    this.officeForm.patchValue({
      name: office.name,
      address: office.address,
      companyName: office.companyName
    });
    this.isEditOfficeModalOpen = true; 
  }

  openDeleteOfficeModal(office: Office): void {
    this.selectedOffice = office; 
    this.isDeleteModalOpen = true; 
  }

  closeAddOfficeModal(): void {
    this.isAddOfficeModalOpen = false;
  }

  closeEditOfficeModal(): void {
    this.isEditOfficeModalOpen = false;
  }

  closeDeleteModal(): void {
    this.isDeleteModalOpen = false; 
    this.selectedOffice = null; 
  }

  submitAddOffice(): void {
    if (this.officeForm.valid) {
      const office = {
        ...this.officeForm.getRawValue(), 
      };
      this.officeService.addOffice(office).subscribe(() => {
        this.messageService.setMessage('Office created successfully!', 'success');
        this.loadOffices();
        this.closeAddOfficeModal();
      });
    }
  }

  submitEditOffice(): void {
    if (this.officeForm.valid) {
      const updatedOffice: Office = {
        id: this.selectedOfficeId, 
        ...this.officeForm.getRawValue() 
      };
  
      this.officeService.updateOffice(updatedOffice).subscribe(() => {
        this.loadOffices(); 
        this.closeEditOfficeModal(); 
        this.messageService.setMessage('Office updated successfully!', 'success'); 
      }, (error) => {
        console.error('Error updating office:', error);
        this.messageService.setMessage('Error updating office.', 'error'); 
      });
    }
  }

  confirmDeleteOffice(): void {
    if (this.selectedOffice) {
      this.officeService.deleteOffice(this.selectedOffice.id).subscribe(() => {
        this.loadOffices(); 
        this.messageService.setMessage('Office deleted successfully!', 'success'); 
        this.closeDeleteModal(); 
      }, (error) => {
        console.error('Error deleting office:', error);
        this.messageService.setMessage('Error deleting office.', 'error'); 
      });
    }
  }
  
}