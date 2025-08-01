import { Component, inject } from '@angular/core';

import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatSliderModule } from "@angular/material/slider";


@Component({
  selector: 'app-ant-simulation-form',
  templateUrl: './ant-simulation-form.component.html',
  styleUrl: './ant-simulation-form.component.css',
  imports: [
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    ReactiveFormsModule,
    MatSliderModule,
  ]
})
export class AntSimulationFormComponent {
  
  private fb = inject(FormBuilder);

  thumbLabel = true;
  
  antSimulationForm = this.fb.group({
    antQuantity: [1, [Validators.required, Validators.min(1), Validators.max(100)]]
  });

  onSubmit(): void {
    if (this.antSimulationForm.valid) {
      alert('Thanks!');
    }
  }
}
