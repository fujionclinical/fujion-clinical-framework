/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.security;

import org.fujion.common.LocalizedMessage;

public class Constants {

    // Miscellaneous security constants

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String PRIV_PREFIX = "PRIV_";

    public static final String ROLE_USER = ROLE_PREFIX + "USER";

    public static final String PRIV_DEBUG = PRIV_PREFIX + "DEBUG"; // Privilege that enables debug modes.

    public static final String ANONYMOUS_USER = "ANONYMOUS_USER";

    public static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

    public static final String SAVED_USER = "SPRING_SECURITY_SAVED_USER";

    public static final String DEFAULT_SECURITY_DOMAIN = "fcf_defaultDomain";

    public static final String DEFAULT_USERNAME = "fcf_defaultUsername";

    public static final String DIALOG_ACCESS_DENIED = "/security/accessDenied";

    // Logout constants

    public static final String PROP_LOGOUT_URL = "org.fujionclinical.security.logout.url";

    public static final LocalizedMessage MSG_LOGOUT_MESSAGE_DEFAULT = new LocalizedMessage("security.logout.message.default");

    public static final String TARGET_URL_PARAMETER = "TARGET_URL";

    // Login constants

    public static final String PROP_LOGIN_LOGO = "LOGIN.LOGO";

    public static final String PROP_LOGIN_HEADER = "LOGIN.HEADER";

    public static final String PROP_LOGIN_FOOTER = "LOGIN.FOOTER";

    public static final String PROP_LOGIN_INFO = "LOGIN.INFO";

    public static final LocalizedMessage MSG_LOGIN_PAGE_TITLE = new LocalizedMessage("security.login.form.panel.title");

    public static final LocalizedMessage MSG_LOGIN_ERROR = new LocalizedMessage("security.login.error");

    public static final LocalizedMessage MSG_LOGIN_FORM_TIMEOUT_MESSAGE = new LocalizedMessage("security.login.form.timeout.message");

    public static final LocalizedMessage MSG_LOGIN_PROGRESS = new LocalizedMessage("security.login.progress");

    public static final LocalizedMessage MSG_LOGIN_REQUIRED_FIELDS = new LocalizedMessage("security.login.required.fields");

    public static final LocalizedMessage MSG_LOGIN_NO_VALID_DOMAINS = new LocalizedMessage("security.login.error.no.valid.domains");

    public static final LocalizedMessage MSG_LOGIN_ERROR_INVALID = new LocalizedMessage("security.login.error.invalid");

    public static final LocalizedMessage MSG_LOGIN_ERROR_EXPIRED_PASSWORD = new LocalizedMessage("security.login.error.expired.password");

    public static final LocalizedMessage MSG_LOGIN_ERROR_EXPIRED_USER = new LocalizedMessage("security.login.error.expired.user");

    public static final LocalizedMessage MSG_LOGIN_ERROR_UNEXPECTED = new LocalizedMessage("security.login.error.unexpected");

    public static final LocalizedMessage MSG_DENIED_ANONYMOUS = new LocalizedMessage("security.denied.message.anonymous");

    public static final LocalizedMessage MSG_DENIED_AUTH = new LocalizedMessage("security.denied.message.authenticated");

    // Password constants

    public static final String PASSWORD_EXPIRED_EXCEPTION = "expiredPasswordException";

    public static final LocalizedMessage MSG_PASSWORD_RANDOM_LENGTH = new LocalizedMessage("security.password.random.length");

    public static final LocalizedMessage MSG_PASSWORD_RANDOM_CONSTRAINTS = new LocalizedMessage("security.password.random.constraints");

    public static final LocalizedMessage MSG_PASSWORD_CHANGE_UNAVAILABLE = new LocalizedMessage("security.password.dialog.unavailable");

    public static final LocalizedMessage MSG_PASSWORD_CHANGE_PAGE_TITLE = new LocalizedMessage("security.password.dialog.panel.title");

    public static final LocalizedMessage MSG_PASSWORD_CHANGE_ERROR = new LocalizedMessage("security.password.dialog.password.change.error");

    public static final LocalizedMessage MSG_PASSWORD_DIALOG_LABEL = new LocalizedMessage("security.password.dialog.label");

    public static final LocalizedMessage MSG_PASSWORD_REQUIRED_FIELDS = new LocalizedMessage("security.password.dialog.required.fields");

    public static final LocalizedMessage MSG_PASSWORD_CONFIRM = new LocalizedMessage("security.password.dialog.confirm.passwords");

    public static final LocalizedMessage MSG_PASSWORD_CHANGED_TEXT = new LocalizedMessage("security.password.dialog.password.changed");

    public static final LocalizedMessage MSG_PASSWORD_CHANGED_TITLE = new LocalizedMessage("security.password.dialog.password.changed.dialog.title");

    public static final LocalizedMessage MSG_PASSWORD_RULES = new LocalizedMessage("security.password.rules.label");


    /**
     * Enforce static class.
     */
    private Constants() {
    }
}
