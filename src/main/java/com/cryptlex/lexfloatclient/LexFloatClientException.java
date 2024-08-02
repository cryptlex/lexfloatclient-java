package com.cryptlex.lexfloatclient;
public class LexFloatClientException extends Exception {

    int errorCode;

    public LexFloatClientException(String message) {
        super(message);
    }

    public LexFloatClientException(int errorCode) {
        super(getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }

    public int getCode() {
        return this.errorCode;
    }

    public static String getErrorMessage(int errorCode) {
        String message;
        switch (errorCode) {
            // Existing error codes
            case LF_E_PRODUCT_ID:
                message = "The product id is incorrect.";
                break;
            case LF_E_CALLBACK:
                message = "Invalid or missing callback function.";
                break;
            case LF_E_HOST_URL:
                message = "Missing or invalid server url.";
                break;
            case LF_E_TIME:
                message = "Ensure system date and time settings are correct.";
                break;
            case LF_E_INET:
                message = "Failed to connect to the server due to network error.";
                break;
            case LF_E_NO_LICENSE:
                message = "License has not been leased yet.";
                break;
            case LF_E_LICENSE_EXISTS:
                message = "License has already been leased.";
                break;
            case LF_E_LICENSE_NOT_FOUND:
                message = "License does not exist on server or has already expired.";
                break;
            case LF_E_LICENSE_EXPIRED_INET:
                message = "License lease has expired due to network error.";
                break;
            case LF_E_LICENSE_LIMIT_REACHED:
                message = "The server has reached it's allowed limit of floating licenses.";
                break;
            case LF_E_BUFFER_SIZE:
                message = "The buffer size was smaller than required.";
                break;
            case LF_E_METADATA_KEY_NOT_FOUND:
                message = "The metadata key does not exist.";
                break;
            case LF_E_METADATA_KEY_LENGTH:
                message = "Metadata key length is more than 256 characters.";
                break;
            case LF_E_METADATA_VALUE_LENGTH:
                message = "Metadata value length is more than 4096 characters.";
                break;
            case LF_E_FLOATING_CLIENT_METADATA_LIMIT:
                message = "The floating client has reached it's metadata fields limit.";
                break;
            case LF_E_METER_ATTRIBUTE_NOT_FOUND:
                message = "The meter attribute does not exist.";
                break;
            case LF_E_METER_ATTRIBUTE_USES_LIMIT_REACHED:
                message = "The meter attribute has reached it's usage limit.";
                break;
            case LF_E_PRODUCT_VERSION_NOT_LINKED:
                message = "No product version is linked with the license.";
                break;
            case LF_E_FEATURE_FLAG_NOT_FOUND:
                message = "The product version feature flag does not exist.";
                break;
            case LF_E_IP:
                message = "IP address is not allowed.";
                break;
            case LF_E_CLIENT:
                message = "Client error.";
                break;
            case LF_E_SERVER:
                message = "Server error.";
                break;
            case LF_E_SERVER_TIME_MODIFIED:
                message = "System time on server has been tampered with.";
                break;
            case LF_E_SERVER_LICENSE_NOT_ACTIVATED:
                message = "The server has not been activated using a license key.";
                break;
            case LF_E_SERVER_LICENSE_EXPIRED:
                message = "The server license has expired.";
                break;
            case LF_E_SERVER_LICENSE_SUSPENDED:
                message = "The server license has been suspended.";
                break;
            case LF_E_SERVER_LICENSE_GRACE_PERIOD_OVER:
                message = "The grace period for server license is over.";
                break;
                
            // Missing error codes
            case LF_E_SYSTEM_PERMISSION:
                message = "Insufficient system permissions.";
                break;
            case LF_E_INVALID_PERMISSION_FLAG:
                message = "Invalid permission flag.";
                break;
            case LF_E_OFFLINE_FLOATING_LICENSE_NOT_ALLOWED:
                message = "Offline floating license is not allowed for per-instance leasing strategy.";
                break;
            case LF_E_MAX_OFFLINE_LEASE_DURATION_EXCEEDED:
                message = "Maximum offline lease duration exceeded.";
                break;
            case LF_E_ALLOWED_OFFLINE_FLOATING_CLIENTS_LIMIT_REACHED:
                message = "Allowed offline floating clients limit reached.";
                break;
            case LF_E_WMIC:
                message = "Fingerprint couldn't be generated because Windows Management Instrumentation (WMI) service has been disabled.";
                break;
            case LF_E_MACHINE_FINGERPRINT:
                message = "Machine fingerprint has changed since activation.";
                break;

            default:
                message = "Unknown error!";
        }
        return message;
    }

    // Existing error codes
    public static final int LF_OK = 0;
    public static final int LF_FAIL = 1;
    public static final int LF_E_PRODUCT_ID = 40;
    public static final int LF_E_CALLBACK = 41;
    public static final int LF_E_HOST_URL = 42;
    public static final int LF_E_TIME = 43;
    public static final int LF_E_INET = 44;
    public static final int LF_E_NO_LICENSE = 45;
    public static final int LF_E_LICENSE_EXISTS = 46;
    public static final int LF_E_LICENSE_NOT_FOUND = 47;
    public static final int LF_E_LICENSE_EXPIRED_INET = 48;
    public static final int LF_E_LICENSE_LIMIT_REACHED = 49;
    public static final int LF_E_BUFFER_SIZE = 50;
    public static final int LF_E_METADATA_KEY_NOT_FOUND = 51;
    public static final int LF_E_METADATA_KEY_LENGTH = 52;
    public static final int LF_E_METADATA_VALUE_LENGTH = 53;
    public static final int LF_E_FLOATING_CLIENT_METADATA_LIMIT = 54;
    public static final int LF_E_METER_ATTRIBUTE_NOT_FOUND = 55;
    public static final int LF_E_METER_ATTRIBUTE_USES_LIMIT_REACHED = 56;
    public static final int LF_E_PRODUCT_VERSION_NOT_LINKED = 57;
    public static final int LF_E_FEATURE_FLAG_NOT_FOUND = 58;
    public static final int LF_E_SYSTEM_PERMISSION = 59;
    public static final int LF_E_IP = 60;
    public static final int LF_E_INVALID_PERMISSION_FLAG = 61;
    public static final int LF_E_OFFLINE_FLOATING_LICENSE_NOT_ALLOWED = 62;
    public static final int LF_E_MAX_OFFLINE_LEASE_DURATION_EXCEEDED = 63;
    public static final int LF_E_ALLOWED_OFFLINE_FLOATING_CLIENTS_LIMIT_REACHED = 64;
    public static final int LF_E_WMIC = 65;
    public static final int LF_E_MACHINE_FINGERPRINT = 66;
}
