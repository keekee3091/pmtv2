import { Component } from '@angular/core';
import { MatToolbar } from "@angular/material/toolbar";
import { RouterOutlet, Router } from '@angular/router';
import { NgIf, AsyncPipe } from '@angular/common';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
    <mat-toolbar color="primary">
      <span>PMTv2</span>
      <span style="flex:1 1 auto;"></span>
      <ng-container *ngIf="auth.currentUser$ | async as user">
        👤 {{ user.username }}
        <button mat-button (click)="logout()">Déconnexion</button>
      </ng-container>
    </mat-toolbar>
    <div style="padding:20px;">
      <router-outlet></router-outlet>
    </div>
  `,
  imports: [MatToolbar, RouterOutlet, NgIf, AsyncPipe]
})
export class AppComponent {
  userId: number | null = null;

  constructor(public auth: AuthService, private router: Router) {
    this.auth.loadUserFromStorage();
  }

  ngOnInit() {
    this.autoLog()
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/auth']);
  }

  autoLog() {
    this.auth.currentUser$.subscribe(user => {
      if (user) {
        this.userId = user.id;
        if (this.userId != null) {
          this.router.navigate(['/dashboard']);
        }
      }
    });
  }
}
