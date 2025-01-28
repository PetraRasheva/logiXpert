import { Component, ElementRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { HttpClient } from '@angular/common/http';
import { Company } from '../../types/company';
import { environment } from '../../../environments/environment';
import { ShipmentService } from '../../services/shipment.service';
import { Shipment } from '../../types/shipment';
import { ShipmentDetails } from '../../types/shipmentDetails';


@Component({
  selector: 'app-calculate-price',
  standalone: true,
  templateUrl: './calculate-price.component.html',
  styleUrls: ['./calculate-price.component.css'],
  imports: [CommonModule] 
})
export class CalculatePriceComponent implements OnInit {
  weightInput!: HTMLInputElement;
  deliveryPriceOutput!: HTMLElement;
  codAmountInput!: HTMLInputElement;
  finalPriceOutput!: HTMLElement;
  fromLocationRadios!: NodeListOf<HTMLInputElement>;
  toLocationRadios!: NodeListOf<HTMLInputElement>;
  boxes!: NodeListOf<HTMLElement>;
  addressFee!: number;

  constructor(private el: ElementRef, private http: HttpClient, private shipmentService: ShipmentService) {}

  ngOnInit(): void {
    this.loadAddressFee();
    this.weightInput = this.el.nativeElement.querySelector('#weight');
    this.deliveryPriceOutput = this.el.nativeElement.querySelector('#deliveryPrice');
    this.codAmountInput = this.el.nativeElement.querySelector('#codAmount');
    this.finalPriceOutput = this.el.nativeElement.querySelector('#finalPrice');
    this.fromLocationRadios = this.el.nativeElement.querySelectorAll('[name="fromLocation"]');
    this.toLocationRadios = this.el.nativeElement.querySelectorAll('[name="toLocation"]');
    this.boxes = this.el.nativeElement.querySelectorAll('.box');

    this.codAmountInput.addEventListener('input', () => this.updateFinalPrice());
    this.weightInput.addEventListener('input', () => this.updateDeliveryPrice());
    this.fromLocationRadios.forEach(radio => radio.addEventListener('change', () => this.updateDeliveryPrice()));
    this.toLocationRadios.forEach(radio => radio.addEventListener('change', () => this.updateDeliveryPrice()));
    this.weightInput.addEventListener('input', () => this.updateBoxHighlight());

    this.updateDeliveryPrice();
  }

  loadAddressFee(): void {
    this.http.get<Company>(`${environment.apiUrl}/company/find/1`, { withCredentials: true })
      .subscribe({
        next: (company) => {
          this.addressFee = company.addressFee;
          this.updateDeliveryPrice();
        },
        error: (err) => console.error('Failed to load company data', err)
      });
  }

  updateDeliveryPrice(): void {
    const weight = Number(this.weightInput.value);

    if (isNaN(weight) || weight < 0) {
      this.deliveryPriceOutput.textContent = 'Invalid weight';
      return;
    }

    let deliveryPrice = this.calculateDeliveryPrice(weight);

    if (this.addressFee !== undefined) {
      if (this.getSelectedValue(this.fromLocationRadios) === 'address') {
        deliveryPrice += this.addressFee;
      }

      if (this.getSelectedValue(this.toLocationRadios) === 'address') {
        deliveryPrice += this.addressFee;
      }
    }

    this.deliveryPriceOutput.textContent = deliveryPrice.toFixed(2);

    this.updateFinalPrice();
  }

  updateFinalPrice(): void {
    const deliveryPrice = Number(this.deliveryPriceOutput.textContent);
    let codAmount = Number(this.codAmountInput.value);

    if (isNaN(codAmount) || codAmount < 0) {
      codAmount = 0;
    }

    const finalPrice = deliveryPrice + codAmount;
    this.finalPriceOutput.textContent = finalPrice.toFixed(2);
  }

  calculateDeliveryPrice(weight: number): number {
    let deliveryPrice = 0;
    if (weight <= 3) {
      deliveryPrice = 2.99;
    } else if (weight <= 6) {
      deliveryPrice = 5.4;
    } else if (weight <= 24) {
      deliveryPrice = 14.99;
    } else if (weight > 24) {
      const kgOvers = weight - 24;
      deliveryPrice = 14.99 + kgOvers * 0.6;
    }
    return deliveryPrice;
  }

  getSelectedValue(radioGroup: NodeListOf<HTMLInputElement>): string | null {
    const radios = Array.from(radioGroup);
    for (const radio of radios) {
      if (radio.checked) {
        return radio.value;
      }
    }
    return null;
  }

  updateBoxHighlight(): void {
    const weight = Number(this.weightInput.value);

    if (isNaN(weight) || weight < 0) {
      this.resetBoxes();
      return;
    }

    this.resetBoxes();

    const index = this.getBoxIndex(weight);

    if (index !== -1 && this.boxes[index]) {
      this.boxes[index].style.backgroundColor = 'rgb(187, 190, 209)';
    }
  }

  getBoxIndex(weight: number): number {
    if (weight <= 3) {
      return 0;
    } else if (weight <= 6) {
      return 1;
    } else if (weight <= 24) {
      return 2;
    } else if (weight > 24) {
      return 3;
    }
    return -1;
  }

  resetBoxes(): void {
    this.boxes.forEach(box => {
      box.style.backgroundColor = '';
    });
  }

  toggleInput(event: Event, section: string): void {
    const radio = event.target as HTMLInputElement;
    const officeDropdown = document.getElementById(`office-${section}`);
    const addressInput = document.getElementById(`address-${section}`);
  
    if (officeDropdown && addressInput) {
      if (radio.value === 'office') {
        officeDropdown.style.display = 'block';
        addressInput.style.display = 'none';
      } else if (radio.value === 'address') {
        officeDropdown.style.display = 'none';
        addressInput.style.display = 'block';
      }
    }
  }


  // Метод за изпращане на резултатите към BE
  submitFinalPrice(): void {
    const senderName = (document.getElementById('senderName') as HTMLInputElement).value;
    const senderPhone = (document.getElementById('senderPhone') as HTMLInputElement).value;
    const senderEmail = (document.getElementById('senderEmail') as HTMLInputElement).value;
    const senderAddress = (document.getElementById('senderAddress') as HTMLInputElement).value;

    const recipientName = (document.getElementById('recipientName') as HTMLInputElement).value;
    const recipientPhone = (document.getElementById('recipientPhone') as HTMLInputElement).value;
    const recipientEmail = (document.getElementById('recipientEmail') as HTMLInputElement).value;
    const recipientAddress = (document.getElementById('recipientAddress') as HTMLInputElement).value;

    const weight = parseFloat((document.getElementById('weight') as HTMLInputElement).value);
    const codAmount = parseFloat((document.getElementById('codAmount') as HTMLInputElement).value);
    const deliveryPrice = parseFloat((document.getElementById('deliveryPrice') as HTMLDivElement).textContent ?? '');
    const finalPrice = parseFloat((document.getElementById('finalPrice') as HTMLDivElement).textContent ?? '');

    const shipment: ShipmentDetails = {
      weight: weight,
      price: finalPrice,
      shipmentDate: new Date().toISOString().replace('T', ' ').slice(0, 16), // Форматиране "yyyy-MM-dd HH:mm"
      source: senderAddress,
      destination: recipientAddress,
      profit: deliveryPrice,
      sender: {
        id: 0,
        name: senderName,
        email: senderEmail,
        phone: senderPhone,
      },
      receiver: {
        id: 0,
        name: recipientName,
        email: recipientEmail,
        phone: recipientPhone,
      },
      ownerId: 2,
      id: 0,
      trackingNumber: '',
      deliveryStatus: '',
      deliveryDate: null,
      owner: {
        id: 0,
        name: recipientName,
        email: recipientEmail,
        phone: recipientPhone,
      },
      courierName: null,
      courierId: null
    };
    
    
    
    this.shipmentService.createShipment(shipment).subscribe({
        next: (response: any) => {
            console.log('Shipment successfully created', response);
            alert('Shipment successfully created!');
        },
        error: (err: any) => {
            console.error('Error creating shipment', err);
            alert('Failed to create shipment. Please try again.');
        },
    });
}




  
  
}