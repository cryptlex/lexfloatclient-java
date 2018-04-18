package com.cryptlex.lexfloatclient;

public class LexFloatClientException extends Exception
{

    int errorCode;

    public LexFloatClientException(String message)
    {
        super(message);
    }

    public LexFloatClientException(int errorCode)
    {
        super(getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }

    public int getCode()
    {
        return this.errorCode;
    }

    public static String getErrorMessage(int errorCode)
    {
        String message;
        switch (errorCode)
        {

            case LF_E_PRODUCT_ID:
                message = "The product id is incorrect.";
                break;
            case LF_E_CALLBACK:
                message = "Invalid or missing callback function.";
                break;
            case LF_E_HANDLE:
                message = "Invalid handle.";
                break;
            case LF_E_SERVER_ADDRESS:
                message = "Missing or invalid server address.";
                break;
            case LF_E_SERVER_TIME:
                message = "System time on server machine has been tampered with. Ensure your date and time settings are correct on the server machine.";
                break;
            case LF_E_TIME:
                message = "The system time has been tampered with. Ensure your date and time settings are correct.";
                break;
            case LF_E_INET:
                message = "Failed to connect to the server due to network error.";
                break;
            case LF_E_NO_FREE_LICENSE:
                message = "No free license is available.";
                break;
            case LF_E_LICENSE_EXISTS:
                message = "License has already been leased.";
                break;
            case LF_E_LICENSE_EXPIRED:
                message = "License lease has expired. This happens when the request to refresh the license fails due to license been taken up by some other client.";
                break;
            case LF_E_LICENSE_EXPIRED_INET:
                message = "License lease has expired due to network error. This happens when the request to refresh the license fails due to network error.";
                break;
            case LF_E_BUFFER_SIZE:
                message = "The buffer size was smaller than required.";
                break;
            case LF_E_METADATA_KEY_NOT_FOUND:
                message = "The metadata key does not exist.";
                break;
            case LF_E_SERVER:
                message = "Server error.";
                break;
            case LF_E_CLIENT:
                message = "Client error.";
                break;
            default:
                message = "Unknown error!";

        }
        return message;
    }

    /**
     * Success code.
     */
    public static final int LF_OK = 0;
    
    /**
     *Failure code.
     */
    public static final int LF_FAIL = 1;

    /**
     * The product id is incorrect.
     */
    public static final int LF_E_PRODUCT_ID = 40;

    /**
     * Invalid or missing callback function.
     */
    public static final int LF_E_CALLBACK = 41;

    /**
     * Invalid handle.
     */
    public static final int LF_E_HANDLE = 42;

    /**
     * Missing or invalid server address.
     */
    public static final int LF_E_SERVER_ADDRESS = 43;

    /**
     * System time on Server Machine has been tampered with. Ensure
     * your date and time settings are correct on the server machine.
     */
    public static final int LF_E_SERVER_TIME = 44;

    /**
     * The system time has been tampered with. Ensure your date
     * and time settings are correct.
     */
    public static final int LF_E_TIME = 45;

    /**
     * Failed to connect to the server due to network error.
     */
    public static final int LF_E_INET = 46;

    /**
     * No free license is available
     */
    public static final int LF_E_NO_FREE_LICENSE = 47;

    /**
     * License has already been leased.
     */
    public static final int LF_E_LICENSE_EXISTS = 48;

    /**
     * License lease has expired. This happens when the
     * request to refresh the license fails due to license been taken
     * up by some other client.
     */
    public static final int LF_E_LICENSE_EXPIRED = 49;

    /**
     * License lease has expired due to network error. This
     * happens when the request to refresh the license fails due to
     * network error.
     */
    public static final int LF_E_LICENSE_EXPIRED_INET = 50;

    /**
     * The buffer size was smaller than required.
     */
    public static final int LF_E_BUFFER_SIZE = 51;

    /**
     * The metadata key does not exist.
     */
    public static final int LF_E_METADATA_KEY_NOT_FOUND = 52;

    /**
     * Server error.
     */
    public static final int LF_E_SERVER = 70;

    /**
     * Client error.
     */
    public static final int LF_E_CLIENT = 71;

}
