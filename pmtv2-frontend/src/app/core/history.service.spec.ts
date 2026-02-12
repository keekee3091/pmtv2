import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { HistoryService } from './history.service';

describe('HistoryService', () => {
  let service: HistoryService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideHttpClient(), provideHttpClientTesting(), HistoryService]
    });
    service = TestBed.inject(HistoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get history by task', () => {
    const mockHistory = [{ id: 1, oldValue: 'TODO', newValue: 'DONE' }];
    service.getByTask(1).subscribe(history => expect(history).toEqual(mockHistory));
    httpMock.expectOne('http://localhost:8080/api/history/task/1').flush(mockHistory);
  });
});
