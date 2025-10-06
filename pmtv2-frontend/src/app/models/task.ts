import { Project } from './project';
import { User } from './user';

export interface Task {
  id?: number;
  name: string;
  description: string;
  dueDate: string;
  priority: string;
  status: string;
  project: Project;
  assignedTo: User;
}
