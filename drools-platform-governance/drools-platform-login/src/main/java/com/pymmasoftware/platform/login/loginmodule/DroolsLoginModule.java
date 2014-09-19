package com.pymmasoftware.platform.login.loginmodule;

import com.pymmasoftware.platform.login.loginmodule.principal.DroolsGroup;
import com.pymmasoftware.platform.login.loginmodule.principal.DroolsPrincipal;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gjnkouyee on 3/16/14.
 */
public class DroolsLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;

    private Map sharedState;
    private Map options;

    //the authencation status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    //username & password
    private String username;
    private String password;

    private DroolsPrincipal userPrincipal;
    private DroolsGroup[] roles;
    private static Context env = null;
    private static DataSource dataSource = null;


    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;

        try {
            if (env == null) {
                env = (Context) new InitialContext().lookup("java:comp/env");
                if (dataSource == null) {
                    dataSource = (DataSource) env.lookup("jdbc/URDroolsDS");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public boolean login() throws LoginException {
        succeeded = false;
        QueryRunner queryRunner = null;
        try {
            userPrincipal = null;
            roles = null;
            if (callbackHandler == null)
                throw new LoginException("No callback handler");

            NameCallback nameCallback = new NameCallback("Username");
            PasswordCallback passwordCallback = new PasswordCallback(
                    "Password", false);

            Callback[] callbacks = new Callback[]{nameCallback,
                    passwordCallback};
            try {
                callbackHandler.handle(callbacks);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedCallbackException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            username = nameCallback.getName();
            password = new String(passwordCallback.getPassword());

            queryRunner = new QueryRunner(dataSource);

            // Create a ResultSetHandler implementation to convert the
            // first row into an Object[].
            ResultSetHandler<DroolsPrincipal> h = new ResultSetHandler<DroolsPrincipal>() {
                public DroolsPrincipal handle(ResultSet rs) throws SQLException {
                    if (!rs.next()) {
                        return null;
                    }

                    ResultSetMetaData meta = rs.getMetaData();
                    String userName = rs.getString("username");

                    DroolsPrincipal droolsPrincipal = new DroolsPrincipal(userName);
                    droolsPrincipal.setId(rs.getInt("id"));

                    return droolsPrincipal;
                }
            };
            ResultSetHandler<List<DroolsGroup>> hh = new ResultSetHandler<List<DroolsGroup>>() {
                public List<DroolsGroup> handle(ResultSet rs) throws SQLException {
                    if (!rs.next()) {
                        return null;
                    }
                    List<DroolsGroup> droolsGroups = new ArrayList<>();
                    boolean goOne = true;
                    while (goOne) {
                        String groupName = rs.getString("groups");

                        DroolsGroup droolsGroup = new DroolsGroup(groupName);
                        droolsGroups.add(droolsGroup);
                        if (rs.next() == false) {
                            goOne = false;
                        }
                    }
                    return droolsGroups;
                }
            };

            String sqlname = "select * from guvnorusers where username = ? and password = ? ";
            DroolsPrincipal user = queryRunner.query(sqlname, h, username, password);
            if (user == null) {
                succeeded = false;
                throw new FailedLoginException("The username or The password is incorrect");
            } else {

                userPrincipal = user;
                String sqlname2 = "select groups from guvnorgroups gr,guvnorusers_groups gr_user " +
                        "where gr.id = gr_user.groups_id  " +
                        "and gr_user.guvnorusers_id= ?";
                List<DroolsGroup> droolsGroups = queryRunner.query(sqlname2, hh, user.getId());
                if (droolsGroups != null) {
                    int i = droolsGroups.size();
                    roles = new DroolsGroup[i];
                    i = 0;
                    for (DroolsGroup droolsGroup : droolsGroups) {
                        roles[i] = droolsGroup;
                    }
                }
                succeeded = true;
                return true;
            }


        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        } finally {
            queryRunner = null;
        }

    }

    @Override
    public boolean commit() throws LoginException {

        // this is the important part to work with JBoss:
        subject.getPrincipals().add(userPrincipal);
        // jboss requires the name 'Roles'
        DroolsGroup group = new DroolsGroup("Roles");
        for (DroolsGroup role : roles) {
            group.addMember(role);
        }
        subject.getPrincipals().add(group);

        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        // TODO Auto-generated method stub
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        password = null;
        userPrincipal = null;
        return true;
    }

    @Override
    public boolean logout() throws LoginException {

        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        password = null;
        userPrincipal = null;

        return true;
    }
}
