import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '0421318c-e687-459a-ab4e-366b7efb63c8',
};

export const sampleWithPartialData: IAuthority = {
  name: '93ba320a-1516-4e83-9b7f-b799eb10a8b0',
};

export const sampleWithFullData: IAuthority = {
  name: 'e9aa1378-6b53-48c7-a74d-e6f699cd07cd',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
