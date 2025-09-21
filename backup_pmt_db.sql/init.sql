CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    owner_id INT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE project_members (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    project_id INT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'MEMBER', 'OBSERVER')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    project_id INT NOT NULL,
    assignee_id INT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    due_date DATE,
    priority VARCHAR(20) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    status VARCHAR(20) DEFAULT 'TODO' CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE task_history (
    id SERIAL PRIMARY KEY,
    task_id INT NOT NULL,
    changed_by INT NOT NULL,
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_value TEXT,
    new_value TEXT,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    task_id INT,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);


INSERT INTO users (username, email, password) VALUES
('Alice', 'alice@example.com', 'hashed_pwd_1'),
('Bob', 'bob@example.com', 'hashed_pwd_2'),
('Charlie', 'charlie@example.com', 'hashed_pwd_3');

INSERT INTO projects (name, description, start_date, owner_id) VALUES
('Project A', 'Premier projet de test', '2025-01-01', 1),
('Project B', 'Deuxième projet de test', '2025-02-01', 2);

INSERT INTO project_members (user_id, project_id, role) VALUES
(1, 1, 'ADMIN'),
(2, 1, 'MEMBER'),
(3, 1, 'OBSERVER'),
(2, 2, 'ADMIN'),
(3, 2, 'MEMBER');

INSERT INTO tasks (project_id, assignee_id, name, description, due_date, priority, status) VALUES
(1, 2, 'Configurer le backend', 'Setup du projet Spring Boot', '2025-01-15', 'HIGH', 'TODO'),
(1, 3, 'Créer le frontend', 'Init Angular project', '2025-01-20', 'MEDIUM', 'IN_PROGRESS'),
(2, 3, 'Écrire les tests', 'Couverture minimale 60%', '2025-02-10', 'HIGH', 'TODO');

INSERT INTO task_history (task_id, changed_by, old_value, new_value) VALUES
(1, 1, 'TODO', 'IN_PROGRESS'),
(2, 2, 'IN_PROGRESS', 'DONE');

INSERT INTO notifications (user_id, task_id, message) VALUES
(2, 1, 'Vous avez été assigné à la tâche : Configurer le backend'),
(3, 2, 'La tâche "Créer le frontend" a été mise à jour');
