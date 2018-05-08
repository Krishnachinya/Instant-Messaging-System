import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KrishnChinya on 3/5/17.
 * This class is multithreaded and is used for recieving inputs/chats and sending output/chats from client to server
 * and vice versa
 * INPUT: SERVER SOCKET REFERENCE
 * OUTPUT: CHAT TEXT TO CLIENTS.
 */
public class ServerIn implements Runnable {

    BufferedReader in;
    PrintStream out;


    String clientName;
    ClientDetails current_Client, current_Client_Modified, next_Client;
    LinkedHashMap<ClientDetails,Socket> client_Socket_Details;
    Socket clientSocket;
    int client_Number;
    static ArrayList<String> allNames = new ArrayList<>();
    static int no_ofconnected = 0;
    Logger logger;
    Pattern pattern = Pattern.compile(
            Pattern.quote("GET")
                    + "(.*?)"
                    + Pattern.quote("HTTP/1.1")
    );
    Matcher matcher;
    String header="";
    Boolean displayheader = true;

    /*
    Constructor to intialize the variables such client socket reference, input stream and outstream
    */

    public ServerIn(BufferedReader in, PrintStream out,
                    LinkedHashMap<ClientDetails,Socket> client_Socket_Details,
                    Socket clientSocket, int client_Number, Logger logger) {
        this.in = in;
        this.out = out;
        this.client_Socket_Details = client_Socket_Details;
        this.clientSocket = clientSocket;
        this.client_Number = client_Number;
        this.logger = logger;
    }

    /*
    Used to parse the GET header coming from client to the server.
     */

    public String parseheader(String readline) {
        String text = "";
        logger.info(readline.replace("::$$", "\n"));
        if (displayheader){
            System.out.println(readline.replace("::$$", "\n"));
            System.out.print("Header wont Shown for upcoming messages. Check log for the same");
            displayheader=false;
        }
        readline = readline.replace("::$$","");
        matcher = pattern.matcher(readline);

        logger.info(text);

        while (matcher.find()) {
            text = matcher.group(1);

        }
        return text.trim();
    }

    /*
    Used to send the header to client wiht the message
     */

    public String getheader(int type,int length)
    {
       header =  "HTTP/1.1 200 OK::#$"+
                "Date:"+new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").format(Calendar.getInstance().getTime())+
                "::#$Server: Apache::#$"+
                "Accept-Ranges: bytes::#$"+
                "Content-Length: "+length+"::#$"+
                "Vary: Accept-Encoding::#$"+
                "Content-Type: text/plain::#$::#$";

        if(type == 1 ) {
            return header.replace("::#$","\n");
        }else
        {
            return header;
        }

    }

    /*
    Default run of multithreaded program.
    Input: Socket reference from SERVER
    Output: Data/chat to the client.

     */

    @Override
    public void run() {
        String inputLine;
        String input="0";
        int length = 0;

        Pattern p = Pattern.compile("\\w*");


        try{
        try {

            do {

                length = "Input your name please and press enter".length();
                out.println(getheader(0,length) + "Input your name please and press enter");
                logger.info(getheader(1,length) + "Input your name please and press enter");

                clientName = parseheader(in.readLine());

                logger.info(clientName);

                if(p.matcher(clientName).matches() && !allNames.contains(clientName) && !clientName.isEmpty())
                {
                    break;
                }
                length = "Cannot contain duplicate name/or Special character. Please enter different Name".length();
                out.println(getheader(0,length)+"Cannot contain duplicate name/or Special character. Please enter different Name");
                logger.info(getheader(1,length)+"Cannot contain duplicate name/or Special character. Please enter different Name");
            }while (true);

            current_Client = new ClientDetails(clientName,false, client_Number, clientSocket);

            client_Socket_Details.put(current_Client,clientSocket);
            allNames.add(clientName);

            // Display output on the client and asking user to select one option
            do{
                length = "******************************************************************************\n Select one of the option\n 1. Select client to connect\n 2. Exit\n******************************************************************************\n".length();
                out.println(getheader(0,length)+"******************************************************************************\n"+
                        "Select one of the option\n"+
                        "1. Select client to connect\n"+
                        "2. Exit\n"+
                "******************************************************************************\n");

                logger.info(getheader(1,length)+"******************************************************************************\n"+
                        "Select one of the option\n"+
                        "1. Select client to connect\n"+
                        "2. Exit\n"+
                        "******************************************************************************\n");

                boolean exitCondition= true;

                while(!current_Client.isConnected() && exitCondition){
                    if(in.ready()){
                        if((input=in.readLine())!=null){
                            // do whatever you like with the line
                            exitCondition = false;
                            input = parseheader(input);
                        }
                    }
                }
                if(current_Client.isConnected()){
                        break;
                }
                 //System.out.println(current_Client.isConnected());
                if(input.trim().equals("1")) {
                    //checking if any client is connected or not, else waiting in the queue
                    while ((client_Socket_Details.size() - no_ofconnected) == 1 ) {
                        length = "Waiting for new client to Connect".length();
                        out.println(getheader(0,length)+"Waiting for new client to Connect");
                        logger.info(getheader(1,length)+"Waiting for new client to Connect");
                        Thread.sleep(5000);
                        if(current_Client.isConnected() == true)
                        {
                            break;
                        }
                    }
                    if(current_Client.isConnected() == true)
                    {
                        break;
                    }

                    for (Map.Entry<ClientDetails,Socket> entry: client_Socket_Details.entrySet())
                    {
                        if(!((ClientDetails) entry.getKey()).isConnected() &
                                !(((ClientDetails) entry.getKey()).getClientref()).equals(clientSocket))
                        {
                            out.println(((ClientDetails) entry.getKey()).getClientno() + ":"
                                    + ((ClientDetails) entry.getKey()).getClientName());
                            logger.info(((ClientDetails) entry.getKey()).getClientno() + ":"
                                    + ((ClientDetails) entry.getKey()).getClientName());
                        }
                    }
                    length = "Showing all the clients Connection can be Made.... Enter the Client number to connect".length();
                    out.println(getheader(0,length)+"Showing all the clients Connection can be Made.... Enter the Client number to connect");
                    logger.info(getheader(0,length)+"Showing all the clients Connection can be Made.... Enter the Client number to connect");
                    input = parseheader(in.readLine());
                    logger.info(input);
                    break;
                }else if(input.trim().equals("2")) {
                    length = "Closing the connection".length();
                    out.println(getheader(0,length)+"Closing the connection");
                    logger.info(getheader(1,length)+"Closing the connection");
                    clientSocket.close();
                }
            }while(true);

            //this for getting and setting the client connected reference.
            for (Map.Entry<ClientDetails,Socket> entry: client_Socket_Details.entrySet())
            {
                if(Integer.parseInt(input) == ((ClientDetails) entry.getKey()).getClientno())
                {
                    current_Client_Modified = current_Client;
                    current_Client.setConnectedto(entry.getValue());
                    client_Socket_Details.put(current_Client_Modified, client_Socket_Details.get(current_Client));

                    current_Client_Modified = entry.getKey();
                    next_Client = current_Client_Modified;
                    current_Client_Modified.setConnectedto(clientSocket);
                    client_Socket_Details.put(next_Client, client_Socket_Details.get(current_Client_Modified));
                    break;
                }
            }

            clientSocket = current_Client.getConnectedto();

            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream(), true);

            length = "******************************************************************************\n******************************************************************************\n".length();

            out.println(getheader(0,length)+"******************************************************************************\n"+
                    "******************************************************************************\n");

            logger.info(getheader(1,length)+"******************************************************************************");
            //System.out.println("Now you are ready to chat, Type in your Message. To exit type Bye");
            out.println(getheader(0,length)+"Now you are ready to chat, Type in your Message. to Exit type Bye");
            logger.info(getheader(1,length)+"Now you are ready to chat, Type in your Message. to Exit type Bye");

            no_ofconnected++;
            //loop where input is taken from clinet and pushed to other client
            while (((inputLine = in.readLine()) != null) || !clientSocket.isClosed()) {
                inputLine = parseheader(inputLine);

                if(inputLine.toLowerCase().contains("bye")){
                    out.println(current_Client.getClientName()+": has left the chat");
                    break;
                }

                //System.out.println(current_Client.getClientName() +":" + inputLine);
                out.println(getheader(0,inputLine.length())+current_Client.getClientName() +":" + inputLine);
                logger.info(getheader(1,inputLine.length())+current_Client.getClientName() +":" + inputLine);
            }
            out.close();
            in.close();
            clientSocket.close();
            logger.info("Socket closed.");
        }catch (Exception ex)
        {
            logger.info("Exception Occured::"+ex);
            out.close();
            in.close();
            clientSocket.close();
        }

    }catch (Exception ex)
        {
            System.err.println("Exception "+ex);
        }
}}
