import { User } from "./user";

export interface Project {
  id?: number;
  name: string;
  description: string;
  owner: User;
}
