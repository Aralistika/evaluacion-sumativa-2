import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpleados } from 'app/entities/empleados/empleados.model';
import { EmpleadosService } from 'app/entities/empleados/service/empleados.service';
import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';
import { DepartamentosService } from 'app/entities/departamentos/service/departamentos.service';
import { EmpleadosDepartamentosService } from '../service/empleados-departamentos.service';
import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosFormGroup, EmpleadosDepartamentosFormService } from './empleados-departamentos-form.service';

@Component({
  standalone: true,
  selector: 'jhi-empleados-departamentos-update',
  templateUrl: './empleados-departamentos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadosDepartamentosUpdateComponent implements OnInit {
  isSaving = false;
  empleadosDepartamentos: IEmpleadosDepartamentos | null = null;

  empleadosSharedCollection: IEmpleados[] = [];
  departamentosSharedCollection: IDepartamentos[] = [];

  protected empleadosDepartamentosService = inject(EmpleadosDepartamentosService);
  protected empleadosDepartamentosFormService = inject(EmpleadosDepartamentosFormService);
  protected empleadosService = inject(EmpleadosService);
  protected departamentosService = inject(DepartamentosService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadosDepartamentosFormGroup = this.empleadosDepartamentosFormService.createEmpleadosDepartamentosFormGroup();

  compareEmpleados = (o1: IEmpleados | null, o2: IEmpleados | null): boolean => this.empleadosService.compareEmpleados(o1, o2);

  compareDepartamentos = (o1: IDepartamentos | null, o2: IDepartamentos | null): boolean =>
    this.departamentosService.compareDepartamentos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleadosDepartamentos }) => {
      this.empleadosDepartamentos = empleadosDepartamentos;
      if (empleadosDepartamentos) {
        this.updateForm(empleadosDepartamentos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleadosDepartamentos = this.empleadosDepartamentosFormService.getEmpleadosDepartamentos(this.editForm);
    if (empleadosDepartamentos.id !== null) {
      this.subscribeToSaveResponse(this.empleadosDepartamentosService.update(empleadosDepartamentos));
    } else {
      this.subscribeToSaveResponse(this.empleadosDepartamentosService.create(empleadosDepartamentos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleadosDepartamentos>>): void {
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

  protected updateForm(empleadosDepartamentos: IEmpleadosDepartamentos): void {
    this.empleadosDepartamentos = empleadosDepartamentos;
    this.empleadosDepartamentosFormService.resetForm(this.editForm, empleadosDepartamentos);

    this.empleadosSharedCollection = this.empleadosService.addEmpleadosToCollectionIfMissing<IEmpleados>(
      this.empleadosSharedCollection,
      empleadosDepartamentos.empleados,
    );
    this.departamentosSharedCollection = this.departamentosService.addDepartamentosToCollectionIfMissing<IDepartamentos>(
      this.departamentosSharedCollection,
      empleadosDepartamentos.departamentos,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empleadosService
      .query()
      .pipe(map((res: HttpResponse<IEmpleados[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleados[]) =>
          this.empleadosService.addEmpleadosToCollectionIfMissing<IEmpleados>(empleados, this.empleadosDepartamentos?.empleados),
        ),
      )
      .subscribe((empleados: IEmpleados[]) => (this.empleadosSharedCollection = empleados));

    this.departamentosService
      .query()
      .pipe(map((res: HttpResponse<IDepartamentos[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamentos[]) =>
          this.departamentosService.addDepartamentosToCollectionIfMissing<IDepartamentos>(
            departamentos,
            this.empleadosDepartamentos?.departamentos,
          ),
        ),
      )
      .subscribe((departamentos: IDepartamentos[]) => (this.departamentosSharedCollection = departamentos));
  }
}
