# author Troels (s161791)
# author Daniel (s151641)

mvn -f payment-service/pom.xml test
mvn -f token-service/pom.xml test
mvn -f account-service/pom.xml test
mvn -f report-service/pom.xml test
mvn -f client/pom.xml test
