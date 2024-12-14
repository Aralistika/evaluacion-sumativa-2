import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosService } from '../service/empleados-departamentos.service';

import empleadosDepartamentosResolve from './empleados-departamentos-routing-resolve.service';

describe('EmpleadosDepartamentos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: EmpleadosDepartamentosService;
  let resultEmpleadosDepartamentos: IEmpleadosDepartamentos | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(EmpleadosDepartamentosService);
    resultEmpleadosDepartamentos = undefined;
  });

  describe('resolve', () => {
    it('should return IEmpleadosDepartamentos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        empleadosDepartamentosResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultEmpleadosDepartamentos = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultEmpleadosDepartamentos).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        empleadosDepartamentosResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultEmpleadosDepartamentos = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultEmpleadosDepartamentos).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IEmpleadosDepartamentos>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        empleadosDepartamentosResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultEmpleadosDepartamentos = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultEmpleadosDepartamentos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
