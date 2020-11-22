package example;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.events.Errors;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.messages.Messages;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class DeviceLimitAuthenticator extends AbstractDirectGrantAuthenticator {

    private static final Logger log = Logger.getLogger(DeviceLimitAuthenticator.class);
    private static final String PROVIDER_ID = "device-limit-for-user";
    private static final String ATTR_KEY_DEVICE_MANAGEMENT_URL = "device-management-url";
    private static final String ATTR_KEY_IDENTITY_AUTH_URL = "identity-auth-url";
    private static final String ATT_KEY_NUMBER_OF_DEVICES = "max-number-of-devices";
    private static final String ATTR_KEY_USER_PRESENTATION_SERVICE_URL = "user-presentation-url";


    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        authenticationFlowContext.success();
    }

    // To handle when a user exceeds device limit
    private void handleUserDeviceLimitExceeded(AuthenticationFlowContext context, int maximumAllowedDevices) {
        String additionalErrorMessage = String.format("You can only register a maximum of %d devices.", maximumAllowedDevices);

        Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(),
                Messages.REGISTRATION_NOT_ALLOWED, Errors.INVALID_REGISTRATION + ". " + additionalErrorMessage);

        challengeResponse.getHeaders().add("errorMessage", Messages.REGISTRATION_NOT_ALLOWED);
        challengeResponse.getHeaders().add("errorDescription",
                Errors.INVALID_REGISTRATION + ". " + additionalErrorMessage);
        context.failure(AuthenticationFlowError.USER_CONFLICT, challengeResponse);
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public String getDisplayType() {
        return "User Device Limit";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.CONDITIONAL,
            AuthenticationExecutionModel.Requirement.DISABLED};

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Limit Device for user";
    }


    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        ProviderConfigProperty property;

        // Identity Auth Url ...
        property = new ProviderConfigProperty();
        property.setName(ATTR_KEY_IDENTITY_AUTH_URL);
        property.setLabel("Identity Auth Url");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The url to be invoked to get the admin's credentials");
        property.setDefaultValue("http://a18bdfa49800544be979d4c172b0b176-277998948.eu-west-1.elb.amazonaws.com:8080/auth/realms/{realm}/protocol/openid-connect/token");
        configProperties.add(property);

        // Device Management Service Url ...
        property = new ProviderConfigProperty();
        property.setName(ATTR_KEY_DEVICE_MANAGEMENT_URL);
        property.setLabel("Device Management Url");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The Url to be used to fetch a user's devices.");
        property.setDefaultValue("http://afc25aca3340641e2a8d40ec46a0fadc-997574935.eu-west-1.elb.amazonaws.com:8080/api/device-management-service/client-api/v1/users/{userName}/devices");
        configProperties.add(property);

        // User Presentation Service Url ...
        property = new ProviderConfigProperty();
        property.setName(ATTR_KEY_USER_PRESENTATION_SERVICE_URL);
        property.setLabel("User Presentation Service Url");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The Url to be used to fetch a user's dbs UserId");
        property.setDefaultValue("http://afc25aca3340641e2a8d40ec46a0fadc-997574935.eu-west-1.elb.amazonaws.com:8080/api/user-presentation-service/client-api/v2/users/identities?externalId={userName}");
        configProperties.add(property);

        // Maximum Number Of Devices That Can Be Registered ...
        property = new ProviderConfigProperty();
        property.setName(ATT_KEY_NUMBER_OF_DEVICES);
        property.setLabel("Maximum Number Of Devices Allowed");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Maximum number of devices that a single user is allowed to register");
        property.setDefaultValue("3");
        configProperties.add(property);

    }


    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
