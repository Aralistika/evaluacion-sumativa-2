import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DepartamentosJefesDetailComponent } from './departamentos-jefes-detail.component';

describe('DepartamentosJefes Management Detail Component', () => {
  let comp: DepartamentosJefesDetailComponent;
  let fixture: ComponentFixture<DepartamentosJefesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartamentosJefesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./departamentos-jefes-detail.component').then(m => m.DepartamentosJefesDetailComponent),
              resolve: { departamentosJefes: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DepartamentosJefesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartamentosJefesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load departamentosJefes on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DepartamentosJefesDetailComponent);

      // THEN
      expect(instance.departamentosJefes()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
