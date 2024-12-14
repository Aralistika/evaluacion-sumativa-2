import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'evsum2App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'empleados',
    data: { pageTitle: 'evsum2App.empleados.home.title' },
    loadChildren: () => import('./empleados/empleados.routes'),
  },
  {
    path: 'departamentos',
    data: { pageTitle: 'evsum2App.departamentos.home.title' },
    loadChildren: () => import('./departamentos/departamentos.routes'),
  },
  {
    path: 'jefes',
    data: { pageTitle: 'evsum2App.jefes.home.title' },
    loadChildren: () => import('./jefes/jefes.routes'),
  },
  {
    path: 'empleados-departamentos',
    data: { pageTitle: 'evsum2App.empleadosDepartamentos.home.title' },
    loadChildren: () => import('./empleados-departamentos/empleados-departamentos.routes'),
  },
  {
    path: 'departamentos-jefes',
    data: { pageTitle: 'evsum2App.departamentosJefes.home.title' },
    loadChildren: () => import('./departamentos-jefes/departamentos-jefes.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
