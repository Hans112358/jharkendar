# jharkendar Project

Fun project. Provides a rest interface for handling summaries, topics and tags.

## How to run
```
# clean up
docker rm -f jharkendar-quarkus-db-1 && docker rm -f jharkendar-quarkus-service-1

# build
./mvnw package -Pnative

# or build withoud tests
./mvnw package -Pnative -DskipTests

# docker build
docker build -f src/main/docker/Dockerfile.native -t jharkendar-app .

# run with docker compose
docker-compose up
```
or
```
# quickstart

docker rm -f jharkendar-quarkus-db-1 && docker rm -f jharkendar-quarkus-service-1 && ./mvnw package -Pnative && docker build -f src/main/docker/Dockerfile.native -t quarkus/jharkendar . && docker-compose up
```
## Swagger

Access Swagger UI via http://localhost:8080/q/swagger-ui/.


## Sonar
```
docker pull sonarqube
docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
docker start sonarqube
sonar-scanner
```
Access Sonar via http://localhost:9000 (User: admin, Password: admin).



