import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DepartamentosJefesResolve from './route/departamentos-jefes-routing-resolve.service';

const departamentosJefesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/departamentos-jefes.component').then(m => m.DepartamentosJefesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/departamentos-jefes-detail.component').then(m => m.DepartamentosJefesDetailComponent),
    resolve: {
      departamentosJefes: DepartamentosJefesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/departamentos-jefes-update.component').then(m => m.DepartamentosJefesUpdateComponent),
    resolve: {
      departamentosJefes: DepartamentosJefesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/departamentos-jefes-update.component').then(m => m.DepartamentosJefesUpdateComponent),
    resolve: {
      departamentosJefes: DepartamentosJefesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default departamentosJefesRoute;
