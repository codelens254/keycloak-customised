package codelens.requiredactions;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class UpdateStaffNumberRequiredActionFactory implements RequiredActionFactory {

    private static final Logger log = Logger.getLogger(UpdateStaffNumberRequiredActionFactory.class);

    private static final UpdateStaffNumberRequiredAction SINGLETON = new UpdateStaffNumberRequiredAction();

    @Override
    public String getDisplayText() {
        return "Update Staff Number";
    }

    @Override
    public RequiredActionProvider create(KeycloakSession keycloakSession) {
        return SINGLETON;
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
        return UpdateStaffNumberRequiredAction.PROVIDER_ID;
    }
}
