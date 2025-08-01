import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntSimulationFormComponent } from './ant-simulation-form.component';

describe('AntSimulationFormComponent', () => {
  let component: AntSimulationFormComponent;
  let fixture: ComponentFixture<AntSimulationFormComponent>;

  beforeEach(() => {
    fixture = TestBed.createComponent(AntSimulationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
