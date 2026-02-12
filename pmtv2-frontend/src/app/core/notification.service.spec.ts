import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let service: NotificationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideHttpClient(), provideHttpClientTesting(), NotificationService]
    });
    service = TestBed.inject(NotificationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get notifications by user', () => {
    const mockNotifs = [{ id: 1, message: 'Task assigned' }];
    service.getByUser(1).subscribe(notifs => expect(notifs).toEqual(mockNotifs));
    httpMock.expectOne('http://localhost:8080/api/notifications/user/1').flush(mockNotifs);
  });

  it('should mark notification as read', () => {
    service.markAsRead(1).subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/notifications/1/read');
    expect(req.request.method).toBe('PATCH');
    req.flush({});
  });
});
