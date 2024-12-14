import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDepartamentosJefes, NewDepartamentosJefes } from '../departamentos-jefes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartamentosJefes for edit and NewDepartamentosJefesFormGroupInput for create.
 */
type DepartamentosJefesFormGroupInput = IDepartamentosJefes | PartialWithRequiredKeyOf<NewDepartamentosJefes>;

type DepartamentosJefesFormDefaults = Pick<NewDepartamentosJefes, 'id'>;

type DepartamentosJefesFormGroupContent = {
  id: FormControl<IDepartamentosJefes['id'] | NewDepartamentosJefes['id']>;
  departamentos: FormControl<IDepartamentosJefes['departamentos']>;
  jefes: FormControl<IDepartamentosJefes['jefes']>;
};

export type DepartamentosJefesFormGroup = FormGroup<DepartamentosJefesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepartamentosJefesFormService {
  createDepartamentosJefesFormGroup(departamentosJefes: DepartamentosJefesFormGroupInput = { id: null }): DepartamentosJefesFormGroup {
    const departamentosJefesRawValue = {
      ...this.getFormDefaults(),
      ...departamentosJefes,
    };
    return new FormGroup<DepartamentosJefesFormGroupContent>({
      id: new FormControl(
        { value: departamentosJefesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      departamentos: new FormControl(departamentosJefesRawValue.departamentos),
      jefes: new FormControl(departamentosJefesRawValue.jefes),
    });
  }

  getDepartamentosJefes(form: DepartamentosJefesFormGroup): IDepartamentosJefes | NewDepartamentosJefes {
    return form.getRawValue() as IDepartamentosJefes | NewDepartamentosJefes;
  }

  resetForm(form: DepartamentosJefesFormGroup, departamentosJefes: DepartamentosJefesFormGroupInput): void {
    const departamentosJefesRawValue = { ...this.getFormDefaults(), ...departamentosJefes };
    form.reset(
      {
        ...departamentosJefesRawValue,
        id: { value: departamentosJefesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepartamentosJefesFormDefaults {
    return {
      id: null,
    };
  }
}
