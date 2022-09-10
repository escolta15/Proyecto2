package system;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Locale;

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
	
	/*********************************************************************
	*
	* Method name: main
	*
	* Description of the Method: It is the main  method and it executes the program method.
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
	
	public static void program() throws IOException{
		read.useLocale(Locale.US);
		System.out.println("Welcome to our program to buy your tickets for the flight from Madrid to London.");
		
		int rows = readRows();
		int columns = readColumns();
		double ticketPrice = readPrice();
		
		String [] [] currentSeats = new String [rows][columns];
		String [] [] occupiedSeats = new String [rows][columns];
		buildPlane(currentSeats,occupiedSeats);
		
		principal_switch(rows, columns, ticketPrice, occupiedSeats, currentSeats);
	}//end program method.

	
	/*********************************************************************
	*
	* Method name: principal_menu
	*
	* Description of the Method: it shows the menu
	*
	* void
	*
	*********************************************************************/ 
	
	public static void principal_menu() {
		System.out.println("Please, introduce an option:");
		System.out.println("1) Buy tickets.");
		System.out.println("2) Cancel reserved tickets.");
		System.out.println("3) Show available seats.");
		System.out.println("0 or other number to finish.");
	}//end principal_menu method.
	
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
	
	public static void buildPlane(String [][] currentSeats, String [][] occupiedSeats) {
		int row = 0, column = 0;
		char columnLetter = 'A';
		for(row = 0; row < occupiedSeats.length; row++) {
			for(column = 0; column< occupiedSeats[0].length; column++) {
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
				}
			String x = Integer.toString(row + 1);
			String y = Character.toString(columnLetter);
			occupiedSeats[row][column] = x + y;
			currentSeats[row][column] = x + y;
			}
		}
	}//end seatsArrays method.
	
	/*********************************************************************
	*
	* Method name: principal_switch
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
	* @param int availableSeats: it accumulates the number of available seats.
	* @param int option: variable for the program
	* @param int totalClients: it counts all the clients
	* 
	* void
	*
	* @throws IOException
	*
	*********************************************************************/ 
	
	public static void principal_switch(int rows, int columns, double price1Way, String [][] occupiedSeats,String [] [] currentSeats) throws IOException {
		boolean [][] isReturnTickets = new boolean [rows][columns];
		int [][] tickets = new int [rows][columns];
		int [][] clients = new int [rows][columns];
		int [][] suitcases = new int [rows][columns];
		int availableSeats = rows * columns, option = 0, totalClients = 0;
		boolean error = false;
		do{
			try {
				error = false;
				principal_menu();
				option = read.nextInt();
				switch (option){
					case 1:
						totalClients = buyTickets(totalClients, availableSeats, occupiedSeats, rows, clients, tickets, suitcases, isReturnTickets, price1Way, currentSeats);
					break;
					case 2:
						cancelTickets(totalClients, clients, tickets, suitcases, price1Way, availableSeats, occupiedSeats, currentSeats, isReturnTickets);
					break;
					case 3:
						showPlane(occupiedSeats, availableSeats, rows);
					break;
					default:
						writePlane(occupiedSeats);
					break;
				}
			} catch(InputMismatchException inputException) {
				error = true;
				System.out.println("Please, introduce a correct option");
				read.nextLine();
			}
		} while((option > 0 && option <=3) || error == true);
	}//end principal_switch method.
	
	/*********************************************************************
	*
	* Method name: buyTickets
	*
	* Description of the Method: with this method the client can buy the tickets.
	*
	* @param int total_clients: it counts all the clients
	* @param int a: it accumulates the number of available seats.
	* @param int number_tickets: number of the tickets
	* @param int one_way_tickets: number of one-way tickets
	* @param int return_tickets: number of return tickets
	* @param String [][] seats_first: it saves the plane with the occupied seats.
	* @param int F: the number of rows which has the plane.
	* @param int i: rows of the plane
	* @param int j: columns of the plane
	* @param int [][] array_clients: array for the clients
	* @param int [][] array_tickets: array with the tickets
	* @param int [][] array_suitcases: array with the suitcases.
	* @param int nsuitcases: the number of suitcases that the client carries.
	* @param int total_suitcases: the counter of suitcases
	* @param boolean [][] array_return: it controls if the seat is for return ticket.
	* @param double price1way: price of the ticket one-way
	* @param String [] [] seats_second: it saves the current seats.
	* @param int clients_number: number of the clients 
	* @param int option: variable for the program
	* @param int tickets: the counter of the tickets
	* @param double total_price: it is the total and final price.
	*
	* @return Return value: clients_number.
	*
	*********************************************************************/ 
	
	public static int buyTickets(int total_clients,int a,String [][] seats_first,int F,int [][] array_clients,int [][] array_tickets,int [][] array_suitcases,boolean [][] array_return,double price1way,String [] [] seats_second){
		int option=0, i = 0, j = 0, one_way_tickets = 0, number_tickets = 0, return_tickets = 0, clients_number = 0, nsuitcases = 0, total_suitcases=0;
		total_clients++;
		clients_number=total_clients;
		if(a!=0){//1st if
			int tickets=selectTickets(a);//total tickets
			for(number_tickets=1;number_tickets<=tickets;number_tickets++){
				System.out.println("Ticket numbers is " + number_tickets + ":");
				System.out.println("Put 1 if you want a one-way ticket or another number for a return ticket.");//Selection of one-way tickets or return tickets
				option=read.nextInt();
				if(option==1){
					one_way_tickets++;
				}//end if
				else {return_tickets++;
				}//end else
				plane(seats_first);
				do{
					i=chooseRow(F);
					j=chooseColumn();
					if(seats_first [i][j].equals("0")){	
						System.out.println("This seat is occupied.");
					}//end if
				}while(seats_first[i][j].equals("0"));
				seats_first [i][j]="0";
				array_clients [i][j]=clients_number;
				a--;
				nsuitcases=suitcases();//suitcases' counter
				total_suitcases=total_suitcases+nsuitcases;
				if(option==1){//Assignment to array
					array_return[i][j]=false;
				}//end if
				else{array_return[i][j]=true;}//end else
				array_suitcases[i][j]=nsuitcases;
				array_tickets [i][j]=number_tickets;
			}//end for
			double total_price=calculate_total_price (price1way,one_way_tickets,return_tickets,tickets,total_suitcases);//Calculation of the price
			information(clients_number, tickets,array_clients, seats_second, array_tickets, array_suitcases, array_return,clients_number);//Print information
			System.out.println("The total price is: " + total_price);
			System.out.println("------------------------------------------------");
			}//end 1st if
			else{
				System.out.println("The plane is full.");
			}//end else
			//Reload
			total_suitcases=0;
			one_way_tickets=0;
			return_tickets=0;
			return clients_number;
	}//end buyTickets method.
	
	/*********************************************************************
	*
	* Method name: cancelTickets
	*
	* Description of the Method: it permits to cancel the tickets.
	*
	* @param int total_clients: it counts all the clients
	* @param int clients_number: number of the clients 
	* @param int [][] array_clients: array for the clients
	* @param int i: rows of the plane
	* @param int j: columns of the plane
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
	
	public static void cancelTickets(int total_clients,int [][] array_clients,int [][] array_tickets,int [][] array_suitcases,double price1way,int a,String [][] seats_first,String [] [] seats_second,boolean [][] array_return){
		if(total_clients>0){
			int one_way_tickets = 0, return_tickets = 0, clients_number = 0, total_suitcases=0, ctickets=0;
			int tickets=0;
			double total_price=0;
			System.out.println("What number has the client?");
			clients_number=read.nextInt();
			while(clients_number<0 || clients_number>total_clients){
				System.out.println("This client doesn't exist. Please, put the client's number:");
				clients_number=read.nextInt();
			}//end while
			for(int i=0;i<array_clients.length;i++){
				for(int j=0;j<array_clients[0].length;j++){
					if(array_clients[i][j]==clients_number){
						tickets++;
					}//end if
				}//end for
			}//end for
			System.out.println("These are your client's tickets");
			information(clients_number,tickets,array_clients,seats_second,array_tickets,array_suitcases,array_return,clients_number);
			if(tickets>0){
				tickets=tickets+ctickets;
				ctickets=cancel(a,tickets,total_price,price1way,array_clients,seats_first,seats_second,array_tickets, return_tickets, one_way_tickets, total_suitcases, array_suitcases,array_return,clients_number);
				a=a+ctickets;
			}//end if
			else{System.out.println("This client hasn't tickets.");}//end else
		}//end if
		else{System.out.println("There aren't clients, so you can't cancel.");}//end else
	}//end cancelTickets method.
	
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
	
	public static void showPlane(String [][] seats_first, int a,int F){
		plane(seats_first);
		System.out.println("There are available seats");
		System.out.println("Put 1 to check if a seat is free or put other number to continue.");
		int option=read.nextInt();
		if (option==1){
			do{
				int i=chooseRow(F);
				int j=chooseColumn();
				if (seats_first[i][j].equals("0")){
					System.out.println("This seat isn't available.");
				}//end if
				else{System.out.println("This seat is available.");}//end else
				System.out.println("Put 1 to check another seat or put other number to continue.");
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
	
	public static void information(int tickets,int client,int [][]array_clients,String [][]seats_second,int [][]array_tickets,int [][]array_suitcases,boolean [][]array_return, int clients_number){
			int i, j,ticketn;
			for(ticketn=1;ticketn<=100;ticketn++){
				for(i=0;i<seats_second.length;i++){
					for(j=0;j<seats_second[0].length;j++){
						if(array_clients[i][j]==client){
							if(array_tickets [i][j]==ticketn){
								System.out.println("Client's number: "+clients_number);
								System.out.println("Ticket number " + ticketn + ": ");
								System.out.println("Position: "+ seats_second[i][j]+". ");
								if(array_return[i][j]==true){
									System.out.println("Return.");
								}//end if
								else{
									System.out.println("One-way.");}//end else
								System.out.println("Suitcases: "+ array_suitcases[i][j]+".");
							}//end if
						}//end if
					}//end for
				}//end for
			}//end for
	}//end information method	
		
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
	
	public static int cancel(int a,int tickets,double p,double total_price,int [][] user_array, String [][] seats_first,String [][] seats_second,int [][] array_tickets,int return_tickets,int one_way_tickets, int total_suitcase, int [][] array_suitcases,boolean [][]array_return,int client){
			int cancel_tickets=0,i=0,j=0,option=0;
			do{
				System.out.println("Which ticket do you want to cancel?");
				int ticketn=read.nextInt();
				while(ticketn<=0 || ticketn>tickets){
					System.out.println("It cannot be possible. Please, select which ticket you want to cancel:");
					ticketn=read.nextInt();
				}//end while
				for (i=0;i<seats_first.length;i++){
					for (j=0;j<seats_first[0].length;j++){
						if(user_array[i][j]==client){
							if(array_tickets [i][j]==ticketn){
								if(array_return[i][j]==true){
									return_tickets--;
								}//end if
								else{one_way_tickets--;}//end else
								total_suitcase=total_suitcase-array_suitcases[i][j];
								seats_first[i][j]=seats_second[i][j];
								array_tickets[i][j]=0;
								user_array[i][j]=0;
								cancel_tickets++;
							}//end if
						}//end if
					}//end for
				}//end for
				double new_total_price=calculate_total_price(p,tickets,one_way_tickets,return_tickets,total_suitcase);//New total_price
				if (new_total_price==total_price){
					System.out.println("It cannot be possible. You cannot cancel the same ticket twice.");
				}//end if
				else{
					if(total_price>0){
						System.out.println("The total price is: " + new_total_price);
					}//end if
				}//end else
				System.out.println("Put 1 to cancel another ticket or put other number to continue.");
				option=read.nextInt();
				total_price=new_total_price;
			}while (option==1 && cancel_tickets!=tickets);
			if(cancel_tickets==tickets){
				System.out.println("It cannot be possible. You cannot cancel more tickets because you don't have it.");
			}//end if
			return cancel_tickets;
	}//end cancel method.
		
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
	
	public static void plane(String[][] seats_first){
		int i, j;
		System.out.println("The seats with 0 are occupied. The rest are free.");
		for(i=0;i<seats_first.length;i++){
			for(j=0;j<seats_first[0].length;j++){
				System.out.print(seats_first [i][j]+"  ");
			}//end for
			System.out.println("");
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
	
	public static int chooseRow(int F){
		System.out.println("In which row do you want your seat?");
		int i=read.nextInt();
		while(i>F || i<=0) {
			System.out.println("It cannot be possible, it must be between 1 and "+F+". Please, put the row you want your seat:");
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
			System.out.println("In which column do you want your seat: A, B, C or D?");
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
				System.out.println("It cannot be possible. Please, put A, B, C or D:");}//end if
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
			System.out.println("How many suitcases will you carry? (Maximun is 2)");
			suitcases=read.nextInt();
			if(suitcases < 0 || suitcases > 2){
				System.out.println("It cannot be possible. Please, put the number of suitcases that you will carry:");
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
	
	public static int selectTickets (int availableSeats){
		int availables, tickets;
		if(availableSeats >= 10){
			availables = 10;
		} else {
			availables = availableSeats;
		}
		System.out.println("How many tickets do you want?");
		tickets=read.nextInt();
		while (tickets > 10 || tickets <= 0 || tickets > availables){
			System.out.println("It cannot be possible. The number of tickets must be less than "+availables+" or the available seats.");
			tickets = read.nextInt();
		}
		return tickets;
	}//end selectTickets method.
							
	/*********************************************************************
	*
	* Method name: calculate_total_price
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
	
	public static double calculate_total_price (double p,int one_way_tickets,int return_tickets,int tickets,int total_suitcase){
		double discount=0.75,suitcase_price=15,group=6,discount_group=0.8;
		double one_way_price=p*one_way_tickets;
		double return_price=2*p*return_tickets*discount;
		double t_suitcase_price=total_suitcase*suitcase_price;
		double total_price=one_way_price+return_price+t_suitcase_price;
		if (tickets>=group){
			total_price=total_price*discount_group;
		}//end if
		return total_price;
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
	
	public static void writePlane(String[][] seats_first) throws IOException {
		int row, column;
		PrintWriter writer= new PrintWriter(new FileWriter("aircraft.txt"));
		for(row=0;row<seats_first.length;row++){
			for(column=0;column<seats_first[0].length;column++){
				writer.println(seats_first[row][column]);
			}//end for
			System.out.println("");
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
		File f = new File("data.txt");
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
		File f = new File("data.txt");
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
		File f = new File("data.txt");
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
