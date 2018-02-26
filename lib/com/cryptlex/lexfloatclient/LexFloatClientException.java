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

            case LF_E_INET:
                message = "Failed to connect to the server due to network error.";
                break;
            case LF_E_CALLBACK:
                message = "Invalid or missing callback function.";
                break;
            case LF_E_NO_FREE_LICENSE:
                message = "No free license is available.";
                break;
            case LF_E_LICENSE_EXISTS:
                message = "License has already been leased.";
                break;
            case LF_E_HANDLE:
                message = "Invalid handle.";
                break;
            case LF_E_LICENSE_EXPIRED:
                message = "License lease has expired.";
                break;
            case LF_E_LICENSE_EXPIRED_INET:
                message = "License lease has expired due to network error.";
                break;
            case LF_E_SERVER_ADDRESS:
                message = "Missing server address.";
                break;
            case LF_E_PFILE:
                message = "Invalid or corrupted product file.";
                break;
            case LF_E_FPATH:
                message = "Invalid product file path.";
                break;
            case LF_E_PRODUCT_VERSION:
                message = "The version GUID of the client and server don't match.";
                break;
            case LF_E_GUID:
                message = "The version GUID doesn't match that of the product file.";
                break;
            case LF_E_SERVER_TIME:
                message = "System time on Server Machine has been tampered with.";
                break;
            case LF_E_TIME:
                message = "The system time has been tampered with. Ensure your date and time settings are correct.";
                break;
            case LF_E_CUSTOM_FIELD_ID:
                message = "Invalid custom field id.";
                break;
            case LF_E_BUFFER_SIZE:
                message = "The buffer size was smaller than required.";
                break;
            default:
                message = "Unknown error!";

        }
        return message;
    }

    /**
     * Failed to connect to the server due to network error.
     */
    public static final int LF_E_INET = 0x02;

    /**
     * Invalid or missing callback function.
     */
    public static final int LF_E_CALLBACK = 0x03;

    /**
     * No free license is available
     */
    public static final int LF_E_NO_FREE_LICENSE = 0x04;

    /**
     * License has already been leased.
     */
    public static final int LF_E_LICENSE_EXISTS = 0x05;

    /**
     * Invalid handle.
     */
    public static final int LF_E_HANDLE = 0x06;

    /**
     * License lease has expired. This happens when the request to refresh the
     * license fails due to license been taken up by some other client.
     */
    public static final int LF_E_LICENSE_EXPIRED = 0x07;

    /**
     * License lease has expired due to network error. This happens when the
     * request to refresh the license fails due to network error.
     */
    public static final int LF_E_LICENSE_EXPIRED_INET = 0x08;

    /**
     * Missing server address.
     */
    public static final int LF_E_SERVER_ADDRESS = 0x09;

    /**
     * Invalid or corrupted product file.
     */
    public static final int LF_E_PFILE = 0x0A;

    /**
     * Invalid product file path.
     */
    public static final int LF_E_FPATH = 0x0B;

    /**
     * The version GUID of the client and server don't match.
     */
    public static final int LF_E_PRODUCT_VERSION = 0x0C;

    /**
     * The version GUID doesn't match that of the product file.
     */
    public static final int LF_E_GUID = 0x0D;

    /**
     * System time on Server Machine has been tampered with. Ensure your date
     * and time settings are correct on the server machine.
     */
    public static final int LF_E_SERVER_TIME = 0x0E;

    /**
     * The system time has been tampered with. Ensure your date and time
     * settings are correct.
     */
    public static final int LF_E_TIME = 0x10;

    /**
     * Invalid custom field id.
     */
    public static final int LF_E_CUSTOM_FIELD_ID = 0x11;

    /**
     * The buffer size was smaller than required.
     */
    public static final int LF_E_BUFFER_SIZE = 0x12;

}
