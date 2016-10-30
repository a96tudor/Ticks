package uk.ac.cam.tma33.fjava.tick2;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * TestMessageReadWrite.java
 *
 * Class used for testing serializability
 *
 * Created by © Tudor Avram on 14/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class TestMessageReadWrite {

   static boolean writeMessage(String message, String filename) {

      TestMessage outMessage = new TestMessage();
      outMessage.setMessage(message);

      try {
         FileOutputStream fos = new FileOutputStream(filename);
         ObjectOutputStream writer = new ObjectOutputStream(fos);
         writer.writeObject(outMessage);
         writer.close();
         return true;
      }
      catch (IOException e) {
         return false;

   }
}

   static String readMessage(String location) {
      try {
         ObjectInputStream ois;
         if (location.startsWith("http://")) {
            // it's a URL
            URL url = new URL(location);
            URLConnection uc = url.openConnection();
            uc.setDoInput(true);
            InputStream is = uc.getInputStream();
            ois = new ObjectInputStream(is);
         }
         else {
            // I assume it is a file
            FileInputStream fis = new FileInputStream(location);
            ois = new ObjectInputStream(fis);
         }
         TestMessage inMessage = (TestMessage) ois.readObject();
         String message = inMessage.getMessage();
         return message;
      }
      catch (IOException e) {
         return null;
      }
      catch (ClassNotFoundException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static void main(String args[]) {

      // Serialising

      // STEP 1 : WRITING
      String message = args[0];
      String location = "test.dat";

      boolean success = writeMessage(message,location);
      if (!success) {
         System.out.println("The writing was not successful");
         return;
      }

      // STEP 2 : READING
      String outputMessage = readMessage(location);
      if (outputMessage == null) {
         System.out.println("Deserialisation was not successful.");
      }
      else {
         System.out.println("Deserialisation was successful. The output is : " + outputMessage);
      }
   }

}
