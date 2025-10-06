import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Notifications</h2>
    <ul>
      <li *ngFor="let notif of notifications">
        {{ notif.message }} - {{ notif.createdAt }}
        <span *ngIf="!notif.isRead"> (non lu)</span>
      </li>
    </ul>
  `
})
export class NotificationListComponent implements OnInit {
  notifications: any[] = [];

  ngOnInit() {
    // TODO: charger via NotificationService
  }
}
