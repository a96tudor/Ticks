package uk.ac.cam.tma33.fjava.tick2star;

import java.io.*;
import java.net.SocketPermission;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.util.PropertyPermission;

/**
 * EmptyNikException.java
 *
 *       Exception thrown if there was an empty nik provided.
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class EmptyNikException extends Exception {

   public EmptyNikException() {
      super(Strings.getEmptyNikMessage());
   }

   /**
    * SafeObjectInputStream.java
    *
    *       A safe ObjectInputStream ---- makes sure classes we read don't do
    *          funny stuff and that they come from trusted sources
    *
    * Created by © Tudor Avram on 14/11/16.
    * Homerton College, University of Cambridge
    * tma33@cam.ac.uk
    */
   public static class SafeObjectInputStream extends ObjectInputStream {

         private SecureClassLoader current = (SecureClassLoader) ClassLoader.getSystemClassLoader();

      /**
       *          CONSTRUCTOR
       *
       * @param in               The input stream the SafeObjectStream connects to
       * @throws IOException     If something goes wrong
       */

      public SafeObjectInputStream(InputStream in) throws IOException {
            super(in);
         }

      /**
       *                Uses the secure class loader to resolve the
       *                            desc class
       *
       * @param desc       The class to be resolved
       * @return        The
       * @throws IOException
       * @throws ClassNotFoundException
       */

         @Override
         protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
                 ClassNotFoundException {
            try {
               return current.loadClass(desc.getName());
            }
            catch (ClassNotFoundException e) {
               return super.resolveClass(desc);
            }
         }

         public void addClass(final String name, final byte[] defn) {

            PropertyPermission propPerm = new PropertyPermission("user.home", "read");
            FilePermission filePerm = new FilePermission(System.getProperty("user.home"), "read");
            SocketPermission socketPerm = new SocketPermission("www.cam.ac.uk:80", "connect");

            Permissions perms = new Permissions();
            perms.add(propPerm);
            perms.add(filePerm);
            perms.add(socketPerm);

            final ProtectionDomain protect = new ProtectionDomain(null, perms);

            current = new SecureClassLoader(current) {

               @Override
               protected Class<?> findClass(String className) throws ClassNotFoundException {
                  if (className.equals(name)) {
                     Class<?> result = defineClass(name, defn, 0, defn.length, protect);
                     return result;
                  }
                  else {
                     throw new ClassNotFoundException();
                  }
               }
            };

         }

   }
}
