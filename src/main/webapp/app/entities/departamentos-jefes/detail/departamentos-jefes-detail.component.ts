import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDepartamentosJefes } from '../departamentos-jefes.model';

@Component({
  standalone: true,
  selector: 'jhi-departamentos-jefes-detail',
  templateUrl: './departamentos-jefes-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DepartamentosJefesDetailComponent {
  departamentosJefes = input<IDepartamentosJefes | null>(null);

  previousState(): void {
    window.history.back();
  }
}
