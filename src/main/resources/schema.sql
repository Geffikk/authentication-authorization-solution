DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS user_visit;
DROP TABLE IF EXISTS verification_token;
DROP TABLE IF EXISTS procedure;
DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS default_capability;
DROP TABLE IF EXISTS capability;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_entity;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS jwtblacklist;

CREATE TABLE user_entity (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) DEFAULT NULL,
    firstName VARCHAR(255) DEFAULT NOT NULL,
    lastName VARCHAR(255) DEFAULT NOT NULL,
    active INT DEFAULT 0,
    phone VARCHAR(255) DEFAULT NULL,
    birthdate DATE DEFAULT NULL,
    authProvider VARCHAR(15) DEFAULT NULL,
    jwtToken VARCHAR(255) DEFAULT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE patient (
     patientId INT AUTO_INCREMENT PRIMARY KEY,
     identification LONG UNIQUE NOT NULL,
     firstName VARCHAR(250) DEFAULT NOT NULL,
     lastName VARCHAR(250) DEFAULT NOT NULL,
     email VARCHAR(250) DEFAULT NULL,
     phone VARCHAR(250) DEFAULT NULL,
     city VARCHAR2(255) DEFAULT NULL,
     address VARCHAR(250) DEFAULT NULL,
     birthdate DATE DEFAULT NULL,
     createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE visit (
    visitId INT AUTO_INCREMENT PRIMARY KEY,
    identification LONG DEFAULT NOT NULL,
    firstName VARCHAR(255) DEFAULT NULL,
    lastName VARCHAR(255) DEFAULT NULL,
    reason VARCHAR(255) DEFAULT NULL,
    symptoms VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    city VARCHAR(255) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    birthdate DATE DEFAULT NULL,
    visitTime DATETIME DEFAULT NULL,
    patientId INT DEFAULT NOT NULL,
    FOREIGN KEY (patientId)
        REFERENCES patient(patientId)
        ON DELETE CASCADE
);

CREATE TABLE procedure (
    procedureId INT AUTO_INCREMENT PRIMARY KEY,
    procedureType VARCHAR(255) DEFAULT NULL,
    result VARCHAR(255) DEFAULT NULL,
    billing VARCHAR(255) DEFAULT NULL,
    procedureTime DATE DEFAULT NULL,
    visitId INT DEFAULT NOT NULL,
    FOREIGN KEY (visitId)
        REFERENCES visit(visitId)
        ON DELETE CASCADE
);

CREATE TABLE role (
    name VARCHAR(50) NOT NULL PRIMARY KEY,
    role VARCHAR(30)
);

CREATE TABLE user_role (
    userId INT,
    role VARCHAR(30),
    origin VARCHAR(30),
    CONSTRAINT fk_user FOREIGN KEY (userId) REFERENCES user_entity(userId),
    CONSTRAINT fk_role FOREIGN KEY(role) REFERENCES role(name)
);

CREATE TABLE user_visit (
    userId INT,
    visitId INT,
    CONSTRAINT fk_user2 FOREIGN KEY (userId) REFERENCES user_entity(userId),
    CONSTRAINT fk_visit FOREIGN KEY (visitId) REFERENCES visit(visitId)
);

CREATE TABLE verification_token (
    verificationId INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) DEFAULT NULL,
    token VARCHAR(255) DEFAULT NULL,
    status VARCHAR(255) DEFAULT NULL,
    expiredDateTime DATE DEFAULT NULL,
    issuedDateTime DATE DEFAULT NULL,
    confirmedDateTime DATE DEFAULT NULL,
    userId INT DEFAULT NOT NULL
);

CREATE TABLE capability (
    capId INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255) DEFAULT NOT NULL,
    user VARCHAR(255) DEFAULT NULL,
    entityType VARCHAR(255) DEFAULT NOT NULL,
    entityId INTEGER DEFAULT NULL,
    read BOOLEAN DEFAULT NULL,
    update BOOLEAN DEFAULT NULL,
    create BOOLEAN DEFAULT NULL,
    delete BOOLEAN DEFAULT NULL,
    capability BOOLEAN DEFAULT NULL,
    priority INT DEFAULT NOT NULL,
    FOREIGN KEY (role)
        REFERENCES role(name)
        ON DELETE CASCADE,
    FOREIGN KEY (user)
        REFERENCES user_entity(email)
        ON DELETE CASCADE
);

CREATE TABLE jwtblacklist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) DEFAULT NOT NULL
);

CREATE TABLE default_capability (
    capId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) DEFAULT NULL,
    role VARCHAR(100) DEFAULT NULL,
    entity VARCHAR(100) DEFAULT NULL,
    read BOOLEAN DEFAULT NULL,
    update BOOLEAN DEFAULT NULL,
    create BOOLEAN DEFAULT NULL,
    delete BOOLEAN DEFAULT NULL,
    capability BOOLEAN DEFAULT NULL,
    FOREIGN KEY (role)
        REFERENCES capability(role)
        ON DELETE CASCADE
);

INSERT INTO user_entity (email, password, firstName, lastName, active, phone, birthdate) VALUES
('doctor@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Jan', 'Polak', '1', '+420731219731', '1998-04-16'),
('doctor2@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Dezo', 'Klobasa', '1', '334-445-111', '1995-04-22'),
('sister@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Janka', 'Stenova', '1', '113-496-125', '2002-08-12'),
('sister2@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Naďa', 'Polakova', '1', '532-531-115', '1980-04-30'),
('user@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Ramon', 'Cibulka', '1', '+420731219731', '1980-06-30'),
('management@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Filip', 'Novak', '1', '423-444-226', '1982-05-20'),
('doctor3@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Dezov', 'Brat', '1', '334-445-111', '1995-04-22'),
('geffert.maros@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Dezov', 'Brat', '1', '+420 731 219 731', '1995-04-22'),
('user2@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'Ramon', 'Drevenak', '1', '321-323-115', '1980-06-30'),
('admin@gmail.com', '$2a$10$MX4Nlz30kjYePZOq0WxD9OgQsgV2A6pZUP5kh.Mny/H8C0O3FZ7rG', 'admin', 'admin', '1', '321-323-115', '1980-06-30');

INSERT INTO patient (identification, firstName, lastName, email, phone, city, address, birthdate) VALUES
(1234567812, 'Palko', 'Mesy', 'palko.mesy@gmail.com', '111-222-333', 'Kosice', 'Pod Lipkou 3', '1998-04-16'),
(4564815645, 'Jozko', 'Markva', 'jozko@gmail.com', '123-456-789', 'Bardejov', 'Bezručova 22', '1995-04-22'),
(1381384564, 'Denko', 'Žemľa', 'denko.zemla@gmail.com', '447-556-789', 'Presov', 'Moliterka 1', '2002-08-12'),
(1381384565, 'Janka', 'Deskova', 'deska@gmail.com', '337-556-123', 'Medzilaborce', 'Dulaj 23', '1980-04-30'),
(1234835245, 'Fero', 'Blaha', 'ferko.blaha@gmail.com', '442-553-235', 'Pezinok', 'Kapitána Morgena 23', '1982-05-20');

INSERT INTO visit(identification, firstName, lastName, reason, symptoms, email, phone, city, address, visitTime, birthdate, patientId) VALUES
(1234567812, 'Palko', 'Mesy', 'Test on covid', 'Caughting', 'palko.mesy@gmail.com', '111-222-333', 'Kosice', 'Pod Lipkou 3', '2021-04-16 12:40:00', '1999-02-15', 1),
(4564815645, 'Jozko', 'Markva', 'Test on covid', 'Nothing', 'jozko@gmail.com', '123-456-789', 'Bardejov', 'Bezručova 22', '2021-02-21 13:00:00', '1988-08-20', 2),
(4564815645, 'Jozko', 'Markva', 'Vaccination', 'Fever', 'jozko@gmail.com', '123-456-789', 'Bardejov', 'Bezručova 22', '2021-01-28 09:40:00', '1978-04-16', 2),
(1381384564, 'Denko', 'Žemľa', 'Test on Antibodies', 'Caughting', 'denko.zemla@gmail.com', '447-556-789', 'Presov', 'Moliterka 1', '2021-02-15 10:40:00', '2000-06-24', 3),
(4564815645, 'Jozko', 'Markva', 'Test on Antibodies', 'Nothing', 'jozko@gmail.com', '123-456-789', 'Bardejov', 'Bezručova 22', '2021-03-10 11:40:00', '1994-04-16', 2),
(1381384565, 'Janka', 'Deskova', 'Test on Antibodies', 'Caughting', 'deska@gmail.com', '337-556-123', 'Medzilaborce', 'Dulaj 23', '2021-02-15 10:40:00', '1998-04-30', 3),
(1234835245, 'Fero', 'Blaha', 'Test on Antibodies', 'Nothing', 'ferko.blaha@gmail.com', '442-553-235', 'Pezinok', 'Kapitána Morgena 23', '2021-03-10 11:40:00', '1998-06-16', 2);

INSERT INTO procedure(procedureType, result, billing, procedureTime, visitId) VALUES
('Test on covid', 'Negative', '60$', '2021-02-15', 1),
('Test on covid', 'Negative', '60$', '2021-04-16', 1),
('Vaccination', 'Negative', '60$', '2021-01-28', 2),
('Test on Antibodies', 'Negative', '60$', '2021-12-10', 2),
('Test on Antibodies', 'Negative', '60$', '2021-03-10', 1);

INSERT INTO role(name, role) VALUES
('DOCTOR', null),
('SISTER', null),
('MANAGEMENT', null),
('USER', null),
('PERSONAL', null),
('EMPLOYER', null),
('ADMIN', null);

INSERT INTO capability(role, user, entityType, entityId, create, read, update, delete, capability, priority) VALUES
('EMPLOYER', null, 'visit', null, false, true, false, false, false, 1),
('EMPLOYER', null, 'procedure', null, false, true, false, false, false, 1),
('EMPLOYER', null, 'patient', null, false, true, false, false, false, 1),
('DOCTOR', null, 'visit', null, true, true, false, false, false, 1),
('DOCTOR', null, 'procedure', null, true, true, false, false, false, 1),
('DOCTOR', null, 'patient', null, true, true, false, false, false, 1),
('SISTER', null, 'visit', null, false, true, false, false, false, 1),
('SISTER', null, 'procedure', null, true, true, false, false, false, 1),
('SISTER', null, 'patient', null, false, true, false, false, false, 1),
('MANAGEMENT', null, 'visit', null, false, true, true, true, true, 1),
('MANAGEMENT', null, 'procedure', null, false, true, true, false, true, 1),
('MANAGEMENT', null, 'patient', null, false, false, false, false, true, 1),
('MANAGEMENT', null, 'user', null, false, true, true, false, true, 1),
('MANAGEMENT', null, 'role', null, true, true, true, true, true, 1),
('ADMIN', null, 'visit', null, true, true, true, true, true, 1),
('ADMIN', null, 'procedure', null, true, true, true, false, true, 1),
('ADMIN', null, 'patient', null, true, true, true, true, true, 1),
('ADMIN', null, 'user', null, true, true, true, true, true, 1),
('ADMIN', null, 'role', null, true, true, true, true, true, 1),
('USER', null, 'visit', null, true, false, false, false, false, 1),
('PERSONAL', 'doctor@gmail.com', 'visit', 1, true, true, true, true, true, 3);

INSERT INTO default_capability(name, role, entity, create, read, update, delete, capability) VALUES
('OWNER', null, null, true, true, true, true, false),
('OWNER', 'EMPLOYER', 'visit', true, true, true, true, true),
('OWNER', 'EMPLOYER', 'procedure', true, true, true, true, true),
('DOCTOR_IN_VISIT', 'DOCTOR', 'visit', false, true, true, false, false),
('SISTER_IN_VISIT', 'SISTER', 'visit', false, true, true, false, false),
('OWNER', 'USER', 'visit', true, true, true, false, false),
('MANAGEMENT_IN_VISIT', 'MANAGEMENT', 'visit', false, true, false, false, false),
('DOCTOR_IN_PROCEDURE', 'DOCTOR', 'procedure', false, true, true, false, false),
('SISTER_IN_PROCEDURE', 'SISTER', 'procedure', false, true, true, false, false),
('USER_IN_PROCEDURE', 'USER', 'procedure', false, true, false, false, false),
('USER_IN_VISIT', 'USER', 'visit', false, true, false, false, false),
('EMPLOYER_IN_PROCEDURE', 'EMPLOYER', 'procedure', false, true, false, false, false),
('EMPLOYER_IN_VISIT', 'EMPLOYER', 'visit', false, true, false, false, false),
('MANAGEMENT_IN_PROCEDURE', 'MANAGEMENT', 'procedure', false, false, false, false, false);

INSERT INTO user_role(userId, role) VALUES
(1, 'DOCTOR'),
(1, 'USER'),
(1, 'EMPLOYER'),
(2, 'DOCTOR'),
(2, 'USER'),
(2, 'EMPLOYER'),
(3, 'SISTER'),
(3, 'USER'),
(3, 'EMPLOYER'),
(4, 'SISTER'),
(4, 'USER'),
(4, 'EMPLOYER'),
(5, 'USER'),
(6, 'USER'),
(6, 'EMPLOYER'),
(6, 'MANAGEMENT'),
(7, 'DOCTOR'),
(7, 'USER'),
(7, 'EMPLOYER'),
(8, 'USER'),
(9, 'USER'),
(10, 'USER'),
(10, 'ADMIN');