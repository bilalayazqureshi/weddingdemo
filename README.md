# WeddingDemoWithTesting

A Spring Boot web application demonstrating wedding event and guest management, featuring Test Driven Development (unit, integration, E2E), Dockerized MySQL for CI, and code quality tools.

---
[![Java CI with Maven](https://github.com/bilalayazqureshi/weddingdemo/actions/workflows/maven.yml/badge.svg)](https://github.com/bilalayazqureshi/weddingdemo/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/bilalayazqureshi/weddingdemo/badge.svg?branch=master)](https://coveralls.io/github/bilalayazqureshi/weddingdemo?branch=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=bugs)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=bilalayazqureshi_weddingdemo&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=bilalayazqureshi_weddingdemo)


---

## Overview

This project manages wedding events and their guests, exposing REST APIs and web pages with Thymeleaf. It is built with Spring Boot 3 and Java 17, leveraging:

- Spring Data JPA for persistence  
- Thymeleaf for UI templating  
- MySQL database (Dockerized for integration tests)  
- Comprehensive testing with JUnit 5, Selenium WebDriver, and REST-assured  
- Mutation testing via PIT  
- Static analysis and coverage with SonarQube and Jacoco  

---

## Technologies Used

- Java 17  
- Spring Boot 3.4.5  
- Maven  
- MySQL 5.7 (via Docker)  
- Thymeleaf  
- JUnit 5 (JUnit Jupiter)  
- Selenium WebDriver & WebDriverManager  
- REST-assured  
- Testcontainers (MYSQL)  
- PIT Mutation Testing  
- Jacoco Code Coverage  
- SonarQube for static analysis  

---

## Getting Started

### Prerequisites

- Java 17 SDK installed  
- Maven 3.8+  
- Docker installed and running  
- IDE (e.g. Eclipse)  

### Clone the repository

```bash
git clone https://github.com/yourusername/weddingdemowithtesting.git
cd weddingdemowithtesting
