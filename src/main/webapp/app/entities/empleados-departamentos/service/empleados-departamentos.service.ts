import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpleadosDepartamentos, NewEmpleadosDepartamentos } from '../empleados-departamentos.model';

export type PartialUpdateEmpleadosDepartamentos = Partial<IEmpleadosDepartamentos> & Pick<IEmpleadosDepartamentos, 'id'>;

export type EntityResponseType = HttpResponse<IEmpleadosDepartamentos>;
export type EntityArrayResponseType = HttpResponse<IEmpleadosDepartamentos[]>;

@Injectable({ providedIn: 'root' })
export class EmpleadosDepartamentosService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empleados-departamentos');

  create(empleadosDepartamentos: NewEmpleadosDepartamentos): Observable<EntityResponseType> {
    return this.http.post<IEmpleadosDepartamentos>(this.resourceUrl, empleadosDepartamentos, { observe: 'response' });
  }

  update(empleadosDepartamentos: IEmpleadosDepartamentos): Observable<EntityResponseType> {
    return this.http.put<IEmpleadosDepartamentos>(
      `${this.resourceUrl}/${this.getEmpleadosDepartamentosIdentifier(empleadosDepartamentos)}`,
      empleadosDepartamentos,
      { observe: 'response' },
    );
  }

  partialUpdate(empleadosDepartamentos: PartialUpdateEmpleadosDepartamentos): Observable<EntityResponseType> {
    return this.http.patch<IEmpleadosDepartamentos>(
      `${this.resourceUrl}/${this.getEmpleadosDepartamentosIdentifier(empleadosDepartamentos)}`,
      empleadosDepartamentos,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmpleadosDepartamentos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmpleadosDepartamentos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmpleadosDepartamentosIdentifier(empleadosDepartamentos: Pick<IEmpleadosDepartamentos, 'id'>): number {
    return empleadosDepartamentos.id;
  }

  compareEmpleadosDepartamentos(o1: Pick<IEmpleadosDepartamentos, 'id'> | null, o2: Pick<IEmpleadosDepartamentos, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpleadosDepartamentosIdentifier(o1) === this.getEmpleadosDepartamentosIdentifier(o2) : o1 === o2;
  }

  addEmpleadosDepartamentosToCollectionIfMissing<Type extends Pick<IEmpleadosDepartamentos, 'id'>>(
    empleadosDepartamentosCollection: Type[],
    ...empleadosDepartamentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empleadosDepartamentos: Type[] = empleadosDepartamentosToCheck.filter(isPresent);
    if (empleadosDepartamentos.length > 0) {
      const empleadosDepartamentosCollectionIdentifiers = empleadosDepartamentosCollection.map(empleadosDepartamentosItem =>
        this.getEmpleadosDepartamentosIdentifier(empleadosDepartamentosItem),
      );
      const empleadosDepartamentosToAdd = empleadosDepartamentos.filter(empleadosDepartamentosItem => {
        const empleadosDepartamentosIdentifier = this.getEmpleadosDepartamentosIdentifier(empleadosDepartamentosItem);
        if (empleadosDepartamentosCollectionIdentifiers.includes(empleadosDepartamentosIdentifier)) {
          return false;
        }
        empleadosDepartamentosCollectionIdentifiers.push(empleadosDepartamentosIdentifier);
        return true;
      });
      return [...empleadosDepartamentosToAdd, ...empleadosDepartamentosCollection];
    }
    return empleadosDepartamentosCollection;
  }
}
