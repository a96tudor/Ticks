package uk.ac.cam.tma33.fjava.tick4star;

import uk.ac.cam.cl.fjava.messages.Execute;
import uk.ac.cam.cl.fjava.messages.Message;

import java.io.Serializable;

/**
 * ClientGUICreatorMessage.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ClientGUICreatorMessage extends Message implements Serializable{

   public static final long serialVersionUID = 1L;
   public String hostName = null;
   public int port;

   /**
    *          CONSTRUCTOR
    *
    * @param hostName      The host name the client GUI has to connect to
    * @param port          The port number
    */
   public ClientGUICreatorMessage(String hostName, int port) {
      this.hostName = hostName;
      this.port = port;
   }

   /**
    *       Method executed on client side
    */

   @uk.ac.cam.cl.fjava.messages.Execute
   public void Execute() {
      new ClientGUI(hostName, port);
   }

}
