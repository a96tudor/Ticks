package uk.ac.cam.tma33.fjava.tick2star;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.cam.cl.fjava.messages.*;

/**
 * SafeMessagesHandler.java
 *
 *          A class that handles the messages (either writes/ reads them).
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class SafeMessagesHandler {

   private static ObjectOutputStream mOutStream;
   private static EmptyNikException.SafeObjectInputStream mInStream;
   private static Message mReadMessage;

   /**
    *
    *          CONSTRUCTOR
    *
    *       Initialises the in/out streams, using the provided is and os
    *
    * @param os      The wanted output stream
    * @param is      The wanted input stream
    * @throws IOException     if connection is unsuccessful
    */

   public SafeMessagesHandler(OutputStream os, InputStream is) throws IOException{
      mInStream = new EmptyNikException.SafeObjectInputStream(is);
      mOutStream = new ObjectOutputStream(os);
   }

   /**
    *          setOutStream() method
    *
    *      Changes the output stream to the new one, provided as parameter
    *
    * @param os         the new output stream
    * @throws IOException        if the reallocation goes wrong
    */

   public void setOutStream(OutputStream os) throws IOException{
      mOutStream = new ObjectOutputStream(os);
   }

   /**
    *       setInStream() method
    *
    *    Changes the input stream to the new one, provided as parameter
    *
    * @param is         the new input stream
    * @throws IOException        if reallocation goes wrong
    */

   public void setInStream(InputStream is) throws IOException {

   }

   /**
    *             writeMessage() method --- private
    *
    *     Used by the public functions to write a message to mOutStream
    *
    * @param out           The output message
    * @throws IOException     if writing is not successful
    */

   private final void  writeMessage(Message out) throws IOException{

      mOutStream.writeObject(out);
   }

   /**
    *          changeNikname() method
    *
    *     Sends an ChangeNikNameMessage to the server
    *
    * @param nikname       the new nikname
    * @throws IOException     if sending the message does not succeed
    */

   private void changeNikname(String nikname) throws IOException{
      ChangeNickMessage outMessage = new ChangeNickMessage(nikname);
      writeMessage(outMessage);
   }

   /**
    *
    *          sendMessage() method
    *
    *     Sends a ChatMessage to the server
    *
    * @param message       The message that will be sent
    * @throws IOException        if sending the message does not succeed
    */

   private void sendMessage(String message) throws IOException {
      ChatMessage out = new ChatMessage(message);
      writeMessage(out);
   }

   /**
    *               parseMessage()
    *
    *       Takes the message the client entered into the console and parses it
    *    in order to decide what is to be done (i.e. it is a command, message, invalid thing)
    *
    *
    * @param message       The raw message that was typed by the user
    * @return           true  -- if there was a "\quit" command
    *                   false   -- otherwise
    * @throws IOException         if writing goes wrong
    * @throws InvalidCommandException        if there the message is an invalid command
    * @throws EmptyNikException           if there is no nikname provided with the "\nik" command
    */

   public boolean parseMessage(String message) throws IOException, InvalidCommandException, EmptyNikException{
      if (message.startsWith("\\")) {
         // our message is a command
         if (message.equals(Strings.QUIT_COMMAND)) {
            // we quit the client, because it's all done
            return true;
         }
         else if (message.startsWith(Strings.NIKNAME_CHANGE_COMMAND)) {
            // I send a ChangeNikMessage

            if (message.equals(Strings.NIKNAME_CHANGE_COMMAND)) {
               throw new EmptyNikException();
            }
            else {
               // getting only the nikname out of the message
               String nik = message.substring(6);
               changeNikname(nik);
            }
            return false;
         }
         else {
            // we have an invalid command
            String eMessage = message.substring(1); // getting only the command out of the message
            String a[] = eMessage.split(" ");
            eMessage = a[0];
            throw new InvalidCommandException(eMessage);
         }
       }
       else {
         // it is just a message
         sendMessage(message);
         return false;
      }
   }

   /**
    *      getMessage() method
    *
    *    Returns the last message that was read
    *
    * @return  A Message object
    */

   public Message getMessage() {
      return mReadMessage;
   }

   /**
    *          getDate() method --- private
    *
    *     Returns the date the message was sent, in the format HH:mm:ss
    *
    * @return     the date as a String
    */

   private final String getDate() {
      Date d = mReadMessage.getCreationTime();
      String pattern = "HH:mm:ss";
      SimpleDateFormat sd = new SimpleDateFormat(pattern);  // setting the pattern for the custom date format
      return sd.format(d);
   }

   /**
    *          getSender() method --- private
    *
    *       Returns the sender of the message :
    *             (a) "[Server]"                     if the message is a StatusMessage
    *             (b) "[" + mReadMessage.getFrom() + "]"     otherwise (i.e. RelayMessage)
    *
    * @return    the name of the sender as a String
    */

   private final String getSender() {
      String sender = "";
      if (mReadMessage instanceof StatusMessage) {
         sender = Strings.SERVER_NAME;
      }
      else if (mReadMessage instanceof RelayMessage){
         RelayMessage message = (RelayMessage) mReadMessage;
         sender = "[" + message.getFrom() + "]";
      }
      return sender;
   }

   /**
    *       getText() method --- private
    *
    *       Returns the text of the message
    *
    * @return     the text, as a String
    */

   private final String getText() throws IllegalAccessException {
      String text = "";
      if (mReadMessage instanceof StatusMessage) {
         StatusMessage message = (StatusMessage) mReadMessage;
         text = message.getMessage();
      }
      else if (mReadMessage instanceof RelayMessage){
         RelayMessage message = (RelayMessage) mReadMessage;
         text = message.getMessage();
      }
      return text;
   }

   /**
    *
    *          getInterpretedMessage() method
    *
    *      Interprets the last message that was read and returns it as a String
    *
    * @return        the interpreted message as a String
    */

   public String getInterpretedMessage() throws IOException, ClassNotFoundException, IllegalAccessException,
           InvocationTargetException, IllegalArgumentException{
      if (mReadMessage == null) {
         // we need to read a new message
         readNewMessage();
      }
      String message;
      if (mReadMessage instanceof RelayMessage || mReadMessage instanceof StatusMessage) {
         String date = getDate();         // getting the date
         String sender = getSender();     // getting the sender
         String text = getText();         // getting the actual content of the message
         message = date + " " + sender + " " + text;
      }
      else {
         message = null;
      }
      return message;
   }

   /**
    *          readNewMessage()
    *
    *       Reads a new message into mReadMessage
    *
    * @throws IOException                 if we can't read
    * @throws ClassNotFoundException      if it can't find the Message class
    */

   public void readNewMessage() throws IOException, ClassNotFoundException, IllegalAccessException,
                        InvocationTargetException, IllegalArgumentException {
      mReadMessage = (Message) mInStream.readObject();
      if (mReadMessage instanceof NewMessageType) {
         // we add the message to the mInStream
         NewMessageType nmt = (NewMessageType) mReadMessage;
         mInStream.addClass(nmt.getName(),nmt.getClassData());                // adding the class
         System.out.println(Strings.getNewMessageTypeMessage(nmt.getName())); // writing the message
      }
      else if (!(mReadMessage instanceof RelayMessage) && !(mReadMessage instanceof StatusMessage)) {
         // we have an unknown message type, so we throw an InvalidMessageTypeException
         System.out.println(Strings.getUnknownTypeMessage(mReadMessage));
         Class c = mReadMessage.getClass();
         Method mthds[] = c.getDeclaredMethods();
         for (Method m : mthds) {
            if (m.isAnnotationPresent(Execute.class)) {
               if (!m.isAccessible()) {
                  m.setAccessible(true);
               }
               m.invoke(mReadMessage, new Object[0]);
            }
         }
      }
   }

}
