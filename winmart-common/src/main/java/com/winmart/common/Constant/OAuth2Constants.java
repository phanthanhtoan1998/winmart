package com.winmart.common.Constant;

public class OAuth2Constants {
    private OAuth2Constants() {
    }

    public static final String CODE = "code";

    public static final String CLIENT_ID = "client_id";

    public static final String CLIENT_SECRET = "client_secret";

    public static final String ERROR = "error";

    public static final String ERROR_DESCRIPTION = "error_description";

    public static final String REDIRECT_URI = "redirect_uri";

    public static final String DISPLAY = "display";

    public static final String SCOPE = "scope";

    public static final String STATE = "state";

    public static final String GRANT_TYPE = "grant_type";

    public static final String RESPONSE_TYPE = "response_type";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String TOKEN_TYPE = "token_type";

    public static final String EXPIRES_IN = "expires_in";

    public static final String ID_TOKEN = "id_token";

    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String LOGOUT_TOKEN = "logout_token";

    public static final String AUTHORIZATION_CODE = "authorization_code";


    public static final String IMPLICIT = "implicit";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String CLIENT_CREDENTIALS = "client_credentials";

    // https://tools.ietf.org/html/draft-ietf-oauth-assertions-01#page-5
    public static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
    public static final String CLIENT_ASSERTION = "client_assertion";

    // https://tools.ietf.org/html/draft-jones-oauth-jwt-bearer-03#section-2.2
    public static final String CLIENT_ASSERTION_TYPE_JWT = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    // http://openid.net/specs/openid-connect-core-1_0.html#OfflineAccess
    public static final String OFFLINE_ACCESS = "offline_access";

    // http://openid.net/specs/openid-connect-core-1_0.html#AuthRequest
    public static final String SCOPE_OPENID = "openid";

    // http://openid.net/specs/openid-connect-core-1_0.html#ScopeClaims
    public static final String SCOPE_PROFILE = "profile";
    public static final String SCOPE_EMAIL = "email";
    public static final String SCOPE_ADDRESS = "address";
    public static final String SCOPE_PHONE = "phone";

    public static final String UI_LOCALES_PARAM = "ui_locales";

    public static final String PROMPT = "prompt";
    public static final String ACR_VALUES = "acr_values";

    public static final String MAX_AGE = "max_age";

    // OIDC Session Management
    public static final String SESSION_STATE = "session_state";

    public static final String JWT = "JWT";

    // https://tools.ietf.org/html/rfc7636#section-6.1
    public static final String CODE_VERIFIER = "code_verifier";
    public static final String CODE_CHALLENGE = "code_challenge";
    public static final String CODE_CHALLENGE_METHOD = "code_challenge_method";

    // https://tools.ietf.org/html/rfc7636#section-6.2.2
    public static final String PKCE_METHOD_PLAIN = "plain";
    public static final String PKCE_METHOD_S256 = "S256";

    public static final String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
    public static final String AUDIENCE = "audience";
    public static final String REQUESTED_SUBJECT = "requested_subject";
    public static final String SUBJECT_TOKEN = "subject_token";
    public static final String SUBJECT_TOKEN_TYPE = "subject_token_type";
    public static final String REQUESTED_TOKEN_TYPE = "requested_token_type";
    public static final String ISSUED_TOKEN_TYPE = "issued_token_type";
    public static final String REQUESTED_ISSUER = "requested_issuer";
    public static final String SUBJECT_ISSUER = "subject_issuer";
    public static final String ACCESS_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:access_token";
    public static final String REFRESH_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:refresh_token";
    public static final String JWT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:jwt";
    public static final String ID_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:id_token";
    public static final String SAML2_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:saml2";

    public static final String UMA_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:uma-ticket";

    // https://tools.ietf.org/html/draft-ietf-oauth-device-flow-15#section-3.4
    public static final String DEVICE_CODE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:device_code";
    public static final String DEVICE_CODE = "device_code";

    public static final String CIBA_GRANT_TYPE = "urn:openid:params:grant-type:ciba";

    public static final String DISPLAY_CONSOLE = "console";
    public static final String INTERVAL = "interval";
    public static final String USER_CODE = "user_code";
}
