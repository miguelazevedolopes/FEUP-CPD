package tests;

import java.net.Socket;

public class TestUtils {
    public static boolean isPortInUse(String hostName, int portNumber) {
        boolean result;

        try {

            Socket s = new Socket(hostName, portNumber);
            s.close();
            result = true;

        }
        catch(Exception e) {
            result = false;
        }

        return(result);
    }
}
