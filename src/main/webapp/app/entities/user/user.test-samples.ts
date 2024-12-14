import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 20892,
  login: 'DM0',
};

export const sampleWithPartialData: IUser = {
  id: 12696,
  login: 'YTz',
};

export const sampleWithFullData: IUser = {
  id: 32163,
  login: 'hz',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
