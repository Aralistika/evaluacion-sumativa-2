import { IDepartamentosJefes, NewDepartamentosJefes } from './departamentos-jefes.model';

export const sampleWithRequiredData: IDepartamentosJefes = {
  id: 14863,
};

export const sampleWithPartialData: IDepartamentosJefes = {
  id: 8482,
};

export const sampleWithFullData: IDepartamentosJefes = {
  id: 11609,
};

export const sampleWithNewData: NewDepartamentosJefes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
