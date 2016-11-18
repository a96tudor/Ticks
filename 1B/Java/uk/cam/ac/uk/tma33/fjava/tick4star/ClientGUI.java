package uk.ac.cam.tma33.fjava.tick4star;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClientGUI.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ClientGUI extends JFrame{

   private static final long serialVersionUID = 1L;

   private final static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

   private JTextField chatBox;
   private JTextArea chatData;
   private JScrollPane scroll;
   private JButton nickButton;

   private String server;
   private int port;
   private transient Socket socket;
   private transient ObjectInputStream in;
   private transient ObjectOutputStream out;

   /**
    *    gets the current date
    *
    * @return The current date in the HH:mm:ss format
    */

   private static String getTime() {
      return dateFormat.format(new Date());
   }

   /**
    *       chat action listener anonymous class
    *
    *                  Takes the messages written in the chatBox and
    *       handles them, depending on the types (i.e. commands/ classic messages)
    */

   public ChatInputActionListener chatActionListener = null;
   /**
    *    button action listener for the nickname button --- anonymous serializable class
    *
    *    Displays an InputDialog and changes the nickname to the new one proviede
    */

   public ActionListener nickButtonListener = null;

   /**
    *    output thread anonymous serializable class
    *
    *    listens for messages that come from the server and adds them to the
    */

   public OutThread outThread = null;


   /**
    *       CONSTRUCTOR
    *
    * @param host       The host name
    * @param port       The port number
    */

   public ClientGUI(String host, int port) {

      super("Chat GUI --- Further Java Tick 4*");

      this.server = host;
      this.port = port;

      // creating the socket

      try {
         socket = new Socket(host, port);
         out = new ObjectOutputStream(socket.getOutputStream());
      } catch (IOException e) {
         System.err.println("Cannot connect to " + server + " on port " + port);
         return;
      }

      // initialising the required GUI elements


      chatData = new JTextArea();
      chatData.setEditable(false);
      scroll = new JScrollPane(chatData, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


      chatBox = new JTextField(50);
      chatActionListener = new ChatInputActionListener(host, port, chatBox, chatData, out);
      chatBox.addActionListener(chatActionListener);

      nickButton = new JButton("Change nickname");
      nickButtonListener = new ChngNickActionListener(host, port, nickButton, out);
      nickButton.addActionListener(nickButtonListener);

      outThread = new OutThread(host, port, chatData, socket);
      outThread.setDaemon(true);
      outThread.start();

      // adding the elements to the GUI
      this.add(scroll);
      this.getContentPane().add(chatBox, BorderLayout.SOUTH);
      this.getContentPane().add(nickButton, BorderLayout.NORTH);
      this.setSize(500, 500);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);

      // printing the welcome message
      chatData.append(getTime() + " [Client] Connected to " + server + " on port " + port + ".\n");
   }
}
