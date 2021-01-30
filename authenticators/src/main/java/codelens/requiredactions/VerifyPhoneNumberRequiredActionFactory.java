package codelens.requiredactions;

import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class VerifyPhoneNumberRequiredActionFactory  implements RequiredActionFactory {
    @Override
    public String getDisplayText() {
        return null;
    }

    @Override
    public RequiredActionProvider create(KeycloakSession keycloakSession) {
        return new VerifyPhoneNumber();
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "verify-phonenumber-action";
    }
}
