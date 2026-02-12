import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { TaskDetailComponent } from './task-detail.component';

describe('TaskDetailComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskDetailComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TaskDetailComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with undefined task', () => {
    const fixture = TestBed.createComponent(TaskDetailComponent);
    expect(fixture.componentInstance.task).toBeUndefined();
  });
});
