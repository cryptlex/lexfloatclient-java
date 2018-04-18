package com.cryptlex.lexfloatclient;

import com.sun.jna.Platform;
import com.sun.jna.WString;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.io.UnsupportedEncodingException;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.List;

public class LexFloatClient
{

    private static final int LF_OK = 0x00;

    private static final int LF_FAIL = 0x01;

    private String productId = null;

    private int handle = 0;

    private LexFloatClientNative.CallbackType privCallback = null;

    private List<LicenseCallbackEvent> listeners = null;

    /**
     * Sets the product id of your application.<p>
     * </p>
     *
     * @param productId the unique product id of your application as
     * mentioned on the product page of your application in the
     * dashboard.
     * @throws LexFloatClientException
     */
    public void SetProductId(String productId) throws LexFloatClientException
    {
        int status;
        IntByReference handleRef = new IntByReference(0);
        status = Platform.isWindows() ? LexFloatClientNative.GetHandle(new WString(productId), handleRef) : LexFloatClientNative.GetHandle(productId, handleRef);
        if (LF_OK != status)
        {
            throw new LexFloatClientException(status);
        }
        this.handle = handleRef.getValue();
        this.productId = productId;
    }

    /**
     * Sets the network address of the LexFloatServer.<p>
     * </p>
     *
     * @param hostAddress hostname or the IP address of the LexFloatServer
     * @param port port of the LexFloatServer
     * @throws LexFloatClientException
     */
    public void SetFloatServer(String hostAddress, short port) throws LexFloatClientException
    {
        int status;
        status = Platform.isWindows() ? LexFloatClientNative.SetFloatServer(this.handle, new WString(hostAddress), port) : LexFloatClientNative.SetFloatServer(this.handle, hostAddress, port);
        if (LF_OK != status)
        {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sets refresh license callback event listener function.<br>
     * Whenever the lease expires, a refresh lease request is sent to the
     * server. If the lease fails to refresh, event listener function gets
     * invoked with the following status error codes: LF_E_LICENSE_EXPIRED,
     * LF_E_LICENSE_EXPIRED_INET, LF_E_SERVER_TIME, LF_E_TIME<p>
     * </p>
     *
     * @param listener
     * @throws LexFloatClientException
     */
    public void AddLicenseCallbackListener(LicenseCallbackEvent listener) throws LexFloatClientException
    {
        if (listeners == null)
        {
            listeners = new ArrayList<>();
            listeners.add(listener);
        }
        if (this.privCallback == null)
        {
            this.privCallback = new LexFloatClientNative.CallbackType()
            {
                public void invoke(int status)
                {
                    // Notify everybody that may be interested.
                    for (LicenseCallbackEvent event : listeners)
                    {
                        event.LicenseCallback(status);
                    }
                }
            };
            int status;
            status = LexFloatClientNative.SetLicenseCallback(this.handle, this.privCallback);
            if (LF_OK != status)
            {
                throw new LexFloatClientException(status);
            }
        }

    }

    /**
     * Sends the request to lease the license from the LexFloatServer.<p>
     * </p>
     *
     * @throws LexFloatClientException
     */
    public void RequestLicense() throws LexFloatClientException
    {
        int status;
        status = LexFloatClientNative.RequestLicense(this.handle);
        if (LF_OK != status)
        {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sends the request to drop the license from the LexFloatServer.<br>
     * Call this function before you exit your application to prevent zombie
     * licenses.<p>
     * </p>
     *
     * @throws LexFloatClientException
     */
    public void DropLicense() throws LexFloatClientException
    {
        int status;
        status = LexFloatClientNative.DropLicense(this.handle);
        if (LF_OK != status)
        {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Checks whether any license has been leased or not.<p>
     * </p>
     *
     * @return True or False
     * @throws LexFloatClientException
     */
    public boolean HasLicense() throws LexFloatClientException
    {
        int status;
        status = LexFloatClientNative.RequestLicense(this.handle);
        switch (status)
        {
            case LF_OK:
                return true;
            case LF_FAIL:
                return false;
            default:
                throw new LexFloatClientException(status);
        }
    }

    /**
     * Get the value of the license metadata field associated with the float server key.
     * key.<p>
     * </p>
     *
     * @param key key of the metadata field whose value you want to get
     * @return Returns the metadat key value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public String GetLicenseMetadata(String key) throws LexFloatClientException, UnsupportedEncodingException
    {

        int status;
        if (Platform.isWindows())
        {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetLicenseMetadata(this.handle, new WString(key), buffer, 256);
            if (LF_OK == status)
            {
                return buffer.toString().trim();
            }
        } else
        {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetLicenseMetadata(this.handle, key, buffer, 256);
            if (LF_OK == status)
            {
                return new String(buffer.array(), "UTF-8");
            }
        }

        throw new LexFloatClientException(status);
    }

    /**
     * Gets the product id
     *
     * @return Returns the product id
     */
    public String GetProductId()
    {
        return this.productId;
    }

    /**
     * Releases the resources acquired for sending network requests. Call this
     * function before you exit your application.
     */
    public static void GlobalCleanUp()
    {
        LexFloatClientNative.GlobalCleanUp();
    }

}
