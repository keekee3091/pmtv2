import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { NotificationListComponent } from './notification-list.component';

describe('NotificationListComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationListComponent],
      providers: [provideZoneChangeDetection({ eventCoalescing: true })]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(NotificationListComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should initialize with empty notifications', () => {
    const fixture = TestBed.createComponent(NotificationListComponent);
    expect(fixture.componentInstance.notifications).toEqual([]);
  });
});
