import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { ProjectListComponent } from './project-list.component';

describe('ProjectListComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectListComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(ProjectListComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with empty projects', () => {
    const fixture = TestBed.createComponent(ProjectListComponent);
    expect(fixture.componentInstance.projects).toEqual([]);
  });
});
