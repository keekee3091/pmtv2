import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class HistoryService {
  private apiUrl = 'http://localhost:8080/api/history';

  constructor(private http: HttpClient) {}

  getByTask(taskId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/task/${taskId}`);
  }
}
