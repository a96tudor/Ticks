package uk.ac.cam.tma33.fjava.tick4;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import uk.ac.cam.cl.fjava.messages.Message;

/**
 * ChatServer.java
 *
 *    Implements the server side of a chat
 *
 * Created by © Tudor Avram on 05/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ChatServer {

   public static void main(String args[]) {
      if (args.length != 1) {
         // There are too many/ no arguments
         System.out.println(Strings.ILLEGAL_ARGUMENTS_MESSAGE); // print error message
         return; //terminate the program
      }
      int port = 0;
      try {
         port = Integer.parseInt(args[0]); // The port to connect
         final ServerSocket socket = new ServerSocket(port);
         final MultiQueue<Message> queue = new MultiQueue<>();
         while(true) {

            Socket clientSocket = socket.accept();    // waiting for a new client

            ClientHandler newClientHandler = new ClientHandler(clientSocket, queue);      // creating a handler for the client
         }
      }
      catch (NumberFormatException e) {
         // There is an invalid argument
         System.out.println(Strings.ILLEGAL_ARGUMENTS_MESSAGE);   // print the error message
      }
      catch (IOException e) {
         // We couldn't connet to the given port
         System.out.println(Strings.ILLEGAL_PORT_MESSAGE + port);
      }
   }

}
