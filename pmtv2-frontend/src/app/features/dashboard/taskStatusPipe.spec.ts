import { TaskStatusPipe } from './taskStatusPipe';

describe('TaskStatusPipe', () => {
  let pipe: TaskStatusPipe;

  beforeEach(() => {
    pipe = new TaskStatusPipe();
  });

  it('should create', () => {
    expect(pipe).toBeTruthy();
  });

  it('should filter tasks by status', () => {
    const tasks = [
      { name: 'T1', status: 'TODO' },
      { name: 'T2', status: 'IN_PROGRESS' },
      { name: 'T3', status: 'TODO' },
      { name: 'T4', status: 'DONE' }
    ];

    const result = pipe.transform(tasks, 'TODO');
    expect(result.length).toBe(2);
    expect(result[0].name).toBe('T1');
    expect(result[1].name).toBe('T3');
  });

  it('should return empty array if tasks is null', () => {
    const result = pipe.transform(null as any, 'TODO');
    expect(result).toEqual([]);
  });

  it('should return empty array if no tasks match', () => {
    const tasks = [{ name: 'T1', status: 'TODO' }];
    const result = pipe.transform(tasks, 'DONE');
    expect(result).toEqual([]);
  });
});
