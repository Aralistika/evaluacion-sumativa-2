import { Component, NgZone, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosService, EntityArrayResponseType } from '../service/empleados-departamentos.service';
import { EmpleadosDepartamentosDeleteDialogComponent } from '../delete/empleados-departamentos-delete-dialog.component';

@Component({
  standalone: true,
  selector: 'jhi-empleados-departamentos',
  templateUrl: './empleados-departamentos.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class EmpleadosDepartamentosComponent implements OnInit {
  subscription: Subscription | null = null;
  empleadosDepartamentos?: IEmpleadosDepartamentos[];
  isLoading = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly empleadosDepartamentosService = inject(EmpleadosDepartamentosService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IEmpleadosDepartamentos): number => this.empleadosDepartamentosService.getEmpleadosDepartamentosIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (!this.empleadosDepartamentos || this.empleadosDepartamentos.length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(empleadosDepartamentos: IEmpleadosDepartamentos): void {
    const modalRef = this.modalService.open(EmpleadosDepartamentosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.empleadosDepartamentos = empleadosDepartamentos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.empleadosDepartamentos = this.refineData(dataFromBody);
  }

  protected refineData(data: IEmpleadosDepartamentos[]): IEmpleadosDepartamentos[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IEmpleadosDepartamentos[] | null): IEmpleadosDepartamentos[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.empleadosDepartamentosService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
