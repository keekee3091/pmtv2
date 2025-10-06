import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Détails du projet</h2>
    <p>Nom : {{ project?.name }}</p>
    <p>Description : {{ project?.description }}</p>
    <p>Date début : {{ project?.startDate }}</p>
    
    <h3>Membres</h3>
    <ul>
      <li *ngFor="let member of members">
        {{ member.user.username }} - {{ member.role }}
      </li>
    </ul>
  `
})
export class ProjectDetailComponent implements OnInit {
  project: any;
  members: any[] = [];

  ngOnInit() {
    // TODO: charger via ProjectService
  }
}
