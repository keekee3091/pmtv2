import { Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ProjectListComponent } from './features/projects/project-list.component';
import { ProjectDetailComponent } from './features/projects/project-detail.component';
import { TaskListComponent } from './features/tasks/task-list.component';
import { TaskDetailComponent } from './features/tasks/task-detail.component';
import { TaskFormComponent } from './features/tasks/task-form.component';
import { NotificationListComponent } from './features/notifications/notification-list.component';
import { TaskHistoryComponent } from './features/history/task-history.component';

export const routes: Routes = [
  { path: 'auth', component: AuthComponent },
  { path: '', component: AuthComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'projects', component: ProjectListComponent },
  { path: 'projects/:id', component: ProjectDetailComponent },
  { path: 'projects/:id/tasks', component: TaskListComponent },
  { path: 'tasks/new', component: TaskFormComponent },
  { path: 'tasks/:id', component: TaskDetailComponent },
  { path: 'tasks/:id/history', component: TaskHistoryComponent },
  { path: 'notifications', component: NotificationListComponent }
];
