import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TaskService } from './task.service';

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideHttpClient(), provideHttpClientTesting(), TaskService]
    });
    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get tasks by project', () => {
    const mockTasks = [{ id: 1, name: 'Task 1' }];
    service.getByProject(1).subscribe(tasks => expect(tasks).toEqual(mockTasks));
    httpMock.expectOne('http://localhost:8080/api/tasks/project/1').flush(mockTasks);
  });

  it('should create a task', () => {
    const task = { name: 'T1', status: 'TODO' };
    service.create(task).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/tasks');
    expect(req.request.method).toBe('POST');
    req.flush(task);
  });

  it('should update a task', () => {
    const task = { name: 'Updated', status: 'DONE' };
    service.update(1, task).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/tasks/1');
    expect(req.request.method).toBe('PUT');
    req.flush(task);
  });

  it('should assign a task', () => {
    service.assign(1, 2).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/tasks/1/assign');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ userId: 2 });
    req.flush({});
  });
});
