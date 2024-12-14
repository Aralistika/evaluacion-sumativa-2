import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartamentosJefes, NewDepartamentosJefes } from '../departamentos-jefes.model';

export type PartialUpdateDepartamentosJefes = Partial<IDepartamentosJefes> & Pick<IDepartamentosJefes, 'id'>;

export type EntityResponseType = HttpResponse<IDepartamentosJefes>;
export type EntityArrayResponseType = HttpResponse<IDepartamentosJefes[]>;

@Injectable({ providedIn: 'root' })
export class DepartamentosJefesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/departamentos-jefes');

  create(departamentosJefes: NewDepartamentosJefes): Observable<EntityResponseType> {
    return this.http.post<IDepartamentosJefes>(this.resourceUrl, departamentosJefes, { observe: 'response' });
  }

  update(departamentosJefes: IDepartamentosJefes): Observable<EntityResponseType> {
    return this.http.put<IDepartamentosJefes>(
      `${this.resourceUrl}/${this.getDepartamentosJefesIdentifier(departamentosJefes)}`,
      departamentosJefes,
      { observe: 'response' },
    );
  }

  partialUpdate(departamentosJefes: PartialUpdateDepartamentosJefes): Observable<EntityResponseType> {
    return this.http.patch<IDepartamentosJefes>(
      `${this.resourceUrl}/${this.getDepartamentosJefesIdentifier(departamentosJefes)}`,
      departamentosJefes,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartamentosJefes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartamentosJefes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDepartamentosJefesIdentifier(departamentosJefes: Pick<IDepartamentosJefes, 'id'>): number {
    return departamentosJefes.id;
  }

  compareDepartamentosJefes(o1: Pick<IDepartamentosJefes, 'id'> | null, o2: Pick<IDepartamentosJefes, 'id'> | null): boolean {
    return o1 && o2 ? this.getDepartamentosJefesIdentifier(o1) === this.getDepartamentosJefesIdentifier(o2) : o1 === o2;
  }

  addDepartamentosJefesToCollectionIfMissing<Type extends Pick<IDepartamentosJefes, 'id'>>(
    departamentosJefesCollection: Type[],
    ...departamentosJefesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const departamentosJefes: Type[] = departamentosJefesToCheck.filter(isPresent);
    if (departamentosJefes.length > 0) {
      const departamentosJefesCollectionIdentifiers = departamentosJefesCollection.map(departamentosJefesItem =>
        this.getDepartamentosJefesIdentifier(departamentosJefesItem),
      );
      const departamentosJefesToAdd = departamentosJefes.filter(departamentosJefesItem => {
        const departamentosJefesIdentifier = this.getDepartamentosJefesIdentifier(departamentosJefesItem);
        if (departamentosJefesCollectionIdentifiers.includes(departamentosJefesIdentifier)) {
          return false;
        }
        departamentosJefesCollectionIdentifiers.push(departamentosJefesIdentifier);
        return true;
      });
      return [...departamentosJefesToAdd, ...departamentosJefesCollection];
    }
    return departamentosJefesCollection;
  }
}
