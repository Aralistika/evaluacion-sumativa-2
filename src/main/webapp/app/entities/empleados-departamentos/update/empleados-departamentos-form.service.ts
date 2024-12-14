import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmpleadosDepartamentos, NewEmpleadosDepartamentos } from '../empleados-departamentos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleadosDepartamentos for edit and NewEmpleadosDepartamentosFormGroupInput for create.
 */
type EmpleadosDepartamentosFormGroupInput = IEmpleadosDepartamentos | PartialWithRequiredKeyOf<NewEmpleadosDepartamentos>;

type EmpleadosDepartamentosFormDefaults = Pick<NewEmpleadosDepartamentos, 'id'>;

type EmpleadosDepartamentosFormGroupContent = {
  id: FormControl<IEmpleadosDepartamentos['id'] | NewEmpleadosDepartamentos['id']>;
  empleados: FormControl<IEmpleadosDepartamentos['empleados']>;
  departamentos: FormControl<IEmpleadosDepartamentos['departamentos']>;
};

export type EmpleadosDepartamentosFormGroup = FormGroup<EmpleadosDepartamentosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadosDepartamentosFormService {
  createEmpleadosDepartamentosFormGroup(
    empleadosDepartamentos: EmpleadosDepartamentosFormGroupInput = { id: null },
  ): EmpleadosDepartamentosFormGroup {
    const empleadosDepartamentosRawValue = {
      ...this.getFormDefaults(),
      ...empleadosDepartamentos,
    };
    return new FormGroup<EmpleadosDepartamentosFormGroupContent>({
      id: new FormControl(
        { value: empleadosDepartamentosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      empleados: new FormControl(empleadosDepartamentosRawValue.empleados),
      departamentos: new FormControl(empleadosDepartamentosRawValue.departamentos),
    });
  }

  getEmpleadosDepartamentos(form: EmpleadosDepartamentosFormGroup): IEmpleadosDepartamentos | NewEmpleadosDepartamentos {
    return form.getRawValue() as IEmpleadosDepartamentos | NewEmpleadosDepartamentos;
  }

  resetForm(form: EmpleadosDepartamentosFormGroup, empleadosDepartamentos: EmpleadosDepartamentosFormGroupInput): void {
    const empleadosDepartamentosRawValue = { ...this.getFormDefaults(), ...empleadosDepartamentos };
    form.reset(
      {
        ...empleadosDepartamentosRawValue,
        id: { value: empleadosDepartamentosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpleadosDepartamentosFormDefaults {
    return {
      id: null,
    };
  }
}
