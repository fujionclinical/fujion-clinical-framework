package org.fujionclinical.api.user;

import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import org.fujionclinical.api.security.ISecurityDomain;

public class UserImpl extends PersonImpl implements User {

    private ISecurityDomain securityDomain;

    private String username;

    private String password;

    public UserImpl() {
    }

    public UserImpl(String username, String password, ISecurityDomain securityDomain) {
        this.username = username;
        this.password = password;
        this.securityDomain = securityDomain;
    }

    @Override
    public String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    @Override
    public ISecurityDomain getSecurityDomain() {
        return securityDomain;
    }

    protected void setSecurityDomain(ISecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
    }

}
