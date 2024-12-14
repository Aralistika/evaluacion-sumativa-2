import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmpleadosDepartamentosResolve from './route/empleados-departamentos-routing-resolve.service';

const empleadosDepartamentosRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empleados-departamentos.component').then(m => m.EmpleadosDepartamentosComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empleados-departamentos-detail.component').then(m => m.EmpleadosDepartamentosDetailComponent),
    resolve: {
      empleadosDepartamentos: EmpleadosDepartamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empleados-departamentos-update.component').then(m => m.EmpleadosDepartamentosUpdateComponent),
    resolve: {
      empleadosDepartamentos: EmpleadosDepartamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empleados-departamentos-update.component').then(m => m.EmpleadosDepartamentosUpdateComponent),
    resolve: {
      empleadosDepartamentos: EmpleadosDepartamentosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empleadosDepartamentosRoute;
