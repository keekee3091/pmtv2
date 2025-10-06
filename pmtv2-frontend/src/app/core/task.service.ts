import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task } from '../models/task';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  getByProject(projectId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/project/${projectId}`);
  }

  create(task: any) {
    return this.http.post(this.apiUrl, task);
  }

  update(taskId: number, task: any) {
    return this.http.put(`${this.apiUrl}/${taskId}`, task);
  }

  assign(taskId: number, userId: number) {
    return this.http.post(`${this.apiUrl}/${taskId}/assign`, { userId });
  }
}

