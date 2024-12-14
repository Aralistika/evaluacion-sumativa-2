jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DepartamentosJefesService } from '../service/departamentos-jefes.service';

import { DepartamentosJefesDeleteDialogComponent } from './departamentos-jefes-delete-dialog.component';

describe('DepartamentosJefes Management Delete Component', () => {
  let comp: DepartamentosJefesDeleteDialogComponent;
  let fixture: ComponentFixture<DepartamentosJefesDeleteDialogComponent>;
  let service: DepartamentosJefesService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DepartamentosJefesDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(DepartamentosJefesDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepartamentosJefesDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DepartamentosJefesService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
