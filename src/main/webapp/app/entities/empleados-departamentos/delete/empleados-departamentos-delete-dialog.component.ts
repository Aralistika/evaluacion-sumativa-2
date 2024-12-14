import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosService } from '../service/empleados-departamentos.service';

@Component({
  standalone: true,
  templateUrl: './empleados-departamentos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmpleadosDepartamentosDeleteDialogComponent {
  empleadosDepartamentos?: IEmpleadosDepartamentos;

  protected empleadosDepartamentosService = inject(EmpleadosDepartamentosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadosDepartamentosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
