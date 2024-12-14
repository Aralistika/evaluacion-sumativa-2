import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';
import { DepartamentosService } from 'app/entities/departamentos/service/departamentos.service';
import { IJefes } from 'app/entities/jefes/jefes.model';
import { JefesService } from 'app/entities/jefes/service/jefes.service';
import { DepartamentosJefesService } from '../service/departamentos-jefes.service';
import { IDepartamentosJefes } from '../departamentos-jefes.model';
import { DepartamentosJefesFormGroup, DepartamentosJefesFormService } from './departamentos-jefes-form.service';

@Component({
  standalone: true,
  selector: 'jhi-departamentos-jefes-update',
  templateUrl: './departamentos-jefes-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartamentosJefesUpdateComponent implements OnInit {
  isSaving = false;
  departamentosJefes: IDepartamentosJefes | null = null;

  departamentosSharedCollection: IDepartamentos[] = [];
  jefesSharedCollection: IJefes[] = [];

  protected departamentosJefesService = inject(DepartamentosJefesService);
  protected departamentosJefesFormService = inject(DepartamentosJefesFormService);
  protected departamentosService = inject(DepartamentosService);
  protected jefesService = inject(JefesService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartamentosJefesFormGroup = this.departamentosJefesFormService.createDepartamentosJefesFormGroup();

  compareDepartamentos = (o1: IDepartamentos | null, o2: IDepartamentos | null): boolean =>
    this.departamentosService.compareDepartamentos(o1, o2);

  compareJefes = (o1: IJefes | null, o2: IJefes | null): boolean => this.jefesService.compareJefes(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamentosJefes }) => {
      this.departamentosJefes = departamentosJefes;
      if (departamentosJefes) {
        this.updateForm(departamentosJefes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departamentosJefes = this.departamentosJefesFormService.getDepartamentosJefes(this.editForm);
    if (departamentosJefes.id !== null) {
      this.subscribeToSaveResponse(this.departamentosJefesService.update(departamentosJefes));
    } else {
      this.subscribeToSaveResponse(this.departamentosJefesService.create(departamentosJefes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartamentosJefes>>): void {
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

  protected updateForm(departamentosJefes: IDepartamentosJefes): void {
    this.departamentosJefes = departamentosJefes;
    this.departamentosJefesFormService.resetForm(this.editForm, departamentosJefes);

    this.departamentosSharedCollection = this.departamentosService.addDepartamentosToCollectionIfMissing<IDepartamentos>(
      this.departamentosSharedCollection,
      departamentosJefes.departamentos,
    );
    this.jefesSharedCollection = this.jefesService.addJefesToCollectionIfMissing<IJefes>(
      this.jefesSharedCollection,
      departamentosJefes.jefes,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departamentosService
      .query()
      .pipe(map((res: HttpResponse<IDepartamentos[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamentos[]) =>
          this.departamentosService.addDepartamentosToCollectionIfMissing<IDepartamentos>(
            departamentos,
            this.departamentosJefes?.departamentos,
          ),
        ),
      )
      .subscribe((departamentos: IDepartamentos[]) => (this.departamentosSharedCollection = departamentos));

    this.jefesService
      .query()
      .pipe(map((res: HttpResponse<IJefes[]>) => res.body ?? []))
      .pipe(map((jefes: IJefes[]) => this.jefesService.addJefesToCollectionIfMissing<IJefes>(jefes, this.departamentosJefes?.jefes)))
      .subscribe((jefes: IJefes[]) => (this.jefesSharedCollection = jefes));
  }
}
