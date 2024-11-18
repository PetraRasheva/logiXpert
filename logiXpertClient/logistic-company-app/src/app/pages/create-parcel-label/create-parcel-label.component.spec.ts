import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateParcelLabelComponent } from './create-parcel-label.component';

describe('CreateParcelLabelComponent', () => {
  let component: CreateParcelLabelComponent;
  let fixture: ComponentFixture<CreateParcelLabelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateParcelLabelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateParcelLabelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
