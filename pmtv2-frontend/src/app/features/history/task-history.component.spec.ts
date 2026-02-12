import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { TaskHistoryComponent } from './task-history.component';

describe('TaskHistoryComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskHistoryComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TaskHistoryComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with empty history', () => {
    const fixture = TestBed.createComponent(TaskHistoryComponent);
    expect(fixture.componentInstance.history).toEqual([]);
  });
});
