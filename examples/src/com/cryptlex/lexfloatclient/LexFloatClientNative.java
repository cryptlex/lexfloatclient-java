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

public class LexFloatClientNative implements Library
{

    public static NativeLibrary GetNativeLibrary()
    {
        String libraryPath, libraryName, arch;
        libraryPath = System.getProperty("user.dir") + File.separator + "lexfloatclient";
        libraryName = null;
        arch = Platform.is64Bit() ? "-64" : "";
        switch (Platform.getOSType())
        {
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

    static
    {

        Native.register(GetNativeLibrary());
    }

    public interface CallbackType extends Callback
    {

        void invoke(int status);
    }

    public static native int GetHandle(String productId, IntByReference handle);

    public static native int GetHandle(WString productId, IntByReference handle);

    public static native int SetFloatServer(int handle, String hostAddress, short port);

    public static native int SetFloatServer(int handle, WString hostAddress, short port);

    public static native int SetLicenseCallback(int handle, CallbackType callback);

    public static native int RequestLicense(int handle);

    public static native int DropLicense(int handle);

    public static native int HasLicense(int handle);

    public static native int GetLicenseMetadata(int handle, String key, ByteBuffer value, int length);

    public static native int GetLicenseMetadata(int handle, WString key, CharBuffer value, int length);

    public static native int FindHandle(String productId, IntByReference handle);

    public static native int FindHandle(WString productId, IntByReference handle);

    public static native int GlobalCleanUp();

}
