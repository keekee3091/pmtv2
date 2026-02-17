import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-task-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>Nouvelle tâche</h2>
    <form [formGroup]="taskForm" (ngSubmit)="save()">
      <label>Nom</label>
      <input formControlName="name" />

      <label>Description</label>
      <input formControlName="description" />

      <label>Statut</label>
      <select formControlName="status">
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>

      <label>Priorité</label>
      <select formControlName="priority">
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
      </select>

      <label>Assigné à</label>
      <select formControlName="assignedTo">
          <option value="">-- Choisir un utilisateur --</option>
  <option *ngFor="let user of projectUsers" [value]="user.id">
    {{ user.username || (user.firstName + ' ' + user.lastName) }}
  </option>
      </select>

      <div style="margin-top: 1rem;">
        <button mat-button type="button" (click)="close()">Annuler</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="taskForm.invalid">Créer</button>
      </div>
    </form>
  `
})
export class TaskFormComponent implements OnInit {
  taskForm!: FormGroup;
  projectUsers: any[] = [];
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private dialogRef: MatDialogRef<TaskFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    this.isEditMode = !!this.data?.task;

    this.taskForm = this.fb.group({
      name: [this.data?.task?.name || '', Validators.required],
      description: [this.data?.task?.description || '', Validators.required],
      status: [this.data?.task?.status || 'TODO', Validators.required],
      priority: [this.data?.task?.priority || 'MEDIUM', Validators.required],
      assignedTo: [this.data?.task?.assignedTo?.id || ''],
      changedBy: ['']
    });

    if (this.data?.projectId) {
      this.loadProjectUsers(this.data.projectId);
    }
    if (this.isEditMode && this.data?.changedBy) {
      this.taskForm.patchValue({ changedBy: this.data.changedBy });
    }
  }

  loadProjectUsers(projectId: number) {
    this.http.get<any[]>(`http://localhost:8080/api/project-members/${projectId}/users`).subscribe({
      next: (users) => (this.projectUsers = users),
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs du projet', err);
        this.projectUsers = [];
      }
    });
  }

  save() {
    if (this.taskForm.valid) {
      this.dialogRef.close(this.taskForm.value);
    }
  }

  close() {
    this.dialogRef.close();
  }
}
