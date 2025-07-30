package com.cryptlex.lexfloatclient;

import com.sun.jna.Platform;
import com.sun.jna.WString;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.io.UnsupportedEncodingException;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

    // Convert long to BigInteger to correctly handle unsigned 64-bit values
    private static BigInteger toUnsignedBigInteger(long value) {
        return BigInteger.valueOf(value).and(BigInteger.valueOf(0xFFFFFFFFFFFFFFFFL));
    }

    /**
     * Sets the product id of your application
     *
     * @param productId the unique product id of your application as mentioned
     * on the product page of your application in the dashboard.
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
     * Sets the permission flag. This function must be called on every start of your program 
     * after SetHostProductId() function in case the application allows borrowing of licenses 
     * or system wide activation.
     *
     * @param flag depending on your application's requirements, choose one of 
     * the following values: LF_USER, LF_ALL_USERS.
     * <ul>
     * <li> LF_USER: This flag indicates that the application does not require admin or root permissions to run.</li>
     * <li> LF_ALL_USERS: This flag is specifically designed for Windows and should be used for system-wide activations.</li>
     * </ul>
     * @throws LexFloatClientException
     */
    public static void SetPermissionFlag(int flag) throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.SetPermissionFlag(flag);
                
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }

    /**
     * Sets the renew license callback function.<br>
     * Whenever the license lease is about to expire, a renew request is sent to
     * the server. When the request completes, the license callback function
     * gets invoked with one of the following status codes:<br>
     * LF_OK, LF_E_INET, LF_E_LICENSE_EXPIRED_INET, LF_E_LICENSE_NOT_FOUND,
     * LF_E_CLIENT, LF_E_IP, LF_E_SERVER, LF_E_TIME,
     * LF_E_SERVER_LICENSE_NOT_ACTIVATED,LF_E_SERVER_TIME_MODIFIED,
     * LF_E_SERVER_LICENSE_SUSPENDED, LF_E_SERVER_LICENSE_EXPIRED,
     * LF_E_SERVER_LICENSE_GRACE_PERIOD_OVER
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
     * Sets the floating client metadata
     * The metadata appears along with the license details of the license in
     * LexFloatServer dashboard
     *
     * @param key string of maximum length 256 characters with utf-8 encoding.
     * encoding.
     * @param value string of maximum length 4096 characters with utf-8 encoding.
     * encoding.
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
     * Gets the lease expiry date timestamp of the floating client.
     *
     * @return Returns the timestamp
     * @throws LexFloatClientException
     */
    public static int GetFloatingClientLeaseExpiryDate() throws LexFloatClientException {
        int status;
        IntByReference expiryDate = new IntByReference(0);
        status = LexFloatClientNative.GetFloatingClientLeaseExpiryDate(expiryDate);
        switch (status) {
            case LF_OK:
                return expiryDate.getValue();
            default:
                throw new LexFloatClientException(status);
        }
    }

    /**
     * Gets the version of this library.
     * 
     * @return libraryVersion - Returns the library version.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetFloatingClientLibraryVersion() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetFloatingClientLibraryVersion(buffer, 256);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetFloatingClientLibraryVersion(buffer, 256);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the host configuration.
     * This function sends a network request to LexFloatServer to get the configuration details.
     
     * @return Returns host configuration.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static HostConfig GetHostConfig() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        int bufferSize = 1024;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostConfigInternal(buffer, bufferSize);
            if (LF_OK == status) {
                String hostConfigJson = buffer.toString().trim();
                if (!hostConfigJson.isEmpty()) {
                    HostConfig hostConfig = null;
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        hostConfig = objectMapper.readValue(hostConfigJson, HostConfig.class);
                    } catch (JsonProcessingException e) {}
                    return hostConfig;
                } else {
                    return null;
                } 
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostConfigInternal(buffer, bufferSize);
            if (LF_OK == status) {
                String hostConfigJson = new String(buffer.array(), "UTF-8").trim();
                if (!hostConfigJson.isEmpty()) {
                    HostConfig hostConfig = null;
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        hostConfig = objectMapper.readValue(hostConfigJson, HostConfig.class);
                    } catch (JsonProcessingException e) {}
                    return hostConfig;
                } else {
                    return null;
                } 
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the product version name.
     * 
     * @deprecated This function is deprecated. Use GetHostLicenseEntitlementSetName() instead.
     * 
     * @return name - Returns the name of the Product Version being used.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
     public static String GetHostProductVersionName() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionName(buffer, 256);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionName(buffer, 256);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the product version display name.
     * 
     * @deprecated This function is deprecated. Use GetHostLicenseEntitlementSetDisplayName() instead.
     * 
     * @return displayName - Returns the display name of the Product Version being
     *         used.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
     public static String GetHostProductVersionDisplayName() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionDisplayName(buffer, 256);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionDisplayName(buffer, 256);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the product version feature flag.
     * 
     * @deprecated This function is deprecated. Use GetHostFeatureEntitlement() instead.
     * 
     * @param name - The name of the Feature Flag.
     * @return The properties of the Feature Flag as an object.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
     public static HostProductVersionFeatureFlag GetHostProductVersionFeatureFlag(String name)
            throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        IntByReference enabled = new IntByReference(0);
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionFeatureFlag(new WString(name), enabled, buffer, 256);
            if (LF_OK == status) {
                return new HostProductVersionFeatureFlag(name, enabled.getValue() > 0, buffer.toString().trim());
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetHostProductVersionFeatureFlag(name, enabled, buffer, 256);
            if (LF_OK == status) {
                return new HostProductVersionFeatureFlag(name, enabled.getValue() > 0,
                        new String(buffer.array(), "UTF-8").trim());
            }
        }

        throw new LexFloatClientException(status);
    }

    /**
     * Gets the name of the entitlement set associated with the LexFloatServer license.
     * 
     * @return Returns the host license entitlement set name.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetHostLicenseEntitlementSetName() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        int bufferSize = 256;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostLicenseEntitlementSetName(buffer, bufferSize);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostLicenseEntitlementSetName(buffer, bufferSize);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the display name of the entitlement set associated with the LexFloatServer license.
     * 
     * @return Returns the host license entitlement set display name.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetHostLicenseEntitlementSetDisplayName() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        int bufferSize = 256;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostLicenseEntitlementSetDisplayName(buffer, bufferSize);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostLicenseEntitlementSetDisplayName(buffer, bufferSize);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the feature entitlements associated with the LexFloatServer license.
     * 
     * Feature entitlements can be linked directly to a license (license feature entitlements) 
     * or via entitlement sets. If a feature entitlement is defined in both, the value from 
     * the license feature entitlement takes precedence, overriding the entitlement set value.
     * 
     * @return Returns a list of host feature entitlements.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static List<HostFeatureEntitlement> GetHostFeatureEntitlements() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        int bufferSize = 4096;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostFeatureEntitlementsInternal(buffer, bufferSize);
            if (LF_OK == status) {
                String hostFeatureEntitlementsJson = buffer.toString().trim();
                if (!hostFeatureEntitlementsJson.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        List<HostFeatureEntitlement> hostFeatureEntitlements = objectMapper.readValue(hostFeatureEntitlementsJson, new TypeReference<List<HostFeatureEntitlement>>() {});
                        return hostFeatureEntitlements;
                    } catch (JsonProcessingException e) {}
                } else {
                    return new ArrayList<>();
                }
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostFeatureEntitlementsInternal(buffer, bufferSize);
            if (LF_OK == status) {
                String hostFeatureEntitlementsJson = new String(buffer.array(), "UTF-8").trim();
                if (!hostFeatureEntitlementsJson.isEmpty()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        List<HostFeatureEntitlement> hostFeatureEntitlements = objectMapper.readValue(hostFeatureEntitlementsJson, new TypeReference<List<HostFeatureEntitlement>>() {});
                        return hostFeatureEntitlements;
                    } catch (JsonProcessingException e) {}
                } else {
                    return new ArrayList<>();
                }
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the feature entitlement associated with the LexFloatServer license.
     * 
     * Feature entitlements can be linked directly to a license (license feature entitlements) 
     * or via entitlement sets. If a feature entitlement is defined in both, the value from 
     * the license feature entitlement takes precedence, overriding the entitlement set value.
     * 
     * @param name The name of the feature.
     * @return Returns the host feature entitlement object.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static HostFeatureEntitlement GetHostFeatureEntitlement(String name) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        int bufferSize = 1024;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostFeatureEntitlementInternal(new WString(name), buffer, bufferSize);
            if (LF_OK == status) {
                String hostFeatureEntitlementJson = buffer.toString().trim();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    HostFeatureEntitlement hostFeatureEntitlement = objectMapper.readValue(hostFeatureEntitlementJson, HostFeatureEntitlement.class);
                    return hostFeatureEntitlement;
                } catch (JsonProcessingException e) {}
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            status = LexFloatClientNative.GetHostFeatureEntitlementInternal(name, buffer, bufferSize);
            if (LF_OK == status) {
                String hostFeatureEntitlementJson = new String(buffer.array(), "UTF-8").trim();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    HostFeatureEntitlement hostFeatureEntitlement = objectMapper.readValue(hostFeatureEntitlementJson, HostFeatureEntitlement.class);
                    return hostFeatureEntitlement;
                } catch (JsonProcessingException e) {}
            }
        }
        throw new LexFloatClientException(status);
    }
    
    /**
     * Get the value of the field associated with the
     * LexFloatServer product metadata key
     *
     * @param key key of the metadata field whose value you want to get
     * @return Returns the metadata key value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetHostProductMetadata(String key) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(4096);
            status = LexFloatClientNative.GetHostProductMetadata(new WString(key), buffer, 4096);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            status = LexFloatClientNative.GetHostProductMetadata(key, buffer, 4096);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8");
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Get the value of the license metadata field associated with the
     * LexFloatServer license key
     *
     * @param key key of the metadata field whose value you want to get
     * @return Returns the metadata key value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetHostLicenseMetadata(String key) throws LexFloatClientException, UnsupportedEncodingException {
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
     * Get the value of the license metadata field associated with the LexFloatServer license.
     *
     * @param key key of the metadata field whose value you want to get
     * @return Returns the metadata key value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetFloatingClientMetadata(String key) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(4096);
            status = LexFloatClientNative.GetFloatingClientMetadata(new WString(key), buffer, 4096);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            status = LexFloatClientNative.GetFloatingClientMetadata(key, buffer, 4096);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8");
            }
        }
        throw new LexFloatClientException(status);
    }
    
    /**
     * Gets the license meter attribute allowed uses and total uses associated 
     * with the LexFloatServer license.
     *
     * @param name name of the meter attribute
     * @return Returns the values of meter attribute allowed and total uses.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static HostLicenseMeterAttribute GetHostLicenseMeterAttribute(String name) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        LongByReference allowedUses = new LongByReference(0);
        // These references can still hold the uint64_t values populated by the native function
        LongByReference totalUses = new LongByReference(0);
        LongByReference grossUses = new LongByReference(0);

        if (Platform.isWindows()) {
            status = LexFloatClientNative.GetHostLicenseMeterAttribute(new WString(name), allowedUses, totalUses, grossUses);
            if (LF_OK == status) {
                return new HostLicenseMeterAttribute(name, allowedUses.getValue(), toUnsignedBigInteger(totalUses.getValue()), toUnsignedBigInteger(grossUses.getValue()));
            }
        } else {
            status = LexFloatClientNative.GetHostLicenseMeterAttribute(name, allowedUses, totalUses, grossUses);
            if (LF_OK == status) {
                return new HostLicenseMeterAttribute(name, allowedUses.getValue(), toUnsignedBigInteger(totalUses.getValue()), toUnsignedBigInteger(grossUses.getValue()));
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
     * Gets the mode of the floating license (online or offline).
     * 
     * @return mode - Returns the floating license mode.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static String GetFloatingLicenseMode() throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            CharBuffer buffer = CharBuffer.allocate(256);
            status = LexFloatClientNative.GetFloatingLicenseMode(buffer, 256);
            if (LF_OK == status) {
                return buffer.toString().trim();
            }
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            status = LexFloatClientNative.GetFloatingLicenseMode(buffer, 256);
            if (LF_OK == status) {
                return new String(buffer.array(), "UTF-8").trim();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Gets the meter attribute uses consumed by the floating client.
     *
     * @param name name of the meter attribute
     * @return Returns the value of meter attribute uses by the floating client.
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static int GetFloatingClientMeterAttributeUses(String name) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        IntByReference uses = new IntByReference(0);
        if (Platform.isWindows()) {
            status = LexFloatClientNative.GetFloatingClientMeterAttributeUses(new WString(name), uses);
            if (LF_OK == status) {
                return uses.getValue();
            }
        } else {
            status = LexFloatClientNative.GetFloatingClientMeterAttributeUses(name, uses);
            if (LF_OK == status) {
                return uses.getValue();
            }
        }
        throw new LexFloatClientException(status);
    }

    /**
     * Sends the request to lease the license from the LexFloatServer for offline usage.
     * The maximum value of lease duration is configured in the config.yml of LexFloatServer.
     *
     * @param leaseDuration value of the lease duration.
     * @throws LexFloatClientException
     */
    public static void RequestOfflineFloatingLicense(int leaseDuration) throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.RequestOfflineFloatingLicense(leaseDuration);
        if (LF_OK != status) {
            throw new LexFloatClientException(status);
        }
    }
    
    /**
     * Sends the request to lease the license from the LexFloatServer
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
     * licenses
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
     * Checks whether any license has been leased or not
     *
     * @return True or False
     * @throws LexFloatClientException
     */
    public static boolean HasFloatingLicense() throws LexFloatClientException {
        int status;
        status = LexFloatClientNative.HasFloatingLicense();
        switch (status) {
            case LF_OK:
                return true;
            case LexFloatClientException.LF_E_NO_LICENSE:
                return false;
            case LexFloatClientException.LF_FAIL:
                return false;
            default:
                throw new LexFloatClientException(status);
        }
    }

    /**
     * Increments the meter attribute uses of the floating client.
     *
     * @param name name of the meter attribute
     * @param increment the increment value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static void IncrementFloatingClientMeterAttributeUses(String name, int increment) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            status = LexFloatClientNative.IncrementFloatingClientMeterAttributeUses(new WString(name), increment);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        } else {
            status = LexFloatClientNative.IncrementFloatingClientMeterAttributeUses(name, increment);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        }
    }

    /**
     * Decrements the meter attribute uses of the floating client.
     *
     * @param name name of the meter attribute
     * @param decrement the decrement value
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static void DecrementFloatingClientMeterAttributeUses(String name, int decrement) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            status = LexFloatClientNative.DecrementFloatingClientMeterAttributeUses(new WString(name), decrement);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        } else {
            status = LexFloatClientNative.DecrementFloatingClientMeterAttributeUses(name, decrement);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        }
    }

    /**
     * Resets the meter attribute uses of the floating client.
     *
     * @param name name of the meter attribute
     * @throws LexFloatClientException
     * @throws UnsupportedEncodingException
     */
    public static void ResetFloatingClientMeterAttributeUses(String name) throws LexFloatClientException, UnsupportedEncodingException {
        int status;
        if (Platform.isWindows()) {
            status = LexFloatClientNative.ResetFloatingClientMeterAttributeUses(new WString(name));
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        } else {
            status = LexFloatClientNative.ResetFloatingClientMeterAttributeUses(name);
            if (LF_OK != status) {
                throw new LexFloatClientException(status);
            }
        }
    }
}
