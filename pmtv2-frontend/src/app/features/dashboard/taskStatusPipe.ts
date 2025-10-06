import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'taskStatus',
  standalone: true
})
export class TaskStatusPipe implements PipeTransform {
  transform(tasks: any[], status: string): any[] {
    if (!tasks) return [];
    return tasks.filter(task => task.status === status);
  }
}
