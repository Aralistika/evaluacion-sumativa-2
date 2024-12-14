import { IEmpleados } from 'app/entities/empleados/empleados.model';
import { IDepartamentos } from 'app/entities/departamentos/departamentos.model';

export interface IEmpleadosDepartamentos {
  id: number;
  empleados?: IEmpleados | null;
  departamentos?: IDepartamentos | null;
}

export type NewEmpleadosDepartamentos = Omit<IEmpleadosDepartamentos, 'id'> & { id: null };
