import { Component, OnInit } from '@angular/core';
import { Shipment } from '../../types/shipment';
import { ShipmentService } from '../../services/shipment.service';
import { ClickOutsideDirective } from '../../utils/ClickOutsideDirective';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ShipmentDetails } from '../../types/shipmentDetails';
import { fade, slideFade } from '../../animations/animations';
import { CourierService } from '../../services/courier.service';
import { Courier } from '../../types/courier';
import { MessageService } from '../../services/message.service';
import { catchError, tap, throwError } from 'rxjs';

@Component({
  selector: 'app-shipments',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ClickOutsideDirective],
  templateUrl: './shipments.component.html',
  styleUrl: './shipments.component.css',
  animations: [fade, slideFade]
})

export class ShipmentsComponent implements OnInit {
  shipments: Shipment[] = [];
  filteredShipments: Shipment[] = [];
  couriers: Courier[] = [];
  isDropdownOpen = false;
  selectedOption = 'Shipments';
  selectedShipment: ShipmentDetails | null = null;
  selectedShipmentDelete: Shipment | null = null;
  isDetailsModalOpen: boolean = false;
  showAssignCourierModal: boolean = false;
  shipmentForm: FormGroup;
  assignedCourier: Courier | null = null;
  showAllCouriers: boolean = true;
  isDeleteModalOpen: boolean = false;

  constructor(private ShipmentService: ShipmentService, private fb: FormBuilder, private courierService: CourierService, private messageService: MessageService) {
    this.shipmentForm = this.fb.group({
      trackingNumber: [{ value: '', disabled: true }],
      weight: ['', Validators.required],
      price: ['', Validators.required],
      destination: ['', Validators.required],
      source: ['', Validators.required],
      deliveryStatus: [{ value: '', disabled: true }],
      shipmentDate: [{ value: '', disabled: true }],
      deliveryDate: [{ value: '', disabled: true }],
    });
  }

  ngOnInit(): void {
    this.loadAllShipments();
  }

  loadAllShipments(label: string = 'All shipments'): void {
    this.ShipmentService.getAllShipments().subscribe((data) => {
      this.shipments = data;
      this.filteredShipments = data;
      this.selectedOption = label; 
    });
    this.closeDropdown();
  }

  loadCouriers(): void {
    this.courierService.getAllCouriers().subscribe((data) => {
      this.couriers = data;
    });
  }

  openEditShipmentModal(trackingNum: string): void {
    this.ShipmentService.getShipmentByTrackingNumber(trackingNum).subscribe(
      (details: ShipmentDetails) => {
        this.selectedShipment = details;
        this.shipmentForm.patchValue({
          trackingNumber: details.trackingNumber,
          weight: details.weight,
          price: details.price,
          destination: details.destination,
          source: details.source,
          deliveryStatus: details.deliveryStatus,
          shipmentDate: details.shipmentDate,
          deliveryDate: details.deliveryDate,
        });
        this.isDetailsModalOpen = true;
      },
      (error) => {
        console.error('Error fetching shipment details:', error);
      }
    );
  }

  saveShipmentDetails(): void {
    if (this.shipmentForm.valid && this.selectedShipment) {
      const updatedShipment = { ...this.selectedShipment, ...this.shipmentForm.value };
      console.log('Updated shipment:', updatedShipment);
      // TODO: Call service to save updated shipment
      this.closeDetailsModal();
    }
  }

  openAssignCourierModal(trackingNumber: string): void {
    this.ShipmentService.getShipmentByTrackingNumber(trackingNumber).subscribe((shipment) => {
      this.selectedShipment = shipment;
  
      if (shipment.deliveryStatus === 'delivered') {
        this.assignedCourier = {
          id: shipment.courierId ?? 0,
          name: shipment.courierName ?? '',
          email: '', 
          phone: ''
        };
        this.showAllCouriers = false;
      } else {
        if (shipment.courierName) {
          this.assignedCourier = {
            id: shipment.courierId ?? 0,
            name: shipment.courierName,
            email: '',
            phone: ''
          };
          this.showAllCouriers = false;
        } else {
          this.loadCouriers();
          this.showAllCouriers = true;
        }
      }
  
      this.showAssignCourierModal = true;
    });
  }

  openDeleteModal(trackingNumber: string): void {
    const shipment = this.shipments.find(s => s.trackingNumber === trackingNumber);
    if (shipment) {
      this.selectedShipmentDelete = shipment;
      this.isDeleteModalOpen = true;
    }
  }

  closeDeleteModal(): void {
    this.isDeleteModalOpen = false;
    this.selectedShipmentDelete = null;
  }

  confirmDeleteShipment(): void {
    if (this.selectedShipmentDelete) {
      this.ShipmentService.deleteShipmentByTrackingNumber(this.selectedShipmentDelete.trackingNumber)
        .pipe(
          tap(() => {
            this.shipments = this.shipments.filter(s => s.trackingNumber !== this.selectedShipmentDelete?.trackingNumber);
            this.filteredShipments = this.filteredShipments.filter(s => s.trackingNumber !== this.selectedShipmentDelete?.trackingNumber);
            this.closeDeleteModal();
            this.messageService.setMessage('Shipment deleted successfully!', 'success');
          }),
          catchError((error) => {
            console.error('Error deleting shipment:', error);
            this.messageService.setMessage('Error deleting shipment.', 'error');
  
            return throwError(() => new Error(error));
          })
        )
        .subscribe();
    }
  }

  assignCourierToShipment(courierId: number): void {
    if (this.selectedShipment) {
      this.ShipmentService.assignShipmentToCourier(this.selectedShipment.trackingNumber, courierId)
        .pipe(
          tap((updatedShipment) => {
            console.log('Courier assigned:', updatedShipment);

            this.closeAssignCourierModal();
            this.loadAllShipments();
            this.messageService.setMessage('Courier assigned successfully!', 'success');
          }),
          catchError((error) => {
            console.error('Error assigning courier:', error);
            this.messageService.setMessage('Error assigning courier.', 'error');
  
            return throwError(() => new Error(error));
          })
        )
        .subscribe();
    }
  }

  unassignCourierFromShipment(): void {
    if (this.selectedShipment) {
      this.ShipmentService.unassignShipmentFromCourier(this.selectedShipment.trackingNumber)
        .pipe(
          tap((updatedShipment) => {
            console.log('Courier unassigned:', updatedShipment);
            this.assignedCourier = null;
            this.showAllCouriers = true;
            this.loadCouriers();
  
            this.messageService.setMessage('Courier unassigned successfully!', 'success');
          }),
          catchError((error) => {
            console.error('Error unassigning courier:', error);
            this.messageService.setMessage('Error unassigning courier.', 'error');
  
            return throwError(() => new Error(error));
          })
        )
        .subscribe();
    }
  }

  filterNotDelivered(label: string = 'Not delivered'): void {
    this.ShipmentService.getNotDeliveredShipments().subscribe((data) => {
      this.filteredShipments = data;
      this.selectedOption = label; 
    });
    this.closeDropdown();
  }

  deleteShipment(id: number): void {
    this.ShipmentService.deleteShipment(id).subscribe(() => {
      this.filteredShipments = this.filteredShipments.filter(s => s.id !== id);
    });
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
    console.log('Dropdown is now:', this.isDropdownOpen);
  }

  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  closeDetailsModal(): void {
    this.isDetailsModalOpen = false;
    this.selectedShipment = null;
  }

  closeAssignCourierModal(): void {
    this.showAssignCourierModal = false;
    this.selectedShipment = null;
    this.assignedCourier = null;
  }

}