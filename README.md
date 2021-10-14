# jharkendar Project

## Jenkins
### Run locally
```aidl
./home/hans/Dokumente/sonarqube/bin/linux-x86-64/sonar.sh start
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
