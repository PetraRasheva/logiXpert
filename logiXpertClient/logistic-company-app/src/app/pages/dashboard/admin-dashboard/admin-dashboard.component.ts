import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Employee } from '../../../types/еmployee';
import { CompanyService } from '../../../services/company.service';
import { CourierService } from '../../../services/courier.service';
import { EmployeeDetails } from '../../../types/employeeDetails';
import { Office } from '../../../types/office';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { fade, slideFade } from '../../../animations/animations';
import { Shipment } from '../../../types/shipment';
import { OfficeEmployeeService } from '../../../services/office-employee.service';
import { ShipmentService } from '../../../services/shipment.service';
import { emailValidator } from '../../../utils/email-validator';
import { passwordValidator } from '../../../utils/password-validator';
import { ShipmentsComponent } from '../../shipments/shipments.component';
import { ClickOutsideDirective } from '../../../utils/ClickOutsideDirective';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ShipmentsComponent, ClickOutsideDirective],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
  animations: [fade, slideFade]
})
export class AdminDashboardComponent implements OnInit{
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  offices: Office[] = [];
  shipments: Shipment[] = []; 
  showAssignedPackages: boolean = false;
  selectedRole: string = 'ALL';
  selectedEmployeeName: string = '';
  selectedEmployee: EmployeeDetails | null = null;
  showAssignPackagesModal: boolean = false;
  unassignedShipments: Shipment[] = [];
  assignedShipments: Shipment[] = [];
  selectedCourierId: number | null = null;
  selectedCourierName: string = '';
  showHireEmployeeModal: boolean = false;
  formSubmitted: boolean = false;

  employeeForm: FormGroup;
  showElement = false;
  showTable = true;
  isDropdownOpen = false;
  selectedOption = 'Employees';

  constructor(private companyService: CompanyService, private courierService: CourierService, private fb: FormBuilder, private officeEmployeeService: OfficeEmployeeService, private shipmentService: ShipmentService) {
    this.employeeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, emailValidator()]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10,15}$/)]],
      salary: ['', [Validators.required]],
      officeName: ['', [Validators.required]],
      role: ['', [Validators.required]],
      vehicleId: [''], 
      companyName: [''], 
      password: ['', [Validators.required, passwordValidator()]], 
    });
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.loadOffices();
  }

  setPasswordValidators(required: boolean): void {
    const passwordControl = this.employeeForm.get('password');
  
    if (required) {
      passwordControl?.setValidators([Validators.required, Validators.minLength(8)]);
    } else {
      passwordControl?.clearValidators(); 
    }
  
    passwordControl?.updateValueAndValidity(); 
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

  loadShipmentsForEmployee(employeeId: number, employeeName: string): void {
    this.selectedEmployeeName = employeeName;
    this.officeEmployeeService.getShipmentsByEmployeeId(employeeId).subscribe({
      next: (data) => {
        this.shipments = data;
        this.showAssignedPackages = true;
        console.log('Loaded shipments:', this.shipments);
      },
      error: (error) => {
        console.error('Error loading shipments:', error);
      }
    });
  }

  
  loadShipmentsForCourier(courierId: number): void {
    this.selectedCourierId = courierId;

    this.shipmentService.getUnassignedShipmentsForCourier(courierId).subscribe({
      next: (data) => {
        this.unassignedShipments = data;
      },
       error: (error) => {
        console.error('Error loading unassigned shipments:', error);
      }
    });

    this.shipmentService.getShipmentsByCourierId(courierId).subscribe({
      next: (data) => {
        this.assignedShipments = data;
      },
      error: (error) => {
        console.error('Error loading assigned shipments:', error);
      }
    });
  
    this.showAssignPackagesModal = true;
  }

  assignShipmentToCourier(trackingNumber: string): void {
    if (this.selectedCourierId !== null) {
      this.shipmentService.assignShipmentToCourier(trackingNumber, this.selectedCourierId).subscribe({
        next: () => {
          alert('Shipment assigned successfully!');
          
          const shipment = this.unassignedShipments.find(s => s.trackingNumber === trackingNumber);
          if (shipment) {
            this.unassignedShipments = this.unassignedShipments.filter(s => s.trackingNumber !== trackingNumber);
            this.assignedShipments.push(shipment);
          }
        },
        error: (error) => {
          console.error('Error assigning shipment:', error);
        }
      });
    } else {
      console.error('Selected courier ID is null.');
    }
  }
  
  unassignShipment(trackingNumber: string): void {
    this.shipmentService.unassignShipmentFromCourier(trackingNumber).subscribe({
      next: () => {
        alert('Shipment unassigned successfully!');
        
        const shipment = this.assignedShipments.find(s => s.trackingNumber === trackingNumber);
        if (shipment) {
          this.assignedShipments = this.assignedShipments.filter(s => s.trackingNumber !== trackingNumber);
          this.unassignedShipments.push(shipment);
        }
      },
      error: (error) => {
        console.error('Error unassigning shipment:', error);
      }
    });
  }

  filterByRole(role: string, label: string): void {
    this.selectedRole = role;
    this.selectedOption = label;
  
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
  
            this.setPasswordValidators(false);
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

  hireEmployee(): void {
    this.formSubmitted = true;
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.value;
  
      const newEmployee = {
        name: formValue.name,
        phone: formValue.phone,
        email: formValue.email,
        salary: formValue.salary,
        officeName: formValue.officeName,
        password: formValue.password, 
        vehicleId: formValue.role === 'COURIER' ? formValue.vehicleId : null,
        id: 0, 
        companyName: '',
        role: formValue.role,
      };
  
      console.log('New employee data:', newEmployee); 
  
      if (newEmployee.role === 'COURIER') {
        this.courierService.addCourier(newEmployee).subscribe({
          next: () => {
            alert('Courier hired successfully!');
            this.closeHireEmployeeModal();
            this.loadEmployees();
          },
          error: (error) => {
            console.error('Error hiring courier:', error);
            alert('Failed to hire courier. Please try again.');
          }
        });
      } else if (newEmployee.role === 'OFFICE_EMPLOYEE') {
        this.officeEmployeeService.addEmployee(newEmployee).subscribe({
          next: () => {
            alert('Office employee hired successfully!');
            this.closeHireEmployeeModal();
            this.loadEmployees();
          },
          error: (error) => {
            console.error('Error hiring office employee:', error);
            alert('Failed to hire office employee. Please try again.');
          }
        });
      } else {
        alert('Invalid role selected. Please choose a valid role.');
      }
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }

  openHireEmployeeModal(): void {
    this.selectedEmployee = null; 
    this.employeeForm.reset(); 
    this.setPasswordValidators(true); 
    this.showHireEmployeeModal = true; 
  }

  closeModal(): void {
    const modal = document.getElementById('editUserModal');
    if (modal) modal.classList.add('hidden');
    this.selectedEmployee = null;
    this.showElement = false;
  }

  closeAssignedPackagesModal(): void {
    this.showAssignedPackages = false;
    this.shipments = [];
  }

  closeHireEmployeeModal(): void {
    this.showHireEmployeeModal = false;
    this.employeeForm.reset();
  }

  closeAssignPackagesModal(): void {
    this.showAssignPackagesModal = false;
    this.unassignedShipments = [];
    this.assignedShipments = [];
  }

  saveEmployeeDetails(): void {
    this.formSubmitted = true;
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.value;
  
      const updatedEmployee = {
        ...this.selectedEmployee,
        ...formValue,
      };

      if (!updatedEmployee.password) {
        delete updatedEmployee.password;
      }
  
      if (updatedEmployee.role === 'COURIER') {
        this.courierService.updateCourierDetails(updatedEmployee).subscribe({
          next: () => {
            alert('Courier details updated successfully!');
            this.closeModal();
            this.loadEmployees();
          },
          error: (backendErrors) => {
            const errorMessage = backendErrors.error?.message || 'An unexpected error occurred.';
            console.error('Error updating courier:', errorMessage);
            this.assignBackendErrors(backendErrors.error, this.employeeForm); 
          },
        });
      } else if (updatedEmployee.role === 'OFFICE_EMPLOYEE') {
        this.companyService.updateEmployeeDetails(updatedEmployee).subscribe({
          next: () => {
            alert('Office employee details updated successfully!');
            this.closeModal();
            this.loadEmployees();
          },
          error: (backendErrors) => {
            const errorMessage = backendErrors.error?.message || 'An unexpected error occurred.';
            console.error('Error updating office employee:', errorMessage);
            this.assignBackendErrors(backendErrors.error, this.employeeForm); 
          },
        });
      } else {
        alert('Unknown role: ' + updatedEmployee.role);
      }
    } else {
      alert('Please fill in all required fields correctly.');
    }
  }

  deleteEmployee(employee: Employee): void {
    if (confirm(`Are you sure you want to delete ${employee.name}?`)) {
      if (employee.roles.includes('COURIER')) {
        this.courierService.deleteCourierById(employee.id).subscribe({
          next: () => {
            alert('Courier deleted successfully!');
            this.loadEmployees(); 
          },
          error: (error) => {
            console.error('Error deleting courier:', error);
            alert('Failed to delete the courier. Please try again.');
          }
        });
      } else if (employee.roles.includes('OFFICE_EMPLOYEE')) {
        this.officeEmployeeService.deleteEmployeeById(employee.id).subscribe({
          next: () => {
            alert('Office employee deleted successfully!');
            this.loadEmployees();
          },
          error: (error) => {
            console.error('Error deleting office employee:', error);
            alert('Failed to delete the office employee. Please try again.');
          }
        });
      } else {
        alert('Unknown role. Cannot delete this employee.');
      }
    }
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  assignBackendErrors(backendErrors: any, formGroup: FormGroup): void {
    for (const [field, errorMessage] of Object.entries(backendErrors)) {
      const control = formGroup.get(field);
      if (control) {
        control.setErrors({ backend: errorMessage });
      }
    }
  }

  getPasswordErrors(): string {
    const controlErrors = this.employeeForm.get('password')?.errors;
    if (!controlErrors) return '';
  
    if (controlErrors['backend']) {
      return controlErrors['backend']; 
    }

    if (controlErrors['required']) {
      return 'Password is required.';
    }

    if (controlErrors['minlength']) {
      return `Password must be at least ${controlErrors['minlength'].requiredLength} characters long.`;
    }
  
    return ''; 
  }
}