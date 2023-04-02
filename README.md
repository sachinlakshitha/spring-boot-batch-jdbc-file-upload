# Spring Boot Batch JDBC File Upload Example

This is a simple example of how to use Spring Boot to read data from a upload CSV file and insert it into a database using JDBC batch processing.

## Requirements

1. Java 17
2. Maven 3
3. MySQL 8.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/sachinlakshitha/spring-boot-batch-jdbc-file-upload.git
```

**2. Change MySQL username and password as per your MySQL installation**

+ open `src/main/resources/application.properties` file.

+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

**3. Build and run the app using maven**

```bash
mvn package
java -jar target/spring-boot-batch-jdbc-file-upload-1.0.0-SNAPSHOT.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Examples of API requests

### Upload customer CSV file

Sample CSV file can be found in `src/main/resources` directory.

```bash
curl -i -X POST -H "Content-Type: multipart/form-data" -F "file=@/path/to/file.csv" http://localhost:8080/api/v1/customer/upload
```

### Get all customers

```bash
curl -i -X GET http://localhost:8080/api/v1/customer
```

## References
https://spring.io/guides/gs/batch-processing/
https://stackoverflow.com/questions/39107254/com-mysql-jdbc-exceptions-jdbc4-mysqlsyntaxerrorexception-table-spring-batch-t
https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/schema-mysql.sql
https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/2.2/migration-mysql.sql
https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/4.1/migration-oracle.sql
https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/4.3/migration-mysql.sql
https://github.com/spring-projects/spring-batch/blob/main/spring-batch-core/src/main/resources/org/springframework/batch/core/migration/5.0/migration-mysql.sql
