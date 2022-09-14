package system;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/*********************************************************************
*
* Class Name: FlightLaMancha
* @author Author/s name: Pablo Mora Herreros
* Release/Creation date: 28/08/2022
* Class version: 1.0.0
* Class description: it simulates a program which buys or cancels tickets for flights.
*
**********************************************************************
*/ 

public class FlightLaMancha {

	static Scanner read=new Scanner(System.in);
	static Logger logger = Logger.getLogger(FlightLaMancha.class.getName());
	static final String INPUT_DATA = "data.txt";
	
	private int rows;
	private double ticketPrice;
	private int availableSeats;
	private String [] [] currentSeats;
	private String [] [] occupiedSeats;
	private boolean [][] isReturnTickets;
	private int [][] tickets;
	private int [][] clients;
	private int [][] suitcases;
	private int totalClients;
	
	/*********************************************************************
	*
	* Method name: constructor
	*
	* Description of the Method: It is the constructor
	* 
	* @param int availableSeats: it accumulates the number of available seats.
	*
	* void
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public FlightLaMancha(int rows, int columns, double ticketPrice) {
		this.rows = rows;
		this.ticketPrice = ticketPrice;
		this.availableSeats = rows * columns;
		this.currentSeats = new String [rows][columns];
		this.occupiedSeats = new String [rows][columns];
		this.isReturnTickets = new boolean [rows][columns];
		this.tickets = new int [rows][columns];
		this.clients = new int [rows][columns];
		this.suitcases = new int [rows][columns];
		this.totalClients = 0;
	}
	
	/*********************************************************************
	*
	* Method name: main
	*
	* Description of the Method: It is the main method and it executes the program method.
	*
	* void
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public static void main(String[] args) throws IOException {
		program();
	}//end main method.
	
	/*********************************************************************
	*
	* Method name: program
	*
	* Description of the Method: It does all the functions of the main method.
	*
	* @param int columns: number of columns.
	* @param double ticketPrice: price of the ticket one-way
	* @param double rows: number of rows
	* @param String [] [] currentSeats: it saves the current seats.
	* @param String [] [] occupiedSeats: it saves the plane with the occupied seats.
	*
	* void
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public static void program() throws IOException {
		read.useLocale(Locale.US);
		logger.log(Level.INFO, "Welcome to our program to buy your tickets for the flight from Madrid to London.");
		int rows = readRows();
		int columns = readColumns();
		double ticketPrice = readPrice();
		FlightLaMancha flight = new FlightLaMancha(rows, columns, ticketPrice);
		buildPlane(flight);
		principalSwitch(flight);
	}//end program method.

	
	/*********************************************************************
	*
	* Method name: principalMenu
	*
	* Description of the Method: it shows the menu
	*
	* void
	*
	*********************************************************************/ 
	
	public static void principalMenu() {
		logger.log(Level.INFO, "Please, introduce an option:");
		logger.log(Level.INFO, "1) Buy tickets.");
		logger.log(Level.INFO, "2) Cancel reserved tickets.");
		logger.log(Level.INFO, "3) Show available seats.");
		logger.log(Level.INFO, "0 or other number to finish.");
	}
	
	/*********************************************************************
	*
	* Method name: buildPlane
	*
	* Description of the Method: it forms the plane with its seats
	*
	* @param String [][] currentSeats: it saves the current seats.
	* @param String [][] occupiedSeats: it saves the plane with the occupied seats.
	* @param int row: rows of the plane
	* @param int column: columns of the plane
	* @param char columnLetter: is to put the columns with letters.
	* 
	* void
	*
	*********************************************************************/ 
	
	public static void buildPlane(FlightLaMancha flight) {
		char columnLetter;
		for(int row = 0; row < flight.occupiedSeats.length; row++) {
			for(int column = 0; column < flight.occupiedSeats[0].length; column++) {
				switch (column){
					case 0:
						columnLetter = 'A';
					break;
					case 1:
						columnLetter = 'B';
					break;
					case 2:
						columnLetter = 'C';
					break;
					case 3:
						columnLetter = 'D';
					break;
					default:
						columnLetter = '-';
					break;
				}
				String x = Integer.toString(row + 1);
				String y = Character.toString(columnLetter);
				flight.occupiedSeats[row][column] = x + y;
				flight.currentSeats[row][column] = x + y;
			}
		}
	}//end seatsArrays method.
	
	/*********************************************************************
	*
	* Method name: principalSwitch
	*
	* Description of the Method: it manages and controls the program.
	*
	* @param int rows: the number of rows which has the plane.
	* @param int columns: the number of columns which has the plane.
	* @param double price1Way: price of the ticket one-way
	* @param String [][] occupiedSeats: it saves the plane with the occupied seats.
	* @param String [][] currentSeats: it saves the current seats.
	* @param boolean [][] isReturnTickets: it controls if the seat is for return ticket.
	* @param int [][] tickets: array with the tickets
	* @param int [][] clients: array for the clients
	* @param int [][] suitcases: array with the suitcases.
	* @param int option: variable for the program
	* @param int totalClients: it counts all the clients
	* 
	* void
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public static void principalSwitch(FlightLaMancha flight) throws IOException {
		int option = 0;
		boolean error = false;
		do{
			try {
				error = false;
				principalMenu();
				option = read.nextInt();
				switch (option){
					case 1:
						buyTickets(flight);
					break;
					case 2:
						cancelTickets(flight);
					break;
					case 3:
						showPlane(flight);
					break;
					default:
						writePlane(flight);
					break;
				}
			} catch(InputMismatchException inputException) {
				error = true;
				logger.log(Level.WARNING, "Please, introduce a correct option");
				read.nextLine();
			}
		} while((option > 0 && option <=3) || error);
	}//end principalSwitch method.
	
	/*********************************************************************
	*
	* Method name: buyTickets
	*
	* Description of the Method: with this method the client can buy the tickets.
	*
	* @param int total_clients: it counts all the clients
	* @param int a: it accumulates the number of available seats.
	* @param int one_way_tickets: number of one-way tickets
	* @param int return_tickets: number of return tickets
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param int F: the number of rows which has the plane.
	* @param int [][] array_tickets: array with the tickets
	* @param int [][] array_suitcases: array with the suitcases.
	* @param int total_suitcases: the counter of suitcases
	* @param boolean [][] array_return: it controls if the seat is for return ticket.
	* @param double price1way: price of the ticket one-way
	* @param String [] [] seats_second: it saves the current seats.
	* @param int clients_number: number of the clients
	* @param int tickets: the counter of the tickets
	* @param double total_price: it is the total and final price.
	*
	* @return Return value: total_clients.
	*
	*********************************************************************/ 
	
	public static void buyTickets(FlightLaMancha flight){
		flight.totalClients++;
		int clientsNumber = flight.totalClients;
		if(flight.availableSeats != 0){
			int tickets=selectTickets(flight);//total tickets
			double totalPrice = chooseTickets(flight, tickets, clientsNumber);
			information(flight, clientsNumber, tickets, clientsNumber);//Print information
			logger.log(Level.INFO, "The total price is: {0}", totalPrice);
			logger.log(Level.INFO, "------------------------------------------------");
		} else {
			logger.log(Level.WARNING, "The plane is full.");
		}
	}//end buyTickets method.
	
	/*********************************************************************
	*
	* Method name: chooseTickets
	*
	* Description of the Method: with this method the client can choose the tickets.
	*
	* @param int tickets: the counter of the tickets
	* @param int one_way_tickets: number of one-way tickets
	* @param int return_tickets: number of return tickets
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param int F: the number of rows which has the plane.
	* @param int clients_number: number of the clients
	* @param int a: it accumulates the number of available seats.
	* @param int total_suitcases: the counter of suitcases
	* @param int [][] array_tickets: array with the tickets
	* @param int [][] array_suitcases: array with the suitcases.
	* @param boolean [][] array_return: it controls if the seat is for return ticket.
	* 
	* @param int option: variable for the program
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int nsuitcases: the number of suitcases that the client carries.
	* @param int number_tickets: number of the tickets
	*
	* void
	*
	*********************************************************************/ 
	
	public static double chooseTickets(FlightLaMancha flight, int tickets, int clientsNumber) {
		int oneWayTickets = 0;
		int returnTickets = 0;
		int totalSuitcases = 0;
		int option = 0;
		int i = 0;
		int j = 0;
		int nsuitcases = 0;
		for(int numberTickets = 1; numberTickets <= tickets; numberTickets++){
			logger.log(Level.INFO, "Ticket numbers is {0}", numberTickets);
			logger.log(Level.INFO, "Put 1 if you want a one-way ticket or another number for a return ticket.");
			option=read.nextInt();
			if(option == 1) oneWayTickets++;
			else returnTickets++;
			plane(flight);
			do {
				i = chooseRow(flight);
				j = chooseColumn();
				if(flight.occupiedSeats [i][j].equals("0")){	
					logger.log(Level.WARNING, "This seat is occupied.");
				}
			} while(flight.occupiedSeats[i][j].equals("0"));
			flight.occupiedSeats [i][j]="0";
			flight.clients [i][j] = clientsNumber;
			flight.availableSeats--;
			nsuitcases=suitcases();//suitcases' counter
			totalSuitcases += nsuitcases;
			if (option == 1) flight.isReturnTickets[i][j] = false;
			else flight.isReturnTickets[i][j] = true;
			flight.suitcases[i][j]=nsuitcases;
			flight.tickets [i][j]=numberTickets;
		}//end for
		return calculateTotalPrice(flight, oneWayTickets, returnTickets, tickets, totalSuitcases);
	}
	
	/*********************************************************************
	*
	* Method name: cancelTickets
	*
	* Description of the Method: it permits to cancel the tickets.
	*
	* @param int total_clients: it counts all the clients
	* @param int clients_number: number of the clients 
	* @param int [][] array_clients: array for the clients
	* @param int ctickets: number of tickets which have been cancelled
	* @param int [][] array_tickets: array with the tickets
	* @param int [][] array_suitcases: array with the suitcases.
	* @param double price1way: price of the ticket one-way
	* @param int a: it accumulates the number of available seats.
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param String [] [] seats_second: it saves the current seats.
	* @param boolean [][] array_return: it controls if the seat is for return ticket.
	* @param int return_tickets: number of return tickets
	* @param int total_suitcases: the counter of suitcases
	* @param int one_way_tickets: number of one-way tickets
	* @param int tickets: the counter of the tickets
	* @param double total_price: it is the total and final price.
	*
	* void
	*
	*********************************************************************/ 
	
	public static void cancelTickets(FlightLaMancha flight){
		if(flight.totalClients > 0){
			int clientsNumber = getClientsNumber(flight);
			int tickets = countClientTickets(flight, clientsNumber);
			logger.log(Level.INFO, "These are your tickets");
			information(flight, clientsNumber,tickets,clientsNumber);
			if(tickets>0){
				cancel(flight, tickets, clientsNumber);
			} else {
				logger.log(Level.WARNING, "This client has not tickets.");
			}
		} else {
			logger.log(Level.WARNING, "There are not clients, so you cannot cancel.");	
		}
	}
	
	/*********************************************************************
	*
	* Method name: getClientsNumber
	*
	* Description of the Method: it obtains the number of the client.
	*
	* @param int total_clients: it counts all the clients
	* @param int clients_number: number of the clients 
	*
	* @return Return value: clients_number
	*
	*********************************************************************/ 
	
	public static int getClientsNumber(FlightLaMancha flight) {
		logger.log(Level.INFO, "What number has the client?");
		int clientsNumber = read.nextInt();
		while(clientsNumber < 0 || clientsNumber > flight.totalClients){
			logger.log(Level.WARNING, "This client does not exist. Please, put the correct number:");
			clientsNumber=read.nextInt();
		}
		return clientsNumber;
	}
	
	/*********************************************************************
	*
	* Method name: countClientTickets
	*
	* Description of the Method: it obtains the number client's tickets.
	*
	* @param int [][] array_clients: array for the clients
	* @param int clients_number: number of the clients 
	* @param int tickets: the counter of the tickets
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	*
	* @return Return value: tickets
	*
	*********************************************************************/ 
	
	public static int countClientTickets(FlightLaMancha flight, int clientsNumber) {
		int tickets = 0;
		for(int i = 0; i < flight.clients.length; i++){
			for(int j = 0; j < flight.clients.length; j++){
				if(flight.clients[i][j] == clientsNumber){
					tickets++;
				}
			}
		}
		return tickets;
	}
	
	/*********************************************************************
	*
	* Method name: showPlane
	*
	* Description of the Method: it shows the plane when the client wants to see it.
	*
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param int a: it accumulates the number of available seats.
	* @param int F: the number of rows which has the plane.
	* @param int option: variable for the program
	*
	* void
	*
	*********************************************************************/ 
	
	public static void showPlane(FlightLaMancha flight){
		plane(flight);
		logger.log(Level.INFO, "There are available seats");
		logger.log(Level.INFO, "Put 1 to check if a seat is free or put other number to continue.");
		int option=read.nextInt();
		if (option==1){
			do{
				int i=chooseRow(flight);
				int j=chooseColumn();
				if (flight.occupiedSeats[i][j].equals("0")){
					logger.log(Level.WARNING, "This seat is not available.");
				} else{
					logger.log(Level.INFO, "This seat is available.");
				}
				logger.log(Level.INFO, "Put 1 to check another seat or put other number to continue.");
				option=read.nextInt();
			}while(option==1);
		}//end if
	}//end showPlane method.
	
	/*********************************************************************
	*
	* Method name: information
	*
	* Description of the Method: It calculates and prints all the information of the order.
	*
	* @param int tickets: the counter of the tickets
	* @param int client: number of each client
	* @param int [][]array_clients: array for the clients
	* @param String [][]seats_second: it saves the current seats.
	* @param int [][]array_tickets: array with the tickets
	* @param int [][]array_suitcases: array with the suitcases.
	* @param boolean [][]array_return: it controls if the seat is for return ticket.
	* @param int clients_number: number of the clients
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int ticketn: id of the ticket
	*
	* void
	*
	*********************************************************************/ 
	
	public static void information(FlightLaMancha flight, int tickets,int client, int clientsNumber){
			for(int ticketn=1;ticketn <= tickets; ticketn++){
				for(int i=0;i<flight.currentSeats.length;i++){
					for(int j=0;j<flight.currentSeats[0].length;j++){
						if(flight.clients[i][j]==client){
							showTicketInformation(flight, i, j, ticketn, clientsNumber);
						}
					}
				}
			}
	}
	
	/*********************************************************************
	*
	* Method name: showTicketInformation
	*
	* Description of the Method: It shows the information of a ticket.
	* 
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int ticketn: id of the ticket
	* @param int [][]array_tickets: array with the tickets
	* @param int clients_number: number of the clients
	* @param String [][]seats_second: it saves the current seats.
	* @param int [][]array_suitcases: array with the suitcases.
	* @param boolean [][]array_return: it controls if the seat is for return ticket.
	*
	* void
	*
	*********************************************************************/ 
	
	public static void showTicketInformation(FlightLaMancha flight, int i, int j, int ticketn, int clientsNumber) {
		if(flight.tickets [i][j] == ticketn){
			logger.log(Level.INFO, "Number of the client: {0}", clientsNumber);
			logger.log(Level.INFO, "Ticket number {0}: ", ticketn);
			logger.log(Level.INFO, "Position: {0}.", flight.currentSeats[i][j]);
			if(flight.isReturnTickets[i][j]){
				logger.log(Level.INFO, "Return.");
			} else {
				logger.log(Level.INFO, "One-way.");
			}
			logger.log(Level.INFO, "Suitcases: {0}.", flight.suitcases[i][j]);
		}//end if
	}
		
	/*********************************************************************
	*
	* Method name: cancel
	*
	* Description of the Method: it cancels the tickets that client wants
	*
	* @param int a: it accumulates the number of available seats.
	* @param int tickets: the counter of the tickets
	* @param double p: price of the ticket one-way
	* @param double total_price: it is the total and final price.
	* @param int [][] user_array: array for the clients (== array_clients)
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param String [][] seats_second: it saves the current seats.
	* @param int [][] array_tickets: array with the tickets
	* @param int return_tickets: number of return tickets
	* @param int one_way_tickets: number of one-way tickets
	* @param int total_suitcase: the counter of suitcases
	* @param int [][] array_suitcases: array with the suitcases.
	* @param boolean [][]array_return: it controls if the seat is for return ticket.
	* @param int client: number of each client
	* @param int cancel_tickets: number of tickets which have been cancelled
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int option: variable for the program
	* @param int ticketn: id of the ticket
	* @param int new_total_price: it update the price
	*
	* @return Return value: cancel_tickets
	*
	*********************************************************************/ 
	
	public static void cancel(FlightLaMancha flight, int tickets, int client){
			int cancelTickets = 0;
			double totalPrice = 0;
			int option = 0;
			do{
				int ticketn = getTicketToCancel(tickets);
				double newtotalPrice = removeTicket(flight, tickets, client, totalPrice, ticketn);
				if (totalPrice != newtotalPrice) {
					cancelTickets++;
					totalPrice = newtotalPrice;
				}
				logger.log(Level.INFO, "Put 1 to cancel another ticket or put other number to continue.");
				option=read.nextInt();
			} while (option==1 && cancelTickets!=tickets);
			if(cancelTickets == tickets){
				logger.log(Level.WARNING, "It cannot be possible. You cannot cancel more tickets because you do not have it.");
			}//end if
			flight.availableSeats += cancelTickets;
	}//end cancel method.
	
	private static double recalculatePrice(FlightLaMancha flight, int tickets, double totalPrice, int oneWayTickets, int returnTickets, int totalSuitcase) {
		double newTotalPrice = calculateTotalPrice(flight, tickets, oneWayTickets, returnTickets, totalSuitcase);
		if (newTotalPrice == totalPrice){
			logger.log(Level.WARNING, "It cannot be possible. You cannot cancel the same ticket twice.");
		} else {
			if(totalPrice>0){
				logger.log(Level.INFO, "The total price is: {0}", newTotalPrice);
			}
		}
		return newTotalPrice;
	}
	
	/*********************************************************************
	*
	* Method name: removeTicket
	*
	* Description of the Method: it removes the ticket that client wants
	*
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int [][] array_tickets: array with the tickets
	* @param int ticketn: id of the ticket
	* @param boolean [][]array_return: it controls if the seat is for return ticket.
	* @param int return_tickets: number of return tickets
	* @param int one_way_tickets: number of one-way tickets
	* @param int total_suitcase: the counter of suitcases
	* @param int [][] array_suitcases: array with the suitcases.
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param String [][] seats_second: it saves the current seats.
	* @param int [][] user_array: array for the clients (== array_clients)
	* @param int cancel_tickets: number of tickets which have been cancelled
	*
	* void
	*
	*********************************************************************/ 
	
	public static double removeTicket(FlightLaMancha flight, int tickets, int client, double totalPrice, int ticketn) {
		int oneWayTickets = 0;
		int returnTickets = 0;
		int totalSuitcase = 0;
		for (int i = 0; i < flight.occupiedSeats.length; i++){
			for (int j = 0; j < flight.occupiedSeats[0].length; j++){
				if(flight.clients[i][j] == client && flight.tickets [i][j] == ticketn){
					if(flight.isReturnTickets[i][j]) returnTickets--;
					else oneWayTickets--;
					totalSuitcase -= flight.suitcases[i][j];
					flight.occupiedSeats[i][j]=flight.currentSeats[i][j];
					flight.tickets[i][j]=0;
					flight.clients[i][j]=0;
				}//end if
			}//end for
		}//end for
		return recalculatePrice(flight, tickets, totalPrice, oneWayTickets, returnTickets, totalSuitcase);
	}
	
	/*********************************************************************
	*
	* Method name: cancel
	*
	* Description of the Method: it cancels the tickets that client wants
	*
	* @param int tickets: the counter of the tickets
	* @param int ticketn: id of the ticket
	*
	* @return Return value: ticketn
	*
	*********************************************************************/ 
	
	public static int getTicketToCancel(int tickets) {
		logger.log(Level.INFO, "Which ticket do you want to cancel?");
		int ticketn = read.nextInt();
		while(ticketn <= 0 || ticketn > tickets){
			logger.log(Level.WARNING, "It cannot be possible. Please, select which ticket you want to cancel:");
			ticketn=read.nextInt();
		}
		return ticketn;
	}
		
	/*********************************************************************
	*
	* Method name: plane
	*
	* Description of the Method: it shows the current state of the plane.
	*
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	*
	* void
	*
	*********************************************************************/ 
	
	public static void plane(FlightLaMancha flight){
		logger.log(Level.INFO, "The seats with 0 are occupied. The rest are free.");
		for(int i=0;i<flight.occupiedSeats.length;i++){
			for(int j=0;j<flight.occupiedSeats[0].length;j++){
				logger.log(Level.INFO, "{0} ", flight.occupiedSeats [i][j]);
			}//end for
			logger.log(Level.INFO, "");
		}//end for
	}//end plane method.
	
	/*********************************************************************
	*
	* Method name: chooseRow
	*
	* Description of the Method: the client chooses the row for his seat.
	*
	* @param int i: the row
	* @param int F: the number of rows which has the plane.
	*
	* @return Return value: i
	*
	*********************************************************************/ 
	
	public static int chooseRow(FlightLaMancha flight){
		logger.log(Level.INFO, "In which row do you want your seat?");
		int i=read.nextInt();
		while(i>flight.rows || i<=0) {
			logger.log(Level.WARNING, "It cannot be possible, it must be between 1 and \"+F+\". Please, put the row you want your seat:");
			i=read.nextInt();
		}//end while
		i--;
		return i;
	}//end chooseRow method.
	
	/*********************************************************************
	*
	* Method name: chooseColumn
	*
	* Description of the Method: the client chooses the column for his seat.
	*
	* @param int j: the column
	* @param int z: variable for the switch to choose the column.
	*
	* @return Return value: j
	*
	*********************************************************************/ 
	
	public static int chooseColumn(){
		int j;
		do{
			logger.log(Level.INFO, "In which column do you want your seat: A, B, C or D?");
			String z=read.next();
			switch (z){
			case "A":
				j=0;
			break;
			case "B":
				j=1;
			break;
			case "C":
				j=2;
			break;
			case "D":
				j=3;
			break;
			default:
				j=4;
			break;}//end switch
			if(j==4){
				logger.log(Level.WARNING, "It cannot be possible. Please, put A, B, C or D:");
			}
		}while (j==4);
		return j;
	}//end chooseColumns method.
	
	/*********************************************************************
	*
	* Method name: suitcases
	*
	* Description of the Method: To know the number of suitcases that the client carries
	*
	* @param int suitcases: the number of suitcases that the client carries.
	*
	* @return Return value: suitcases
	*
	*********************************************************************/ 
	
	public static int suitcases(){
		int suitcases;
		do {
			logger.log(Level.INFO, "How many suitcases will you carry? (Maximun is 2)");
			suitcases=read.nextInt();
			if(suitcases < 0 || suitcases > 2){
				logger.log(Level.WARNING, "It cannot be possible. Please, put the number of suitcases that you will carry:");
				read.nextLine();
			}
		} while (suitcases < 0 || suitcases > 2);
		return suitcases;
	}//end suitcases method.
	
	/*********************************************************************
	*
	* Method name: selectTickets
	*
	* Description of the Method: To know the number of tickets that the client wants
	*
	* @param int availables: it is the maximun number of tickets that the client can buy, in this case 10.
	* @param int availableSeats: it accumulates the number of available seats.
	* @param int tickets: the number of tickets that the client wants to buy.
	*
	* @return Return value: tickets
	*
	*********************************************************************/ 
	
	public static int selectTickets (FlightLaMancha flight){
		int availables;
		int tickets;
		if(flight.availableSeats >= 10){
			availables = 10;
		} else {
			availables = flight.availableSeats;
		}
		logger.log(Level.INFO, "How many tickets do you want?");
		tickets=read.nextInt();
		while (tickets > 10 || tickets <= 0 || tickets > availables){
			logger.log(Level.WARNING, "It cannot be possible. The number of tickets must be less than \"+availables+\" or the available seats.");
			tickets = read.nextInt();
		}
		return tickets;
	}//end selectTickets method.
							
	/*********************************************************************
	*
	* Method name: calculateTotalPrice
	*
	* Description of the Method: it calculates the final price.
	*
	* @param double p: price of the ticket one-way
	* @param int one_way_tickets: number of one-way tickets
	* @param int return_tickets: number of return tickets
	* @param int tickets: the counter of the tickets
	* @param int total_suitcase: the counter of suitcases
	* @param double discount: this is the discount for the return tickets
	* @param double suitcase_price: the price of one suitcase
	* @param double group: the number to be a group
	* @param double discount_group: the discount for the groups
	* @param double one_way_price: it accumulates the price for the one-way tickets
	* @param double return_price: it accumulates the price for the return tickets
	* @param double t_suitcase_price: it accumulates the price for the suitcases
	* @param double total_price: it is the total and final price.
	*
	* @return Return value: total_price
	*
	*
	*********************************************************************/ 
	
	public static double calculateTotalPrice(FlightLaMancha flight, int tickets, int oneWayTickets,int returnTickets,int totalSuitcase){
		double discount=0.75;
		double suitcasePrice=15;
		double group=6;
		double discountGroup=0.8;
		double oneWayPrice=flight.ticketPrice*oneWayTickets;
		double returnPrice=2*flight.ticketPrice*returnTickets*discount;
		double tSuitcasePrice=totalSuitcase*suitcasePrice;
		double totalPrice=oneWayPrice+returnPrice+tSuitcasePrice;
		if (tickets>=group){
			totalPrice=totalPrice*discountGroup;
		}//end if
		return totalPrice;
	}//end calculate_total_price method.
	
	/*********************************************************************
	*
	* Method name: writePlane
	*
	* Description of the Method: this method writes the current plane in the file "aircraft.txt"
	*
	* @param int row: index of rows of the plane
	* @param int column: index of columns of the plane
	* @param PrintWriter writer: aircraft.txt
	* @param seats_first: it saves the plane with the occupied seats.
	*
	* void
	*
	* aircraft.txt
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public static void writePlane(FlightLaMancha flight) throws IOException {
		PrintWriter writer= new PrintWriter(new FileWriter("aircraft.txt"));
		for(int row=0;row<flight.occupiedSeats.length;row++){
			for(int column=0;column<flight.occupiedSeats[0].length;column++){
				writer.println(flight.occupiedSeats[row][column]);
			}//end for
		}//end for
	    writer.close();
	}//end writePlane method.
	
	/*********************************************************************
	*
	* Method name: readRows
	*
	* Description of the Method: this method read the file "data.txt" and it returns the rows.
	*
	* @param File f: data.txt
	* @param int rows: number of rows
	* @param Scanner read: f
	*
	* @return Return value: rows
	*
	* data.txt
	*
	* @throws FileNotFoundException
	*
	*********************************************************************/ 
	
	private static int readRows() throws FileNotFoundException {
		File f = new File(INPUT_DATA);
		int rows = 0;
		Scanner read = new Scanner(f);
		read.useLocale(Locale.ENGLISH);
		while(read.hasNext()){
			rows = read.nextInt();
			read.nextInt();
			read.nextDouble();
		}
		read.close();
		return rows;
	}//end readRows method.
	
	/*********************************************************************
	*
	* Method name: readColumns
	*
	* Description of the Method: this method read the file "data.txt" and it returns the columns.
	*
	* @param File f: data.txt
	* @param int columns: number of columns
	* @param Scanner read: f
	*
	* @return Return value: columns
	*
	* data.txt
	*
	* @throws FileNotFoundException
	*
	*********************************************************************/ 
	
	private static int readColumns() throws FileNotFoundException {
		File f = new File(INPUT_DATA);
		int columns = 0;
		Scanner read = new Scanner(f);
		read.useLocale(Locale.ENGLISH);
		while(read.hasNext()){
			read.nextInt();
			columns = read.nextInt();
			read.nextDouble();
		}
		read.close();
		return columns;
	}//end readColumns method.
	
	/*********************************************************************
	*
	* Method name: readPrice
	*
	* Description of the Method: this method read the file "data.txt" and it returns the price.
	*
	* @param File f: data.txt
	* @param double price: price of the ticket one-way
	* @param Scanner read: f
	*
	* @return Return value: price
	*
	* data.txt
	*
	* @throws FileNotFoundException
	*
	*********************************************************************/ 
	
	private static double readPrice () throws FileNotFoundException {
		File f = new File(INPUT_DATA);
		double price = 0;
		Scanner read = new Scanner(f);
		read.useLocale(Locale.ENGLISH);
		while(read.hasNext()){
			read.nextInt();
			read.nextInt();
			price = read.nextDouble();
		}
		read.close();
		return price;
	}//end readPrice method.
	
}//end FlightLaMancha class.
