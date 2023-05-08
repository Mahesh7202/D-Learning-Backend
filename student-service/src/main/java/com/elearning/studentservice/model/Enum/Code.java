package com.elearning.studentservice.model.Enum;

public enum Code {

    INTERNAL_SERVER_ERROR,
    UNAUTHORIZED,
    ACCESS_TOKEN_EXPIRED,
    ACCESS_TOKEN_REVOKED,
    FORBIDDEN,
    APPLICATION_DISABLED,
    V1_ACCESS_TOKEN,
    BAD_REQUEST,
    MISSING_REQUIRED_PARAMETER,
            INCORRECT_TYPE,
            INVALID_VALUE,
            UNKNOWN_QUERY_PARAMETER,
            CONFLICTING_PARAMETERS,
            EXPECTED_JSON_BODY,
            VALUE_REGEX_MISMATCH,
    VALUE_TOO_SHORT,
            VALUE_TOO_LONG,
            VALUE_TOO_LOW,
            VALUE_TOO_HIGH,
            VALUE_EMPTY,
    ARRAY_LENGTH_TOO_LONG,
            ARRAY_LENGTH_TOO_SHORT,
            ARRAY_EMPTY,
            EXPECTED_BOOLEAN,
    EXPECTED_INTEGER,
    EXPECTED_FLOAT,
    EXPECTED_STRING,
    EXPECTED_OBJECT,
            EXPECTED_ARRAY,
            EXPECTED_BASE64_ENCODED_BYTE_ARRAY,
            INVALID_ARRAY_VALUE,
    INVALID_CONTENT_TYPE,
    INVALID_FORM_VALUE,
            NO_FIELDS_SET,
    UNEXPECTED_VALUE,
    API_VERSION_INCOMPATIBLE,
            NOT_FOUND,
    METHOD_NOT_ALLOWED,
    NOT_ACCEPTABLE,
    REQUEST_TIMEOUT,
    CONFLICT,
    GONE,
    REQUEST_ENTITY_TOO_LARGE,
    UNSUPPORTED_MEDIA_TYPE,
    BAD_GATEWAY,
    SERVICE_UNAVAILABLE,
    GATEWAY_TIMEOUT;

}