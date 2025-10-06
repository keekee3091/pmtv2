import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { UserService } from '../../core/user.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    CommonModule,
    MatListModule,
  ],
  template: `
    <h2>Liste des utilisateurs</h2>
    <mat-list>
      <mat-list-item *ngFor="let u of users">
        {{ u.username }} - {{ u.email }}
      </mat-list-item>
    </mat-list>
  `
})
export class UserListComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getAll().subscribe(data => (this.users = data));
  }
}
