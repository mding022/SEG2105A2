// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  boolean isCommand = false;
	  
	  if(message.trim().length() >= 5) {
		  if(message.trim().charAt(0) == '#') {
			  isCommand = true;
			  switch(message.trim().substring(1).split(" ")[0]) {
			  
			  case "quit":
				  quit();
				  break;
				  
			  case "logoff":
				  try {
					clientUI.display("You will now be logged off from the server.");
				    closeConnection();
				  } catch (IOException e) {
				    clientUI.display("Error while logging off: " + e.getMessage());
				  }
				  break;

			  case "sethost":
				  if(isConnected()) {
					  clientUI.display("Error setting host since client is already logged on.");
				  } else {
					  String[] args = message.trim().split(" ");
					  if(args.length != 2) {
						  clientUI.display(String.format("Error setting host due to invalid user input. \"%s\"", message));
					  } else {
						  setHost(args[1]);
						  clientUI.display(String.format("Successfully set the host to %s", args[1]));
					  }
				  }
				  break;
				  
			  case "setport":
				  if(isConnected()) {
					  clientUI.display("Error setting port since client is already logged on.");
				  } else {
					  String[] args = message.trim().split(" ");
					  if(args.length != 2) {
						  clientUI.display(String.format("Error setting port due to invalid user input. \"%s\"", message));
					  } else {
						  setPort(Integer.parseInt(args[1]));
						  clientUI.display(String.format("Successfully set the port to %s", args[1]));
					  }
				  }
				  break;
			  
			  case "login":
				  if(isConnected()) {
					  clientUI.display("Error logging in since client is already logged in to the server.");
				  } else {
					  try {
						  openConnection();
					  } catch (IOException e) {
						  clientUI.display("Error logging in to the server with exception: " + e.getMessage());
					  }
				  }
				  break;
				  
			  case "gethost":
				  String hostMsg = getHost() != null ? getHost() : "Error getting server host since host is null.";
				  clientUI.display(hostMsg);
				  break;
				  
			  case "getport":
				  if(getPort() > -1) {
					  clientUI.display(String.valueOf(getPort()));
				  } else {
					  clientUI.display("Error getting port of server.");
				  }
				  break;
				  
				  default:
					  clientUI.display("Error - invalid command.");
					  break;
			  }
		  }
	  }
	  
    try
    {
    	if(!isCommand) {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method overrides the connectionClosed method in AbstractClient and sends a message
   */
  protected void connectionClosed() {
	  clientUI.display("The connection to the server has been successfully closed.");
  }
  
  
  protected void connectionException(Exception exception) {
	  clientUI.display("The server connection was lost, and the client will be terminated.");
	  quit();
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
