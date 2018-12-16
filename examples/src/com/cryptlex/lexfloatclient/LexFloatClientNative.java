package com.cryptlex.lexfloatclient;

import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.WString;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Callback;
import java.io.File;

public class LexFloatClientNative implements Library {

    public static NativeLibrary GetNativeLibrary() {
        String libraryPath, libraryName, arch;
        libraryPath = System.getProperty("user.dir") + File.separator + "lexfloatclient";
        libraryName = null;
        arch = Platform.is64Bit() ? "-64" : "";
        switch (Platform.getOSType()) {
        case Platform.WINDOWS:
            libraryPath = libraryPath + File.separator + "win32-x86" + arch;
            libraryName = "LexFloatClient.dll";
            break;
        case Platform.MAC:
            libraryPath = libraryPath + File.separator + "mac";
            libraryName = "libLexFloatClient.dylib";
            break;
        case Platform.LINUX:
            libraryPath = libraryPath + File.separator + "linux-x86" + arch;
            libraryName = "libLexFloatClient.so";
            break;
        }
        return NativeLibrary.getInstance(libraryPath + File.separator + libraryName);

    }

    static {
        Native.register(GetNativeLibrary());
    }

    public interface CallbackType extends Callback {
        void invoke(int status);
    }

    public static native int SetHostProductId(String productId);

    public static native int SetHostProductId(WString productId);

    public static native int SetHostUrl(String hostUrl);

    public static native int SetHostUrl(WString hostUrl);

    public static native int SetFloatingLicenseCallback(CallbackType callback);

    public static native int SetFloatingClientMetadata(String key, String value);

    public static native int SetFloatingClientMetadata(WString key, WString value);

    public static native int GetHostLicenseMetadata(String key, ByteBuffer value, int length);

    public static native int GetHostLicenseMetadata(WString key, CharBuffer value, int length);

    public static native int GetHostLicenseExpiryDate(IntByReference expiryDate);

    public static native int RequestFloatingLicense();

    public static native int DropFloatingLicense();

    public static native int HasFloatingLicense();
}
