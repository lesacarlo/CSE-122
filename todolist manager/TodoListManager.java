//Carlo Lesaca
//07/17/2025
//CSE 122
//P1: To-do List Manager
//TA: Nicole Ham
/* this program allows a user to create a to-do list
 * the user can add to the list,
 * mark items as done,
 * load items into the to do list from a file,
 * save the to do list to a file,
 * and quit the program.
 * When the extension flag is set to true,
 * the user is allowed to mark multiple items as done
 * by entering a range of items
 */

import java.io.*;
import java.util.*;

public class TodoListManager {
    public static final boolean EXTENSION_FLAG = true;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        List<String> todos = new ArrayList<>();

        System.out.println("Welcome to your TODO List Manager!");
        System.out.println("What would you like to do?");
        System.out.print("(A)dd TODO, (M)ark TODO as done,"
                                 + " (L)oad TODOs, (S)ave TODOs, (Q)uit? ");
        String choice = console.nextLine();
    
        while(!choice.equalsIgnoreCase("q")){

            if(choice.equalsIgnoreCase("A")){
                addItem(console, todos);
            }

            else if(choice.equalsIgnoreCase("M")){
                markItemAsDone(console, todos);   
            }

            else if(choice.equalsIgnoreCase("L")){
                loadItems(console, todos);     
            }

            else if(choice.equalsIgnoreCase("S")){
               saveItems(console, todos);
            }

            else{
                System.out.println("Unknown input: " + choice);
            }
            
            printTodos(todos);

            System.out.println("What would you like to do?");
            System.out.print("(A)dd TODO, (M)ark TODO as done,"
                                 + " (L)oad TODOs, (S)ave TODOs, (Q)uit? ");
            choice = console.nextLine();
        }
    }

     /* 
     * B: This method prints the current to do list. If the to do list is empty,
     *      it prints a message saying that it is empty.
     * E: none
     * R: none
     * P: Array List of to do items to print.
     */
    public static void printTodos(List<String> todos) {
        System.out.println("Today's TODOs:");
        if(todos.size()==0){
            System.out.println("  You have nothing to do yet today! Relax!");
        }

        else{
            for(int i = 0; i < todos.size() ; i++){
                System.out.println("  " + (i+1) + ": " + todos.get(i));
            }
        }
    }
    
     /* 
     * B: This method adds an item to the to do list. If the user adds more than
     *      one item, it asks the user where in the list they would like to add it.
     * E: none
     * R: none
     * P: Scanner console to read user's added item, Array List to add items into.
     */
    public static void addItem(Scanner console, List<String> todos) {
        System.out.print("What would you like to add? ");
        String addition = console.nextLine();
        
        if(todos.size()==0){
            todos.add(addition); 
        }
        
        else{
            System.out.print("Where in the list should it be (1-"
                             + (todos.size()+1) + ")? (Enter for end): ");
            String listOrder = console.nextLine();

            if(listOrder.equals("")){
                todos.add(addition);
            }

            else{
                todos.add(Integer.parseInt(listOrder) -1 , addition);  
            }
        }
    }

     /* 
     * B: This method marks an item by done by removing it from the to do list. If
     *      the to do list is empty, it prints a message saying 
     *      that there is nothing to mark as done.
     *      If the extension flag is set to true, it allows the user to mark multiple
     *      items as done by entering a range of values.
     * E: Throws an IllegalStateException if the user's end range is greater than
     *      the todo list, and if the users starting range is a value 
     *      greater than the ending range
     * R: none
     * P: Scanner console to obtain specific item(s) to mark as done,
     *      Array List of to do list items to remove from.
     */
    public static void markItemAsDone(Scanner console, List<String> todos) {
        if(todos.size() == 0){
            System.out.println("All done! Nothing left to mark as done!");
        }
        else if(EXTENSION_FLAG){
            System.out.print("What is the first item you completed (1-" + todos.size() + ")? ");
            int start = Integer.parseInt(console.nextLine());
            System.out.print("What is the last item you completed (1-" + todos.size() + ")? ");
            int end = Integer.parseInt(console.nextLine());

            // created a new conditional for the exception that is not
            // if(todos.size() == 0), as keeping a conditional that is
            // if(todos.size() == 0) would not run as there is an if(todos.size() == 0)
            // in line 130.
            // Kept this conditional after the user prompts because I need user input
            // to run this conditional. I would have put the original conditional above the 
            // user prompts IF my conditional was "if(todos.size() == 0){"
            if (end > todos.size() || start > end) {
                throw new IllegalArgumentException("Invalid range for marking items as done.");
            }

            //removed else branch after exception 
            for(int i = end -1 ; i >= start -1; i--){
                todos.remove(i);
            }
        }  
        else{
            System.out.print("Which item did you complete (1-" + todos.size() + ")? ");
            int completedIndex = Integer.parseInt(console.nextLine());
            todos.remove(completedIndex -1);
        }
    }

     /* 
     * B: this method loads in items from a file to add to the to do list.
     * E: none
     * R: none
     * P: Scanner console to read file name, Array List of items to load into.
     */
    public static void loadItems(Scanner console, List<String> todos)
                                throws FileNotFoundException {
        System.out.print("File name? ");
        Scanner fileScan = new Scanner(new File(console.nextLine()));
        
        //removed unncecessary arraylist
        todos.clear();
        while(fileScan.hasNextLine()){
            todos.add(fileScan.nextLine());
        }
    }

     /* 
     * B: This method saves the current to do list to a file.
     * E: If the to do list is empty, an IllegalStateException is thrown.
     * R: none
     * P: Scanner console to read file name, Array List of items to save to file.
     */
    public static void saveItems(Scanner console, List<String> todos)
                                throws FileNotFoundException {
        if(todos.size() == 0){
            throw new IllegalStateException();
        }
        
        //removed else branch after exception 
        System.out.print("File name? ");
        PrintStream out = new PrintStream(new File (console.nextLine()));
        for(int i = 0; i < todos.size() ; i++){
            out.println(todos.get(i));
        }
    }
}