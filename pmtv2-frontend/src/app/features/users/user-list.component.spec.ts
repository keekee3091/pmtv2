import { TestBed } from '@angular/core/testing';
import { provideZoneChangeDetection } from '@angular/core';
import { UserListComponent } from './user-list.component';
import { UserService } from '../../core/user.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { of } from 'rxjs';

describe('UserListComponent', () => {
  let userServiceMock: any;

  beforeEach(async () => {
    userServiceMock = {
      getAll: jasmine.createSpy('getAll').and.returnValue(of([
        { id: 1, username: 'Alice', email: 'alice@example.com' }
      ]))
    };

    await TestBed.configureTestingModule({
      imports: [UserListComponent],
      providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: UserService, useValue: userServiceMock }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(UserListComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should load users on init', () => {
    const fixture = TestBed.createComponent(UserListComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance.users.length).toBe(1);
    expect(fixture.componentInstance.users[0].username).toBe('Alice');
  });
});
