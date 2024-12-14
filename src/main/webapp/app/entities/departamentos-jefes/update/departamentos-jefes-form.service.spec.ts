import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../departamentos-jefes.test-samples';

import { DepartamentosJefesFormService } from './departamentos-jefes-form.service';

describe('DepartamentosJefes Form Service', () => {
  let service: DepartamentosJefesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepartamentosJefesFormService);
  });

  describe('Service methods', () => {
    describe('createDepartamentosJefesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDepartamentosJefesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            departamentos: expect.any(Object),
            jefes: expect.any(Object),
          }),
        );
      });

      it('passing IDepartamentosJefes should create a new form with FormGroup', () => {
        const formGroup = service.createDepartamentosJefesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            departamentos: expect.any(Object),
            jefes: expect.any(Object),
          }),
        );
      });
    });

    describe('getDepartamentosJefes', () => {
      it('should return NewDepartamentosJefes for default DepartamentosJefes initial value', () => {
        const formGroup = service.createDepartamentosJefesFormGroup(sampleWithNewData);

        const departamentosJefes = service.getDepartamentosJefes(formGroup) as any;

        expect(departamentosJefes).toMatchObject(sampleWithNewData);
      });

      it('should return NewDepartamentosJefes for empty DepartamentosJefes initial value', () => {
        const formGroup = service.createDepartamentosJefesFormGroup();

        const departamentosJefes = service.getDepartamentosJefes(formGroup) as any;

        expect(departamentosJefes).toMatchObject({});
      });

      it('should return IDepartamentosJefes', () => {
        const formGroup = service.createDepartamentosJefesFormGroup(sampleWithRequiredData);

        const departamentosJefes = service.getDepartamentosJefes(formGroup) as any;

        expect(departamentosJefes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDepartamentosJefes should not enable id FormControl', () => {
        const formGroup = service.createDepartamentosJefesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDepartamentosJefes should disable id FormControl', () => {
        const formGroup = service.createDepartamentosJefesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
