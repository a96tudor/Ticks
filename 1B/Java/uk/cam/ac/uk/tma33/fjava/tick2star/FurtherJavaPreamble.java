package uk.ac.cam.tma33.fjava.tick2star;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * FurtherJavaPreamble.java
 *
 *          Annotation used in ChatClient class
 *
 * Created by © Tudor Avram on 23/10/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface FurtherJavaPreamble {
   enum Ticker {A,B,C,D};
   String author();
   String date();
   String crsid();
   String summary();
   Ticker ticker();
}
