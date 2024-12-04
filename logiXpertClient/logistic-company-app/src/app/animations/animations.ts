import { trigger, transition, style, animate, state } from '@angular/animations';

export const fadeIn = trigger('fadeIn', [
  transition(':enter', [
    style({ opacity: 0 }),
    animate('0.5s', style({ opacity: 1 })),
  ]),
]);

export const fadeOut = trigger('fadeOut', [
  transition(':leave', [
    animate('0.5s', style({ opacity: 0 }))
  ]),
]);

export const fadeInOut = trigger('fadeInOut', [
  transition(':enter', [
    style({ opacity: 0 }),
    animate('0.5s', style({ opacity: 1 })),
  ]),
  transition(':leave', [
    animate('0.5s', style({ opacity: 0 }))
  ]),
]);

export const slideFade = trigger('slideFade', [
  transition(':enter', [
    style({ transform: 'translateY(-3%)', opacity: 0 }),
    animate('0.4s ease-in', style({ transform: 'translateY(0)', opacity: 1 })),
  ]),
  transition(':leave', [
    style({ opacity: 1 }),
    animate('0.4s ease-out', style({ transform: 'translateY(-3%)', opacity: 0 })),
  ]),
]);

export const slideInAnimation = trigger('slideIn', [
  state('in', style({
    transform: 'translateX(0)',
    opacity: 1
  })),
  transition(':enter', [
    style({
      transform: 'translateX(-100%)',
      opacity: 0
    }),
    animate('0.5s ease-out')
  ])
]);

export const slideInFromLeft = trigger('slideInFromLeft', [
  transition(':enter', [
    style({ transform: 'translateX(-100%)', opacity: 0.8 }), 
    animate(
      '1.2s ease-out', 
      style({ transform: 'translateX(0)', opacity: 0.06 }) 
    )
  ])
]);