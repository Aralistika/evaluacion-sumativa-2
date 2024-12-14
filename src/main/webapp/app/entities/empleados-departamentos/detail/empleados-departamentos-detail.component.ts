import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';

@Component({
  standalone: true,
  selector: 'jhi-empleados-departamentos-detail',
  templateUrl: './empleados-departamentos-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EmpleadosDepartamentosDetailComponent {
  empleadosDepartamentos = input<IEmpleadosDepartamentos | null>(null);

  previousState(): void {
    window.history.back();
  }
}
