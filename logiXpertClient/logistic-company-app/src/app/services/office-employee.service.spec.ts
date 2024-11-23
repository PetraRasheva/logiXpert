import { TestBed } from '@angular/core/testing';

import { OfficeEmployeeService } from './office-employee.service';

describe('OfficeEmployeeService', () => {
  let service: OfficeEmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OfficeEmployeeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
