import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by KrishnChinya on 3/5/17.
 * INPUT: NOTHING
 * OUTPUT: CREATES SERVER ON PORT 8189 AND LISTENS FOR CLIENT TO CONNECT.
 */
public class Server {

    public static void main(String[] args)
    {

        LinkedHashMap<ClientDetails,Socket> clientDetailsSocketMap = new LinkedHashMap<>();

        Logger logger = Logger.getLogger("ServerLog");;
        FileHandler fh;
        BufferedReader in;
        PrintStream out;
        int i=0;

        try{
            fh = new FileHandler(System.getProperty("user.dir")+"/Server.log",true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            ServerSocket serverProgram = new ServerSocket(8189);
            do {
                Socket clientSocket = serverProgram.accept();

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintStream(clientSocket.getOutputStream(), true);

                //handles all input
                ServerIn serverIn = new ServerIn(in,out,clientDetailsSocketMap,clientSocket,++i,logger);
                Thread t = new Thread(serverIn);
                t.start();
                logger.info("New Client Connected");
            }while (!serverProgram.isClosed());

            serverProgram.close();
            logger.info("Server Connection Closed");

        }catch (Exception ex)
        {
            logger.info("Exception occured:: "+ex);

        }
    }
}
