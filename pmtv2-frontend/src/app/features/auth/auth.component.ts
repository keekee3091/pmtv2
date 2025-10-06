import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { BehaviorSubject, Observable, tap } from 'rxjs';


@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2 *ngIf="isLoginMode">Connexion</h2>
    <h2 *ngIf="!isLoginMode">Inscription</h2>

    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div *ngIf="!isLoginMode">
        <label>Nom d'utilisateur :</label>
        <input formControlName="username" />
      </div>

      <label>Email :</label>
      <input formControlName="email" type="email" />

      <label>Mot de passe :</label>
      <input formControlName="password" type="password" />

      <button type="submit">
        {{ isLoginMode ? 'Se connecter' : "S'inscrire" }}
      </button>
    </form>

    <p>
      <a href="#" (click)="toggleMode($event)">
        {{ isLoginMode ? "Pas encore de compte ? S'inscrire" : "Déjà inscrit ? Se connecter" }}
      </a>
    </p>
  `
})
export class AuthComponent {
  isLoginMode = true; // true = login, false = register
  form: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      username: [''], // seulement utilisé pour inscription
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  toggleMode(event: Event) {
    event.preventDefault();
    this.isLoginMode = !this.isLoginMode;
  }

  onSubmit() {
    if (this.form.invalid) {
      return
    }
    if (this.isLoginMode) {
      this.authService.login({
        email: this.form.value.email,
        password: this.form.value.password
      }).subscribe({
        next: res => {
          console.log('Login ok', res)
          this.router.navigate(['/dashboard']);

        },
        error: err => console.error('lol dumbass', err)
      })
    } else {
      this.authService.register({
        username: this.form.value.username,
        email: this.form.value.email,
        password: this.form.value.password
      }).subscribe({
        next: res => {
          console.log('Register OK', res);
          this.router.navigate(['/dashboard']);
        },
        error: err => console.error('Erreur register', err)
      });
    }
  }
}
