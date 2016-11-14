package uk.ac.cam.tma33.fjava.tick1star;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ImageCanvas.java
 *
 *    Class representing an image canvas for the ImageChatClient
 *
 * Created by © Tudor Avram on 14/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class ImageCanvas extends Canvas {

   private BufferedImage img;

   /**
    *       CONSTRUCTOR
    */
   public ImageCanvas() {
      img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB); // setting the image to the initial
      this.setBackground(Color.BLACK);
   }

   /**
    *          Method that draws the buffered image in
    *                the given Graphics object
    *
    * @param g    The object to draw the image to
    */
   @Override
   public void paint(Graphics g) {
      g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
      g.dispose();
   }

   /**
    *       Changes the buffered to a given image
    *
    * @param newImage      the new buffered image
    */

   public void changeImage(BufferedImage newImage) {
      this.img = newImage; // changing the image
      this.repaint();   // repainting the canvas
   }

}
