package uk.ac.cam.tma33.fjava.tick4;

/**
 * Strings.java
 *
 *    Class that provides the messages/ methods to generate them for the other classes in the project
 *
 * Created by © Tudor Avram on 05/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class Strings {

   public static final String ILLEGAL_ARGUMENTS_MESSAGE = "Usage: java ChatServer <port>";
   public static final String ILLEGAL_PORT_MESSAGE = "Cannot use port number ";
   public static final String ILLEGAL_OBJECT_RECEIVED_MESSAGE = "The server received an illegal type of object";
   public static final String STACK_TRACE_MESSAGE = "See the stack trace below: ";

   public static final String DEFAULT_NICKNAME_PREFIX = "Anonymous";

   private static final String WELCOME_MESSAGE_MIDDLE = " connected from ";
   private static final String DISCONNECT_MESSAGE_BODY = " has diconnected.";
   private static final String NEW_NICK_MESSAGE = " is now known as ";

   public static String getWelcomeMessage(String nick, String host) {
      String message;
      message = nick + WELCOME_MESSAGE_MIDDLE + host + ".";
      return message;
   }

   public static String getDisconnectMessage(String nick) {
      String message;
      message = nick + DISCONNECT_MESSAGE_BODY;
      return message;
   }

   public static String getNewNickMessage(String oldNick, String newNick) {
      String message;
      message = oldNick + NEW_NICK_MESSAGE + newNick;
      return message;
   }

}
