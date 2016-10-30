package uk.ac.cam.tma33.fjava.tick1;

/**
 * HelloWorld
 *
 *      Has 2 different functionalities :
 *          (1) case 1 : if there is no name provided, it just prints "Hello, world!"
 *          (2) case 2 : if there is at least one name provided, it prints "Hello, " + the name + "!"
 *          for evert entry.
 *
 * Created on 12/10/2016
 * by © Tudor Mihai Avram
 *
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */
public class HelloWorld {

    public static String message = "Hello, ";

    public static void main(String args[]) {
        if (args.length == 0) {
            // no name provided
            message = "Hello, world";
            System.out.println(message);
        }
        else {
            message = "Hello, " + args[0];
            System.out.println(message);
        }
    }

}
