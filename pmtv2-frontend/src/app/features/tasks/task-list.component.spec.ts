import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { TaskListComponent } from './task-list.component';

describe('TaskListComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskListComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TaskListComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with empty tasks', () => {
    const fixture = TestBed.createComponent(TaskListComponent);
    expect(fixture.componentInstance.tasks).toEqual([]);
  });
});
