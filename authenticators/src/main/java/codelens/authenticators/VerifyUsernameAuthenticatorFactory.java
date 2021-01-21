package codelens.authenticators;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class VerifyUsernameAuthenticatorFactory implements AuthenticatorFactory {

    private static final VerifyUsernameAuthenticator SINGLETON = new VerifyUsernameAuthenticator();

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SINGLETON;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Verifies a username from external system.";
    }

    @Override
    public String getDisplayType() {
        return "CodeLens Verify Username";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        final List<ProviderConfigProperty> configProperties = new ArrayList<>();
        ProviderConfigProperty property = new ProviderConfigProperty();
        property.setName("external.url");
        property.setLabel("External service base url");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Base url for the external service base url");
        configProperties.add(property);


        // Get User Details URL ...
        property = new ProviderConfigProperty();
        property.setName("external.url.client");
        property.setLabel("External service client");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("External service client id");
        configProperties.add(property);

        return configProperties;
    }

    @Override
    public void init(Config.Scope scope) {
        // self reg validation init not used
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        // self reg validation post init not used
    }

    public void close() {

    }

    @Override
    public String getId() {
        return VerifyUsernameAuthenticator.PROVIDER_ID;
    }
}
