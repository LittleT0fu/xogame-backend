# XO Game Backend

A Tic-Tac-Toe game backend API built with Spring Boot that features an AI opponent using the Minimax algorithm.

## Prerequisites

Before setting up this project, ensure you have the following installed on your system:

-   **Java Development Kit (JDK) 21** or higher
    -   [Download JDK](https://www.oracle.com/java/technologies/downloads/)
    -   Verify installation: `java -version`
-   **Apache Maven 3.6+**
    -   [Download Maven](https://maven.apache.org/download.cgi)
    -   Verify installation: `mvn -version`
-   **MySQL 8.0+**
    -   [Download MySQL](https://dev.mysql.com/downloads/mysql/)
    -   Verify installation: `mysql --version`

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd xogame-backend
```

### 2. Configure MySQL Database

1. Start your MySQL server
2. Create a database named `xogame`:

```sql
CREATE DATABASE xogame;
```

3. Update database credentials (if needed) in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/xogame
spring.datasource.username=root
spring.datasource.password=root
```

> **Note:** The application will automatically create required tables on first run using JPA's `hibernate.ddl-auto=update` configuration.

4. Configure CORS Settings

By default, browsers block API requests from different origins (domains, ports, or protocols) for security reasons. The backend is configured to accept requests from specific origins:

- http://localhost:5173 (for Vite apps) default frontend project use this
- http://localhost:3000 (for React.js apps)
- http://localhost:4200 (for Angular apps)

If your frontend runs on a different port or domain, update the allowed origins in `src/main/java/com/kraiwit/xogame/config/CorsConfig.java`:

### 3. Install Dependencies

```bash
mvn clean install
```

## Running the Application

### Option 1: Using Maven

```bash
mvn spring-boot:run
```

### Option 2: Using Maven Wrapper (Windows)

```cmd
mvnw.cmd spring-boot:run
```

### Option 3: Using Maven Wrapper (Linux/Mac)

```bash
./mvnw spring-boot:run
```

### Option 4: Run JAR File

```bash
mvn clean package
java -jar target/xogame-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

## API Endpoints

| Method | Endpoint         | Description             |
| ------ | ---------------- | ----------------------- |
| GET    | `/game`          | Health check endpoint   |
| POST   | `/game/start`    | Start a new game        |
| POST   | `/game/makemove` | Make a move in the game |
| POST   | `/game/cancel`   | Cancel an ongoing game  |
| GET    | `/game/history`  | Get game history        |

### Example Requests

**Start a New Game:**

```bash
POST http://localhost:8080/game/start
Content-Type: application/json

{
  "boardSize" : 3,
  "isAI" : true / false,
  "firstPlayer" : "X / O"
}
```

**Make a Move:**

```bash
POST http://localhost:8080/game/makemove
Content-Type: application/json

{
  "gameID": "game-id-here",
  "col" : int,
  "row" : int,
  "player" : "X / O" get from respone currentPlayer when start game
}
```

## Technologies Used

-   **Java 21** - Programming language
-   **Spring Boot 3.5.6** - Application framework
-   **Spring Data JPA** - Database operations and ORM
-   **Hibernate** - ORM implementation
-   **MySQL** - Relational database
-   **Maven** - Build automation and dependency management
-   **Lombok** - Reduce boilerplate code
-   **Spring Boot Starter Validation** - Input validation

## วิธีออกแบบ

เมื่อเริ่มเกมให้ใช้
| POST | `/game/start` | Start a new game |
เพื่อระบุขนาดสนามและสู้กับ AI ใช่ไหม
หลังจากนั้นโปรแกรมจะเรียกใช้ GameService เพื่อสร้างเกมเก็บใว้ใน memory
(ถ้าเริ่มเซิฟใหม่เกมที่กำลังดำเนินการอยู่จะหายไป)
แล้วส่งค่าของเกมนั้นกลับ (gameID ,ข้อมูลสนาม ,ผู้เล่นคณะนั้น)
start game : request -> GameController -> Game service -> create and store in memory

การเดิน
| POST | `/game/makemove` | Make a move in the game |
ส่ง gameID และตำแหน่ง (col , row) และ player คณะนั้นไปเพื่อทำการเดินในเทิร์น
start game : request -> GameController -> Game service -> Game -> makemove method

## AI algorithm

จะใช้อย่างคือ minimax และการ random
โดยจะสุ่มให้ 70% เป็น best move และ 30% เป็น random

โดย best move จะใช้ Minimax algorithm

### Minimax algorithm

Minimax คืออัลกอริทึมสำหรับเกม 2 ผู้เล่น ทำงานโดยสำรวจทุกท่าที่เป็นไปได้ล่วงหน้า ผู้เล่นหนึ่งพยายามเพิ่มคะแนนสูงสุด (Max) อีกฝ่ายพยายามลดคะแนนต่ำสุด (Min) สลับกันไปจนเลือกท่าที่ดีที่สุดได้ โดยสมมติว่าคู่แข่งเล่นอย่างสมบูรณ์แบบ