import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { ProjectDetailComponent } from './project-detail.component';

describe('ProjectDetailComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectDetailComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(ProjectDetailComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with empty members', () => {
    const fixture = TestBed.createComponent(ProjectDetailComponent);
    expect(fixture.componentInstance.members).toEqual([]);
  });
});
