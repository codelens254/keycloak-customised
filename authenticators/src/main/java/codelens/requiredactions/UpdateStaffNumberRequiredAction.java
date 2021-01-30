package codelens.requiredactions;

import codelens.dtos.Staff;
import codelens.utils.StaffNumberHelper;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class UpdateStaffNumberRequiredAction implements RequiredActionProvider {

    public static final String PROVIDER_ID = "usn-required-action";
    private static final String UPDATE_STAFF_NUMBER_FORM = "update-staff-number.ftl";

    @Override
    public void evaluateTriggers(RequiredActionContext requiredActionContext) {
        /*
            - no triggers to evaluate. proceeding with default implementation
         */
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        LoginFormsProvider formsProvider = context.form();

        Response challenge = formsProvider.createForm(UPDATE_STAFF_NUMBER_FORM);
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String staffNumber = formData.getFirst("staffNumber").trim();

        UserModel userModel = context.getUser();
        // mock staff number ...
        Staff staffMember = StaffNumberHelper.getStaffs().stream()
                .filter(staff -> staff.getEmail().equals(userModel.getEmail()))
                .findFirst()
                .orElse(null);

        LoginFormsProvider formsProvider = context.form();
        if (staffMember == null || !staffNumber.equals(staffMember.getStaffNumber())) {
            Response challenge = formsProvider
                    .addError(new FormMessage("Invalid Staff Number"))
                    .createForm(UPDATE_STAFF_NUMBER_FORM);

            context.challenge(challenge);
            return;
        }

        // :-) looks good ....

        userModel.setSingleAttribute("staffNumber", staffMember.getStaffNumber());
        context.success();
    }

    @Override
    public void close() {
 /*
            - no triggers to evaluate. proceeding with default implementation
         */
    }
}
