package codelens.authenticators;

import codelens.dtos.Customer;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.forms.RegistrationPage;
import org.keycloak.events.Details;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static codelens.utils.DeviceHelper.VALID_EMAILS;

public class VerifyUsernameAuthenticator implements Authenticator {

    public static final String PROVIDER_ID = "codelens-verify-username";
    public static final String VERIFY_USERNAME_FORM = "verify-username.ftl";
    public static final String EMAIL_NOT_FOUND = "emailNotFoundMessage";

    private static final org.jboss.logging.Logger log = Logger.getLogger(VerifyUsernameAuthenticator.class);




    @Override
    public void authenticate(AuthenticationFlowContext context) {
        Response challenge = context.form()
                .createForm(VERIFY_USERNAME_FORM);
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String email = formData.getFirst(RegistrationPage.FIELD_EMAIL);

        // validate email exists ....
        // you could a rest api call to an external service .....
        Customer customer = VALID_EMAILS.stream().filter(f -> f.getEmail().equals(email)).findFirst().orElse(null);

        if (customer == null) {
            Response response = context.form()
                    .addError(new FormMessage(EMAIL_NOT_FOUND))
                    .createForm(VERIFY_USERNAME_FORM);
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, response);
            return;
        }

        KeycloakSession session = context.getSession();
        RealmModel realm = context.getRealm();
        UserModel user = session.users().addUser(realm, email);
        user.setEnabled(true);
        user.setEmail(email);
        user.setFirstName(customer.getFirstName());
        user.setLastName(customer.getLastName());

        context.setUser(user);

        context.getEvent().user(user);
        context.getEvent().success();
        context.newEvent().event(EventType.LOGIN);

        context.getEvent().client(context.getAuthenticationSession().getClient().getClientId())
                .detail(Details.REDIRECT_URI, context.getAuthenticationSession().getRedirectUri())
                .detail(Details.AUTH_METHOD, context.getAuthenticationSession().getProtocol());

        String authType = context.getAuthenticationSession().getAuthNote(Details.AUTH_TYPE);
        if (authType != null) {
            context.getEvent().detail(Details.AUTH_TYPE, authType);
        }

        // todo:- have a method to generate a secure password then send to the email supplied ...
        String password = "Password1*";
        session.userCredentialManager().updateCredential(realm, user, UserCredentialModel.password(password, false));

        user.addRequiredAction(UserModel.RequiredAction.UPDATE_PASSWORD);
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
        log.info("Set Required Actions Called ...");
    }

    @Override
    public void close() {
        log.info("Close Called ...");
    }
}
