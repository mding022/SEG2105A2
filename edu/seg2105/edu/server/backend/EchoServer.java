package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI)  throws IOException
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    serverUI.display("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  serverUI.display(String.format("A new client [%s] has connected to the server.", client.toString()));
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  serverUI.display(String.format("The client [%s] has disconnected from the server.", client.toString()));
  }
  
  public void handleMessageFromServerUI(String message)
  {
	  boolean isCommand = false;
	  
	  if(message.trim().length() >= 5) {
		  if(message.trim().charAt(0) == '#') {
			  isCommand = true;
			  switch(message.trim().substring(1).split(" ")[0]) {
			  
			  case "quit":
				  serverUI.display("Gracefully terminating the server.");
				  quit();
				  break;
				  
			  case "stop":
				  try {
                      this.stopListening();
                      serverUI.display("Server has stopped listening for new connections.");
                  } catch (Exception e) {
                      serverUI.display("Error stopping the server: " + e.getMessage());
                  }
                  break;
                  
			  case "close":
                  try {this.close();} catch(Exception e) {
                	  serverUI.display("Error closing the server: " + e.getMessage());
                  }
                  break;
                  
			  case "setport":
				  if(isListening()) {
					  serverUI.display("Error setting port since server is currently listening for clients.");
				  } else {
					  String[] args = message.trim().split(" ");
					  if(args.length != 2) {
						  serverUI.display(String.format("Error setting port due to invalid user input. \"%s\"", message));
					  } else {
						  setPort(Integer.parseInt(args[1]));
						  serverUI.display(String.format("Successfully set the port to %s", args[1]));
					  }
				  }
				  break;
                  
			  case "start":
                  try {
                      this.listen();
                      serverUI.display("Server is now listening for new connections.");
                  } catch (IOException e) {
                      serverUI.display("Error starting the server: " + e.getMessage());
                  }
                  break;
			  
			  case "getport":
				  if(getPort() > -1) {
					  serverUI.display(String.valueOf(getPort()));
				  }
				  else {
				  serverUI.display("Error getting port of server.");
				  }
				  break;
				  
			  default:
				  serverUI.display("Error - invalid command.");
				  break;
			  }
		  }
	  }
	  
	  try
	    {
	    	if(!isCommand) {
	    		serverUI.display(String.format("Broadcasting '%s' to all connected clients.", message));
	    		this.sendToAllClients(message);
	    	}
	    }
	    catch(Exception e)
	    {
	      serverUI.display
	        ("Could not send message to all clients. Closing the server.");
	      System.exit(0);
	    }
  }
  
  /**
   * Gracefully terminate the server.
   */
  public void quit() {
	  System.exit(0);
  }
}
  
  //Class methods ***************************************************
  

//End of EchoServer class
