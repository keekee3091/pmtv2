import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-history',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Historique des modifications</h2>
    <ul>
      <li *ngFor="let h of history">
        {{ h.changeDate }} - {{ h.changedBy?.username }} :
        "{{ h.oldValue }}" → "{{ h.newValue }}"
      </li>
    </ul>
  `
})
export class TaskHistoryComponent implements OnInit {
  history: any[] = [];

  ngOnInit() {
  }
}
