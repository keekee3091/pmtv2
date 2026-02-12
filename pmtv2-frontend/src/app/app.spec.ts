import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { AppComponent } from './app.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AuthService } from './core/auth.service';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

describe('AppComponent', () => {
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      currentUser$: new BehaviorSubject(null),
      loadUserFromStorage: jasmine.createSpy('loadUserFromStorage'),
      logout: jasmine.createSpy('logout')
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call loadUserFromStorage on construction', () => {
    TestBed.createComponent(AppComponent);
    expect(authServiceMock.loadUserFromStorage).toHaveBeenCalled();
  });

  it('should call logout and navigate to /auth', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.componentInstance.logout();
    expect(authServiceMock.logout).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/auth']);
  });

  it('should set userId when user is logged in via autoLog', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    authServiceMock.currentUser$.next({ id: 42, username: 'Test' });
    fixture.componentInstance.ngOnInit();
    expect(fixture.componentInstance.userId).toBe(42);
  });
});
