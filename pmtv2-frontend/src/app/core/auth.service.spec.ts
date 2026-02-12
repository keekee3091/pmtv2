import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and store user', () => {
    const mockUser = { id: 1, username: 'Alice', email: 'alice@example.com' };

    service.login({ email: 'alice@example.com', password: 'pwd' }).subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockUser);

    service.currentUser$.subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    expect(localStorage.getItem('currentUser')).toBe(JSON.stringify(mockUser));
  });

  it('should register and store user', () => {
    const mockUser = { id: 1, username: 'Alice', email: 'alice@example.com' };

    service.register({ username: 'Alice', email: 'alice@example.com', password: 'pwd' }).subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush(mockUser);

    expect(localStorage.getItem('currentUser')).toBe(JSON.stringify(mockUser));
  });

  it('should logout and clear storage', () => {
    localStorage.setItem('currentUser', JSON.stringify({ id: 1 }));

    service.logout();

    service.currentUser$.subscribe(user => {
      expect(user).toBeNull();
    });

    expect(localStorage.getItem('currentUser')).toBeNull();
  });

  it('should load user from storage', () => {
    const mockUser = { id: 1, username: 'Alice' };
    localStorage.setItem('currentUser', JSON.stringify(mockUser));

    service.loadUserFromStorage();

    service.currentUser$.subscribe(user => {
      expect(user).toEqual(mockUser);
    });
  });

  it('should not load user if storage is empty', () => {
    service.loadUserFromStorage();

    service.currentUser$.subscribe(user => {
      expect(user).toBeNull();
    });
  });
});
