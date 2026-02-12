import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideHttpClient(), provideHttpClientTesting(), UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all users', () => {
    const mockUsers = [{ id: 1, username: 'Alice', email: 'a@a.com' }];
    service.getAll().subscribe(users => expect(users).toEqual(mockUsers));
    httpMock.expectOne('http://localhost:8080/api/users').flush(mockUsers);
  });

  it('should get user by id', () => {
    const mockUser = { id: 1, username: 'Alice', email: 'a@a.com' };
    service.getById(1).subscribe(user => expect(user).toEqual(mockUser));
    httpMock.expectOne('http://localhost:8080/api/users/1').flush(mockUser);
  });

  it('should create user', () => {
    const newUser = { username: 'Bob', email: 'b@b.com' } as any;
    service.create(newUser).subscribe(user => expect(user.username).toBe('Bob'));
    const req = httpMock.expectOne('http://localhost:8080/api/users');
    expect(req.request.method).toBe('POST');
    req.flush(newUser);
  });

  it('should update user', () => {
    const updated = { id: 1, username: 'Updated', email: 'a@a.com' } as any;
    service.update(1, updated).subscribe(user => expect(user.username).toBe('Updated'));
    const req = httpMock.expectOne('http://localhost:8080/api/users/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updated);
  });

  it('should update password', () => {
    service.updatePassword(1, 'newpwd').subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/users/1/password');
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ password: 'newpwd' });
    req.flush({});
  });

  it('should delete user', () => {
    service.delete(1).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/users/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
