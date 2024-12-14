export interface IDepartamentos {
  id: number;
  nombredepartamento?: string | null;
  ubicaciondepartamento?: string | null;
  presupuestodepartamento?: number | null;
}

export type NewDepartamentos = Omit<IDepartamentos, 'id'> & { id: null };
