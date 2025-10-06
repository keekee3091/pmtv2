import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Détail de la tâche</h2>
    <p>Nom : {{ task?.name }}</p>
    <p>Description : {{ task?.description }}</p>
    <p>Échéance : {{ task?.dueDate }}</p>
    <p>Priorité : {{ task?.priority }}</p>
    <p>Assignée à : {{ task?.assignedTo?.username }}</p>
  `
})
export class TaskDetailComponent implements OnInit {
  task: any;

  ngOnInit() {
    // TODO: charger via TaskService
  }
}
