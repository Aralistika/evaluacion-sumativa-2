import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosService } from '../service/empleados-departamentos.service';

const empleadosDepartamentosResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmpleadosDepartamentos> => {
  const id = route.params.id;
  if (id) {
    return inject(EmpleadosDepartamentosService)
      .find(id)
      .pipe(
        mergeMap((empleadosDepartamentos: HttpResponse<IEmpleadosDepartamentos>) => {
          if (empleadosDepartamentos.body) {
            return of(empleadosDepartamentos.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default empleadosDepartamentosResolve;
