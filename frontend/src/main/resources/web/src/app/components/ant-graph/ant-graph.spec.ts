import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntGraph } from './ant-graph';

describe('AntGraph', () => {
  let component: AntGraph;
  let fixture: ComponentFixture<AntGraph>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AntGraph]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AntGraph);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
