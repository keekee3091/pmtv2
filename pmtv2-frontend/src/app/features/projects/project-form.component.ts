import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-project-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule, HttpClientModule],
  template: `
    <h2>Créer un projet</h2>

    <form [formGroup]="projectForm" (ngSubmit)="save()">

      <label>Libellé</label>
      <input formControlName="name" />

      <label>Description</label>
      <textarea formControlName="description"></textarea>

      <label>Propriétaire (owner)</label>
<label><strong>{{ owner?.username }}</strong></label>
      <hr />
      <h3>Membres du projet</h3>

      <div formArrayName="members">
        <div *ngFor="let member of members.controls; let i = index" [formGroupName]="i" class="member-row">
          <select formControlName="user_id">
            <option value="">-- Choisir un membre --</option>
            <option *ngFor="let user of users" [value]="user.id">
              {{ user.username || (user.firstName + ' ' + user.lastName) }}
            </option>
          </select>

          <select formControlName="role">
            <option value="ADMIN">Administrateur</option>
            <option value="MEMBER">Membre</option>
            <option value="OBSERVER">Observateur</option>
          </select>

          <button mat-button color="warn" type="button" (click)="removeMember(i)">❌</button>
        </div>
      </div>

      <button mat-button color="primary" type="button" (click)="addMember()">+ Ajouter un membre</button>

      <div style="margin-top: 1rem;">
        <button mat-button type="button" (click)="close()">Annuler</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="projectForm.invalid">
          Créer
        </button>
      </div>
    </form>
  `,
  styles: [`
    form { display: flex; flex-direction: column; gap: 0.75rem; }
    .member-row { display: flex; align-items: center; gap: 0.5rem; }
  `]
})
export class ProjectFormComponent implements OnInit {
  projectForm!: FormGroup;
  users: any[] = [];
  owner: any = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private dialogRef: MatDialogRef<ProjectFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.projectForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      owner_id: [this.data?.owner_id || '', Validators.required],
      members: this.fb.array([]),
    });
    if (this.data?.owner_id) {
        this.loadOwner(this.data.owner_id);
    }

    this.loadUsers();
  }

  get members() {
    return this.projectForm.get('members') as FormArray;
  }

  addMember() {
    this.members.push(
      this.fb.group({
        user_id: ['', Validators.required],
        role: ['', Validators.required],
      })
    );
  }

  removeMember(index: number) {
    this.members.removeAt(index);
  }

  loadUsers() {
    this.http.get<any[]>('http://localhost:8080/api/users').subscribe({
      next: (users) => (this.users = users),
      error: (err) => console.error('Erreur chargement utilisateurs', err),
    });
  }

loadOwner(owner_id: number) {
  this.http.get<any>(`http://localhost:8080/api/users/${owner_id}`).subscribe({
    next: (owner) => {
      this.owner = owner; 
      this.projectForm.patchValue({ owner_id: owner.id });
    },
    error: (err) => console.error('Erreur chargement owner', err)
  });
}

  save() {
    if (this.projectForm.valid) {
      this.dialogRef.close(this.projectForm.value);
    }
  }

  close() {
    this.dialogRef.close();
  }
}
