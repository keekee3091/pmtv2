import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { AuthComponent } from './auth.component';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';

describe('AuthComponent', () => {
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      login: jasmine.createSpy('login').and.returnValue(of({ id: 1, username: 'Alice' })),
      register: jasmine.createSpy('register').and.returnValue(of({ id: 1, username: 'Alice' }))
    };

    await TestBed.configureTestingModule({
      imports: [AuthComponent],
      providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should default to login mode', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    expect(fixture.componentInstance.isLoginMode).toBeTrue();
  });

  it('should toggle mode', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    const event = new Event('click');
    spyOn(event, 'preventDefault');
    fixture.componentInstance.toggleMode(event);
    expect(fixture.componentInstance.isLoginMode).toBeFalse();
    fixture.componentInstance.toggleMode(event);
    expect(fixture.componentInstance.isLoginMode).toBeTrue();
  });

  it('should not submit if form is invalid', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    fixture.componentInstance.onSubmit();
    expect(authServiceMock.login).not.toHaveBeenCalled();
    expect(authServiceMock.register).not.toHaveBeenCalled();
  });

  it('should call login on submit in login mode', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    const router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.componentInstance.form.setValue({ username: '', email: 'alice@example.com', password: 'pwd' });
    fixture.componentInstance.isLoginMode = true;
    fixture.componentInstance.onSubmit();
    expect(authServiceMock.login).toHaveBeenCalledWith({ email: 'alice@example.com', password: 'pwd' });
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should call register on submit in register mode', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    const router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.componentInstance.form.setValue({ username: 'Alice', email: 'alice@example.com', password: 'pwd' });
    fixture.componentInstance.isLoginMode = false;
    fixture.componentInstance.onSubmit();
    expect(authServiceMock.register).toHaveBeenCalledWith({ username: 'Alice', email: 'alice@example.com', password: 'pwd' });
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should handle login error', () => {
    authServiceMock.login.and.returnValue(throwError(() => new Error('Unauthorized')));
    const fixture = TestBed.createComponent(AuthComponent);
    spyOn(console, 'error');
    fixture.componentInstance.form.setValue({ username: '', email: 'alice@example.com', password: 'wrong' });
    fixture.componentInstance.isLoginMode = true;
    fixture.componentInstance.onSubmit();
    expect(console.error).toHaveBeenCalled();
  });

  it('should handle register error', () => {
    authServiceMock.register.and.returnValue(throwError(() => new Error('Bad request')));
    const fixture = TestBed.createComponent(AuthComponent);
    spyOn(console, 'error');
    fixture.componentInstance.form.setValue({ username: 'Alice', email: 'alice@example.com', password: 'pwd' });
    fixture.componentInstance.isLoginMode = false;
    fixture.componentInstance.onSubmit();
    expect(console.error).toHaveBeenCalled();
  });
});
