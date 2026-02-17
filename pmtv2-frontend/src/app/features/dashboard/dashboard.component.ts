import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TaskFormComponent } from '../tasks/task-form.component';
import { ProjectFormComponent } from '../projects/project-form.component';
import { AddMemberFormComponent } from '../projects/add-member-form.component';

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
        <h3>Projets</h3><button (click)="openProjectModal()">+ Créer un projet</button>
        <ul>
          <li *ngFor="let project of projects" (click)="selectProject(project)" [class.active]="project.id === selectedProject?.id">
            <strong>{{ project.name }}</strong> - {{ project.description }}
          </li>
        </ul>

        <div *ngIf="selectedProject" class="tasks">
          <h4>Tasks du projet : {{ selectedProject?.name }}</h4> <button *ngIf="canAddTask()" (click)="openTaskModal(false)">+ Ajouter une tâche</button>
          <ul>
            <li *ngFor="let task of selectedProjectTasks" (click)="selectTask(task)" [class.active]="task.id === selectedTask?.id"><button *ngIf="canAddTask()" (click)="openTaskModal(true)">+ Editer une tâche</button>
              <strong>{{ task.name }}</strong> : {{ task.description }} - <em>{{ task.status }}</em> (prio: {{ task.priority }}) {{task.assignedTo?.username || 'Non assignée'}}
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
          <h4>Membres : {{ selectedProject?.name }}</h4><button *ngIf="canAddAssignee()" (click)="openAddMemberModal()">+ Ajouter un membre</button>
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
      priority: ['MEDIUM', Validators.required],
      assignedTo: ['', Validators.required]
    });
  }

  canSelectTask(): boolean {
    return this.role === 'ADMIN' || this.role === 'MEMBER' || this.role === 'OBSERVER'
  }

  canAddTask(): boolean {
    return this.role === 'ADMIN' || this.role === 'MEMBER';
  }

  canAddAssignee(): boolean {
    return this.role === 'ADMIN'
  }

  openAddMemberModal() {
    const dialogRef = this.dialog.open(AddMemberFormComponent, {
      width: '400px',
      data: { projectId: this.selectedProject.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.selectProject(this.selectedProject);
      }
    });
  }

  openProjectModal() {
    const dialogRef = this.dialog.open(ProjectFormComponent, {
      width: '500px',
      height: 'auto',
      data: { owner_id: this.userId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const today = new Date();
        const formattedDate = today.toISOString().split('T')[0];

        const resultForProject = {
          name: result.name,
          description: result.description,
          startDate: formattedDate,
          ownerId: result.owner_id
        };
        this.http.post<any>('http://localhost:8080/api/projects', resultForProject).subscribe({
          next: (project) => {
            this.projects.push(project);

            const ownerAttribution = {
              user: result.owner_id,
              project: project.id,
              role: 'ADMIN'
            };
            this.http.post('http://localhost:8080/api/project-members', ownerAttribution).subscribe({
              error: (err) => console.error('Erreur ajout owner', err)
            });
            if (result.members && result.members.length > 0) {
              result.members.forEach((member: any) => {
                const memberPayload = {
                  user: member.user_id,
                  project: project.id,
                  role: member.role
                };
                this.http.post('http://localhost:8080/api/project-members', memberPayload).subscribe({
                  next: () => console.log('✅ Membre ajouté :', memberPayload),
                  error: (err) => console.error('❌ Erreur ajout membre', err)
                });
              });
            }
          },
          error: (err) => console.error('Erreur création projet', err)
        });
      }
    });
  }

  openTaskModal(isEdit: boolean) {
    let dialogRef;

    if (!isEdit) {
      dialogRef = this.dialog.open(TaskFormComponent, {
        width: '350px',
        height: '350px',
        data: {
          projectId: this.selectedProject.id
        }
      });
    } else {
      dialogRef = this.dialog.open(TaskFormComponent, {
        width: '350px',
        height: '350px',
        data: {
          projectId: this.selectedProject.id,
          task: this.selectedTask,
          changedBy : this.userId
        }
      });
    }
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (!isEdit) {
          this.http.post<any>(`http://localhost:8080/api/tasks/${this.selectedProject.id}/tasks`, result).subscribe({
            next: (task) => this.selectedProjectTasks.push(task),
            error: (err) => console.error('Erreur création tâche', err)
          });
        } else {
          this.http.put<any>(`http://localhost:8080/api/tasks/${this.selectedTask.id}`, result).subscribe({
            next: (updated) => {
              const index = this.selectedProjectTasks.findIndex(t => t.id === updated.id);
              if (index !== -1) this.selectedProjectTasks[index] = updated;
            },
            error: (err) => console.error('Erreur édition tâche', err)
          });
        }
      }
    });
  }

  ngOnInit() {

    this.auth.currentUser$.subscribe(user => {
      if (user) {
        this.userId = user.id;
      }
    });
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
    this.role = null;
    this.selectedProjectTasks = [];
    this.members = [];
    this.selectedTask = null;
    this.selectedTaskHistory = [];

    this.http.get<any>(`http://localhost:8080/api/projects/${project.id}`).subscribe({
      next: (data) => {
        this.selectedProjectTasks = data.tasks;
        this.members = data.members;

        const currentMember = this.members.find((m: any) => m.user.id === this.userId);
        this.role = currentMember ? currentMember.role : null;

      },
      error: (err) => console.error('Erreur chargement tasks', err)
    });
  }

  selectTask(task: any) {
    if (!this.canSelectTask()) {
      return;
    }
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

    this.http.post<any>(`http://localhost:8080/api/tasks/${this.selectedProject.id}/tasks`, this.taskForm.value).subscribe({
      next: (task) => {
        this.selectedProjectTasks.push(task);
        this.showTaskModal = false;
        this.taskForm.reset();
      },
      error: (err) => console.error('Erreur création tâche', err)
    });
  }
}
