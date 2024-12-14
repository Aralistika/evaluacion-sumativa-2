import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EmpleadosService } from '../service/empleados.service';
import { IEmpleados } from '../empleados.model';
import { EmpleadosFormService } from './empleados-form.service';

import { EmpleadosUpdateComponent } from './empleados-update.component';

describe('Empleados Management Update Component', () => {
  let comp: EmpleadosUpdateComponent;
  let fixture: ComponentFixture<EmpleadosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadosFormService: EmpleadosFormService;
  let empleadosService: EmpleadosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadosUpdateComponent],
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
      .overrideTemplate(EmpleadosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadosFormService = TestBed.inject(EmpleadosFormService);
    empleadosService = TestBed.inject(EmpleadosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const empleados: IEmpleados = { id: 456 };

      activatedRoute.data = of({ empleados });
      comp.ngOnInit();

      expect(comp.empleados).toEqual(empleados);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleados>>();
      const empleados = { id: 123 };
      jest.spyOn(empleadosFormService, 'getEmpleados').mockReturnValue(empleados);
      jest.spyOn(empleadosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleados });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleados }));
      saveSubject.complete();

      // THEN
      expect(empleadosFormService.getEmpleados).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadosService.update).toHaveBeenCalledWith(expect.objectContaining(empleados));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleados>>();
      const empleados = { id: 123 };
      jest.spyOn(empleadosFormService, 'getEmpleados').mockReturnValue({ id: null });
      jest.spyOn(empleadosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleados: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleados }));
      saveSubject.complete();

      // THEN
      expect(empleadosFormService.getEmpleados).toHaveBeenCalled();
      expect(empleadosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleados>>();
      const empleados = { id: 123 };
      jest.spyOn(empleadosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleados });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
