import { Project } from "./project";
import { User } from "./user";

export interface Member {
    id?: number;
    user: User;
    project: Project;
    role: String;
}