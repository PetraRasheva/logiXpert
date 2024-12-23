import { Component,  OnInit, OnDestroy  } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent implements OnInit, OnDestroy{
  
  currentIndex = 0;
  autoSlideInterval: any;
  slides = Array(3).fill(null); 
  
  
  ngOnInit(): void {
    this.startAutoSlide();
  }
  ngOnDestroy(): void {
    this.stopAutoSlide();
  }

  goToSlide(index: number): void {
    this.stopAutoSlide();
    this.currentIndex = index;
    this.updateTransform();
  }

  startAutoSlide(): void {
    this.autoSlideInterval = setInterval(() => {
      this.currentIndex = (this.currentIndex + 1) % this.slides.length;
      this.updateTransform();
    }, 3000);
  }

  stopAutoSlide(): void {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  updateTransform(): void {
    const slidesElement = document.querySelector('.slides') as HTMLElement;
    if (slidesElement) {
      slidesElement.style.transform = `translateX(-${this.currentIndex * 100}%)`;
    }
  }

}
