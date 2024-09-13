-- Create roles table if not exists

DROP TABLE role;

CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

-- Insert roles if they do not exist
INSERT INTO role (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE name = 'ROLE_USER'
);

INSERT INTO role (name)
SELECT 'ROLE_MANAGER'
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE name = 'ROLE_MANAGER'
);

INSERT INTO role (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE name = 'ROLE_ADMIN'
);

-- Example SQL to insert an employee with ADMIN role
INSERT INTO employee (name, password, firstname, lastname, dob, gender, email, address, position, hiredate, department, salary, status, rfid, phone, image_url)
VALUES ('admin', '$2a$10$ZUl2GGLn2OOGn8ss9ofYi.4Kd1aA6MDuwECijkNpZLUWB3Y2dIccS','admin',  'Admin', '1990-01-01', 'Male', 'admin@example.com', '123 Admin St', 'Administrator', '2024-01-01', 'IT', '100000', 'Active', 'RFID123', '1234567890', 'http://example.com/admin.jpg');

-- Get the ID of the inserted employee
SELECT id FROM employee WHERE name = 'admin';

-- Insert ADMIN role for the employee
INSERT INTO user_roles (user_id, role_id) VALUES (1, (SELECT id FROM role WHERE name = 'ROLE_ADMIN'));


INSERT INTO attendent (create_date, create_time, status, employee_id)
VALUES ('2024-07-02', TIME '08:18:00', 'start', 1);

INSERT INTO attendent (create_date, create_time, status, employee_id)
VALUES ('2024-07-02', TIME '18:19:00', 'finish', 1);