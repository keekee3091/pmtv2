import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-add-member-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule],
  template: `
    <h2>Ajouter un membre</h2>
    <form [formGroup]="memberForm" (ngSubmit)="save()">
      <label>Utilisateur</label>
      <select formControlName="user_id">
        <option value="">-- Choisir un utilisateur --</option>
        <option *ngFor="let user of users" [value]="user.id">
          {{ user.username }} ({{ user.email }})
        </option>
      </select>

      <label>Rôle</label>
      <select formControlName="role">
        <option value="ADMIN">Administrateur</option>
        <option value="MEMBER">Membre</option>
        <option value="OBSERVER">Observateur</option>
      </select>

      <div style="margin-top: 1rem;">
        <button mat-button type="button" (click)="close()">Annuler</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="memberForm.invalid">Ajouter</button>
      </div>
    </form>
  `,
  styles: [`
    form { display: flex; flex-direction: column; gap: 0.75rem; }
  `]
})
export class AddMemberFormComponent implements OnInit {
  memberForm!: FormGroup;
  users: any[] = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private dialogRef: MatDialogRef<AddMemberFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.memberForm = this.fb.group({
      user_id: ['', Validators.required],
      role: ['MEMBER', Validators.required]
    });
    this.loadUsers();
  }

  loadUsers() {
    this.http.get<any[]>('http://localhost:8080/api/users').subscribe({
      next: (users) => (this.users = users),
      error: (err) => console.error('Erreur chargement utilisateurs', err)
    });
  }

  save() {
    if (this.memberForm.valid) {
      const payload = {
        user: Number(this.memberForm.value.user_id),
        project: this.data.projectId,
        role: this.memberForm.value.role
      };
      this.http.post<any>('http://localhost:8080/api/project-members', payload).subscribe({
        next: (member) => this.dialogRef.close(member),
        error: (err) => console.error('Erreur ajout membre', err)
      });
    }
  }

  close() {
    this.dialogRef.close();
  }
}
