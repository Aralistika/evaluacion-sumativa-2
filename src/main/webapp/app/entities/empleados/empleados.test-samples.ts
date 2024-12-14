import { IEmpleados, NewEmpleados } from './empleados.model';

export const sampleWithRequiredData: IEmpleados = {
  id: 3830,
};

export const sampleWithPartialData: IEmpleados = {
  id: 8206,
  nombreempleado: 'voluminous snowplow',
  apellidoempleado: 'task',
  telefonoempleado: 'as awful',
};

export const sampleWithFullData: IEmpleados = {
  id: 20788,
  nombreempleado: 'which',
  apellidoempleado: 'drat till utterly',
  telefonoempleado: 'fruitful safely',
  correoempleado: 'stall zowie concerning',
};

export const sampleWithNewData: NewEmpleados = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
