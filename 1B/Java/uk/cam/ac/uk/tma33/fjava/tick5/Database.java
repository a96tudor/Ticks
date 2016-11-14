package uk.ac.cam.tma33.fjava.tick5;

/**
 * Database.java
 *
 *    Handles a database for a chat server.
 *
 * Created by © Tudor Avram on 13/11/16.
 * Homerton College, University of Cambridge
 * tma33@cam.ac.uk
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.cam.cl.fjava.messages.RelayMessage;

public class Database {
	
	 private Connection mConnection;
	 
	 /**
	  * 	Creates the messages and the statistics tables in the db.	 
	  * 		Also initializes the statistics table rows.
	  *  
	  * @throws SQLException	If I can't initiate the SQL statement
	  */
	 private void createTables() throws SQLException {

       Statement sqlStmt = mConnection.createStatement();
		 try {
			 
		  try {
			  sqlStmt.execute(Strings.QUERY_CREATE_MESSAGES_TABLE); // running the messages table creation query
		  } catch (SQLException e) {
			  // do nothing, the db table already exists
		  }
		  sqlStmt.execute(Strings.QUERY_CREATE_STATS_TABLE); // running the stats table creation query
		  
		  sqlStmt.execute(Strings.QUERY_INSERT_STATS_ROW1); 
		  sqlStmt.execute(Strings.QUERY_INSERT_STATS_ROW2); 
		  mConnection.commit();
		 } catch (SQLException e) {
		  // do nothing, because the table already exists
			 
		 } finally {
		  sqlStmt.close();
		 }
	 }

	 
	 /**
	  * 		CONSTRUCTOR
	  * 
	  * @param databasePath		the path to create the database at
	  * @throws SQLException	if creating the connection fails
	  */
	 public Database(String databasePath) throws SQLException {

       try {
          Class.forName("org.hsqldb.jdbcDriver");
       } catch (ClassNotFoundException e) {
          e.printStackTrace();
       }
       mConnection = DriverManager.getConnection("jdbc:hsqldb:file:"
               + databasePath, "SA", "");

       Statement delayStmt = mConnection.createStatement();
       try {delayStmt.execute("SET WRITE_DELAY FALSE");}  //Always update data on disk
       finally {delayStmt.close();}

       mConnection.setAutoCommit(false);

       createTables();	// creating the tables, if they are not available
	 }
	 
	 /**
	  * 	Closes the db connection
	  * @throws SQLException	If something goes wrong
	  */
	 public void close() throws SQLException { 
		 mConnection.close();
	 }
	 
	 /**
	  * 	Updates the 1st row of the stats table 
	  * 
	  * @throws SQLException	if can't create a statement
	  */
	 public void incrementLogins() throws SQLException { 
		Statement stmt = mConnection.createStatement();
		try {
			stmt.execute(Strings.QUERY_STATS_INC_LOGINS); // updates the logins
		} finally {
			stmt.close();
		}
		mConnection.commit();
	 }
	 

	 public void addMessage(RelayMessage m) throws SQLException { 

		/*
		 * 		updating the messages count
		 */
		Statement stmt = mConnection.createStatement();
		try {

			stmt.execute(Strings.QUERY_STATS_INC_MSGS); // updates the logins
		} finally {
			stmt.close();
		}
		
		/*
		 * 		adding the new message to the messages table
		 */
		
		PreparedStatement insertMessage = mConnection.prepareStatement(Strings.QUERY_INSERT_MSGS);
		try {
			insertMessage.setString(1, m.getFrom()); //set value of first "?" to "Alastair"
			insertMessage.setString(2, m.getMessage());
			Date time = m.getCreationTime();
			insertMessage.setLong(3, time.getTime());
			insertMessage.executeUpdate();
		} finally { //Notice use of finally clause here to finish statement
			insertMessage.close();
		}
		
		mConnection.commit();
		 
	 }
	 
	 public List<RelayMessage> getRecent() throws SQLException {
		 String stmt = Strings.QUERY_SELECT_FIRST_10_MSGS;
		 PreparedStatement recentMessages = mConnection.prepareStatement(stmt);
		 ArrayList<RelayMessage> msgsList = new ArrayList<>();
		 try {
			ResultSet rs = recentMessages.executeQuery();
			try {
				while (rs.next()) {
					String msg = rs.getString(2);
					String sender = rs.getString(1);
					Date time = new Date(rs.getLong(3));
					RelayMessage newMessage = new RelayMessage(sender, msg, time);
					msgsList.add(newMessage);
				}
			} finally {
				rs.close();
			  }
			} finally {
			  recentMessages.close();
		  }
		 return msgsList;

	} 
	


	public static void main(String []args) {
      if (args.length >= 1) {
         try {
            Class.forName("org.hsqldb.jdbcDriver");

            Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:"
                    + args[0], "SA", "");

            Statement delayStmt = connection.createStatement();
            try {
               delayStmt.execute("SET WRITE_DELAY FALSE");
            }  //Always update data on disk
            finally {
               delayStmt.close();
            }

            connection.setAutoCommit(false);


            Statement sqlStmt = connection.createStatement();
            try {
               sqlStmt.execute("CREATE TABLE messages(nick VARCHAR(255) NOT NULL," +
                       "message VARCHAR(4096) NOT NULL,timeposted BIGINT NOT NULL)");
            } catch (SQLException e) {
               System.out.println("Warning: Database table \"messages\" already exists.");
            } finally {
               sqlStmt.close();
            }

            String stmt = "INSERT INTO MESSAGES(nick,message,timeposted) VALUES (?,?,?)";
            PreparedStatement insertMessage = connection.prepareStatement(stmt);
            try {
               insertMessage.setString(1, "Alastair"); //set value of first "?" to "Alastair"
               insertMessage.setString(2, "Hello, Andy");
               insertMessage.setLong(3, System.currentTimeMillis());
               insertMessage.executeUpdate();
            } finally { //Notice use of finally clause here to finish statement
               insertMessage.close();
            }

            connection.commit();

            stmt = "SELECT nick,message,timeposted FROM messages " +
                    "ORDER BY timeposted DESC LIMIT 10";
            PreparedStatement recentMessages = connection.prepareStatement(stmt);
            try {
               ResultSet rs = recentMessages.executeQuery();
               try {
                  while (rs.next())
                     System.out.println(rs.getString(1) + ": " + rs.getString(2) +
                             " [" + rs.getLong(3) + "]");
               } finally {
                  rs.close();
               }
            } finally {
               recentMessages.close();
            }

            connection.close();

         } catch (SQLException e) {
            e.printStackTrace();
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
      } else {
         System.err.println(Strings.WRONG_DATABASE_PATH_MESSAGE);
      }

   }
}
