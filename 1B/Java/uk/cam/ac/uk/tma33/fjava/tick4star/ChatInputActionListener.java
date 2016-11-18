package uk.ac.cam.tma33.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.ChangeNickMessage;
import uk.ac.cam.cl.fjava.messages.ChatMessage;
import uk.ac.cam.cl.fjava.messages.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ChatInputActionListener.java
 *
 *               Class that implements the ActionListener interface
 *  to describe the behaviour of the Chat Client GUI when sending a new message
 *
 * Created by © Tudor Avram on 17/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

public class ChatInputActionListener implements ActionListener, Serializable {

   public static final long serialVersionUID = 1L;

   private final static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

   private String server;
   private int port;
   private JTextArea chatData;
   private JTextField chatBox;
   private ObjectOutputStream out;

   /**
    * CONSTRUCTOR
    *
    * @param h The host name
    * @param p The port number
    * @param f The JTextField instance
    * @param a The JTextArea instance
    * @param o The ObjectOutputStream assigned to the Socket
    */
   public ChatInputActionListener(String h, int p, JTextField f, JTextArea a, ObjectOutputStream o) {
      server = h;
      port = p;
      chatBox = f;
      chatData = a;
      out = o;
   }

   /**
    *
    * @return     The current time in the format "hh:mm:ss"
    */
   private static String getTime() {
      return dateFormat.format(new Date());
   }

   /**
    *       The implementation of the actionPerformed() method
    *       given by the ActionListener interface. Handles the
    *       messages that are typed in by the user. It accepts :
    *
    *       (a) normal messages --- sends new ChatMessage to the
    *                      server
    *       (b) command messages --- 3 more sub-cases :
    *
    *                   (i) "\nick <newNickname>" command --- sends
    *                         a ChangeNickMessage to the server
    *
    *                   (ii) "\quit" command --- closes the client app
    *
    *                   (iii) <unknown command> --- displays a message
    *                         on the client GUI. Has the format :
    *
    *                     <"HH:mm:ss"> [Client] Unknown command : <command>
    *
    * @param e    The event
    */
   @Override
   public void actionPerformed (ActionEvent e){
      if (e.getSource() == chatBox) {
         try {
            String msg = chatBox.getText();
            if (!msg.isEmpty()) {
               Message newMsg;
               if (msg.startsWith("\\")) {
                  // it is a command, so we want to see what kind of command it is
                  if (msg.startsWith("\\nick")) {
                     // we have to change the nickname
                     String tokens[] = msg.split(" ");
                     try {
                        newMsg = new ChangeNickMessage(tokens[1]);
                        out.writeObject(newMsg);
                     } catch (ArrayIndexOutOfBoundsException exp) {
                        // there was no nickname provided
                        chatData.append(getTime() + " [Client] Nickname command received, but no nickname provided.\n");
                     }
                  } else if (msg.equals("\\quit")) {
                     // we just quit
                     chatData.append(getTime() + " [Client] Connection terminated.\n");
                     System.exit(0);
                     return;
                  } else {
                     // it is an invalid command
                     String tokens[] = msg.split(" ");
                     chatData.append(getTime() + "[Client] Unknown command \\" + tokens[0] + "\n");
                  }
               } else {
                  // it is just a normal message
                  newMsg = new ChatMessage(msg);
                  out.writeObject(newMsg);
               }
            }
         } catch (IOException exp) {
            System.err.println("Cannot connect to " + server + " on " + port);
            return;
         }
         chatBox.setText("");
      }
   }
}
