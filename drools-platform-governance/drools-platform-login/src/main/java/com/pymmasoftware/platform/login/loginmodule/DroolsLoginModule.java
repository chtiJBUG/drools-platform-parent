package com.pymmasoftware.platform.login.loginmodule;

import com.pymmasoftware.platform.login.loginmodule.principal.DroolsGroup;
import com.pymmasoftware.platform.login.loginmodule.principal.DroolsPrincipal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private DroolsPrincipal[] roles;
    private Context env;
    private DataSource pool;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private boolean passed;


    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;

        try {
            env = (Context) new InitialContext().lookup("java:comp/env");
            pool = (DataSource) env.lookup("jdbc/URDroolsDS");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public boolean login() throws LoginException {

        succeeded = false;

        try {

            conn = pool.getConnection();

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

            conn = pool.getConnection();
            stmt = conn.createStatement();
            // Some sql commands

            String sqlname = "select * from guvnorusers where username ='"
                    + username + "'and password ='"
                    + password + "'";

            rs = stmt.executeQuery(sqlname);

            passed = rs.next();
            if (!passed) {
                succeeded = false;
                System.out.print("unknown user");
                throw new FailedLoginException("The username is incorrect");
            } else {
                String basehash = rs.getString("password");
                Integer id = rs.getInt("id");
                // crypto.UserPasswordCheck.compareWith=crypto.UserPasswordCheck.putIntoDateBase(name,
                // pass);
                if (!basehash.equals(password)) {
                    throw new FailedLoginException("The password is incorrect");
                } else {
                    userPrincipal = new DroolsPrincipal(username);
                    Statement stmt2;
                    ResultSet rs2;
                    String sqlname2 = "select groups from guvnorgroups gr,guvnorusers_groups gr_user " +
                            "where gr.id = gr_user.groups_id  " +
                            "and gr_user.guvnorusers_id=" + id;
                    stmt2 = conn.createStatement();
                    rs2 = stmt2.executeQuery(sqlname2);

                    List<DroolsPrincipal> rolesList = new ArrayList<>();

                    int i = 0;
                    while (rs2.next()) {
                        DroolsPrincipal droolsPrincipal = new DroolsPrincipal(rs2.getString("groups"));
                        rolesList.add(droolsPrincipal);
                        i++;
                    }
                    roles = new DroolsPrincipal[i];
                    i = 0;
                    for (DroolsPrincipal droolsPrincipal : rolesList) {
                        roles[i] = droolsPrincipal;
                    }
                    succeeded = true;
                    return true;
                }

            }
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException sqle) {
            }
            return true;
        }

    }

    @Override
    public boolean commit() throws LoginException {

        // this is the important part to work with JBoss:
        subject.getPrincipals().add(userPrincipal);
        // jboss requires the name 'Roles'
        DroolsGroup group = new DroolsGroup("Roles");
        for (DroolsPrincipal role : roles) {
            group.addMember(role);
        }
        subject.getPrincipals().add(group);

        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        // TODO Auto-generated method stub
        return false;
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
