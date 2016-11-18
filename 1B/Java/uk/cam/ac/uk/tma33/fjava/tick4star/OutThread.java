package uk.ac.cam.tma33.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.Message;
import uk.ac.cam.cl.fjava.messages.RelayMessage;
import uk.ac.cam.cl.fjava.messages.StatusMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OutThread.java
 *
 *        Thread class that listens to a server on a given
 *    host and port and handles the messages that come from it.
 *
 * Created by © Tudor Avram on 17/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

public class OutThread extends Thread implements Serializable{

   public static final long serialVersionUID = 1L;

   private final static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


   private String server;
   private int port;
   private Socket socket;
   private ObjectInputStream in;
   private JTextArea chatData;

   /**
    *       CONSTRUCTOR
    *
    * @param h    The host name
    * @param p    The port number
    * @param c    The JTextArea to write in
    * @param s    The Socket the GUI Client is connected to
    */

   public OutThread(String h, int p, JTextArea c, Socket s) {
      server = h;
      port = p;
      socket = s;
      chatData = c;
   }

   /**
    *             getTime()
    *
    * @return     The current time in the "hh:mm:ss" hour
    */
   private static String getTime() {
      return dateFormat.format(new Date());
   }

   /**
    *           Implementing the run() method.
    * Takes a message that comes to the server and treats two different cases :
    *
    *              (1) it is a RelayMessage --- prints the message in the format :
    *                         "hh:mm:ss" [<SenderName>] <Message Content>
    *
    *              (2) it is a StatusMessage --- prints the message in the format :
    *                         "hh:mm:ss" [Server] <Message Content></Message>
    */

   @Override
   public void run() {
      try {
         in = new ObjectInputStream(socket.getInputStream());
      }
      catch (IOException e1) {
         System.err.println("Cannot connect to " + server + " on port " + port);
         return;
      }
      while (true) {
         try {

            Message msg = null;
            try {
               msg = (Message) in.readObject();
            } catch (Exception e) {
               //
            }

            if (msg != null ) {
               if (msg instanceof RelayMessage) {
                  RelayMessage rmsg = (RelayMessage) msg;
                  chatData.append(getTime() + " [" + rmsg.getFrom() + "] " + rmsg.getMessage() + "\n");
               } else if (msg instanceof StatusMessage) {
                  System.out.println("status");
                  StatusMessage smsg = (StatusMessage) msg;
                  chatData.append(getTime() + " [Server] " + smsg.getMessage() + "\n");
               }
            }
         } catch (Exception e) {
               e.printStackTrace();
               return;
         }
      }
   }

}
