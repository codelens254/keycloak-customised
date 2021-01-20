FROM quay.io/keycloak/keycloak:12.0.1

ARG AUTHENTICATOR_JAR=authenticators/target/authenticators.jar
ARG CODELENS_THEME_BASE_DIR=/opt/jboss/keycloak/themes/codelens

ARG CODELENS_THEMES_LOCAL_ROOT_DIR=themes/codelens

# copy the jars ...
COPY ${AUTHENTICATOR_JAR} /opt/jboss/keycloak/standalone/deployments/

# mkdir for new codelens theme ... let's call the theme codelens
RUN mkdir ${CODELENS_THEME_BASE_DIR}

#copy codelens theme to Keycloak themes folder ...
COPY ${CODELENS_THEMES_LOCAL_ROOT_DIR} ${CODELENS_THEME_BASE_DIR}