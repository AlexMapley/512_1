import ResInterface.*;
import ResImpl.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RMISecurityManager;

import java.util.*;
import java.io.*;


public class client
{
	static String message = "blank";
	static ResourceManager rm = null;
	static int port = 5959;

	public static void main(String args[])
	{
		client obj = new client();
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String command = "";
		Vector arguments  = new Vector();
		int Id, Cid;
		int flightNum;
		int flightPrice;
		int flightSeats;
		boolean Room;
		boolean Car;
		int price;
		int numRooms;
		int numCars;
		String location;
		boolean help;
		boolean shutdown;
		boolean started;
		String clientNum;
		long startTime;
		long endTime;

		//initialize supporting variables
		Id = 0;
		startTime = -1;
		endTime = -1;
		clientNum = "-1";
		started = false;

		String server = "localhost";

		if (args.length != 2)
		{
			System.out.println ("Usage: java client [rmihost] [clientnumber]");
			System.exit(1);
		}
		else {
			server = args[0];
			clientNum = args[1];
		}

		try
		{
			// get a reference to the rmiregistry
			Registry registry = LocateRegistry.getRegistry(server, port);
			// get the proxy and the remote reference by rmiregistry lookup
			rm = (ResourceManager) registry.lookup("group_21");
			try {
				rm.getBanner();
				System.out.println("\nSuccessful");
				System.out.println("Connected to RM at [" + server + "]");
			} catch (Exception e)
			{
				System.out.println("Unsuccessful connection to MW");
				System.exit(0);
			}
			// make call on remote method
		}
		catch (Exception e)
		{
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}



		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}


		System.out.println("\n\nClient Interface: !@ <( 'o')> <('O' )> @! ");
		System.out.println("Type \"help\" for list of supported commands");
		while(true) {
			shutdown = false;
			help = false;
			System.out.print("\n>");
			try{
				//read the next command
				command = stdin.readLine();
			}
			catch (IOException io){
				System.out.println("Unable to read from standard in");
				System.exit(1);
			}
			//remove heading and trailing white space
			command=command.trim();
			arguments=obj.parse(command);
			int commandCase = obj.findChoice((String)arguments.elementAt(0));

			//Help and shutdown cases
			if(commandCase == 1) {
				help = true;
				if(arguments.size()==1)   //command was "help"
					  obj.listCommands();
					else if (arguments.size()==2)  //command was "help <commandname>"
					  obj.listSpecific((String)arguments.elementAt(1));
					else  //wrong use of help command
					  System.out.println("Improper use of help command. Type help or help, <commandname>");
			}
			else if(commandCase == 23) {
				shutdown = true;
			}

			//Start Transaction error cases
			if(!help && !shutdown) {
				if(commandCase == 24) {
					if(started) { // Transaction has already been started
						System.out.println("A new transaction cannot be started until you finish the current one");
						System.out.println("Call commit/abort to proceed");
						continue;
					}
				}
				else {
					if(!started) { // A transaction hasn't been started yet
						System.out.println("Transaction has not been started");
						System.out.println("Call start to proceed");
						continue;
					}
				}
			}

			//decide which of the commands this was
			switch(commandCase){
			case 1: //help section
				break;

			case 2:  //new flight
				if(arguments.size()!=5){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Adding a new Flight using id: "+Id);
				System.out.println("Flight number: "+arguments.elementAt(2));
				System.out.println("Add Flight Seats: "+arguments.elementAt(3));
				System.out.println("Set Flight Price: "+arguments.elementAt(4));

				try{

				  flightNum = obj.getInt(arguments.elementAt(2));
				  flightSeats = obj.getInt(arguments.elementAt(3));
				  flightPrice = obj.getInt(arguments.elementAt(4));

				  if(rm.addFlight(Id,flightNum,flightSeats,flightPrice))
					System.out.println("Flight added");
				  else
					System.out.println("Flight could not be added");
				} catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				  break;
				}
				break;

			case 3:  //new Car
				if(arguments.size()!=5){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Adding a new Car using id: "+Id);
				System.out.println("Car Location: "+arguments.elementAt(2));
				System.out.println("Add Number of Cars: "+arguments.elementAt(3));
				System.out.println("Set Price: "+arguments.elementAt(4));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  numCars = obj.getInt(arguments.elementAt(3));
				  price = obj.getInt(arguments.elementAt(4));
				  if(rm.addCars(Id,location,numCars,price))
					System.out.println("Cars added");
				  else
					System.out.println("Cars could not be added");
				}

				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 4:  //new Room
				if(arguments.size()!=5){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Adding a new Room using id: "+Id);
				System.out.println("Room Location: "+arguments.elementAt(2));
				System.out.println("Add Number of Rooms: "+arguments.elementAt(3));
				System.out.println("Set Price: "+arguments.elementAt(4));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  numRooms = obj.getInt(arguments.elementAt(3));
				  price = obj.getInt(arguments.elementAt(4));
				  if(rm.addRooms(Id,location,numRooms,price))
					System.out.println("Rooms added");
				  else
					System.out.println("Rooms could not be added");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 5:  //new Customer
				if(arguments.size()!=2){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Adding a new Customer using id:"+Id);

				try{

				  int customer=rm.newCustomer(Id);
				  System.out.println("new customer id:"+customer);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 6: //delete Flight
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Deleting a flight using id: "+Id);
				System.out.println("Flight Number: "+arguments.elementAt(2));

				try{

				  flightNum = obj.getInt(arguments.elementAt(2));
				  if(rm.deleteFlight(Id,flightNum))
					System.out.println("Flight Deleted");
				  else
					System.out.println("Flight could not be deleted");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 7: //delete Car
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Deleting the cars from a particular location  using id: "+Id);
				System.out.println("Car Location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));

				  if(rm.deleteCars(Id,location))
					System.out.println("Cars Deleted");
				  else
					System.out.println("Cars could not be deleted");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 8: //delete Room
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Deleting all rooms from a particular location  using id: "+Id);
				System.out.println("Room Location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  if(rm.deleteRooms(Id,location))
					System.out.println("Rooms Deleted");
				  else
					System.out.println("Rooms could not be deleted");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 9: //delete Customer
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Deleting a customer from the database using id: "+Id);
				System.out.println("Customer id: "+arguments.elementAt(2));

				try{

				  int customer = obj.getInt(arguments.elementAt(2));
				  if(rm.deleteCustomer(Id,customer))
					System.out.println("Customer Deleted");
				  else
					System.out.println("Customer could not be deleted");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 10: //querying a flight
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a flight using id: "+Id);
				System.out.println("Flight number: "+arguments.elementAt(2));

				try{

				  flightNum = obj.getInt(arguments.elementAt(2));
				  int seats=rm.queryFlight(Id,flightNum);
				  System.out.println("Number of seats available:"+seats);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 11: //querying a Car Location
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a car location using id: "+Id);
				System.out.println("Car location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  numCars=rm.queryCars(Id,location);
				  System.out.println("number of Cars at this location:"+numCars);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 12: //querying a Room location
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a room location using id: "+Id);
				System.out.println("Room location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  numRooms=rm.queryRooms(Id,location);
				  System.out.println("number of Rooms at this location:"+numRooms);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 13: //querying Customer Information
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying Customer information using id: "+Id);
				System.out.println("Customer id: "+arguments.elementAt(2));

				try{

				  int customer = obj.getInt(arguments.elementAt(2));
				  String bill=rm.queryCustomerInfo(Id,customer);
				  System.out.println("Customer info:"+bill);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 14: //querying a flight Price
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a flight Price using id: "+Id);
				System.out.println("Flight number: "+arguments.elementAt(2));

				try{

				  flightNum = obj.getInt(arguments.elementAt(2));
				  price=rm.queryFlightPrice(Id,flightNum);
				  System.out.println("Price of a seat:"+price);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 15: //querying a Car Price
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a car price using id: "+Id);
				System.out.println("Car location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  price=rm.queryCarsPrice(Id,location);
				  System.out.println("Price of a car at this location:"+price);
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 16: //querying a Room price
				if(arguments.size()!=3){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Querying a room price using id: "+Id);
				System.out.println("Room Location: "+arguments.elementAt(2));

				try{

				  location = obj.getString(arguments.elementAt(2));
				  price=rm.queryRoomsPrice(Id,location);
				  System.out.println("Price of Rooms at this location:"+price);
				}
				catch(Exception e){
				 rebind(server);
				 System.out.println("Please enter your command again");
				}
				break;

			case 17:  //reserve a flight
				if(arguments.size()!=4){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Reserving a seat on a flight using id: "+Id);
				System.out.println("Customer id: "+arguments.elementAt(2));
				System.out.println("Flight number: "+arguments.elementAt(3));

				try{

				  int customer = obj.getInt(arguments.elementAt(2));
				  flightNum = obj.getInt(arguments.elementAt(3));
				  if(rm.reserveFlight(Id,customer,flightNum))
					System.out.println("Flight Reserved");
				  else
					System.out.println("Flight could not be reserved.");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 18:  //reserve a car
				if(arguments.size()!=4){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Reserving a car at a location using id: "+Id);
				System.out.println("Customer id: "+arguments.elementAt(2));
				System.out.println("Location: "+arguments.elementAt(3));

				try{

				  int customer = obj.getInt(arguments.elementAt(2));
				  location = obj.getString(arguments.elementAt(3));
				  if(rm.reserveCar(Id,customer,location))
					System.out.println("Car Reserved");
				  else
					System.out.println("Car could not be reserved.");
				}
				catch(Exception e){
				  rebind(server);
				  System.out.println("Please enter your command again");
				}
				break;

			case 19:  //reserve a room
				if(arguments.size()!=4){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Reserving a room at a location using id: "+Id);
				System.out.println("Customer id: "+arguments.elementAt(2));
				System.out.println("Location: "+arguments.elementAt(3));

				try{

				  int customer = obj.getInt(arguments.elementAt(2));
				  location = obj.getString(arguments.elementAt(3));
				  if(rm.reserveRoom(Id,customer,location))
					System.out.println("Room Reserved");
				  else
					System.out.println("Room could not be reserved.");
				}
				catch(Exception e){
				 	rebind(server);
				 	System.out.println("Please enter your command again");
				}
				break;

			case 20:  //reserve an Itinerary
				if(arguments.size()<7){
				 	obj.wrongNumber();
				 	break;
				}
				System.out.println("Reserving an Itinerary using id:"+Id);
				System.out.println("Customer id:"+arguments.elementAt(2));

				for(int i=0;i<arguments.size()-6;i++)
				  System.out.println("Flight number"+arguments.elementAt(3+i));
				System.out.println("Location for Car/Room booking:"+arguments.elementAt(arguments.size()-3));
				System.out.println("Car to book?:"+arguments.elementAt(arguments.size()-2));
				System.out.println("Room to book?:"+arguments.elementAt(arguments.size()-1));

				try{

					int customer = obj.getInt(arguments.elementAt(2));
				 	Vector<Integer> flightNumbers = new Vector<>();
				 	for(int i=0;i<arguments.size()-6;i++) {
						flightNumbers.addElement(Integer.parseInt((String) arguments.elementAt(3+i)));

				  	}
				 	location = obj.getString(arguments.elementAt(arguments.size()-3));
					Car = obj.getBoolean(arguments.elementAt(arguments.size()-2));
				 	Room = obj.getBoolean(arguments.elementAt(arguments.size()-1));

				 	if(rm.itinerary(Id,customer,flightNumbers,location,Car,Room))
						System.out.println("Itinerary Reserved");
				 	else
						System.out.println("Itinerary could not be reserved.");
				}
				catch(Exception e){
				 	rebind(server);
				 	System.out.println("Please enter your command again");
				}
				break;

			case 21:  //quit the client
				if(arguments.size()!=1){
				  obj.wrongNumber();
				  break;
				}
				System.out.println("Quitting client.");
				System.exit(1);


			case 22:  //new Customer given id
				if(arguments.size()!=3){
					obj.wrongNumber();
					break;
				}
				System.out.println("Adding a new Customer using id:"+Id + " and cid " +arguments.elementAt(2));

				try{

					Cid = obj.getInt(arguments.elementAt(2));
					boolean customer=rm.newCustomer(Id,Cid);
					System.out.println("new customer id:"+Cid);
				}
				catch(Exception e){
					rebind(server);
					System.out.println("Please enter your command again");
				}
				break;

			case 23: // shutdown
				shutdown = true;
				System.out.println("Calling shutdown at Middleware. Will restart if no active transactions");
				try {
					rm.shutdown();
				}
				catch(Exception e) {
					rebind(server);
					System.out.println("Please enter your command again");
				}
				break;

			case 24: // start
				try {

					// Crash Case for Testing
					if (arguments.size() == 2) {
						int useCase = obj.getInt(arguments.elementAt(1));
						System.out.println("\nCrash Case: " + useCase);
						Id = rm.start(0, useCase);
					}
					// Normal Use Case
					else {
						Id = rm.start(0, 0);
					}
					started = true;
					System.out.println("\nStarting Transaction " + Id + "\n");
				} catch(Exception e){
					rebind(server);
					System.out.println("Please enter your command again");
				}
				break;

			case 25: // commit
				System.out.println("Attempting to Commit Transaction " + Id);
				try {
					boolean commitWorthy = rm.commit(Id);
					if (commitWorthy) {
						System.out.println("Transaction " + Id + " Committed Successfully");
					}
					else {
						System.out.println("Either Transaction " + Id + " had nothing to commit, or a halted commit has been finished");
					}
					started = false;
				} catch (Exception e) {
					rebind(server);
					System.out.println("Please enter your command again");
				}
				break;

			case 26:
				System.out.println("Attempting to Abort Transaction " + Id);
				try {
					rm.abort(Id);
					started = false;
				} catch(Exception e) {
					rebind(server);
				  	System.out.println("Please enter your command again");
				}
				break;

			default:
				System.out.println("The interface does not support this command.");
				break;
			}//end of switch

		}//end of while(true)
	}

	public Vector parse(String command)
	{
	Vector arguments = new Vector();
	StringTokenizer tokenizer = new StringTokenizer(command,",");
	String argument ="";
	while (tokenizer.hasMoreTokens())
		{
		argument = tokenizer.nextToken();
		argument = argument.trim();
		arguments.add(argument);
		}
	return arguments;
	}
	public int findChoice(String argument)
	{
	if (argument.compareToIgnoreCase("help")==0)
		return 1;
	else if(argument.compareToIgnoreCase("newflight")==0)
		return 2;
	else if(argument.compareToIgnoreCase("newcar")==0)
		return 3;
	else if(argument.compareToIgnoreCase("newroom")==0)
		return 4;
	else if(argument.compareToIgnoreCase("newcustomer")==0)
		return 5;
	else if(argument.compareToIgnoreCase("deleteflight")==0)
		return 6;
	else if(argument.compareToIgnoreCase("deletecar")==0)
		return 7;
	else if(argument.compareToIgnoreCase("deleteroom")==0)
		return 8;
	else if(argument.compareToIgnoreCase("deletecustomer")==0)
		return 9;
	else if(argument.compareToIgnoreCase("queryflight")==0)
		return 10;
	else if(argument.compareToIgnoreCase("querycar")==0)
		return 11;
	else if(argument.compareToIgnoreCase("queryroom")==0)
		return 12;
	else if(argument.compareToIgnoreCase("querycustomer")==0)
		return 13;
	else if(argument.compareToIgnoreCase("queryflightprice")==0)
		return 14;
	else if(argument.compareToIgnoreCase("querycarprice")==0)
		return 15;
	else if(argument.compareToIgnoreCase("queryroomprice")==0)
		return 16;
	else if(argument.compareToIgnoreCase("reserveflight")==0)
		return 17;
	else if(argument.compareToIgnoreCase("reservecar")==0)
		return 18;
	else if(argument.compareToIgnoreCase("reserveroom")==0)
		return 19;
	else if(argument.compareToIgnoreCase("itinerary")==0)
		return 20;
	else if (argument.compareToIgnoreCase("quit")==0)
		return 21;
	else if (argument.compareToIgnoreCase("newcustomerid")==0)
		return 22;
	else if (argument.compareToIgnoreCase("shutdown")==0)
		return 23;
	else if (argument.compareToIgnoreCase("start")==0)
		return 24;
	else if (argument.compareToIgnoreCase("commit")==0)
		return 25;
	else if (argument.compareToIgnoreCase("abort")==0)
		return 26;
	else
		return 666;

	}

	public void listCommands()
	{
	  System.out.println("\nWelcome to the client interface provided to test your project.");
	  System.out.println("Commands accepted by the interface are:");
	  System.out.println("help");
	  System.out.println("start\ncommit\nabort");
	  System.out.println("newflight\nnewcar\nnewroom\nnewcustomer\nnewcusomterid\ndeleteflight\ndeletecar\ndeleteroom");
	  System.out.println("deletecustomer\nqueryflight\nquerycar\nqueryroom\nquerycustomer");
	  System.out.println("queryflightprice\nquerycarprice\nqueryroomprice");
	  System.out.println("reserveflight\nreservecar\nreserveroom\nitinerary");
	  System.out.println("quit");
	  System.out.println("\ntype help, <commandname> for detailed info(NOTE the use of comma).");
	}


	public void listSpecific(String command)
	{
	System.out.print("Help on: ");
	switch(findChoice(command))
		{
		case 1:
		System.out.println("Help");
		System.out.println("\nTyping help on the prompt gives a list of all the commands available.");
		System.out.println("Typing help, <commandname> gives details on how to use the particular command.");
		break;

		case 2:  //new flight
		System.out.println("Adding a new Flight.");
		System.out.println("Purpose:");
		System.out.println("\tAdd information about a new flight.");
		System.out.println("\nUsage:");
		System.out.println("\tnewflight,<id>,<flightnumber>,<flightSeats>,<flightprice>");
		break;

		case 3:  //new Car
		System.out.println("Adding a new Car.");
		System.out.println("Purpose:");
		System.out.println("\tAdd information about a new car location.");
		System.out.println("\nUsage:");
		System.out.println("\tnewcar,<id>,<location>,<numberofcars>,<pricepercar>");
		break;

		case 4:  //new Room
		System.out.println("Adding a new Room.");
		System.out.println("Purpose:");
		System.out.println("\tAdd information about a new room location.");
		System.out.println("\nUsage:");
		System.out.println("\tnewroom,<id>,<location>,<numberofrooms>,<priceperroom>");
		break;

		case 5:  //new Customer
		System.out.println("Adding a new Customer.");
		System.out.println("Purpose:");
		System.out.println("\tGet the system to provide a new customer id. (same as adding a new customer)");
		System.out.println("\nUsage:");
		System.out.println("\tnewcustomer,<id>");
		break;


		case 6: //delete Flight
		System.out.println("Deleting a flight");
		System.out.println("Purpose:");
		System.out.println("\tDelete a flight's information.");
		System.out.println("\nUsage:");
		System.out.println("\tdeleteflight,<id>,<flightnumber>");
		break;

		case 7: //delete Car
		System.out.println("Deleting a Car");
		System.out.println("Purpose:");
		System.out.println("\tDelete all cars from a location.");
		System.out.println("\nUsage:");
		System.out.println("\tdeletecar,<id>,<location>,<numCars>");
		break;

		case 8: //delete Room
		System.out.println("Deleting a Room");
		System.out.println("\nPurpose:");
		System.out.println("\tDelete all rooms from a location.");
		System.out.println("Usage:");
		System.out.println("\tdeleteroom,<id>,<location>,<numRooms>");
		break;

		case 9: //delete Customer
		System.out.println("Deleting a Customer");
		System.out.println("Purpose:");
		System.out.println("\tRemove a customer from the database.");
		System.out.println("\nUsage:");
		System.out.println("\tdeletecustomer,<id>,<customerid>");
		break;

		case 10: //querying a flight
		System.out.println("Querying flight.");
		System.out.println("Purpose:");
		System.out.println("\tObtain Seat information about a certain flight.");
		System.out.println("\nUsage:");
		System.out.println("\tqueryflight,<id>,<flightnumber>");
		break;

		case 11: //querying a Car Location
		System.out.println("Querying a Car location.");
		System.out.println("Purpose:");
		System.out.println("\tObtain number of cars at a certain car location.");
		System.out.println("\nUsage:");
		System.out.println("\tquerycar,<id>,<location>");
		break;

		case 12: //querying a Room location
		System.out.println("Querying a Room Location.");
		System.out.println("Purpose:");
		System.out.println("\tObtain number of rooms at a certain room location.");
		System.out.println("\nUsage:");
		System.out.println("\tqueryroom,<id>,<location>");
		break;

		case 13: //querying Customer Information
		System.out.println("Querying Customer Information.");
		System.out.println("Purpose:");
		System.out.println("\tObtain information about a customer.");
		System.out.println("\nUsage:");
		System.out.println("\tquerycustomer,<id>,<customerid>");
		break;

		case 14: //querying a flight for price
		System.out.println("Querying flight.");
		System.out.println("Purpose:");
		System.out.println("\tObtain price information about a certain flight.");
		System.out.println("\nUsage:");
		System.out.println("\tqueryflightprice,<id>,<flightnumber>");
		break;

		case 15: //querying a Car Location for price
		System.out.println("Querying a Car location.");
		System.out.println("Purpose:");
		System.out.println("\tObtain price information about a certain car location.");
		System.out.println("\nUsage:");
		System.out.println("\tquerycarprice,<id>,<location>");
		break;

		case 16: //querying a Room location for price
		System.out.println("Querying a Room Location.");
		System.out.println("Purpose:");
		System.out.println("\tObtain price information about a certain room location.");
		System.out.println("\nUsage:");
		System.out.println("\tqueryroomprice,<id>,<location>");
		break;

		case 17:  //reserve a flight
		System.out.println("Reserving a flight.");
		System.out.println("Purpose:");
		System.out.println("\tReserve a flight for a customer.");
		System.out.println("\nUsage:");
		System.out.println("\treserveflight,<id>,<customerid>,<flightnumber>");
		break;

		case 18:  //reserve a car
		System.out.println("Reserving a Car.");
		System.out.println("Purpose:");
		System.out.println("\tReserve a given number of cars for a customer at a particular location.");
		System.out.println("\nUsage:");
		System.out.println("\treservecar,<id>,<customerid>,<location>,<nummberofCars>");
		break;

		case 19:  //reserve a room
		System.out.println("Reserving a Room.");
		System.out.println("Purpose:");
		System.out.println("\tReserve a given number of rooms for a customer at a particular location.");
		System.out.println("\nUsage:");
		System.out.println("\treserveroom,<id>,<customerid>,<location>,<nummberofRooms>");
		break;

		case 20:  //reserve an Itinerary
		System.out.println("Reserving an Itinerary.");
		System.out.println("Purpose:");
		System.out.println("\tBook one or more flights.Also book zero or more cars/rooms at a location.");
		System.out.println("\nUsage:");
		System.out.println("\titinerary,<id>,<customerid>,<flightnumber1>....<flightnumberN>,<LocationToBookCarsOrRooms>,<NumberOfCars>,<NumberOfRoom>");
		break;


		case 21:  //quit the client
		System.out.println("Quitting client.");
		System.out.println("Purpose:");
		System.out.println("\tExit the client application.");
		System.out.println("\nUsage:");
		System.out.println("\tquit");
		break;

		case 22:  //new customer with id
			System.out.println("Create new customer providing an id");
			System.out.println("Purpose:");
			System.out.println("\tCreates a new customer with the id provided");
			System.out.println("\nUsage:");
			System.out.println("\tnewcustomerid, <id>, <customerid>");
			break;

		case 23:
			System.out.println("Call shutdown with no argument");
			System.out.println("Purpose: Attempts to restart middleware if no active transactions in any RM");
			System.out.println("\nUsage:");
			System.out.println("\tshutdown");
			break;

		case 24:
			System.out.println("Start a transaction");
			System.out.println("Purpose: asks the Middlware for a transaction id");
			System.out.println("\nUsage:");
			System.out.println("\tstart");
			break;

		case 25:
			System.out.println("Commit a transaction");
			System.out.println("Purpose: commit the transaction currently started at this client");
			System.out.println("\nUsage:");
			System.out.println("\tcommit");
			break;

		case 26:
			System.out.println("Abort a transaction");
			System.out.println("Purpose: abort the transaction currently started at this client");
			System.out.println("\nUsage:");
			System.out.println("\tabort");
			break;

		default:
		System.out.println(command);
		System.out.println("The interface does not support this command.");
		break;
		}
	}

	public void wrongNumber() {
	System.out.println("The number of arguments provided in this command are wrong.");
	System.out.println("Type help, <commandname> to check usage of this command.");
	}

	public int getInt(Object temp) throws Exception {
	try {
		return (new Integer((String)temp)).intValue();
		}
	catch(Exception e) {
		throw e;
		}
	}

	public boolean getBoolean(Object temp) throws Exception {
		try {
			return (new Boolean((String)temp)).booleanValue();
			}
		catch(Exception e) {
			throw e;
			}
	}

	public String getString(Object temp) throws Exception {
	try {
		return (String)temp;
		}
	catch (Exception e) {
		throw e;
		}
	}

	public static void rebind(String hostname) {
	    System.out.println("Attempting reconnect with Middleware...");
	    int i = 0;
	    while(true) {
	        i++;
	        try {
	            Registry registry = LocateRegistry.getRegistry(hostname, port);
	            rm = (ResourceManager) registry.lookup("group_21");

	            rm.getBanner(); // ping mw

	            System.out.println("MW successfully reconnected!");
	            return;
	        } catch (Exception e) {
	            System.out.print(".");
	            if(i % 5 == 0)
	                System.out.println("");
	            try {
	                Thread.sleep(3000); // sleep for 3 seconds
	            } catch(InterruptedException ee) {

	            }
	        }
	    }
	}
}
