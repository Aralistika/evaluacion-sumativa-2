import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDepartamentosJefes } from '../departamentos-jefes.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../departamentos-jefes.test-samples';

import { DepartamentosJefesService } from './departamentos-jefes.service';

const requireRestSample: IDepartamentosJefes = {
  ...sampleWithRequiredData,
};

describe('DepartamentosJefes Service', () => {
  let service: DepartamentosJefesService;
  let httpMock: HttpTestingController;
  let expectedResult: IDepartamentosJefes | IDepartamentosJefes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DepartamentosJefesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DepartamentosJefes', () => {
      const departamentosJefes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(departamentosJefes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepartamentosJefes', () => {
      const departamentosJefes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(departamentosJefes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepartamentosJefes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepartamentosJefes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DepartamentosJefes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDepartamentosJefesToCollectionIfMissing', () => {
      it('should add a DepartamentosJefes to an empty array', () => {
        const departamentosJefes: IDepartamentosJefes = sampleWithRequiredData;
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing([], departamentosJefes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departamentosJefes);
      });

      it('should not add a DepartamentosJefes to an array that contains it', () => {
        const departamentosJefes: IDepartamentosJefes = sampleWithRequiredData;
        const departamentosJefesCollection: IDepartamentosJefes[] = [
          {
            ...departamentosJefes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing(departamentosJefesCollection, departamentosJefes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepartamentosJefes to an array that doesn't contain it", () => {
        const departamentosJefes: IDepartamentosJefes = sampleWithRequiredData;
        const departamentosJefesCollection: IDepartamentosJefes[] = [sampleWithPartialData];
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing(departamentosJefesCollection, departamentosJefes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departamentosJefes);
      });

      it('should add only unique DepartamentosJefes to an array', () => {
        const departamentosJefesArray: IDepartamentosJefes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const departamentosJefesCollection: IDepartamentosJefes[] = [sampleWithRequiredData];
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing(departamentosJefesCollection, ...departamentosJefesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const departamentosJefes: IDepartamentosJefes = sampleWithRequiredData;
        const departamentosJefes2: IDepartamentosJefes = sampleWithPartialData;
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing([], departamentosJefes, departamentosJefes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departamentosJefes);
        expect(expectedResult).toContain(departamentosJefes2);
      });

      it('should accept null and undefined values', () => {
        const departamentosJefes: IDepartamentosJefes = sampleWithRequiredData;
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing([], null, departamentosJefes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departamentosJefes);
      });

      it('should return initial array if no DepartamentosJefes is added', () => {
        const departamentosJefesCollection: IDepartamentosJefes[] = [sampleWithRequiredData];
        expectedResult = service.addDepartamentosJefesToCollectionIfMissing(departamentosJefesCollection, undefined, null);
        expect(expectedResult).toEqual(departamentosJefesCollection);
      });
    });

    describe('compareDepartamentosJefes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDepartamentosJefes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDepartamentosJefes(entity1, entity2);
        const compareResult2 = service.compareDepartamentosJefes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDepartamentosJefes(entity1, entity2);
        const compareResult2 = service.compareDepartamentosJefes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDepartamentosJefes(entity1, entity2);
        const compareResult2 = service.compareDepartamentosJefes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
