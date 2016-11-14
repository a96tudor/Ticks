package uk.ac.cam.tma33.fjava.tick2star;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Strings.java
 *
 *    Class containing constant messages and methods that create new messages
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Strings {

   public final static String SERVER_NAME = "[Server]";
   public final static String CLIENT_NAME = "[Client]";

   public final static String QUIT_COMMAND = "\\quit";
   public final static String NIKNAME_CHANGE_COMMAND = "\\nick";

   private final static String CONNECTION_TERMINATED_MESSAGE = "Connection terminated.";
   private final static String INVALID_COMMAND_MESSAGE = "Unknown command ";
   private final static String EMPTY_NIK_MESSAGE = "Nikname command received, but no nikname provided.";

   private final static String NEW_MESSAGE_TYPE_MESSAGE_P1 = "New class";
   private final static String NEW_MESSAGE_TYPE_MESSAGE_P2 = "loaded.";
   private final static String INVALID_MESSAGE_TYPE_MESSAGE = "New message of unknown type received.";

   private final static String UNSUCCESSFUL_CONNECTION_MESSAGE_1 = "Cannot connect to";
   private final static String UNSUCCESSFUL_CONNECTION_MESSAGE_2 = "on port";
   public final static String INVALID_ARGUMENTS_MESSAGE = "This application requires two arguments: <machine> <port>";

   public final static String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "There was an illegal argument passed within a method";
   public final static String ILLEGAL_ACCESS_EXCEPTION_MESSAGE = "There was an attempt to access an unacccessable field/method";
   public final static String INVOCATION_TARGET_EXCEPTION_MESSAGE = "There was an attempt to invoke a method that can't be invoked";

   private final static String WELCOME_MESSAGE_1 = "Connected to";
   private final static String WELCOME_MESSAGE_2 = "on port";

   /**
    *             getTime() --- private
    *
    *       Returns the current time in the format HH:mm:ss
    *
    * @return     The time in the required format, as a String
    */

   private static String getTime() {
      Date d = new Date();
      String pattern = "HH:mm:ss";
      SimpleDateFormat sd = new SimpleDateFormat(pattern);  // setting the pattern for the custom date format
      return sd.format(d);
   }

   /**
    *          getInvalidCommandMessage()
    *
    *    Method that constructs the invalid command message, in the format :
    *
    *             HH:mm:ss [Client] "command"
    *
    * @param command      The name of the command provided
    * @return             The message in the required format
    */

   public static String getInvalidCommandMessage(String command) {
      String message = getTime() + " ";
      message += CLIENT_NAME + " ";
      message += INVALID_COMMAND_MESSAGE + " \"" + command + "\"";
      return message;
   }

   /**
    *          getEmptyNikMessage()
    *
    *    Method that will return the empty nikname message in the format
    *
    *        HH:mm:ss [Client] Nikname command received, but no nikname provided.
    *
    * @return     The message in the required format
    */

   public static String getEmptyNikMessage() {
      String message = getTime();
      message += " " + CLIENT_NAME + " ";
      message += EMPTY_NIK_MESSAGE;
      return message;
   }

   /**
    *          getQuitMessage()
    *
    *    Method that will return the quit message in the following format :
    *
    *             HH:mm:ss [Client] Connection terminated.
    *
    * @return     the message in the required format
    */

   public static String getQuitMessage() {
      String message = getTime();
      message += " " + CLIENT_NAME + " ";
      message += CONNECTION_TERMINATED_MESSAGE;
      return message;
   }

   /**
    *       getNewMessageTypeMessage()
    *
    *     Method that will return the new message type message in the following format :
    *
    *     HH:mm:ss [Client] New class <name> loaded.
    *
    * @param name       The name of the new class.
    * @return           The message in the required format
    */

   public static String getNewMessageTypeMessage(String name) {
      String message = getTime();
      message += " " + CLIENT_NAME + " ";
      message += NEW_MESSAGE_TYPE_MESSAGE_P1 +" "+ name +" " + NEW_MESSAGE_TYPE_MESSAGE_P2;
      return message;
   }

   /**
    *          getUnknownTypeMessage()
    *
    *      Returns the message if the chat receives a message of an unknown type. It uses the format :
    *
    *        HH:mm:ss [Client] <class name> <field1(field1Value)>, <field2(field2Value)>, ...
    *
    * @param o       The reader, as an object
    * @return     The message in the required format
    */

   public static String getUnknownTypeMessage(Object o) throws IllegalAccessException{
      Class c=o.getClass();
      Field fields[] = c.getDeclaredFields();
      String name = c.getSimpleName();
      String message = getTime();
      message += " " + CLIENT_NAME + " ";
      message += name + ":";
      boolean first = true;
      for (Field f : fields) {
         if (first) first = false;
         else message += ",";

         message += " ";
         message += f.getName() + "(";

         if (!f.isAccessible()) {
              f.setAccessible(true);
           }
         Object ob = f.get(o);

         message += ob.toString();
         message += ")";
      }
      return message;
   }

   /**
    *                getUnsuccessfulConnectionMessage()
    *
    *       Returns the message that is displayed in case of an unsuccessful connection.
    *                   It uses the following format :
    *            Cannot connect to <hostName> on port <portNumber>
    *
    * @param host       The host name
    * @param port       the port number
    * @return           the message in the required format
    */

   public static String getUnsuccessfulConnectionMessage(String host, int port) {
      String message = UNSUCCESSFUL_CONNECTION_MESSAGE_1;
      message += (" " + host + " " + UNSUCCESSFUL_CONNECTION_MESSAGE_2 + " " + Integer.toString(port));
      return message;
   }

   /**
    *             getWelcomeMessage()
    *
    *       Returns the welcome message, the first time it connects to the server
    *                Has the following format :
    *
    *          HH:mm:ss [Client] Connected to <host> on port <port>.
    *
    * @param host          host name
    * @param port          port number
    * @return              the message in the required format
    */

   public static String getWelcomeMessage(String host, int port) {
      String message = getTime();
      message += " " + CLIENT_NAME + " ";
      message += WELCOME_MESSAGE_1 + " " + host + " " + WELCOME_MESSAGE_2 + " " + port + ".";
      return message;
   }

}
