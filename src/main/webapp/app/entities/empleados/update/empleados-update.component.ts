import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpleados } from '../empleados.model';
import { EmpleadosService } from '../service/empleados.service';
import { EmpleadosFormGroup, EmpleadosFormService } from './empleados-form.service';

@Component({
  standalone: true,
  selector: 'jhi-empleados-update',
  templateUrl: './empleados-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadosUpdateComponent implements OnInit {
  isSaving = false;
  empleados: IEmpleados | null = null;

  protected empleadosService = inject(EmpleadosService);
  protected empleadosFormService = inject(EmpleadosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadosFormGroup = this.empleadosFormService.createEmpleadosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleados }) => {
      this.empleados = empleados;
      if (empleados) {
        this.updateForm(empleados);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleados = this.empleadosFormService.getEmpleados(this.editForm);
    if (empleados.id !== null) {
      this.subscribeToSaveResponse(this.empleadosService.update(empleados));
    } else {
      this.subscribeToSaveResponse(this.empleadosService.create(empleados));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleados>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(empleados: IEmpleados): void {
    this.empleados = empleados;
    this.empleadosFormService.resetForm(this.editForm, empleados);
  }
}
