FROM quay.io/keycloak/keycloak:9.0.3

ARG AUTHENTICATOR_JAR=authenticators/target/authenticators.jar

# copy the jars ...
COPY ${AUTHENTICATOR_JAR} /opt/jboss/keycloak/standalone/deployments/