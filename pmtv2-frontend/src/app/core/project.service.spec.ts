import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ProjectService } from './project.service';

describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideHttpClient(), provideHttpClientTesting(), ProjectService]
    });
    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all projects', () => {
    const mockProjects = [{ id: 1, name: 'P1' }];
    service.getAll().subscribe(projects => expect(projects).toEqual(mockProjects));
    httpMock.expectOne('http://localhost:8080/api/projects').flush(mockProjects);
  });

  it('should create a project', () => {
    const project = { name: 'P1', description: 'desc', startDate: '2025-01-01' };
    service.create(project).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/projects');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(project);
    req.flush(project);
  });

  it('should invite a member', () => {
    service.inviteMember(1, 'bob@example.com').subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/projects/1/invite');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'bob@example.com' });
    req.flush({});
  });

  it('should set role', () => {
    service.setRole(1, 2, 'MEMBER').subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/projects/1/role');
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({ userId: 2, role: 'MEMBER' });
    req.flush({});
  });
});
