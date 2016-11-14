package uk.ac.cam.tma33.fjava.tick1star;


import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

/**
 * ImageChatClient.java
 * TODO : complete with functionality
 * <p>
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ImageChatClient extends JFrame {

   private ImageCanvas canvas;
   private Button buttonUp;
   private Socket socket;
   private static final JFileChooser fileChooser = new JFileChooser();

   /**
    *       Constructor
    *
    * @param host    the server host to connect to
    * @param port    the server port
    */
   public ImageChatClient(String host, int port) {
      super(Strings.GUI_CAPTION_PART + host + ":" + port);

      try {
         socket = new Socket(host, port);
      } catch (IOException e) {
         System.err.println(Strings.WRONG_HOST_MESSAGE_P1 + host + Strings.WRONG_HOST_MESSAGE_P2 + port);
      }

      canvas = new ImageCanvas();
      buttonUp = new Button(Strings.UPLOAD_BUTTON_CAPTON);

      this.getContentPane().add(canvas, BorderLayout.CENTER);
      this.getContentPane().add(buttonUp, BorderLayout.SOUTH);
      this.setSize(500, 500);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);

      handleClient();
   }

   /**
    *       handles the client server
    *
    * Has 2 major functionalities :
    *          (a) adds the action listener for the upload button
    *          (b) adds the listener thread, that gets images from the server
    *                   and refreshes the ImageCanvas
    *
    */

   private void handleClient() {

      /*
            Setting up the action listener for the upload button
       */

      buttonUp.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonUp) {
               int upload = fileChooser.showOpenDialog(ImageChatClient.this);
               if (upload == JFileChooser.APPROVE_OPTION) {

                  File file = fileChooser.getSelectedFile();
                  OutputStream outStream;
                  // getting the output stream
                  try {
                     outStream = socket.getOutputStream();
                  }
                  catch (Exception exp) {
                     return;
                  }
                  // sending the image to the server, as a byte stream
                  try
                  {
                     BufferedImage buffImg = ImageIO.read(file);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ImageIO.write(buffImg, "jpg", baos);
                     baos.flush();
                     outStream.write(baos.toByteArray());
                  }
                  catch (Exception exp) {
                     return;
                  }
               }
            }
         }
      });

      /*
         Setting up the listening thread
       */

      Thread listener = new Thread()
      {
         @Override
         public void run()
         {
            byte buff[] = new byte[1024];
            InputStream inStream;
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            try
            {
               inStream = socket.getInputStream();
            }
            catch (IOException e)
            {
               return;
            }
            byte previousByte = 99;
            while (true)
            {
               try
               {
                  int bytesRead = inStream.read(buff);
                  for (int i=0;i<bytesRead;i++)
                  {
                     bStream.write(buff[i]);
                     if (previousByte == -1 && buff[i] == -39)
                     {
                        BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(bStream.toByteArray()));
                        canvas.changeImage(buffImg);
                        ImageChatClient.this.setSize(buffImg.getWidth(), buffImg.getHeight());
                        bStream.reset();
                     }
                     previousByte = buff[i];
                  }
               }
               catch (IOException e)
               {
                  return;
               }
            }
         }
      };
      listener.setDaemon(true);
      listener.start();
   }

   public static void main(String args[]) {
      if (args.length < 2)
      {
         System.err.println(Strings.WRONG_ARGUMENTS_MESSAGE);
         return;
      }
      try
      {
         String host = args[0];
         int port = Integer.parseInt(args[1]);
         ImageChatClient client = new ImageChatClient(host, port);
      }
      catch (NumberFormatException e)
      {
         System.err.println(Strings.WRONG_ARGUMENTS_MESSAGE);
         return;
      }


   }
}
