export interface IJefes {
  id: number;
  nombrejefe?: string | null;
  telefonojefe?: string | null;
}

export type NewJefes = Omit<IJefes, 'id'> & { id: null };
