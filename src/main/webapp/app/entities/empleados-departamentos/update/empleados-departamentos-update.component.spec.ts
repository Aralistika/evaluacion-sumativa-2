import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpleados } from 'app/entities/empleados/empleados.model';
import { EmpleadosService } from 'app/entities/empleados/service/empleados.service';
import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';
import { DepartamentosService } from 'app/entities/departamentos/service/departamentos.service';
import { IEmpleadosDepartamentos } from '../empleados-departamentos.model';
import { EmpleadosDepartamentosService } from '../service/empleados-departamentos.service';
import { EmpleadosDepartamentosFormService } from './empleados-departamentos-form.service';

import { EmpleadosDepartamentosUpdateComponent } from './empleados-departamentos-update.component';

describe('EmpleadosDepartamentos Management Update Component', () => {
  let comp: EmpleadosDepartamentosUpdateComponent;
  let fixture: ComponentFixture<EmpleadosDepartamentosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadosDepartamentosFormService: EmpleadosDepartamentosFormService;
  let empleadosDepartamentosService: EmpleadosDepartamentosService;
  let empleadosService: EmpleadosService;
  let departamentosService: DepartamentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadosDepartamentosUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmpleadosDepartamentosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadosDepartamentosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadosDepartamentosFormService = TestBed.inject(EmpleadosDepartamentosFormService);
    empleadosDepartamentosService = TestBed.inject(EmpleadosDepartamentosService);
    empleadosService = TestBed.inject(EmpleadosService);
    departamentosService = TestBed.inject(DepartamentosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleados query and add missing value', () => {
      const empleadosDepartamentos: IEmpleadosDepartamentos = { id: 456 };
      const empleados: IEmpleados = { id: 4935 };
      empleadosDepartamentos.empleados = empleados;

      const empleadosCollection: IEmpleados[] = [{ id: 7144 }];
      jest.spyOn(empleadosService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadosCollection })));
      const additionalEmpleados = [empleados];
      const expectedCollection: IEmpleados[] = [...additionalEmpleados, ...empleadosCollection];
      jest.spyOn(empleadosService, 'addEmpleadosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadosDepartamentos });
      comp.ngOnInit();

      expect(empleadosService.query).toHaveBeenCalled();
      expect(empleadosService.addEmpleadosToCollectionIfMissing).toHaveBeenCalledWith(
        empleadosCollection,
        ...additionalEmpleados.map(expect.objectContaining),
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamentos query and add missing value', () => {
      const empleadosDepartamentos: IEmpleadosDepartamentos = { id: 456 };
      const departamentos: IDepartamentos = { id: 4448 };
      empleadosDepartamentos.departamentos = departamentos;

      const departamentosCollection: IDepartamentos[] = [{ id: 702 }];
      jest.spyOn(departamentosService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentosCollection })));
      const additionalDepartamentos = [departamentos];
      const expectedCollection: IDepartamentos[] = [...additionalDepartamentos, ...departamentosCollection];
      jest.spyOn(departamentosService, 'addDepartamentosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadosDepartamentos });
      comp.ngOnInit();

      expect(departamentosService.query).toHaveBeenCalled();
      expect(departamentosService.addDepartamentosToCollectionIfMissing).toHaveBeenCalledWith(
        departamentosCollection,
        ...additionalDepartamentos.map(expect.objectContaining),
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleadosDepartamentos: IEmpleadosDepartamentos = { id: 456 };
      const empleados: IEmpleados = { id: 32495 };
      empleadosDepartamentos.empleados = empleados;
      const departamentos: IDepartamentos = { id: 26736 };
      empleadosDepartamentos.departamentos = departamentos;

      activatedRoute.data = of({ empleadosDepartamentos });
      comp.ngOnInit();

      expect(comp.empleadosSharedCollection).toContain(empleados);
      expect(comp.departamentosSharedCollection).toContain(departamentos);
      expect(comp.empleadosDepartamentos).toEqual(empleadosDepartamentos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadosDepartamentos>>();
      const empleadosDepartamentos = { id: 123 };
      jest.spyOn(empleadosDepartamentosFormService, 'getEmpleadosDepartamentos').mockReturnValue(empleadosDepartamentos);
      jest.spyOn(empleadosDepartamentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadosDepartamentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadosDepartamentos }));
      saveSubject.complete();

      // THEN
      expect(empleadosDepartamentosFormService.getEmpleadosDepartamentos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadosDepartamentosService.update).toHaveBeenCalledWith(expect.objectContaining(empleadosDepartamentos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadosDepartamentos>>();
      const empleadosDepartamentos = { id: 123 };
      jest.spyOn(empleadosDepartamentosFormService, 'getEmpleadosDepartamentos').mockReturnValue({ id: null });
      jest.spyOn(empleadosDepartamentosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadosDepartamentos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadosDepartamentos }));
      saveSubject.complete();

      // THEN
      expect(empleadosDepartamentosFormService.getEmpleadosDepartamentos).toHaveBeenCalled();
      expect(empleadosDepartamentosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadosDepartamentos>>();
      const empleadosDepartamentos = { id: 123 };
      jest.spyOn(empleadosDepartamentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadosDepartamentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadosDepartamentosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmpleados', () => {
      it('Should forward to empleadosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(empleadosService, 'compareEmpleados');
        comp.compareEmpleados(entity, entity2);
        expect(empleadosService.compareEmpleados).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartamentos', () => {
      it('Should forward to departamentosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentosService, 'compareDepartamentos');
        comp.compareDepartamentos(entity, entity2);
        expect(departamentosService.compareDepartamentos).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
