import { User } from "./user";
import { Task } from "./task";

export interface Notification {
    id?: number;
    message: string;
    isRead: boolean;
    user_id: User;
    task_id: Task;
}