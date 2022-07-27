call mvn clean install -DskipTests

call java -agentpath:%USER_HOME%/agent/lightrun_agent.dll -jar target/spring-account-transfer-1.0.0-SNAPSHOT.jar