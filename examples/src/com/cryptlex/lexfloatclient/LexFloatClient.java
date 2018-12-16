package com.cryptlex.lexfloatclient;

import com.sun.jna.Platform;
import com.sun.jna.WString;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.io.UnsupportedEncodingException;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.List;

public class LexFloatClient {
    private static LexFloatClientNative.CallbackType privateCallback = null;
    private static List<LicenseCallbackEvent> listeners = null;

    /**
     * Success code.
     */
    public static final int LF_OK = 0;

    /**
     * Failure code.
     */
    public static final int LF_FAIL = 1;

    /**
     * Sets the product id of your application.
     * <p>
     * </p>
     *
     * @param productId the unique product id of your application as mentioned on
     *                  the product page of your application in the dashboard.
     * @throws LexFloatClientException
     */
    public static void SetHostProductId(String productId) throws LexFloatClientException {
        int status;
        status = Platform.isWindows() ? LexFloatClientNative.SetHostProductId(new WString(productId))
                : LexFloatClientNative.SetHostProductId(productId);
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sets the network address of the LexFloatServer.<br>
     * The url format should be: http://[ip or hostname]:[port]
     * </p>
     *
     * @param hostUrl url string having the correct format
     * @throws LexFloatClientException
     */
    public static void SetHostUrl(String hostUrl) throws LexFloatClientException {
        int status;
        status = Platform.isWindows() ? LexFloatClientNative.SetHostUrl(new WString(hostUrl))
                : LexFloatClientNative.SetHostUrl(hostUrl);
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sets the renew license callback function.<br>
     * Whenever the license lease is about to expire, a renew request is sent to the
     * server. When the request completes, the license callback function gets
     * invoked with one of the following status codes:<br>
     * LF_OK, LF_E_INET, LF_E_LICENSE_EXPIRED_INET, LF_E_LICENSE_NOT_FOUND,
     * LF_E_CLIENT, LF_E_IP, LF_E_SERVER, LF_E_TIME,
     * LF_E_SERVER_LICENSE_NOT_ACTIVATED,LF_E_SERVER_TIME_MODIFIED,
     * LF_E_SERVER_LICENSE_SUSPENDED, LF_E_SERVER_LICENSE_EXPIRED,
     * LF_E_SERVER_LICENSE_GRACE_PERIOD_OVER
     * <p>
     * </p>
     *
     * @param listener
     * @throws LexFloatClientException
     */
    public static void AddLicenseCallbackListener(LicenseCallbackEvent listener) throws LexFloatClientException {
        if (listeners == null) {
            listeners = new ArrayList<>();
            listeners.add(listener);
        }
        if (privateCallback == null) {
            privateCallback = new LexFloatClientNative.CallbackType() {
                public void invoke(int status) {
                    // Notify everybody that may be interested.
                    for (LicenseCallbackEvent event : listeners) {
                        event.LicenseCallback(status);
                    }
                }
            };
            int status;
            status = LexFloatClientNative.SetFloatingLicenseCallback(privateCallback);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        }
    }

    /**
     * Sets the floating client metadata.
     * <p>
     * </p>
     * The metadata appears along with the license details of the license in
     * LexFloatServer dashboard.
     * <p>
     * </p>
     *
     * @param key   string of maximum length 256 characters with utf-8 encoding.
     *              encoding.
     * @param value string of maximum length 256 characters with utf-8 encoding.
     *              encoding.
     * @throws LexFloatClientException
     */
    public static void SetFloatingClientMetadata(String key, String value) throws LexFloatClientException {
        int status;
        status = Platform.isWindows()
                ? LexFloatClientNative.SetFloatingClientMetadata(new WString(key), new WString(value))
                : LexFloatClientNative.SetFloatingClientMetadata(key, value);
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Get the value of the license metadata field associated with the
     * LexFloatServer license. key.
     * <p>
     * </p>
     *
     * @param key key of the metadata field whose value you want to get
     * @return Returns the metadata key value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public String GetHostLicenseMetadata(String key) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetHostLicenseMetadata(new WString(key), buffer, 256);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetHostLicenseMetadata(key, buffer, 256);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8");
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the license expiry date timestamp of the LexFloatServer license.
     *
     * @return Returns the timestamp
     * @throws LexFloatClientException
     */
    public static int GetHostLicenseExpiryDate() throws LexFloatClientException {
        int status;
        IntByReference expiryDate = new IntByReference(0);
        status = LexFloatClientNative.GetHostLicenseExpiryDate(expiryDate);
        switch (status) {
        case LF_OK:
            return expiryDate.getValue();
        default:
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sends the request to lease the license from the LexFloatServer.
     * <p>
     * </p>
     *
     * @throws LexFloatClientException
     */
    public static void RequestFloatingLicense() throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.RequestFloatingLicense();
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sends the request to the LexFloatServer to free the license.<br>
     * Call this function before you exit your application to prevent zombie
     * licenses.
     * <p>
     * </p>
     *
     * @throws LexFloatClientException
     */
    public static void DropFloatingLicense() throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.DropFloatingLicense();
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Checks whether any license has been leased or not.
     * <p>
     * </p>
     *
     * @return True or False
     * @throws LexFloatClientException
     */
    public boolean HasFloatingLicense() throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.HasFloatingLicense();
        switch (status) {
        case LF_OK:
            return true;
        case LexFloatClientException.LF_E_NO_LICENSE:
            return false;
        default:
            throw new LexFloatClientException(status);
        }
    }
}
