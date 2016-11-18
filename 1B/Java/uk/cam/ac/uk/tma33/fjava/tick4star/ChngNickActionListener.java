package uk.ac.cam.tma33.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.ChangeNickMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.Serializable;
import java.net.Socket;

/**
 * ChngNickActionListener.java
 *
 *             Class that implements the ActionListener interface
 *  to describe the behaviour of the "New nickname" button from the ClientGUI class
 *
 * Created by © Tudor Avram on 17/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ChngNickActionListener implements ActionListener, Serializable{

   public static final long serialVersionUID = 1L;

   private String server;
   private int port;
   private JButton nickButton;
   private transient ObjectOutputStream out;

   /**
    *             CONSTRUCTOR
    *
    * @param h      the host name
    * @param p      the port number
    * @param b      the button shutter
    */

   public ChngNickActionListener(String h, int p, JButton b, ObjectOutputStream o) {
      server = h;
      port = p;
      out = o;
      nickButton = b;
   }

   /**
    *                The implementation of the actionPerformed() method
    *       given by the ActionListener interface. It sends a new ChangeNickMessage
    *                                to the server
    *
    * @param e    The action event
    */

   @Override
   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == nickButton) {
         String newNick = JOptionPane.showInputDialog(ChngNickActionListener.this, "Enter the new nickname");
         if (newNick != null) {
            try {
               ChangeNickMessage newMsg = new ChangeNickMessage(newNick);
               out.writeObject(newMsg);
            } catch (IOException exp) {
               System.err.println("Cannot connect to " + server + " on " + port);
               return;
            }
         }
      }

   }

}
