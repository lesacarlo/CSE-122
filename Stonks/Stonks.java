//Carlo Lesaca
//07/10/2025
//CSE 122
//P0: Stonks
//TA: Nicole Ham
//this program prompts the user if they would like to buy or sell a selection of
//stock imported as a file
//they also have the option to sell and display their stock porfolio as they please
//finally their entire portfolio is displayed when they are done making their choices

import java.io.*;
import java.util.*;

public class Stonks {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);

        System.out.print("Enter stocks file name: ");
        String fileName = console.next();

        System.out.println();

        Scanner fileInput = new Scanner (new File(fileName));
        String strStocks = fileInput.nextLine();
        int numStocks = Integer.parseInt(strStocks);

        System.out.println("Welcome to the CSE 122 Stocks Simulator!");
        System.out.println("There are " + numStocks + " stocks on the market:");
        
        String tickerArr[] = new String[numStocks];
        double prices[] = new double[numStocks];
        double portfolio[] = new double[numStocks];

        printStocks(fileInput, tickerArr, prices);

        String choice = menuChoice(console);

        while(!choice.equalsIgnoreCase("q")){
            if (choice.equalsIgnoreCase("b")){
                buy(console, tickerArr, portfolio, prices);
            }

            else if (choice.equalsIgnoreCase("se")){
              sell(console, tickerArr, portfolio);

            }

            else if (choice.equalsIgnoreCase("d")){
                display(portfolio, tickerArr, numStocks);
            }

            else if (choice.equalsIgnoreCase("s")){
                save(console, portfolio, tickerArr, numStocks);
            }
            
            else {
                System.out.println("Invalid choice: " + choice);
                System.out.println("Please try again");

            }

            choice = menuChoice(console);
        } 

        totalPortfolio(numStocks, portfolio, prices);
    }

    //Behavior:
    //  -prints the number of stocks in the file that is imported
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  Scanner fileInput to read tickers and prices, 
    //  String array tickerArr to store ticker strings, 
    //  double array prices to store stock prices
    public static void printStocks(Scanner fileInput, String tickerArr[],
                                     double prices[]) throws FileNotFoundException{
        int index = 0;
        fileInput.nextLine();

        while(fileInput.hasNextLine()){
            String line = fileInput.nextLine();

            Scanner lineToken = new Scanner(line);

            tickerArr[index] = lineToken.next(); 
            prices[index] = lineToken.nextDouble();

            System.out.println(tickerArr[index] + ": " + prices[index]); 
            //System.out.println();
            
            index++;
        }
    }

    //Behavior:
    //  -obtains what the user wants to do with their stocks
    //Exceptions:
    //  -none
    //Returns:
    //  -the String value of the user's choice
    //Parameters:
    //  -Scanner console to obtain user input
    public static String menuChoice(Scanner console){
        System.out.println();
        System.out.println("Menu: (B)uy, (Se)ll, (D)isplay, (S)ave, (Q)uit");
        System.out.print("Enter your choice: ");
        String choice = console.next();
        return choice;
    }

    //Behavior:
    //  -processes the user's purchase for a specific stock. The purchase
    //  -is only processed if the user's budget is greater than
    //  -or equal to $5
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  -Scanner console to obtain user input,
    //  -String array tickerArr to obtain stock name,
    //  -double array portfolio to store stock purchases, 
    //  -double array prices to compute number of shares bought
    public static void buy (Scanner console, String tickerArr[],
                             double portfolio[], double prices[]){
        String ticker = findStockTicker(console);

        System.out.print("Enter your budget: "); 
        double budget = console.nextDouble();

        if(budget < 5.0){
            System.out.println("Budget must be at least $5");
        }

        else{
            System.out.println("You successfully bought " + ticker + ".");
            int stockIndex = findStockIndex(ticker, tickerArr);
            portfolio[stockIndex] += budget/prices[stockIndex]; 
        }
    }

    //Behavior:
    //  -processes when the user sells x amount of stock from y company;
    //  only processed if they have enough number of shares to sell
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  -Scanner console to obtain user input, 
    //  -String array tickerArr to obtain stock name,
    //  -double array portfolio to obtain amount of shares owned
    public static void sell (Scanner console, String tickerArr[], double portfolio[]){
        String ticker = findStockTicker(console);
        System.out.print("Enter the number of shares to sell: ");
        double numShares = console.nextDouble();

        int stockIndex = findStockIndex(ticker, tickerArr);

        if (portfolio[stockIndex]>=numShares){
            portfolio[stockIndex] -= numShares;
            System.out.println("You successfully sold " + numShares +
                                    " shares of " + ticker + ".");
        }
        else {
            System.out.println("You do not have enough shares of " + ticker +
                                    " to sell " + numShares + " shares.");
        }
    }

    //Behavior:
    //  -displays the user's portfolio as <stockName> <sharesOwned>.
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  -double array portfolio to obtain number of shares owned, 
    //  -String array tickerArr to obtain stock name,
    //  -int numStocks to navigate portfolio and ticker arrays
    public static void display(double portfolio[], String tickerArr[], int numStocks){
        System.out.println();
        System.out.println("Portfolio:");
        for (int index = 0 ; index < numStocks; index ++){
            if(portfolio[index]>0.0){
                System.out.println(tickerArr[index] + " " + portfolio[index]);
            }
            
        }
    }

    //Behavior:
    //  -saves the user's portfolio into a new file
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  -int numStocks to navigate portfolio and ticker arrays,
    //  -Scanner console to obtain user input,
    //  -String array tickerArr to obtain stock name,
    //  -double array portfolio to obtain number of shares owned
    public static void save(Scanner console, double portfolio[],
                             String tickerArr[], int numStocks) throws FileNotFoundException {
        System.out.print("Enter new portfolio file name: ");
        String saveFile = console.next();

        File outputFile = new File(saveFile);
        PrintStream output = new PrintStream(outputFile);

        for (int index = 0 ; index < numStocks; index ++){
            if(portfolio[index]>0.0){
                output.println(tickerArr[index] + " " + portfolio[index]);
            }
            
        }
        
    }
 
    //Behavior:
    //  -prints the total value of the user's portfolio/net worth
    //Exceptions:
    //  -none
    //Returns:
    //  -none
    //Parameters:
    //  -int numStocks to loop through portfolio and prices arrays
    //  -double array portfolio to obtain number of shares owned
    //  -double array prices to compute the total value of shares owned
    public static void totalPortfolio(int numStocks, double portfolio[], double prices[]){
        double totalPortfolio = 0.0;
        for(int index = 0 ; index < numStocks; index++){
            totalPortfolio += portfolio[index] * prices[index];
        }
        System.out.println("Your portfolio is currently valued at: $" + totalPortfolio);
    }

    //Behavior:
    //  -prompts the user to enter the stock ticker they want to buy or sell
    //Exceptions:
    //  -none
    //Returns:
    //  -the string value of the user's stock ticker
    //Parameters:
    //  -Scanner console to obtain user stock ticker input
    public static String findStockTicker(Scanner console){
        System.out.print("Enter the stock ticker: ");
        String ticker = console.next();
        return ticker;
    }

    //Behavior:
    //  -obtains the index of the stock ticker in the ticker array
    //Exceptions:
    //  -none
    //Returns:
    //  -the index of the stock ticker in the ticker array
    //Parameters:
    //  -String ticker to find in the ticker array,
    //  -String array tickerArr to search for the stock ticker
    public static int findStockIndex(String ticker, String tickerArr[]){
        int index = 0;
        while (!tickerArr[index].equalsIgnoreCase(ticker)){
            index++;
        }
        return index;
    }
}
