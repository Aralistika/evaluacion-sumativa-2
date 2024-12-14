import { IJefes, NewJefes } from './jefes.model';

export const sampleWithRequiredData: IJefes = {
  id: 15850,
};

export const sampleWithPartialData: IJefes = {
  id: 29927,
  telefonojefe: 'outside gee',
};

export const sampleWithFullData: IJefes = {
  id: 22606,
  nombrejefe: 'gloomy see',
  telefonojefe: 'yippee because outside',
};

export const sampleWithNewData: NewJefes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
