import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Employee } from '../../../types/еmployee';
import { CompanyService } from '../../../services/company.service';
import { CourierService } from '../../../services/courier.service';
import { EmployeeDetails } from '../../../types/employeeDetails';
import { Office } from '../../../types/office';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { fade } from '../../../animations/animations';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
  animations: [fade]
})
export class AdminDashboardComponent implements AfterViewInit, OnInit{
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  offices: Office[] = [];
  selectedRole: string = 'ALL';
  selectedEmployee: EmployeeDetails | null = null;

  employeeForm: FormGroup;
  showElement = false;

  constructor(private companyService: CompanyService, private courierService: CourierService, private fb: FormBuilder) {
    this.employeeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10,15}$/)]],
      salary: ['', [Validators.required]],
      officeName: ['', [Validators.required]],
      role: ['', [Validators.required]],
      vehicleId: [''], 
      companyName: [''], 
    });
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.loadOffices();
  }

  loadEmployees(): void {
    this.companyService.getAllEmployees().subscribe((data) => {
      console.log('Loaded employees:', data);
      this.employees = data;
      this.filteredEmployees = [...this.employees];
    });
  }

  loadOffices(): void {
    this.companyService.getAllOffices().subscribe({
      next: (data) => {
        console.log('Offices loaded successfully:', data);
        this.offices = data;
      },
      error: (error) => {
        console.error('Error loading offices:', error);
      },
      complete: () => {
        console.log('Office loading complete.');
      }
    });
  }

  filterByRole(role: string): void {
    this.selectedRole = role;
  
    if (role === 'ALL') {
      this.filteredEmployees = [...this.employees]; 
    } else {
      this.filteredEmployees = this.employees.filter((employee) =>
        employee.roles.includes(role)
      );
    }
  }

  showDetails(id: number): void {
    const employee = this.employees.find(emp => emp.id === id);
  
    if (employee) {
      this.selectedRole = employee.roles.includes('COURIER') ? 'COURIER' : 'OFFICE_EMPLOYEE';
  
      if (this.selectedRole === 'COURIER') {
        this.courierService.getCourierDetails(id).subscribe({
          next: (data) => {
            this.selectedEmployee = data;

            this.employeeForm.patchValue({
              name: data.name,
              email: data.email,
              phone: data.phone,
              salary: data.salary,
              officeName: data.officeName,
              role: 'COURIER',
              vehicleId: data.vehicleId,
              companyName: data.companyName,
            });
  
            this.showElement = true; 
          },
          error: (error) => {
            console.error('Error fetching courier details:', error);
          },
        });
      } else if (this.selectedRole === 'OFFICE_EMPLOYEE') {
        this.companyService.getEmployeeDetails(id).subscribe({
          next: (data) => {
            this.selectedEmployee = data;

            this.employeeForm.patchValue({
              name: data.name,
              email: data.email,
              phone: data.phone,
              salary: data.salary,
              officeName: data.officeName,
              role: 'OFFICE_EMPLOYEE',
            });
  
            this.showElement = true; 
          },
          error: (error) => {
            console.error('Error fetching office employee details:', error);
          },
        });
      }
    } else {
      console.error('Employee not found for ID:', id);
    }
  }


  closeModal(): void {
    const modal = document.getElementById('editUserModal');
    if (modal) modal.classList.add('hidden');
    this.selectedEmployee = null;
    this.showElement = false;
  }

  saveEmployeeDetails(): void {
    if (this.employeeForm.valid && this.selectedEmployee) {
      const updatedEmployee = {
        ...this.selectedEmployee,
        ...this.employeeForm.value,
      };
  
      if (updatedEmployee.role === 'COURIER') {
        this.courierService.updateCourierDetails(updatedEmployee).subscribe({
          next: () => {
            console.log('Courier details updated successfully!');
            alert('Courier details updated successfully!');
            this.closeModal();
            this.loadEmployees();
          },
          error: (error) => {
            console.error('Error updating courier details:', error);
            alert('Error updating courier details.');
          },
        });
      } else if (updatedEmployee.role === 'OFFICE_EMPLOYEE') {
        this.companyService.updateEmployeeDetails(updatedEmployee).subscribe({
          next: () => {
            console.log('Office employee details updated successfully!');
            alert('Office employee details updated successfully!');
            this.closeModal();
            this.loadEmployees();
          },
          error: (error) => {
            console.error('Error updating office employee details:', error);
            alert('Error updating office employee details.');
          },
        });
      } else {
        console.error('Unknown role:', updatedEmployee.role);
        alert('Unknown role: ' + updatedEmployee.role);
      }
    } else {
      alert('Please fix the errors in the form before saving.');
    }
  }

  ngAfterViewInit(): void {
    const dropdownButton = document.getElementById('dropdownActionButton');
    const dropdownMenu = document.getElementById('dropdownAction');

    if (dropdownButton && dropdownMenu) {
      dropdownButton.addEventListener('click', () => {
        dropdownMenu.classList.toggle('hidden');
      });
    }
  }
}