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

    public static native int SetProductFile(String filePath);

    public static native int SetProductFile(WString filePath);

    public static native int GetHandle(String versionGUID, IntByReference handle);

    public static native int GetHandle(WString versionGUID, IntByReference handle);

    public static native int SetFloatServer(int handle, String hostAddress, short port);

    public static native int SetFloatServer(int handle, WString hostAddress, short port);

    public static native int SetLicenseCallback(int handle, CallbackType callback);

    public static native int RequestLicense(int handle);

    public static native int DropLicense(int handle);

    public static native int HasLicense(int handle);

    public static native int GetCustomLicenseField(int handle, String fieldId, ByteBuffer fieldValue, int length);

    public static native int GetCustomLicenseField(int handle, WString fieldId, CharBuffer fieldValue, int length);

    public static native int FindHandle(String versionGUID, IntByReference handle);

    public static native int FindHandle(WString versionGUID, IntByReference handle);

    public static native int GlobalCleanUp();

}
