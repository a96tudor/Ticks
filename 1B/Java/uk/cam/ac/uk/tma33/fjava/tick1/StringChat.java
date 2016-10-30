package uk.ac.cam.tma33.fjava.tick1;

/**
 * StringChat
 *
 *      A basic chat, using multithreading
 *  to be able to read and write (somehow)
 *  in the same time.
 *
 * Created on 12/10/2016
 * by © Tudor Mihai Avram
 *
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class StringChat {

    /**
            getMessage() method

            Reads whatever message it can get from the inputStream
        and returns it as a string.

        @param inputStream      the input stream for the server
        @return The message as a string
     */

    private static String getMessage(InputStream inputStream) throws IOException {

        byte[] buffer = new byte[1024]; // the buffer I read the stream into
        int lng = inputStream.read(buffer,0,1024); // feeding the buffer and storing the number of bytes
                                                   // I read, so that I can initialise the message correctly


        String message = new String(buffer,0,lng); // creating and returning the message
        return message;                            // corresponding to the byte stream
    }

    /**
            writeMessage() method

            Sends a given message to the server,
         using outputStream

         @param outputStream    The output stream for the message
         @param message         The message to be sent
         @throws IOException    If something goes wrong while writing the data
     */

    private static void writeMessage(OutputStream outputStream, String message) throws IOException{

        byte[] buffer = message.getBytes(); // the buffer used for writing the message
        int lng = buffer.length;

        outputStream.write(buffer);
    }


    public static void main(String[] args) {

        String host = null;
        int p = 0;

        if (args.length < 1 || args.length > 2) {
            // there are no/ too many/ too few arguments
            System.out.println("This application requires two arguments: <machine> <port>");
            return;
        }

        try {
            final String server = args[0];
            final int port = Integer.parseInt(args[1]);

            host = args[0];
            p = port;

            //"s" is declared final in order to make sure that it does not get overwritten
            // in time and the connection to the server stays open the all time.

            final Socket s = new Socket(server, port);

            InputStream inputStream = s.getInputStream();
            OutputStream outputStream = s.getOutputStream();

            Thread output = new Thread() {
                @Override
                public void run() {
                    try {
                        String input;
                        while (true) {
                            input = getMessage(inputStream);
                            System.out.println(input);
                        }
                    }
                    catch (IOException e) {
                        // could not connect to the server
                        System.out.println("Cannot connect to " + server + " on port " + port);
                    }
                }
            };

            output.setDaemon(true); // makes sure that at the end of the
            output.start();         // program, the thread is killed as well

            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            String messageOut;
            while (true) {
                messageOut = r.readLine();
                writeMessage(outputStream, messageOut);
            }
        } catch (NumberFormatException e) {
            // the port number is not in the right format
            System.out.println("This application requires two arguments: <machine> <port>");
        } catch (IOException e) {
            // could not connect to the server
            System.out.println("Cannot connect to " + host + " on port " + p);
        }

    }
}
