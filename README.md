# jharkendar Project

## Jenkins
### Run locally
```aidl
cd /home/hans/Dokumente/sonarqube/bin/linux-x86-64
./sonar.sh start
```

Access http://localhost:9000

User: admin

Password: admin


```aidl
docker pull sonarqube
docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
docker start sonarqube
sonar-scanner
```

# Quarkus
## Run
```aidl
# build
./mvnw package -Pnative

# docker build
docker build -f src/main/docker/Dockerfile.native -t quarkus/jharkendar .

# run with docker compose
docker-compose up
```

```aidl
curl --header "Content-Type: application/json" \
                            --request GET \
                            localhost:8081/topic/get

```
