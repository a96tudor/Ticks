package uk.ac.cam.tma33.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.ArrayList;

/**
 * ClientHandler.java
 *
 *    Class representing a client.
 *
 * Created by © Tudor Avram on 15/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ClientHandler {

   private Socket socket;
   private MultiQueue<Message> multiQueue;
   private String nickname;
   private MessageQueue<Message> clientMessages;
   private ObjectOutputStream outStream;
   private ObjectInputStream inStream;

   /**
    *          CONSTRUCTOR
    *
    * @param s       The socket the client is connected to
    * @param q       The queue used by the server(i.e. The one to which the
    *                            client gets subscribed to
    */

   public ClientHandler(Socket s, MultiQueue<Message> q) {
      socket = s;
      multiQueue = q;
      clientMessages = new SafeMessageQueue<Message>();
      multiQueue.register(clientMessages); //
      nickname = Strings.DEFAULT_NICKNAME_PREFIX+getDefaultId(); // setting the default nickname
      try {
         outStream = new ObjectOutputStream(socket.getOutputStream());
         inStream = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
         e.printStackTrace();
      }

      // sending the status message for the new connection

      String host = socket.getInetAddress().getHostName(); // name of the host (the new connection)
      String text = Strings.getWelcomeMessage(nickname, host); // the contents of the message
      StatusMessage msg = new StatusMessage(text); // creating the new status message
      multiQueue.put(msg); // putting the message into the queue

      sendGUI();

      handleClient();
   }

   /**
    *    Returns an Anonymous id, using the following format :
    *          "Anonymous<UniqueID>"
    *     where <UniqueID> is a random 5 digits number
    *
    * @return     The default ID the client is assigned when it connects to the server
    */
   private String getDefaultId() {
      Random rand = new Random();
      int anon_id = rand.nextInt(90000) + 10000;
      return Integer.toString(anon_id);
   }

   /**
    *       Creates and runs 2 Thread objects :
    *
    *          (1) listener --- The thread that listens for the messages that
    *                are sent by the client and redistributes the right type of
    *                messages (either RelayMessage or StatusMessage) to all the
    *                members of multiQueue
    *
    *          (2) writer --- The thread that writes all the messages that are
    *                in the client's message queue
    */
   private void handleClient() {
      Thread listener = new Thread(){
         // the listener thread
         @Override
         public void run() {
            try {
               while(true) {
                  Message inMessage = (Message) inStream.readObject();
                  handleMessage(inMessage);
               }
            } catch (IOException e) {
               // the reading failed somewhere
               multiQueue.deregister(clientMessages); // we deregister the client
               String msg = Strings.getDisconnectMessage(nickname);
               StatusMessage out = new StatusMessage(msg);
               multiQueue.put(out);
            } catch (ClassNotFoundException e) {
               // problem with the class sent by the client --- not of type Message
               System.out.println(Strings.ILLEGAL_OBJECT_RECEIVED_MESSAGE);
               System.out.println(Strings.STACK_TRACE_MESSAGE);
               e.printStackTrace();
            }
         }

         /**
          *       Takes the message read by the run method and treats 2 separate cases :
          *             (1) it is a ChatMessage --- Just sends back a RelayMessage to all the clients
          *             (2) it is a ChangeNickMessage --- sends back a Status message to all clients
          *                                           --- changes the nickname of the current client
          *
          * @param msg        The message to handle with
          */
         private void handleMessage(Message msg) {
            if (msg instanceof ChatMessage) {
               // we send a RelayMessage back
               ChatMessage m = (ChatMessage) msg;
               RelayMessage msgOut = new RelayMessage(nickname, m);
               multiQueue.put(msgOut);
            } else if (msg instanceof ChangeNickMessage) {
               // we send a StatusMessage out and change the nickname
               ChangeNickMessage m = (ChangeNickMessage) msg;
               String newNick = m.name;
               String text = Strings.getNewNickMessage(nickname, newNick);
               StatusMessage msgOut = new StatusMessage(text);
               nickname = newNick;
               multiQueue.put(msgOut);
            }
         }
      }; // listens for the messages
      listener.setDaemon(true);
      listener.start();          // starting the listener thread

      Thread sender = new Thread() {
         // the sender thread
         @Override
         public void run() {
            try {
               while(true) {
                  Message msg = clientMessages.take();
                  outStream.writeObject(msg);
               }
            } catch(IOException e) {
               multiQueue.deregister(clientMessages);
               String msg = Strings.getDisconnectMessage(nickname);
               StatusMessage out = new StatusMessage(msg);
               multiQueue.put(out);
            }
         }
      };

      sender.setDaemon(true);
      sender.start();
   }

   private void sendBytesStream(InputStream is, String name) throws IOException{
      byte buff[] = new byte[1024];

      ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();

      while (true) {
         int bytesRead = is.read(buff);
         if (bytesRead <= 0) break;
         bytesStream.write(buff, 0, bytesRead);
      }

         NewMessageType newMsg = new NewMessageType(name, bytesStream.toByteArray());

         outStream.writeObject(newMsg);

   }

   /**
    *       Creates and sends a ClientGUI object to the client
    */
   private void sendGUI() {
      try {

         /*
               Sending the ChatInputActionListener class as a byte stream
          */

         Class<ChatInputActionListener> CIALClass = ChatInputActionListener.class;
         String name = CIALClass.getName();
         InputStream is = CIALClass.getClassLoader().getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
         sendBytesStream(is, name);
         /*
               Sending the ChngNickActionListener class as a byte stream
          */

         Class<ChngNickActionListener> CNALClass = ChngNickActionListener.class;
         name = CNALClass.getName();
         is = CNALClass.getClassLoader().getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
         sendBytesStream(is, name);

         /*
               Sending the OutThread class a byte stream
          */

         Class<OutThread> OTClass = OutThread.class;
         name = OTClass.getName();
         is = OTClass.getClassLoader().getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
         sendBytesStream(is, name);

         /*
               Sending the ClientGUI class over to the client.
          */

         Class<ClientGUI> GUIClass = ClientGUI.class;
         name = GUIClass.getName();
         is = GUIClass.getClassLoader().getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
         sendBytesStream(is, name);

         /*
               Seding the ClienGUICreatorMessage class
          */

         Class<ClientGUICreatorMessage> CreatorClass = ClientGUICreatorMessage.class;
         name = CreatorClass.getName();
         is = CreatorClass.getClassLoader().getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
         sendBytesStream(is, name);

         outStream.writeObject(new ClientGUICreatorMessage(ChatServer.getHost(), ChatServer.getPort()));

      } catch (IOException e) {
         e.printStackTrace();
      } catch (SecurityException e) {
         e.printStackTrace();
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
      }
   }

}
