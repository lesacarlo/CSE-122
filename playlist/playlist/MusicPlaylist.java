// Carlo Lesaca
// 07/24/25
// CSE 123
// P1: MusicPlaylist
// TA: Nicole Ham
// This program lets a user play music just like spotify.
// They also have the ability to view and add to their que, and 
// view, clear, or delete parts of their listening history.
import java.util.*;
public class MusicPlaylist {
    public static void main (String [] args){
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 122 Music Playlist!");
        Queue<String> playlist = new LinkedList<String>();
        Stack<String> history = new Stack<String>();
        Stack<String> auxStack = new Stack<String>();
        String userChoice = menu(console);

        while(!userChoice.equalsIgnoreCase("Q")){
            if(userChoice.equalsIgnoreCase("A")){
                addSong(console, playlist);
            }
            else if (userChoice.equalsIgnoreCase("P")) {
                playSong(playlist, history);
            }
            else if (userChoice.equalsIgnoreCase("H")) {
                printHistory(history, auxStack);
            }
            else if (userChoice.equalsIgnoreCase("V")) {
                viewPlaylist(playlist);
            }
            else if (userChoice.equalsIgnoreCase("C")) {
                history.clear();
            }
            else if (userChoice.equalsIgnoreCase("D")) {
                deleteHistory(history, console, auxStack);
            }
            System.out.println();
            userChoice = menu(console);
        }
        System.out.println();

    }

    // B: this method prints out a menu selection for the user to choose from
    // E: nothing
    // R: returns a string representing the user's choice 
    // P: a Scanner named console to read user's choice
    public static String menu (Scanner console){
        System.out.println("(A) Add song");
        System.out.println("(P) Play song");
        System.out.println("(H) Print history");
        System.out.println("(V) View playlist");
        System.out.println("(C) Clear history");
        System.out.println("(D) Delete from history");
        System.out.println("(Q) Quit");
        System.out.println();
        System.out.print("Enter your choice: ");
        String choice = console.nextLine();
        return choice;
    }

    // B: this method adds the user's chosen song to their queue/playlist
    // E: nothing
    // R: nothing
    // P: a Scanner named console to read user's chosen song,
    //   and a Queue<String> named playlist to add to the user's queue/playlist
    public static void addSong(Scanner console, Queue<String> playlist) {
        System.out.print("Enter song name: ");
        String songName = console.nextLine();
        playlist.add(songName);
        System.out.println("Successfully added " + songName);
        System.out.println();

    }

    // B: this method plays the next song in the user's queue/playlist
    // E: throws an IllegalStateException if there are no songs in the playlist
    // R: nothing
    // P: a Queue<String> named playlist to play the next song in the user's playlist/queue,
    //  and a Stack named history to collect the user's listening history
    public static void playSong (Queue<String> playlist, Stack<String> history){
       if(playlist.isEmpty()){
            throw new IllegalStateException();
        }
        System.out.println("Playing song: " + history.push(playlist.remove()));
        System.out.println();
    }

    // B: this method prints out the user's listening history
    // E: throws an IllegalStateException if the user does not have a listening history
    // R: nothing
    // P: a Stack named history to print out user's history,
    //   and another stack named auxStack to restore history after printing
    public static void printHistory(Stack<String> history, Stack<String> auxStack){
        if(history.isEmpty()){
            throw new IllegalStateException();
        }
      
        while(!history.isEmpty()){
            String printHistory = history.pop();
            System.out.println("    " + printHistory);
            auxStack.push(printHistory);  
        }
        System.out.println();

        transferStacks(auxStack, history);
    }
    // B: this method prints out the user's playlist
    // E: throws an IllegalStateException if the user's playlist is empty
    // R: nothing
    // P: a Queue<String> named playlist to read and print out the user's playlist
    public static void viewPlaylist(Queue<String> playlist){
        if(playlist.isEmpty()){
            throw new IllegalStateException();
        }
        for(int index  = 0 ; index < playlist.size() ; index++){
            String song = playlist.remove();
            System.out.println("    " + song);
            playlist.add(song);
        }
        System.out.println();
    }

    // B: this program lets the user choose what quantity and period of time they would
    // like their history deleted
    // E: throws an IllegalArgumentException if the number of songs the user wants to delete
    //     from their history exceeds the actual amount of songs in their history
    // R: nothing
    // P: a Stack to obtain and delete the user's history, a Scanner to read the user's input,
    //     and another stack to restore history after printing and deleting. 
    public static void deleteHistory(Stack<String> history, Scanner console,
                                     Stack<String> auxStack){
        System.out.println("A positive number will delete from recent history.");
        System.out.println("A negative number will delete from the beginning of history.");
        System.out.print("Enter number of songs to delete: ");
        int numSongs = Integer.parseInt(console.nextLine());
        int absoluteNumSongs = Math.abs(numSongs);

        if(absoluteNumSongs > history.size()){
            throw new IllegalArgumentException();
        }
        
        if(numSongs != 0){
            if(numSongs>0){
                removeTopVal(numSongs, history);
            }

            else if(numSongs < 0){
                transferStacks(history, auxStack);
                removeTopVal(absoluteNumSongs, auxStack);
                transferStacks(auxStack, history); 
            }
        }
        System.out.println();
    }

    // B: this is a helper method that transfers between an auxilary stack the original stack. 
    //     In other words, this method restores the playlists and history to its original 
    //     values after manipulation
    // E: nothing
    // R: nothing
    // P: a Stack named stackOne to read and pull information out of,
    //   and another Stack named stackTwo to transfer into
    public static void transferStacks(Stack<String> stackOne, Stack<String> stackTwo) {
        while (!stackOne.isEmpty()) {
            stackTwo.push(stackOne.pop());
        }
    }

    // B: this is a helper method that 
    //     removes the top value of a stack in a specific number of iterations
    // E: nothing
    // R: nothing
    // P: an int named num for the number of iterations,
    //   and a Stack named stack to remove values from
    public static void removeTopVal(int num, Stack<String> stack){
        for(int iteration = 0; iteration < num; iteration++){
            stack.pop();
        }
    }
}