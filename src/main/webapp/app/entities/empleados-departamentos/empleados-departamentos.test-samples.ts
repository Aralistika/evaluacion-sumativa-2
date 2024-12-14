import { IEmpleadosDepartamentos, NewEmpleadosDepartamentos } from './empleados-departamentos.model';

export const sampleWithRequiredData: IEmpleadosDepartamentos = {
  id: 15496,
};

export const sampleWithPartialData: IEmpleadosDepartamentos = {
  id: 32590,
};

export const sampleWithFullData: IEmpleadosDepartamentos = {
  id: 18961,
};

export const sampleWithNewData: NewEmpleadosDepartamentos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
