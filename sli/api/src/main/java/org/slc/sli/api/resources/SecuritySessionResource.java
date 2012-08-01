/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.security.OauthSessionManager;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.roles.Role;
import org.slc.sli.api.security.roles.RoleRightAccess;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;

/**
 * System resource class for security session context.
 * Hosted at the URI path "/system/session"
 */
@Path("{a:v1/|}system/session")
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8", HypermediaType.VENDOR_SLC_JSON + ";charset=utf-8" })
public class SecuritySessionResource {

    @Autowired
    private RoleRightAccess roleAccessor;

    @Autowired
    private OauthSessionManager sessionManager;

    @Value("${sli.security.noSession.landing.url}")
    private String realmPage;

    /**
     * Method processing HTTP GET requests to the logout resource, and producing "application/json" MIME media
     * type.
     *
     * @return HashMap indicating success or failure for logout action (matches type "application/json" through jersey).
     */
    @GET
    @Path("logout")
    public Map<String, Object> logoutUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication oAuth = ((OAuth2Authentication) auth).getUserAuthentication();

        Map<String, Object> logoutMap = new HashMap<String, Object>();
        logoutMap.put("logout", true);
        logoutMap.put("msg", "You are logged out of SLI");
        if (oAuth instanceof PreAuthenticatedAuthenticationToken) {
            PreAuthenticatedAuthenticationToken userAuth = (PreAuthenticatedAuthenticationToken) oAuth;
            logoutMap.put("logout", this.sessionManager.logout((String) userAuth.getCredentials()));
        }

        return logoutMap;
    }

    /**
     * Method processing HTTP GET requests to debug resource, producing "application/json" MIME media
     * type.
     *
     * @return SecurityContext that will be send back as a response of type "application/json".
     */
    @GET
    @Path("debug")
    public SecurityContext sessionDebug() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new InsufficientAuthenticationException("User must be logged in");
        } else if (auth instanceof OAuth2Authentication) {
            if (((OAuth2Authentication) auth).getUserAuthentication() instanceof AnonymousAuthenticationToken) {
                throw new InsufficientAuthenticationException("User must be logged in");
            }
        } else if (auth instanceof AnonymousAuthenticationToken) {
            throw new InsufficientAuthenticationException("User must be logged in");
        }

        SLIPrincipal principal = (SLIPrincipal) auth.getPrincipal();
        principal.setSliRoles(principal.getRoles());
        return SecurityContextHolder.getContext();
    }

    /**
     * Method processing HTTP GET requests to check resource, producing "application/json" MIME media
     * type.
     *
     * @return Map containing relevant user details (if authenticated).
     */
    @GET
    @Path("check")
    public Object sessionCheck() {

        final Map<String, Object> sessionDetails = new TreeMap<String, Object>();

        if (isAuthenticated(SecurityContextHolder.getContext())) {
            sessionDetails.put("authenticated", true);

            SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            sessionDetails.put("user_id", principal.getId());
            sessionDetails.put("full_name", principal.getName());
            sessionDetails.put("granted_authorities", principal.getRoles());
            sessionDetails.put("realm", principal.getRealm());
            sessionDetails.put("edOrg", principal.getEdOrg());
            sessionDetails.put("edOrgId", principal.getEdOrgId());

            sessionDetails.put("sliRoles", principal.getRoles());
            sessionDetails.put("tenantId", principal.getTenantId());
            sessionDetails.put("external_id", principal.getExternalId());
            sessionDetails.put("email", getUserEmail(principal));

            List<Role> allRoles = SecurityUtil.sudoRun(new SecurityTask<List<Role>>() {
                @Override
                public List<Role> execute() {
                    return roleAccessor.fetchAllRoles();
                }
            });

            sessionDetails.put("all_roles", allRoles);

        } else {
            sessionDetails.put("authenticated", false);
            sessionDetails.put("redirect_user", realmPage);
        }

        return sessionDetails;
    }

    private String getUserEmail(SLIPrincipal principal) {
        // Admin users are special cases.
        if (principal.getEntity().getBody().isEmpty()) {
            return principal.getExternalId();
        }
        Map<String, Object> body = principal.getEntity().getBody();
        if (!body.containsKey("electronicMail")) {
            return "";
        }
        List emails = (List) body.get("electronicMail");
        if (emails.size() == 1) {
            Map<String, String> email = (Map) emails.get(0);
            return email.get("emailAddress");
        }

        String address = getEmailAddressByType(emails, "Work");
        if (address.length() != 0) {
            return address;
        }

        address = getEmailAddressByType(emails, "Organization");
        if (address.length() != 0) {
            return address;
        }

        address = getEmailAddressByType(emails, "Other");
        if (address.length() != 0) {
            return address;
        }

        address = getEmailAddressByType(emails, "Home/Personal");
        if (address.length() != 0) {
            return address;
        }
        return "";
    }

    private String getEmailAddressByType(List emails, String checkedType) {
        for (Object baseEmail : emails) {
            Map<String, String> email = (Map) baseEmail;
            String type = email.get("emailAddressType");
            String address = email.get("emailAddress");
            if (type.equals(checkedType)) {
                return address;
            }
        }
        return "";
    }

    /**
     * Indicates whether or not the current user is authenticated into SLI.
     *
     * @param securityContext User's Security Context (checked for authentication credentials).
     * @return true (indicating user is authenticated) or false (indicating user is NOT authenticated).
     */
    private boolean isAuthenticated(SecurityContext securityContext) {
        return securityContext.getAuthentication().isAuthenticated();
    }
}
