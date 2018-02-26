package floatsample;

import com.cryptlex.lexfloatclient.LexFloatClient;
import com.cryptlex.lexfloatclient.LicenseCallbackEvent;
import com.cryptlex.lexfloatclient.LexFloatClientException;
import java.io.IOException;
import java.io.File;
import java.io.UnsupportedEncodingException;

class CallbackEventListener implements LicenseCallbackEvent
{

    @Override
    public void LicenseCallback(int status)
    {
        switch (status)
        {
            case LexFloatClientException.LF_E_LICENSE_EXPIRED:
                System.out.println("The lease expired before it could be renewed.");
                break;
            case LexFloatClientException.LF_E_LICENSE_EXPIRED_INET:
                System.out.println("The lease expired due to network connection failure.");
                break;
            case LexFloatClientException.LF_E_SERVER_TIME:
                System.out.println("The lease expired because Server System time was modified.");
                break;
            case LexFloatClientException.LF_E_TIME:
                System.out.println("The lease expired because Client System time was modified.");
                break;
            default:
                System.out.println("The lease expired due to some other reason.");
                break;
        }
    }
}

public class FloatSample
{

    public static void main(String[] args)
    {

        String path = System.getProperty("user.dir") + File.separator +"Product.dat";
        try
        {
            LexFloatClient.SetProductFile(path);
            CallbackEventListener eventListener = new CallbackEventListener();
            LexFloatClient floatClient = new LexFloatClient();
            floatClient.SetVersionGUID("59A44CE9-5415-8CF3-BD54-EA73A64E9A1B");
            floatClient.SetFloatServer("localhost", (short) 8090);
            floatClient.AddLicenseCallbackListener(eventListener);
            floatClient.RequestLicense();
            System.out.println("Success! License Acquired");
            System.out.println("Press Enter to drop the license ...");
            System.in.read();
            floatClient.DropLicense();   
            System.out.println("Success! License Dropped");
            LexFloatClient.GlobalCleanUp();
        } catch (LexFloatClientException ex)
        {
            System.out.println(ex.getCode() + ": " + ex.getMessage());
        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

}
