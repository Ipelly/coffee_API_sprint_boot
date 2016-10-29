# Xipli - API Server

Powered by Spring Boot.

### Installation
1. Install the dependencies (see **Dependencies** section below)
2. Create `xilpi` database in mysql server
3. Apply database migrations
```
$ gradle flywayMigrate
```

4. Start the server
```
$ gradle clean build run
```

Visit `http://localhost:8888/v1/status` to check the server status.

### Reset database
```
$ gradle flywayClean flywayMigrate
```

### Run tests
```
$ gradle clean test
```
### Reset & Run tests
```
$ gradle clean flywayClean flywayMigrate test
```

### Dependencies
* Java 1.8 or higher
* Gradle 2.3 or higher
* MySQL

```
On Mac:
Download and Install JDK 1.8 from Oracle 
Install homebrew
$ brew install gradle
$ brew install mysql

On Linux:
$ sudo yum install java-1.8.0-openjdk-devel
$ sudo yum install gradle
$ sudo yum install mysql
```
