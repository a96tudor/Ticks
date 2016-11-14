package uk.ac.cam.tma33.fjava.tick2star;

/**
 * SafeChatClient.java
 *
 *          A safe chat client
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */



import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@FurtherJavaPreamble(
        author="© Tudor Avram",
        ticker = FurtherJavaPreamble.Ticker.B,
        date = "24/10/2016",
        crsid = "tma33",
        summary = "A safe chat client."
)

public class SafeChatClient {

   public static void main(String args[]) {

      System.setProperty("java.security.policy", "http://www.cl.cam.ac.uk/teaching/1011/FJava/all.policy");
      System.setSecurityManager(new SecurityManager());

      String host = "";
      int p = 0;

      if (args.length < 1 || args.length > 2) {
         // there are no/ too many/ too few arguments
         System.out.println(Strings.INVALID_ARGUMENTS_MESSAGE);
         return;
      }

      try {
         final String server = args[0];
         final int port = Integer.parseInt(args[1]);
         boolean quit = false;

         host = args[0];
         p = port;

         //"s" is declared final in order to make sure that it does not get overwritten
         // in time and the connection to the server stays open the all time.

         final Socket s = new Socket(server, port);

         InputStream inputStream = s.getInputStream();
         OutputStream outputStream = s.getOutputStream();

         final SafeMessagesHandler mh = new SafeMessagesHandler(outputStream, inputStream); // creating the handler

         System.out.println(Strings.getWelcomeMessage(host,port));

         Thread output = new Thread() {
            @Override
            public void run() {
               try {
                  String input;
                  while (true) {
                     try {
                        mh.readNewMessage();
                        input = mh.getInterpretedMessage();
                        if (input != null) {
                           // it's of an accepted type
                           System.out.println(input);
                        }
                     }
                     catch (IllegalAccessException e) {
                        System.out.println(Strings.ILLEGAL_ACCESS_EXCEPTION_MESSAGE);
                     }
                     catch (InvocationTargetException e) {
                        System.out.println(Strings.INVOCATION_TARGET_EXCEPTION_MESSAGE);
                     }
                     catch (IllegalArgumentException e) {
                        System.out.println(Strings.ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
                     }
                  }
               }
               catch (IOException e) {
                  // could not connect to the server
                  System.out.println(Strings.getUnsuccessfulConnectionMessage(server,port));
               } catch (ClassNotFoundException e) {
                  System.out.println(Strings.getUnsuccessfulConnectionMessage(server,port));
               }
            }
         };

         output.setDaemon(true); // makes sure that at the end of the
         output.start();         // program, the thread is killed as well

         BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
         String messageOut;
         while (!quit) {
            messageOut = r.readLine();
            try {
               quit = mh.parseMessage(messageOut);
               if (quit) {
                  System.out.println(Strings.getQuitMessage());
                  Runtime.getRuntime().exit(0);
               }
            }
            catch (InvalidCommandException e) {
               System.out.println(e.getMessage());
            }
            catch (EmptyNikException e) {
               System.out.println(e.getMessage());
            }
         }
      } catch (NumberFormatException e) {
         // the port number is not in the right format
         System.out.println(Strings.INVALID_ARGUMENTS_MESSAGE);
      } catch (IOException e) {
         // could not connect to the server
         System.out.println(Strings.getUnsuccessfulConnectionMessage(host,p));
      }


   }

}
