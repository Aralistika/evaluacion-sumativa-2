import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepartamentosJefes } from '../departamentos-jefes.model';
import { DepartamentosJefesService } from '../service/departamentos-jefes.service';

const departamentosJefesResolve = (route: ActivatedRouteSnapshot): Observable<null | IDepartamentosJefes> => {
  const id = route.params.id;
  if (id) {
    return inject(DepartamentosJefesService)
      .find(id)
      .pipe(
        mergeMap((departamentosJefes: HttpResponse<IDepartamentosJefes>) => {
          if (departamentosJefes.body) {
            return of(departamentosJefes.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default departamentosJefesResolve;
