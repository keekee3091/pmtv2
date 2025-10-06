import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TaskFormComponent } from '../tasks/task-form.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="dashboard">
      <div class="users">
        <h3>Utilisateurs</h3>
        <ul>
          <li *ngFor="let user of users">
            {{ user.username }} ({{ user.email }})
          </li>
        </ul>
      </div>

      <ng-container *ngIf="auth.currentUser$ | async as user" style="display: none;">
  <p>User ID : {{ user.id }}</p>
  <p>Role dans ce projet : {{ role || 'non défini' }}</p>
</ng-container>

      <div class="projects">
        <h3>Projets</h3><button>+ Créer un projet</button>
        <ul>
          <li *ngFor="let project of projects" (click)="selectProject(project)" [class.active]="project.id === selectedProject?.id">
            <strong>{{ project.name }}</strong> - {{ project.description }}
          </li>
        </ul>

        <div *ngIf="selectedProjectTasks.length > 0" class="tasks">
          <h4>Tasks du projet : {{ selectedProject?.name }}</h4><button *ngIf="canAddTask()" (click)="openTaskModal()">+ Ajouter une tâche</button>
          <ul>
            <li *ngFor="let task of selectedProjectTasks" (click)="selectTask(task)" [class.active]="task.id === selectedTask?.id"><button *ngIf="canAddTask()">+ Editer une tâche</button>
              <strong>{{ task.name }}</strong> : {{ task.description }} - <em>{{ task.status }}</em> (prio: {{ task.priority }})
            </li>
          </ul>
        </div>

        <div *ngIf="selectedTaskHistory.length > 0" class="history">
  <h4>Historique de la tâche : {{ selectedTask?.name }}</h4>
  <ul>
    <li *ngFor="let h of selectedTaskHistory">
      {{ h.oldValue }} ➡  <strong>{{ h.newValue }}</strong>
      <br />
      <small>
        {{ h.changeDate || 'Date inconnue' }} — 
        par {{ h.changedBy?.username || 'N/A' }}
      </small>
    </li>
  </ul>
</div>

<div *ngIf="members.length > 0" class="members">
          <h4>Membres : {{ selectedProject?.name }}</h4><button *ngIf="canAddAssignee()">+ Ajouter un membre</button>
          <ul>
          <li *ngFor="let member of members">
      {{ member.user.username }} - <strong>{{ member.role }}</strong>
          </li>
                  </ul>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard { display: flex; gap: 2rem; }
    .users, .projects {
      flex: 1; padding: 1rem; border: 1px solid #ddd;
      border-radius: 8px; background: #f9f9f9;
    }
    .projects ul li {
      cursor: pointer;
      padding: 5px;
    }
    .projects ul li.active {
      background: #e3f2fd;
      border-radius: 5px;
    }
    .tasks { margin-top: 1rem; }
  `]
})
export class DashboardComponent implements OnInit {
  users: any[] = [];
  projects: any[] = [];
  members: any[] = [];

  selectedProject: any = null;
  selectedProjectTasks: any[] = [];
  selectedMembers: any[] = [];
  selectedTask: any = null;
  selectedTaskHistory: any[] = [];

  userId: number | null = null;
  role: string | null = null;


  showProjectModal = false;
  showTaskModal = false;

  projectForm!: FormGroup;
  taskForm!: FormGroup;

  constructor(private http: HttpClient, public auth: AuthService, private fb: FormBuilder, private dialog: MatDialog) {

    this.auth.loadUserFromStorage();

    this.projectForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });

    this.taskForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      status: ['TODO', Validators.required],
      priority: ['MEDIUM', Validators.required]
    });
  }

  canAddTask(): boolean {
    return this.role === 'ADMIN' || this.role === 'MEMBER';
  }

  canAddAssignee(): boolean {
    return this.role === 'ADMIN'
  }

  openTaskModal() {
    const dialogRef = this.dialog.open(TaskFormComponent, {
      width: '350px',
      height: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.post<any>(`http://localhost:8080/api/tasks/${this.selectedProject.id}/tasks`, result)
          .subscribe(task => {
            this.selectedProjectTasks.push(task);
          });
      }
    });
  }

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/api/users').subscribe({
      next: (data) => this.users = data,
      error: (err) => console.error('Erreur chargement users', err)
    });

    this.http.get<any[]>('http://localhost:8080/api/projects').subscribe({
      next: (data) => this.projects = data,
      error: (err) => console.error('Erreur chargement projets', err)
    });

  }

  selectProject(project: any) {
    this.selectedProject = project;

    this.http.get<any>(`http://localhost:8080/api/projects/${project.id}`).subscribe({
      next: (data) => {
        this.selectedProjectTasks = data.tasks;
        this.members = data.members;

        this.auth.currentUser$.subscribe(user => {
          if (user) {
            this.userId = user.id;
          }
        });
        const currentMember = this.members.find((m: any) => m.user.id === this.userId);
        this.role = currentMember ? currentMember.role : null;

      },
      error: (err) => console.error('Erreur chargement tasks', err)
    });
  }

  selectTask(task: any) {
    this.selectedTask = task;

    this.http.get<any>(`http://localhost:8080/api/tasks/${task.id}`).subscribe({
      next: (data) => {
        this.selectedTask = data;
        this.selectedTaskHistory = data.history || [];
      },
      error: (err) => console.error('Erreur chargement task + history', err)
    });
  }

  createProject() {
    if (this.projectForm.invalid) return;

    this.http.post<any>('http://localhost:8080/api/projects', this.projectForm.value).subscribe({
      next: (project) => {
        this.projects.push(project);
        this.showProjectModal = false;
        this.projectForm.reset();
      },
      error: (err) => console.error('Erreur création projet', err)
    });
  }

  createTask() {
    if (!this.selectedProject) return;
    if (this.taskForm.invalid) return;

    this.http.post<any>(`http://localhost:8080/api/projects/${this.selectedProject.id}/tasks`, this.taskForm.value).subscribe({
      next: (task) => {
        this.selectedProjectTasks.push(task);
        this.showTaskModal = false;
        this.taskForm.reset();
      },
      error: (err) => console.error('Erreur création tâche', err)
    });
  }
}
