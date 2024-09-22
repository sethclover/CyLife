CREATE DATABASE StudentClubOrganisationDB;

USE StudentClubOrganisationDB;

CREATE TABLE Student (
    studentId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    isInternational BOOLEAN NOT NULL
);

CREATE TABLE Staff (
    staffId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Club (
    clubId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    president VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    FOREIGN KEY (president) REFERENCES Student(studentId)
);

CREATE TABLE ClubMembers (
    clubId VARCHAR(255),
    studentId VARCHAR(255),
    PRIMARY KEY (clubId, studentId),
    FOREIGN KEY (clubId) REFERENCES Club(clubId),
    FOREIGN KEY (studentId) REFERENCES Student(studentId)
);

CREATE TABLE Organisation (
    organisationId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    director VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    emailOfOrganisation VARCHAR(255) UNIQUE,
    urlForAppointment VARCHAR(255),
    FOREIGN KEY (director) REFERENCES Staff(staffId)
);

CREATE TABLE OrganisationMembers (
    organisationId VARCHAR(255),
    studentId VARCHAR(255),
    PRIMARY KEY (organisationId, studentId),
    FOREIGN KEY (organisationId) REFERENCES Organisation(organisationId),
    FOREIGN KEY (studentId) REFERENCES Student(studentId)
);
