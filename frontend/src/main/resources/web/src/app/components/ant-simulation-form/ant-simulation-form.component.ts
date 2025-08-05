import { Component, inject } from '@angular/core';

import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatSliderModule } from "@angular/material/slider";
import { GraphService } from 'app/services/graph.service';
import { SimulationData } from 'app/models/SimulationData';
import { Router } from '@angular/router';


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
  private _graphService = inject(GraphService);
  private _router = inject(Router);

  thumbLabel = true;
  
  antSimulationForm = this.fb.group({
    antQuantity: [3, [Validators.required, Validators.min(3), Validators.max(30)]],
    antSpeed: [15, [Validators.required, Validators.min(3), Validators.max(30)]],
  });

  onSubmit(): void {
    if (this.antSimulationForm.valid) {
      const simulationData = this.antSimulationForm.value as SimulationData;
      this._graphService.sendSimulationData(simulationData).subscribe({
        next: () => {
          this._router.navigate(["/simulation"], {queryParams: {
            antSpeed: this.antSimulationForm.controls.antSpeed.value
          }});
        }
      });
    }
  }
}
