import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<any[]>(this.apiUrl);
  }

  create(project: {name: string; description: string; startDate: string}) {
    return this.http.post(this.apiUrl, project);
  }

  inviteMember(projectId: number, email: string) {
    return this.http.post(`${this.apiUrl}/${projectId}/invite`, { email });
  }

  setRole(projectId: number, userId: number, role: string) {
    return this.http.patch(`${this.apiUrl}/${projectId}/role`, { userId, role });
  }
}

