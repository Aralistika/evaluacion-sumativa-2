export interface IEmpleados {
  id: number;
  nombreempleado?: string | null;
  apellidoempleado?: string | null;
  telefonoempleado?: string | null;
  correoempleado?: string | null;
}

export type NewEmpleados = Omit<IEmpleados, 'id'> & { id: null };
