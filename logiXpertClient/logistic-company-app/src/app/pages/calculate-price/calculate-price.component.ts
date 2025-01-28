import { Component, ElementRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ShipmentService } from '../../services/shipment.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from '../../services/message.service';
import { Office } from '../../types/office';
import { CompanyService } from '../../services/company.service';
import { CreateShipment } from '../../types/CreateShipment';


@Component({
  selector: 'app-calculate-price',
  standalone: true,
  templateUrl: './calculate-price.component.html',
  styleUrls: ['./calculate-price.component.css'],
  imports: [CommonModule, ReactiveFormsModule] 
})

export class CalculatePriceComponent implements OnInit {
  shipmentForm: FormGroup;
  addressFee!: number;  
  user: any;
  offices: Office[] = [];

  constructor(
    private el: ElementRef,
    private fb: FormBuilder,
    private shipmentService: ShipmentService,
    private messageService: MessageService,
    private http: HttpClient,
    private companyService: CompanyService
  ) {
    this.shipmentForm = this.fb.group({
      senderName: ['', Validators.required],
      senderPhone: ['', Validators.required],
      senderEmail: ['', [Validators.required, Validators.email]],
      senderAddress: [''],
      senderOffice: [''],
      recipientName: ['', Validators.required],
      recipientPhone: ['', Validators.required],
      recipientEmail: ['', [Validators.required, Validators.email]],
      recipientAddress: [''],
      recipientOffice: [''],
      weight: ['', [Validators.required, Validators.min(0)]],
      codAmount: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    const userJson = localStorage.getItem('[user]');
    if (!userJson) {
      console.error('User data not found in localStorage');
      return;
    }
    this.user = JSON.parse(userJson);
    this.loadAddressFee();
    this.loadOffices();
    this.initializeEventListeners();
  }

  loadAddressFee(): void {
    this.http.get<any>(`${environment.apiUrl}/company/find/1`, { withCredentials: true })
      .subscribe({
        next: (company) => {
          this.addressFee = company.addressFee;
          console.log('[CalculatePriceComponent] Loaded addressFee:', this.addressFee);
        },
        error: (err) => console.error('Failed to load company data', err),
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

  calculateDeliveryPrice(weight: number): number {
    let deliveryPrice = 0;

    if (weight <= 3) {
      deliveryPrice = 2.99;
    } else if (weight <= 6) {
      deliveryPrice = 5.4;
    } else if (weight <= 24) {
      deliveryPrice = 14.99;
    } else {
      const kgOvers = weight - 24;
      deliveryPrice = 14.99 + kgOvers * 0.6;
    }

    return deliveryPrice;
  }

  initializeEventListeners(): void {
    const weightInput = this.el.nativeElement.querySelector('#weight');
    const deliveryPriceOutput = this.el.nativeElement.querySelector('#deliveryPrice');
    const codAmountInput = this.el.nativeElement.querySelector('#codAmount');
    const finalPriceOutput = this.el.nativeElement.querySelector('#finalPrice');
    const fromLocationRadios = Array.from(this.el.nativeElement.querySelectorAll('[name="fromLocation"]')) as HTMLInputElement[];
    const toLocationRadios = Array.from(this.el.nativeElement.querySelectorAll('[name="toLocation"]')) as HTMLInputElement[];
    const boxes = Array.from(this.el.nativeElement.querySelectorAll('.box')) as HTMLElement[];

    weightInput.addEventListener('input', () =>
      this.updateDeliveryPrice(weightInput, deliveryPriceOutput, fromLocationRadios, toLocationRadios, boxes));
    codAmountInput.addEventListener('input', () =>
      this.updateFinalPrice(deliveryPriceOutput, codAmountInput, finalPriceOutput));
    fromLocationRadios.forEach((radio: HTMLInputElement) =>
      radio.addEventListener('change', () =>
        this.updateDeliveryPrice(weightInput, deliveryPriceOutput, fromLocationRadios, toLocationRadios, boxes)));
    toLocationRadios.forEach((radio: HTMLInputElement) =>
      radio.addEventListener('change', () =>
        this.updateDeliveryPrice(weightInput, deliveryPriceOutput, fromLocationRadios, toLocationRadios, boxes)));
  }

  updateDeliveryPrice(
    weightInput: HTMLInputElement,
    deliveryPriceOutput: HTMLElement,
    fromLocationRadios: HTMLInputElement[],
    toLocationRadios: HTMLInputElement[],
    boxes: HTMLElement[]
  ): void {
    const weight = parseFloat(weightInput.value);

    if (isNaN(weight) || weight < 0) {
      deliveryPriceOutput.textContent = 'Invalid weight';
      return;
    }

    let deliveryPrice = this.calculateDeliveryPrice(weight);

    if (this.addressFee !== undefined) {
      if (this.getSelectedValue(fromLocationRadios) === 'address') {
        deliveryPrice += this.addressFee;
      }
      if (this.getSelectedValue(toLocationRadios) === 'address') {
        deliveryPrice += this.addressFee;
      }
    }

    deliveryPriceOutput.textContent = deliveryPrice.toFixed(2);
    this.updateFinalPrice(deliveryPriceOutput, this.el.nativeElement.querySelector('#codAmount'), this.el.nativeElement.querySelector('#finalPrice'));
    this.updateBoxHighlight(weight, boxes);
  }

  updateFinalPrice(deliveryPriceOutput: HTMLElement, codAmountInput: HTMLInputElement, finalPriceOutput: HTMLElement): void {
    const deliveryPrice = parseFloat(deliveryPriceOutput.textContent || '0');
    const codAmount = parseFloat(codAmountInput.value || '0');

    if (isNaN(codAmount) || codAmount < 0) {
      finalPriceOutput.textContent = deliveryPrice.toFixed(2);
      return;
    }

    const finalPrice = deliveryPrice + codAmount;
    finalPriceOutput.textContent = finalPrice.toFixed(2);
  }

  getSelectedValue(radioGroup: HTMLInputElement[]): string | null {
    for (const radio of radioGroup) {
      if (radio.checked) {
        return radio.value;
      }
    }
    return null;
  }

  toggleInput(event: Event, section: string): void {
    const radio = event.target as HTMLInputElement;
  
    const officeDropdown = this.el.nativeElement.querySelector(`#office-${section}`) as HTMLElement;
    const addressInput = this.el.nativeElement.querySelector(`#address-${section}`) as HTMLElement;
  
    const officeControl = this.shipmentForm.get(`${section}Office`);
    const addressControl = this.shipmentForm.get(`${section}Address`);
  
    if (!officeDropdown || !addressInput) {
      console.error(`Missing elements for section: ${section}`);
      return;
    }
  
    if (radio.value === 'office') {
      officeDropdown.style.display = 'block';
      addressInput.style.display = 'none';
      officeControl?.setValidators(Validators.required);
      addressControl?.clearValidators();
    } else if (radio.value === 'address') {
      officeDropdown.style.display = 'none';
      addressInput.style.display = 'block';
      addressControl?.setValidators(Validators.required);
      officeControl?.clearValidators();
    }
  
    officeControl?.updateValueAndValidity();
    addressControl?.updateValueAndValidity();
  }

  updateBoxHighlight(weight: number, boxes: HTMLElement[]): void {
    boxes.forEach((box) => (box.style.backgroundColor = ''));

    if (weight <= 3 && boxes[0]) {
      boxes[0].style.backgroundColor = 'rgb(187, 190, 209)';
    } else if (weight <= 6 && boxes[1]) {
      boxes[1].style.backgroundColor = 'rgb(187, 190, 209)';
    } else if (weight <= 24 && boxes[2]) {
      boxes[2].style.backgroundColor = 'rgb(187, 190, 209)';
    } else if (weight > 24 && boxes[3]) {
      boxes[3].style.backgroundColor = 'rgb(187, 190, 209)';
    }
  }

  submitFinalPrice(): void {
    console.log('[CalculatePriceComponent] Form values:', this.shipmentForm.value);
    console.log('[CalculatePriceComponent] Form valid:', this.shipmentForm.valid);
  
    if (this.shipmentForm.invalid) {
      console.error('[CalculatePriceComponent] Form is invalid');
      return;
    }
  
    const formValues = this.shipmentForm.value;
    const weight = parseFloat(formValues.weight);
    const codAmount = parseFloat(formValues.codAmount);
    
    let deliveryPrice = this.calculateDeliveryPrice(weight);

    if (this.addressFee !== undefined) {
      //  fromLocation=address, deliveryPrice += this.addressFee; ...
      //  toLocation=address, deliveryPrice += this.addressFee; ...
    }
  
    const finalPrice = deliveryPrice + codAmount;

    const senderOfficeId = parseInt(formValues.senderOffice, 10);
    let sourceValue: string;
    if (senderOfficeId) {
      const foundOffice = this.offices.find(o => o.id === senderOfficeId);
      sourceValue = foundOffice
        ? `office: ${foundOffice.name}`
        : `office: unknown`;
    } else {
      sourceValue = `address: ${formValues.senderAddress || ''}`;
    }
  
    const recipientOfficeId = parseInt(formValues.recipientOffice, 10);
    let destinationValue: string;
    if (recipientOfficeId) {
      const foundOffice = this.offices.find(o => o.id === recipientOfficeId);
      destinationValue = foundOffice
        ? `office: ${foundOffice.name}`
        : `office: unknown`;
    } else {
      destinationValue = `address: ${formValues.recipientAddress || ''}`;
    }

    const newShipment: CreateShipment = {
      trackingNumber: `TRK-${Date.now()}`,
      weight: weight,
      price: finalPrice,         
      profit: deliveryPrice,     
      deliveryStatus: 'CREATED',
      shipmentDate: new Date().toISOString().replace('T', ' ').slice(0, 16),
      deliveryDate: null,
      source: sourceValue,
      destination: destinationValue,
      sender: {
        id: undefined, 
        name: formValues.senderName,
        email: formValues.senderEmail,
        phone: formValues.senderPhone,
      },
      receiver: {
        id: undefined,
        name: formValues.recipientName,
        email: formValues.recipientEmail,
        phone: formValues.recipientPhone,
      },
      ownerId: this.user.id,
      companyId: 1
    };
  
    console.log('[CalculatePriceComponent] Payload for create:', newShipment);

    this.shipmentService.createShipment(newShipment).subscribe({
      next: (response) => {
        console.log('[CalculatePriceComponent] Shipment successfully created:', response);
        this.messageService.setMessage('Shipment successfully created!', 'success');
      },
      error: (err) => {
        console.error('[CalculatePriceComponent] Error creating shipment:', err);
        if (err && err.error) {
          console.log('[CalculatePriceComponent] Full error body:', err.error);
        }
        this.messageService.setMessage('Failed to create shipment. Please try again.', 'error');
      },
    });
  }
}