# HexOust

HexOust is a Java-based hex-grid strategy game featuring procedural hexagonal grid generation and custom rendering using Java Swing.

The original game was created collaboratively by a team of three
- Charlie Zhang
- Mahad Maqsood
- Hamza Yasin

Subsequent refactoring, build system migration, automated testing, Docker integration, and documentation were completed independently by Charlie Zhang

---

## Project Overview

The game generates a dynamic hexagonal grid rendered over a sky-themed background with simple UI elements such as a reset button and visual effects. All rendering and logic are implemented entirely in Java.

---

## Build and Run (Current – Recommended)

### Prerequisites
- Java JDK 21 (Temurin recommended)
- Maven
- Windows (for GUI execution)

---

### Local Build and Test

From the project root, run:

    mvn clean test
    mvn package

This will:
- Compile the project
- Run all unit tests
- Produce a runnable JAR in the target directory

---

### Run the Game (GUI)

Because HexOust is a Swing GUI application, it must be run in an environment with a display.

On Windows (PowerShell), run:

    java -jar target\hexoust-1.0.0.jar 25 400 400

Arguments:
- <size>     Size of the hex grid
- <originX>  X-coordinate of grid origin
- <originY>  Y-coordinate of grid origin

Example:

    java -jar target\hexoust-1.0.0.jar 25 400 400

Note:
Running the GUI inside WSL or Docker without an X server will result in a headless exception. Docker is used for build and testing only.

---

### Docker (Build and Test Only)

Docker is used to provide a reproducible, headless build environment.

From the project root, run:

    docker build -t hexoust-build .

This container:
- Compiles the project
- Runs all tests
- Confirms the project builds cleanly in CI-style environments

---

## Project Structure (Current)

    HexOust/
    ├─ pom.xml
    ├─ Dockerfile
    ├─ src/
    │  ├─ main/
    │  │  └─ java/com/charliezhang/hexoust/
    │  │     ├─ HexGrid.java        Main game logic and rendering
    │  │     └─ Confetti.java       Visual effects
    │  └─ test/
    │     └─ java/com/charliezhang/hexoust/
    │        └─ HexGridTest.java    Unit tests
    ├─ target/                      Build output (ignored in git)
    └─ images/
       └─ Hexoust.png

---

## Features

- Procedural hexagonal grid generation
- Custom coordinate system with cube-coordinate math
- Java Swing rendering with textures and background
- Command-line configurable grid size and origin
- Reset functionality to regenerate the board
- Automated unit tests
- Maven-based build system
- Dockerised build and test pipeline

---

## Screenshots

<img src="images/Hexoust.png" width="320" />

---

## Technical Contributions (Post-Project)

The following improvements were completed independently after the original group project:

- Migrated the project to an industry-standard Maven structure
- Fixed package and classpath issues
- Implemented proper value equality for coordinate classes
- Added and fixed automated JUnit tests
- Configured a runnable JAR with an explicit entry point
- Added a Docker build and test pipeline
- Documented a professional build and run workflow

---

## Future Improvements

- Player interaction on grid
- Additional tile types and mechanics
- Improved UI and menus
- Headless mode for non-GUI execution
- Animation polish and transitions
