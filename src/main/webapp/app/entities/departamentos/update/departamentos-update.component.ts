import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDepartamentos } from '../departamentos.model';
import { DepartamentosService } from '../service/departamentos.service';
import { DepartamentosFormGroup, DepartamentosFormService } from './departamentos-form.service';

@Component({
  standalone: true,
  selector: 'jhi-departamentos-update',
  templateUrl: './departamentos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartamentosUpdateComponent implements OnInit {
  isSaving = false;
  departamentos: IDepartamentos | null = null;

  protected departamentosService = inject(DepartamentosService);
  protected departamentosFormService = inject(DepartamentosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartamentosFormGroup = this.departamentosFormService.createDepartamentosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamentos }) => {
      this.departamentos = departamentos;
      if (departamentos) {
        this.updateForm(departamentos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departamentos = this.departamentosFormService.getDepartamentos(this.editForm);
    if (departamentos.id !== null) {
      this.subscribeToSaveResponse(this.departamentosService.update(departamentos));
    } else {
      this.subscribeToSaveResponse(this.departamentosService.create(departamentos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartamentos>>): void {
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

  protected updateForm(departamentos: IDepartamentos): void {
    this.departamentos = departamentos;
    this.departamentosFormService.resetForm(this.editForm, departamentos);
  }
}
