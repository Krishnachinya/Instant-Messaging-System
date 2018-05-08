import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KrishnChinya on 3/5/17.
 * INPUT: SOCKET INPUT AND OUTPUT REFERENCE. ALL MESSAGE WILL BE SEND TO SERVER FROM HERE ONLY
 */
public class ClientInputs implements Runnable {

    BufferedReader input;
    PrintStream out;
    String inputline;
    Socket client;
    Pattern p = Pattern.compile("Type: text/plain(.*)");
    Matcher matcher;

    public ClientInputs(BufferedReader input, PrintStream out,Socket client) {
        this.input = input;
        this.out = out;
        this.client = client;
    }

    //TO PARSE THE GET AND POST MESSAGES.
    public String parseheader(String inputline){
            matcher = p.matcher(inputline.replace("::#$",""));
            if ( matcher.find() ) {
                return (matcher.group(1)).trim();
            }else {
               return inputline;
            }

    }



    @Override
    public void run() {
        //Here I get the put header and message from server. so I have to start strip data from put..
        boolean displayheader = true;
        try {
            while ((inputline=input.readLine())!= null ) {
                if(displayheader)
                {
                    System.out.print("Header wont Shown for upcoming messages. Check log for the same");
                    inputline = inputline.replace("::#$","\n");
                    out.println(inputline);
                    displayheader=false;
                }else {
                    inputline = parseheader(inputline);
                    out.println(inputline);
                }
            }
        }catch (Exception ex){
            System.err.println("Socket is closed, programming is existing");
            System.exit(0);
        }
    }
}
