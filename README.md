# Understand sequence diagram and trace distributed applications with Open Telemetry/PlantUml SDK

## 使用步驟

1. `git clone` 此專案

2. 包裝成容器映像檔

```
cd sequencetracing
cd gateway 
./gradlew build
docker build -t gateway .

cd ../appserver
./gradlew build
docker build -t gateway .

cd ../dataserver
./gradlew build
docker build -t gateway .
```

3. 啟動容器
```
cd ../sequencetracing
docker compose up -d
```

4. 啟動socat來對應2375 port到docker.sock

`docker run -p 2375:2375 -v //var/run/docker.sock://var/run/docker.sock alpine/socat tcp-listen:2375,reuseaddr,fork unix-connect:/var/run/docker.sock`

5. curl API

```
curl -H "x-bdd-corrid:qwert" http://localhost:8080/test1
curl -H "x-bdd-corrid:qwert" http://localhost:8080/test2
```

6. 執行測試程式

```
cd sequencetracing/sequencetracing

# use the corrid set in the previous step(curl/postman/insomnia)
# /test1
gradlew.bat test --tests SeqTraceTest -DcorrHeader=x_bdd_corrid -Doutput=src/test/resources/seqtrace/gateway-test1.png -Dcorrid=qwert -Duml=src/test/resources/seqtrace/gateway-test1.puml --no-daemon --rerun

# /test2
# -info will print all the system output
gradlew.bat test --tests SeqTraceTest -DcorrHeader=x_bdd_corrid -Doutput=src/test/resources/seqtrace/gateway-test2.png -Dcorrid=qwert -Duml=src/test/resources/seqtrace/gateway-test2.puml --no-daemon -info
```

## 出處
https://medium.com/@wirelesser/understand-sequence-diagram-and-trace-distributed-applications-with-open-telemetry-plantuml-sdk-ace13688de74


