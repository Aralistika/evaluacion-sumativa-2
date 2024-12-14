import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';
import { IJefes } from 'app/entities/jefes/jefes.model';

export interface IDepartamentosJefes {
  id: number;
  departamentos?: IDepartamentos | null;
  jefes?: IJefes | null;
}

export type NewDepartamentosJefes = Omit<IDepartamentosJefes, 'id'> & { id: null };
