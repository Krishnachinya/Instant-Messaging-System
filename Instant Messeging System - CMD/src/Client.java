import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by KrishnChinya on 3/5/17.
 * INPUT: NOTHING
 * OUTPUT: CREATES CLIENT AND CREATES SEPERATE THREAD TO HANDLE STANDARD INPUT AND INPUT FOR OTHER CLIENT.
 */
public class Client {
    public static void main(String[] args)
    {
        String serverName = "localhost";
        try{
            Socket client = new Socket(serverName,8189);
            System.out.println("Client is connected to server now");

            OutputStream outputStream = client.getOutputStream();

            PrintStream out = new PrintStream(outputStream,true);

            BufferedReader sktin = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            BufferedReader stdIn = new BufferedReader(
                    new InputStreamReader(System.in));

            //different thread to handle Std inputs
            ClientInputsIO stdinputs = new ClientInputsIO(stdIn,out,client);
            //ClientInputs stdinputs = new ClientInputs(stdIn,out,client);
            Thread stdthread = new Thread(stdinputs);
            stdthread.start();

            //different thread to handle socket inputs
            ClientInputs socketinputs = new ClientInputs(sktin,System.out,client);
            Thread socketinpthread = new Thread(socketinputs);
            socketinpthread.start();

//            while (client.isConnected())
//            {
//
//            }
//
//            System.err.println("Server is not reachable. closing the socket");
//            client.close();

        }catch (Exception ex)
        {
            //System.out.println("");
            System.err.println("No Server to connect");
        }
    }
}
