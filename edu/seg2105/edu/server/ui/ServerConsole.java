package edu.seg2105.edu.server.ui;

import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ClientConsole;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {

	
	/**
	   * The instance of the client that created this ServerConsole.
	   */
	  EchoServer server;
	  
	  final public static int DEFAULT_PORT = 5555;
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 
	
	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
	    try 
	    {
	      server= new EchoServer(port, this);
	      server.listen();
	      
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Closing Server.");
	      System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
	  }
	  
	@Override
	/**
	 * Display the message using the prefix 'SERVER MSG> '
	 * @param String message
	 */
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println("SERVER MSG> " + message);
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }


	  /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }

	    ServerConsole chat;
	    
	    try 
	    {
	    	chat = new ServerConsole(port);
	    	chat.accept();
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients! Error msg: " + ex.getMessage());
	    }
	  }
	}
