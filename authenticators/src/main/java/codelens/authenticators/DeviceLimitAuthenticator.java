package codelens.authenticators;

import codelens.dtos.Customer;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;

import org.jboss.logging.Logger;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.provider.ProviderConfigProperty;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static codelens.authenticators.VerifyUsernameAuthenticator.EMAIL_NOT_FOUND;
import static codelens.utils.DeviceHelper.VALID_EMAILS;

public class DeviceLimitAuthenticator extends AbstractDirectGrantAuthenticator {
    private static final Logger log = Logger.getLogger(DeviceLimitAuthenticator.class);
    private static final String PROVIDER_ID = "device-limit-for-user";
    private static final String DEVICE_LIMIT_REACHED = "deviceLimitMessage";

    private static final int MAX_DEVICES_ALLOWED = 5;

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        UserModel userModel = context.getUser();

        String email = userModel.getEmail();
        log.info(String.format("Email found is: %s", email));

        /// do the rest call ....

        Customer customer = VALID_EMAILS.stream().filter(f -> f.getEmail().equals(email)).findFirst().orElse(null);

        if (customer == null) {
            Response response = context.form()
                    .addError(new FormMessage(EMAIL_NOT_FOUND))
                    .createForm(VerifyUsernameAuthenticator.VERIFY_USERNAME_FORM);
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, response);
            return;
        }

        int devices = customer.getDevices().size();
        log.info(String.format("Number of devices: %d", devices));
        if (devices >= MAX_DEVICES_ALLOWED) {
            Response response = context.form()
                    .addError(new FormMessage(DEVICE_LIMIT_REACHED))
                    .createForm(VerifyUsernameAuthenticator.VERIFY_USERNAME_FORM);
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, response);
            return;
        }

        context.success();
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
    public List<RequiredActionFactory> getRequiredActions(KeycloakSession session) {
        return null;
    }

    @Override
    public String getDisplayType() {
        return "Device Limit Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    protected static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED};


    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Validated the number of devices for a user.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();


    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
