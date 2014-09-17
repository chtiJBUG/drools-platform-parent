package com.pymmasoftware.platform.login.loginmodule.principal;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjnkouyee on 3/16/14.
 */
public class DroolsGroup implements Group, Serializable {


    private static final long serialVersionUID = 1L;
    private final String name;
    private final Set<Principal> users = new HashSet<Principal>();

    public DroolsGroup(String name) {
        this.name = name;
    }

    public boolean addMember(Principal user) {
        return users.add(user);
    }

    public boolean removeMember(Principal user) {
        return users.remove(user);
    }

    public boolean isMember(Principal member) {
        return users.contains(member);
    }

    public Enumeration<? extends Principal> members() {
        return Collections.enumeration(users);
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        // you are smart enough to write this; just compare the name
        return true;
    }

    // yeah, write your own hashCode method too
}
