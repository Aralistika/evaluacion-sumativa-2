import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDepartamentos, NewDepartamentos } from '../departamentos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartamentos for edit and NewDepartamentosFormGroupInput for create.
 */
type DepartamentosFormGroupInput = IDepartamentos | PartialWithRequiredKeyOf<NewDepartamentos>;

type DepartamentosFormDefaults = Pick<NewDepartamentos, 'id'>;

type DepartamentosFormGroupContent = {
  id: FormControl<IDepartamentos['id'] | NewDepartamentos['id']>;
  nombredepartamento: FormControl<IDepartamentos['nombredepartamento']>;
  ubicaciondepartamento: FormControl<IDepartamentos['ubicaciondepartamento']>;
  presupuestodepartamento: FormControl<IDepartamentos['presupuestodepartamento']>;
};

export type DepartamentosFormGroup = FormGroup<DepartamentosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepartamentosFormService {
  createDepartamentosFormGroup(departamentos: DepartamentosFormGroupInput = { id: null }): DepartamentosFormGroup {
    const departamentosRawValue = {
      ...this.getFormDefaults(),
      ...departamentos,
    };
    return new FormGroup<DepartamentosFormGroupContent>({
      id: new FormControl(
        { value: departamentosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombredepartamento: new FormControl(departamentosRawValue.nombredepartamento),
      ubicaciondepartamento: new FormControl(departamentosRawValue.ubicaciondepartamento),
      presupuestodepartamento: new FormControl(departamentosRawValue.presupuestodepartamento),
    });
  }

  getDepartamentos(form: DepartamentosFormGroup): IDepartamentos | NewDepartamentos {
    return form.getRawValue() as IDepartamentos | NewDepartamentos;
  }

  resetForm(form: DepartamentosFormGroup, departamentos: DepartamentosFormGroupInput): void {
    const departamentosRawValue = { ...this.getFormDefaults(), ...departamentos };
    form.reset(
      {
        ...departamentosRawValue,
        id: { value: departamentosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepartamentosFormDefaults {
    return {
      id: null,
    };
  }
}
