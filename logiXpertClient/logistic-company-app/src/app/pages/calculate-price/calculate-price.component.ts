import { Component, ElementRef, OnInit } from '@angular/core';

@Component({
  selector: 'app-calculate-price',
  standalone: true,
  imports: [],
  templateUrl: './calculate-price.component.html',
  styleUrl: './calculate-price.component.css'
})
export class CalculatePriceComponent implements OnInit{
  weightInput!: HTMLInputElement;
  deliveryPriceOutput!: HTMLElement;
  codAmountInput!: HTMLInputElement;
  finalPriceOutput!: HTMLElement;
  fromLocationRadios!: NodeListOf<HTMLInputElement>;
  toLocationRadios!: NodeListOf<HTMLInputElement>;
  boxes!: NodeListOf<HTMLElement>;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    // Инициализация на елементите
    this.weightInput = this.el.nativeElement.querySelector('#weight');
    this.deliveryPriceOutput = this.el.nativeElement.querySelector('#deliveryPrice');
    this.codAmountInput = this.el.nativeElement.querySelector('#codAmount');
    this.finalPriceOutput = this.el.nativeElement.querySelector('#finalPrice');
    this.fromLocationRadios = this.el.nativeElement.querySelectorAll('[name="fromLocation"]');
    this.toLocationRadios = this.el.nativeElement.querySelectorAll('[name="toLocation"]');
    this.boxes = this.el.nativeElement.querySelectorAll('.box');


    // Свързване на събития
    this.codAmountInput.addEventListener('input', () => this.updateFinalPrice());
    this.weightInput.addEventListener('input', () => this.updateDeliveryPrice());
    this.fromLocationRadios.forEach(radio => radio.addEventListener('change', () => this.updateDeliveryPrice()));
    this.toLocationRadios.forEach(radio => radio.addEventListener('change', () => this.updateDeliveryPrice()));
    this.weightInput.addEventListener('input', () => this.updateBoxHighlight());

    // Първоначална калкулация
    this.updateDeliveryPrice();
  }


  updateDeliveryPrice(): void{
    const weight = Number(this.weightInput.value);

    if (isNaN(weight) || weight < 0) {
      this.deliveryPriceOutput.textContent = 'Invalid weight';
      return;
    }

    let deliveryPrice = this.calculateDeliveryPrice(weight);

    if (this.getSelectedValue(this.fromLocationRadios) === 'address') {
      deliveryPrice += 3.99;
    }

    if (this.getSelectedValue(this.toLocationRadios) === 'address') {
      deliveryPrice += 3.99;
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
    const radios = Array.from(radioGroup); // Преобразуване на NodeList в масив
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
  
}
