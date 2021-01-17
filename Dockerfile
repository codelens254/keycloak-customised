FROM quay.io/keycloak/keycloak:12.0.1

ARG AUTHENTICATOR_JAR=authenticators/target/authenticators.jar

# copy the jars ...
COPY ${AUTHENTICATOR_JAR} /opt/jboss/keycloak/standalone/deployments/