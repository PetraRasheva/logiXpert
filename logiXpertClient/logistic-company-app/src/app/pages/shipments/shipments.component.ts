import { Component, OnInit } from '@angular/core';
import { Shipment } from '../../types/shipment';
import { ShipmentService } from '../../services/shipment.service';
import { ClickOutsideDirective } from '../../utils/ClickOutsideDirective';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-shipments',
  standalone: true,
  imports: [CommonModule, ClickOutsideDirective],
  templateUrl: './shipments.component.html',
  styleUrl: './shipments.component.css'
})

export class ShipmentsComponent implements OnInit {
  shipments: Shipment[] = [];
  filteredShipments: Shipment[] = [];
  isDropdownOpen = false;
  selectedOption = 'Shipments';

  constructor(private ShipmentService: ShipmentService) {}

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
}