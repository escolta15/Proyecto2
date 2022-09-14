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
* Class version: 1.0.1
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
	* @param int row: rows of the plane
	* @param int column: columns of the plane
	* @param double ticketPrice: price of one ticket
	*
	* void
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
	}
	
	/*********************************************************************
	*
	* Method name: program
	*
	* Description of the Method: It does all the functions of the main method.
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
	}

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
	* @param FlightLaMancha flight: it has all the properties of a flight
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
	}
	
	/*********************************************************************
	*
	* Method name: principalSwitch
	*
	* Description of the Method: it manages and controls the program.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
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
		} while((option > 0 && option <= 3) || error);
	}
	
	/*********************************************************************
	*
	* Method name: buyTickets
	*
	* Description of the Method: with this method the client can buy the tickets.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	*
	* void
	*
	*********************************************************************/ 
	
	public static void buyTickets(FlightLaMancha flight){
		flight.totalClients++;
		int clientsNumber = flight.totalClients;
		if(flight.availableSeats != 0){
			int tickets = selectTickets(flight);
			double totalPrice = chooseTickets(flight, tickets, clientsNumber);
			information(flight, clientsNumber, tickets, clientsNumber);
			logger.log(Level.INFO, "The total price is: {0}", totalPrice);
			logger.log(Level.INFO, "------------------------------------------------");
		} else {
			logger.log(Level.WARNING, "The plane is full.");
		}
	}
	
	/*********************************************************************
	*
	* Method name: chooseTickets
	*
	* Description of the Method: with this method the client can choose the tickets.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param int clientsNumber: number of the clients
	*
	* @return Return value: totalPrice
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
			nsuitcases=getSuitcases();
			totalSuitcases += nsuitcases;
			if (option == 1) flight.isReturnTickets[i][j] = false;
			else flight.isReturnTickets[i][j] = true;
			flight.suitcases[i][j]=nsuitcases;
			flight.tickets [i][j]=numberTickets;
		}
		return calculateTotalPrice(flight, oneWayTickets, returnTickets, tickets, totalSuitcases);
	}
	
	/*********************************************************************
	*
	* Method name: cancelTickets
	*
	* Description of the Method: it permits to cancel the tickets.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
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
	* @param FlightLaMancha flight: it has all the properties of a flight
	*
	* @return Return value: clientsNumber
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
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int clientsNumber: number of the clients
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
	* @param FlightLaMancha flight: it has all the properties of a flight
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
		}
	}
	
	/*********************************************************************
	*
	* Method name: information
	*
	* Description of the Method: It calculates and prints all the information of the order.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param int client: number of each client
	* @param int clientsNumber: number of the clients
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
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int ticketn: id of the ticket
	* @param int clientsNumber: number of the clients
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
		}
	}
		
	/*********************************************************************
	*
	* Method name: cancel
	*
	* Description of the Method: it cancels the tickets that client wants
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param int client: number of each client
	*
	* void
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
			}
			flight.availableSeats += cancelTickets;
	}
	
	/*********************************************************************
	*
	* Method name: recalculatePrice
	*
	* Description of the Method: it recalculates the total price
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param double totalPrice: it is the total and final price.
	* @param int oneWayTickets: number of one-way tickets.
	* @param int returnTickets: number of return tickets
	* @param int totalSuitcase: the counter of suitcases
	*
	* @return Return value: newTotalPrice
	*
	*********************************************************************/ 
	
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
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param int client: number of each client
	* @param double totalPrice: it is the total and final price.
	* @param int ticketn: id of the ticket
	*
	* @return Return value: newTotalPrice
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
				}
			}
		}
		return recalculatePrice(flight, tickets, totalPrice, oneWayTickets, returnTickets, totalSuitcase);
	}
	
	/*********************************************************************
	*
	* Method name: getTicketToCancel
	*
	* Description of the Method: it selects ticket to cancel
	*
	* @param int tickets: the counter of the tickets
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
	* @param FlightLaMancha flight: it has all the properties of a flight
	*
	* void
	*
	*********************************************************************/ 
	
	public static void plane(FlightLaMancha flight){
		logger.log(Level.INFO, "The seats with 0 are occupied. The rest are free.");
		for(int i=0;i<flight.occupiedSeats.length;i++){
			for(int j=0;j<flight.occupiedSeats[0].length;j++){
				logger.log(Level.INFO, "{0} ", flight.occupiedSeats [i][j]);
			}
			logger.log(Level.INFO, "");
		}
	}
	
	/*********************************************************************
	*
	* Method name: chooseRow
	*
	* Description of the Method: the client chooses the row for his seat.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
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
		}
		i--;
		return i;
	}
	
	/*********************************************************************
	*
	* Method name: chooseColumn
	*
	* Description of the Method: the client chooses the column for his seat.
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
				break;
			}
			if(j==4){
				logger.log(Level.WARNING, "It cannot be possible. Please, put A, B, C or D:");
			}
		}while (j==4);
		return j;
	}
	
	/*********************************************************************
	*
	* Method name: getSuitcases
	*
	* Description of the Method: To know the number of suitcases that the client carries
	*
	* @return Return value: suitcases
	*
	*********************************************************************/ 
	
	public static int getSuitcases(){
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
	}
	
	/*********************************************************************
	*
	* Method name: selectTickets
	*
	* Description of the Method: To know the number of tickets that the client wants
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
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
	}
							
	/*********************************************************************
	*
	* Method name: calculateTotalPrice
	*
	* Description of the Method: it calculates the final price.
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
	* @param int tickets: the counter of the tickets
	* @param int oneWayTickets: number of one-way tickets
	* @param int returnTickets: number of return tickets
	* @param int totalSuitcase: the counter of suitcases
	*
	* @return Return value: totalPrice
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
		}
		return totalPrice;
	}
	
	/*********************************************************************
	*
	* Method name: writePlane
	*
	* Description of the Method: this method writes the current plane in the file "aircraft.txt"
	*
	* @param FlightLaMancha flight: it has all the properties of a flight
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
			}
		}
	    writer.close();
	}
	
	/*********************************************************************
	*
	* Method name: readRows
	*
	* Description of the Method: this method read the file "data.txt" and it returns the rows.
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
	}
	
	/*********************************************************************
	*
	* Method name: readColumns
	*
	* Description of the Method: this method read the file "data.txt" and it returns the columns.
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
	}
	
	/*********************************************************************
	*
	* Method name: readPrice
	*
	* Description of the Method: this method read the file "data.txt" and it returns the price.
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
	}
	
}
