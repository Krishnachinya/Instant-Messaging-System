import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by KrishnChinya on 3/5/17.
 * INPUT: STANDARD INPUT ADDRESS. USER TYPED IN TEXT WILL BE CAPTURED AND DISPLAYED TO THE USER
 * OUTPUT: OUTPUT WILL BE PUSHED TO SERVER AFTER ADDING GET FORMAT
 */
public class ClientInputsIO implements Runnable {

    BufferedReader input;
    PrintStream out;
    String inputline;
    Socket client;

    public ClientInputsIO(BufferedReader input, PrintStream out, Socket client) {
        this.input = input;
        this.out = out;
        this.client = client;
    }



    @Override
    public void run() {
        String header =   "GET ";

        String tail = " HTTP/1.1::$$"+
        "User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3::$$"+
        "Host: InstantMessaging System::$$"+
        "Accept-Language: en, mi::$$";

        String message = "";

        try {
            while ((inputline=input.readLine())!= null && client.isConnected()) {
                message = header + inputline + tail;
                out.println(message);
            }
        }catch (Exception ex){
            System.err.println("Socket is closed, programming is existing");
            System.exit(0);
        }
    }
}
