import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';
import { DepartamentosService } from 'app/entities/departamentos/service/departamentos.service';
import { IJefes } from 'app/entities/jefes/jefes.model';
import { JefesService } from 'app/entities/jefes/service/jefes.service';
import { IDepartamentosJefes } from '../departamentos-jefes.model';
import { DepartamentosJefesService } from '../service/departamentos-jefes.service';
import { DepartamentosJefesFormService } from './departamentos-jefes-form.service';

import { DepartamentosJefesUpdateComponent } from './departamentos-jefes-update.component';

describe('DepartamentosJefes Management Update Component', () => {
  let comp: DepartamentosJefesUpdateComponent;
  let fixture: ComponentFixture<DepartamentosJefesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departamentosJefesFormService: DepartamentosJefesFormService;
  let departamentosJefesService: DepartamentosJefesService;
  let departamentosService: DepartamentosService;
  let jefesService: JefesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DepartamentosJefesUpdateComponent],
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
      .overrideTemplate(DepartamentosJefesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartamentosJefesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departamentosJefesFormService = TestBed.inject(DepartamentosJefesFormService);
    departamentosJefesService = TestBed.inject(DepartamentosJefesService);
    departamentosService = TestBed.inject(DepartamentosService);
    jefesService = TestBed.inject(JefesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departamentos query and add missing value', () => {
      const departamentosJefes: IDepartamentosJefes = { id: 456 };
      const departamentos: IDepartamentos = { id: 5673 };
      departamentosJefes.departamentos = departamentos;

      const departamentosCollection: IDepartamentos[] = [{ id: 4190 }];
      jest.spyOn(departamentosService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentosCollection })));
      const additionalDepartamentos = [departamentos];
      const expectedCollection: IDepartamentos[] = [...additionalDepartamentos, ...departamentosCollection];
      jest.spyOn(departamentosService, 'addDepartamentosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departamentosJefes });
      comp.ngOnInit();

      expect(departamentosService.query).toHaveBeenCalled();
      expect(departamentosService.addDepartamentosToCollectionIfMissing).toHaveBeenCalledWith(
        departamentosCollection,
        ...additionalDepartamentos.map(expect.objectContaining),
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Jefes query and add missing value', () => {
      const departamentosJefes: IDepartamentosJefes = { id: 456 };
      const jefes: IJefes = { id: 17454 };
      departamentosJefes.jefes = jefes;

      const jefesCollection: IJefes[] = [{ id: 24570 }];
      jest.spyOn(jefesService, 'query').mockReturnValue(of(new HttpResponse({ body: jefesCollection })));
      const additionalJefes = [jefes];
      const expectedCollection: IJefes[] = [...additionalJefes, ...jefesCollection];
      jest.spyOn(jefesService, 'addJefesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departamentosJefes });
      comp.ngOnInit();

      expect(jefesService.query).toHaveBeenCalled();
      expect(jefesService.addJefesToCollectionIfMissing).toHaveBeenCalledWith(
        jefesCollection,
        ...additionalJefes.map(expect.objectContaining),
      );
      expect(comp.jefesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const departamentosJefes: IDepartamentosJefes = { id: 456 };
      const departamentos: IDepartamentos = { id: 26842 };
      departamentosJefes.departamentos = departamentos;
      const jefes: IJefes = { id: 21405 };
      departamentosJefes.jefes = jefes;

      activatedRoute.data = of({ departamentosJefes });
      comp.ngOnInit();

      expect(comp.departamentosSharedCollection).toContain(departamentos);
      expect(comp.jefesSharedCollection).toContain(jefes);
      expect(comp.departamentosJefes).toEqual(departamentosJefes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentosJefes>>();
      const departamentosJefes = { id: 123 };
      jest.spyOn(departamentosJefesFormService, 'getDepartamentosJefes').mockReturnValue(departamentosJefes);
      jest.spyOn(departamentosJefesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentosJefes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamentosJefes }));
      saveSubject.complete();

      // THEN
      expect(departamentosJefesFormService.getDepartamentosJefes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departamentosJefesService.update).toHaveBeenCalledWith(expect.objectContaining(departamentosJefes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentosJefes>>();
      const departamentosJefes = { id: 123 };
      jest.spyOn(departamentosJefesFormService, 'getDepartamentosJefes').mockReturnValue({ id: null });
      jest.spyOn(departamentosJefesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentosJefes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamentosJefes }));
      saveSubject.complete();

      // THEN
      expect(departamentosJefesFormService.getDepartamentosJefes).toHaveBeenCalled();
      expect(departamentosJefesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamentosJefes>>();
      const departamentosJefes = { id: 123 };
      jest.spyOn(departamentosJefesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamentosJefes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departamentosJefesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartamentos', () => {
      it('Should forward to departamentosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentosService, 'compareDepartamentos');
        comp.compareDepartamentos(entity, entity2);
        expect(departamentosService.compareDepartamentos).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareJefes', () => {
      it('Should forward to jefesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(jefesService, 'compareJefes');
        comp.compareJefes(entity, entity2);
        expect(jefesService.compareJefes).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
