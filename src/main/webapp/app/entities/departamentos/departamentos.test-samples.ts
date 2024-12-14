import { IDepartamentos, NewDepartamentos } from './departamentos.model';

export const sampleWithRequiredData: IDepartamentos = {
  id: 24726,
};

export const sampleWithPartialData: IDepartamentos = {
  id: 3591,
  nombredepartamento: 'shallow hospitable zowie',
};

export const sampleWithFullData: IDepartamentos = {
  id: 28380,
  nombredepartamento: 'before',
  ubicaciondepartamento: 'amongst however',
  presupuestodepartamento: 26516.73,
};

export const sampleWithNewData: NewDepartamentos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
