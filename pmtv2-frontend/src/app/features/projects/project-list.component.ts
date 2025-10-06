import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Liste des projets</h2>
    <ul>
      <li *ngFor="let project of projects">
        {{ project.name }} - {{ project.startDate }}
      </li>
    </ul>
  `
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];

  ngOnInit() {
    // TODO: appeler ProjectService
  }
}
