import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Liste des tâches</h2>
    <ul>
      <li *ngFor="let task of tasks">
        {{ task.name }} - {{ task.status }}
      </li>
    </ul>
  `
})
export class TaskListComponent implements OnInit {
  tasks: any[] = [];

  ngOnInit() {
    // TODO: charger via TaskService
  }
}
