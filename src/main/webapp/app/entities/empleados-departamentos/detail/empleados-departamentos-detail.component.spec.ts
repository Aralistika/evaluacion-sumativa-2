import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmpleadosDepartamentosDetailComponent } from './empleados-departamentos-detail.component';

describe('EmpleadosDepartamentos Management Detail Component', () => {
  let comp: EmpleadosDepartamentosDetailComponent;
  let fixture: ComponentFixture<EmpleadosDepartamentosDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpleadosDepartamentosDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./empleados-departamentos-detail.component').then(m => m.EmpleadosDepartamentosDetailComponent),
              resolve: { empleadosDepartamentos: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EmpleadosDepartamentosDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpleadosDepartamentosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load empleadosDepartamentos on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EmpleadosDepartamentosDetailComponent);

      // THEN
      expect(instance.empleadosDepartamentos()).toEqual(expect.objectContaining({ id: 123 }));
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
