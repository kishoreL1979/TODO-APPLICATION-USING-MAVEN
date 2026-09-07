# 📝 Todo List Application (Java Swing + MySQL + Maven)

[![Build Status](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/actions/workflows/build-and-release.yml/badge.svg)](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/actions)
[![Release](https://img.shields.io/github/v/release/kishoreL1979/TODO-APPLICATION-USING-MAVEN)](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/releases/tag/v1.0.0)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36.svg)](https://maven.apache.org/)

A clean, modern desktop **Todo Application** built with **Java Swing**, **MySQL Database**, and **Apache Maven**, featuring a soft sky-blue user interface, real-time filtering, automated table management, and full CRUD operations.

---

## 🌟 Key Features

- 🎨 **Modern Sky-Blue UI**: Soft, fresh palette with clean spacing, modern input controls, and custom typography.
- 🟢🔴 **Status Color Indicators**: 
  - **Completed tasks** automatically highlighted in soft green (`#E8F8F5`).
  - **Pending tasks** automatically highlighted in soft red (`#FDEDEC`).
- ⚡ **Full CRUD Capabilities**: Add, update, delete, and refresh tasks seamlessly.
- 🔍 **Real-Time Task Filtering**: Filter tasks by **All**, **Completed**, or **Pending**.
- 🗄️ **Automated Database Schema**: Auto-creates `todos` table on first connection if missing.
- 🚀 **CI/CD & Releases**: Automated build pipeline via GitHub Actions.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java (JDK 11+)
- **GUI Framework**: Java Swing (EDT Thread Safe)
- **Database**: MySQL Server (JDBC Driver `8.0.33`)
- **Build System**: Apache Maven (`maven-shade-plugin`)
- **Architecture**: DAO Pattern (Data Access Object)

---

## 📥 Download & Quick Run

### 🚀 Direct Executable Download for HR / Reviewers
Download the latest pre-compiled executable JAR:
👉 **[Download Todo Application (v1.0.0)](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/releases/tag/v1.0.0)**

Run the app:
```bash
java -jar todo-application-1.0.0.jar
```

---

## 💻 Building from Source

```bash
# Clone the repository
git clone https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN.git

# Navigate into directory
cd TODO-APPLICATION-USING-MAVEN

# Build with Maven
mvn clean install

# Run application
mvn exec:java
```

---

## 📁 Project Structure

```
├── src
│   └── main
│       └── java
│           └── com
│               └── todo
│                   ├── Main.java                 # Entry Point
│                   ├── dao
│                   │   └── TodoDAO.java          # MySQL Data Access Object
│                   ├── gui
│                   │   └── TodoGUI.java          # Swing User Interface
│                   ├── model
│                   │   └── Todo.java             # Data Model
│                   └── util
│                       └── DatabaseConnection.java# JDBC Driver Manager
├── .github
│   └── workflows
│       └── build-and-release.yml                # CI/CD Workflow
├── pom.xml                                      # Maven Configuration
└── README.md
```

---

## 📬 Contact & Links

- **GitHub Repository**: [https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN)
- **Releases Page**: [https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/releases](https://github.com/kishoreL1979/TODO-APPLICATION-USING-MAVEN/releases)
