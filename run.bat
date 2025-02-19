set JAVA_HOME=%JAVA_HOME_17%

call mvn clean install -DskipTests

rem call java -Duser.timezone="UTC" -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar target/spring-account-transfer-1.0.0-SNAPSHOT.jar
call java -Duser.timezone="UTC" -jar target/spring-account-transfer-1.0.0-SNAPSHOT.jar