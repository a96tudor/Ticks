package uk.ac.cam.tma33.fjava.tick5;

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
	
	/*
	 * 		GENERAL PURPOSE MESSAGES
	 */

   public static final String ILLEGAL_ARGUMENTS_MESSAGE = "Usage: java ChatServer <port>";
   public static final String ILLEGAL_PORT_MESSAGE = "Cannot use port number ";
   public static final String ILLEGAL_OBJECT_RECEIVED_MESSAGE = "The server received an illegal type of object";
   public static final String STACK_TRACE_MESSAGE = "See the stack trace below: ";
   public static final String WRONG_DATABASE_PATH_MESSAGE = "Usage: uk.ac.cam.tma33.fjava.tick5.Database" +  " " +
                                                                  "<database name>";
   
   /*
    * 		PARTS OF MESSAGES
    */

   public static final String DEFAULT_NICKNAME_PREFIX = "Anonymous";

   private static final String WELCOME_MESSAGE_MIDDLE = " connected from ";
   private static final String DISCONNECT_MESSAGE_BODY = " has diconnected.";
   private static final String NEW_NICK_MESSAGE = " is now known as ";
   
   /*
    * 		MESSAGES GENERATING FUNCTIONS
    */

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

   /*
    * 		GENERAL PURPOSE SQL QUERRIES 
    */
   
   // table creation queries
   public static final String QUERY_CREATE_MESSAGES_TABLE = "CREATE TABLE messages(nick VARCHAR(255) NOT NULL,"
           								+ "message VARCHAR(4096) NOT NULL,timeposted BIGINT NOT NULL)";
   public static final String QUERY_CREATE_STATS_TABLE = "CREATE TABLE statistics(key VARCHAR(255),value INT)";
   
   // insert queries
   public static final String QUERY_INSERT_STATS_ROW1 = "INSERT INTO statistics(key,value) VALUES ('Total messages',0)";
   public static final String QUERY_INSERT_STATS_ROW2 = "INSERT INTO statistics(key,value) VALUES ('Total logins',0)";
   public static final String QUERY_INSERT_MSGS = "INSERT INTO MESSAGES(nick,message,timeposted) VALUES (?,?,?)";
   
   //update queries 
   public static final String QUERY_STATS_INC_LOGINS = "UPDATE statistics SET value = value+1 WHERE key='Total logins'";
   public static final String QUERY_STATS_INC_MSGS = "UPDATE statistics SET value = value+1 WHERE key='Total messages'";
   
   //select queries
   public static final String QUERY_SELECT_FIRST_10_MSGS = "SELECT nick,message,timeposted FROM messages "+
                      "ORDER BY timeposted DESC LIMIT 10";
}
