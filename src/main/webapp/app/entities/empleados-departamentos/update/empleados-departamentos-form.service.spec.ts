import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../empleados-departamentos.test-samples';

import { EmpleadosDepartamentosFormService } from './empleados-departamentos-form.service';

describe('EmpleadosDepartamentos Form Service', () => {
  let service: EmpleadosDepartamentosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadosDepartamentosFormService);
  });

  describe('Service methods', () => {
    describe('createEmpleadosDepartamentosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            empleados: expect.any(Object),
            departamentos: expect.any(Object),
          }),
        );
      });

      it('passing IEmpleadosDepartamentos should create a new form with FormGroup', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            empleados: expect.any(Object),
            departamentos: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmpleadosDepartamentos', () => {
      it('should return NewEmpleadosDepartamentos for default EmpleadosDepartamentos initial value', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup(sampleWithNewData);

        const empleadosDepartamentos = service.getEmpleadosDepartamentos(formGroup) as any;

        expect(empleadosDepartamentos).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmpleadosDepartamentos for empty EmpleadosDepartamentos initial value', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup();

        const empleadosDepartamentos = service.getEmpleadosDepartamentos(formGroup) as any;

        expect(empleadosDepartamentos).toMatchObject({});
      });

      it('should return IEmpleadosDepartamentos', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup(sampleWithRequiredData);

        const empleadosDepartamentos = service.getEmpleadosDepartamentos(formGroup) as any;

        expect(empleadosDepartamentos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmpleadosDepartamentos should not enable id FormControl', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmpleadosDepartamentos should disable id FormControl', () => {
        const formGroup = service.createEmpleadosDepartamentosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
