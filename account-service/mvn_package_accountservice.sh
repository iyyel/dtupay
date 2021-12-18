# author Troels (s161791)
# author Daniel (s151641)

mvn -f ../utils/pom.xml -D maven.test.skip=true clean install
mvn -f pom.xml -D maven.test.skip=false package
