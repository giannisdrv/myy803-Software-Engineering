# Web-Based Requirements Specification & Analysis App

## Overview
This repository contains the implementation of a Web-Based Requirements Specification and Analysis Application, developed as part of a Software Engineering university project. Built with **Java** and the **Spring Boot** framework, the application is designed to assist software developers and system analysts in specifying, managing, and analyzing the requirements of software engineering projects through Use Cases and CRC (Class-Responsibility-Collaboration) cards.

## Features
* **User Authentication & Authorization:** Secure user registration and login system utilizing Spring Security (`UserDetailsService`, `BCryptPasswordEncoder`, etc.).
* **Project Management:** Users can create, view, manage, and delete software projects directly from their personalized dashboard.
* **Use Case Specification:** Robust tools to define Use Cases, including tracking Actors, Pre-conditions, Main Flows, Alternative Flows, and Post-conditions.
* **CRC Card Management:** Allows the specification of classes using CRC cards, detailing Class Names, Responsibilities, and Collaborations. Use cases and CRC cards can be linked together to map functional requirements to object-oriented designs.
* **Automated UML Generation:** Implements dynamic generation of textual description scripts that can be visualized as UML Use Case and Class diagrams using external tools like **PlantUML** and **Nomnoml**.

## Architecture & Technical Details
* **Tech Stack:** Java, Spring Boot, Spring Security, Spring Data JPA (Hibernate), and MySQL for database management.
* **Domain Model:** The core logic is driven by a well-structured domain model featuring classes such as `User`, `Project`, `UseCase`, `Crc`, and `AlternativeFlow`. The database schema is automatically generated from these entities using JPA annotations.
* **Design Patterns:** The application's architecture heavily relies on established GoF (Gang of Four) design patterns. Specifically, it employs the **Strategy**, **Template Method**, and **Parameterized Factory** patterns to handle the alternative diagram generation strategies (e.g., swapping between PlantUML and Nomnoml script generators).
* **Testing:** The application logic (repositories, services, controllers, and domain layers) is thoroughly tested using JUnit and Mockito.

## Evaluation & Development Flow (Sprints)
The project was developed in iterative sprints, establishing a robust CI/CD-like pipeline of feature rollouts:
* **v1.0:** Initial setup, database configuration, and user authentication infrastructure.
* **v1.1:** Core CRUD operations for Project entities linked to user accounts.
* **v1.2 & v1.3:** Complete Use Case and CRC card logic, relational linking between entities, and the automated script generation engine for diagram visualization.

## Authors
* **Ioannis Drivas** (5216)
* **Eirini Kolonelou** (5456)
* **Eugenia Pappa** (5534)
* University of Ioannina, Department of Computer Science and Engineering
