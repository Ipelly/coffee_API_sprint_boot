# Coffee App - API Server

Powered by Spring Boot.

### Installation
1. Install the dependencies (see **Dependencies** section below)
2. Apply database migrations
```
$ gradle flywayMigrate
```

3. Start the server
```
$ gradle clean build run
```

Visit `http://localhost:8080/v1/status` to check the server status.

### Dependencies
* Java 1.8 or higher
* Gradle 2.3 or higher
* MySQL

```
On Mac (using homebrew):
Download and Install JDK 1.8 from Oracle 
$ brew install gradle
$ brew install mysql

On Linux:
$ sudo yum install java-1.8.0-openjdk-devel
$ sudo yum install gradle
$ sudo yum install mysql
```
