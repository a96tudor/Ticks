package uk.ac.cam.tma33.fjava.tick1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * StringReceive
 *
 *      Takes a provided host name and port number,
 *   connects to them and outputs the stream coming from the
 *   host 1024 bytes at a time, until the application is killed.
 *
 * Created on 12/10/2016
 * by © Tudor Mihai Avram
 *
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class StringReceive {

    private static Socket mySocket;
    private static String mHostName;
    private static int mPortNo;

    public static void main(String  args[]) {

        if (args.length <1 || args.length > 2) {
            // there are no/ too many/ too few arguments
            System.out.println("This application requires two arguments: <machine> <port>");
            return;
        }

        try {
            mHostName = args[0];
            mPortNo = Integer.parseInt(args[1]);

            mySocket = new Socket(mHostName,mPortNo);

            byte[] buffer = new byte[1024];

            InputStream myInputStream  = mySocket.getInputStream();

            while(true) {
                int lng = myInputStream.read(buffer,0,1024);
                String message = new String(buffer,0,lng);
                System.out.println(message);
            }
        }
        catch (NumberFormatException e) {
            // the port number is not in the right format
            System.out.println("This application requires two arguments: <machine> <port>");
        }
        catch (IOException e) {
            // the connection didn't work
            System.out.println("Cannot connect to " + mHostName + " on port " + mPortNo);
        }

    }

}
