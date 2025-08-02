//Carlo Lesaca
//07/03/2025
//CSE 122
//C0: Image Manipulation
//TA: Nicole Ham
//this program asks the user for an image they want to manipulate.
//the user is then prompted different options on how they want to
//manipulate their photo.
//the photo is then saved as a new name that the user chooses. 

import java.awt.*;
import java.util.*;

public class ImageManipulationRevised {
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);
        System.out.print("Enter image name: ");
        String imageName = console.next();
        System.out.println();

        Picture pic = new Picture(imageName);
        Color[][] pixels = pic.getPixels();

        printMenu();

        System.out.print("Enter option: ");
        String input = console.next();

        while (!input.equalsIgnoreCase("Q")) {
            if (input.equalsIgnoreCase("CF")) {
                colorFilter(pixels,console);
                savePic(pixels,pic,console);
            }

            else if (input.equalsIgnoreCase("cr")) {
                colorFilterRegion(pixels,console);
                savePic(pixels,pic,console);
            }

            else if (input.equalsIgnoreCase("mr")) {
                mirrorRight(pixels);
                savePic(pixels,pic,console);
            }

            else if (input.equalsIgnoreCase("ng")) {
                negative(pixels);
                savePic(pixels,pic,console);
            }

            System.out.println();
            printMenu();

            System.out.print("Enter option: ");
            input = console.next();
        }
        System.out.println("Exiting. Goodbye!");
    }

    //Behavior:
    //  -This method saves the filter options based off the user's choice
    //  -and saved as a new file name
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -2d array of color constructs of the image that the user inputs
    //  -picture object of the user's image
    //  -scanner console to obtain new file name
    public static void savePic(Color[][] pixels, Picture pic, Scanner console){
        pic.setPixels(pixels);
        System.out.print("Enter picture name to save: ");
        String saveName = console.next();
        pic.save(saveName);
        System.out.println();
        System.out.println("Color filter applied and saved."); 

    }

    //Behavior:
    //  -prints out menu options
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -none
    public static void printMenu() {
        System.out.println("Menu Options:");
        System.out.println("(CF) Color Filter");
        System.out.println("(CR) Color Filter Region");
        System.out.println("(MR) Mirror Right");
        System.out.println("(NG) Negative");
        System.out.println("(Q) Quit");
        System.out.println();
    }

    //Behavior:
    //  -This method changes the RGB value of the user's picture based off
    //  -user input
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -2d array of color constructs of the image that the user inputs
    //  -scanner console to obtain different RGB values
    public static void colorFilter(Color[][] pixels, Scanner console) {
        int[] rgbPrompts = rgbValues(console);

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
               applyFilter(i, j, rgbPrompts, pixels);
            }
        }
    }

    //Behavior:
    //  -This method changes RGB value of user's picture in a specified
    //  -dimension based off user input
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -2d array of color constructs of the image that the user inputs
    //  -scanner console to obtain dimensions and rgb values
    public static void colorFilterRegion(Color[][] pixels, Scanner console){
        System.out.print("Enter row 1: ");
        int row1 = console.nextInt();
        System.out.print("Enter row 2: ");
        int row2 = console.nextInt();
        System.out.print("Enter col 1: ");
        int col1 = console.nextInt();
        System.out.print("Enter col 2: ");
        int col2 = console.nextInt();

        int[] rgbPrompts = rgbValues(console);

        for (int i = row1; i < row2; i++) {
            for (int j = col1; j < col2; j++) {
                applyFilter(i, j, rgbPrompts, pixels);
            }
        }
    }

    //Behavior:
    //  -This method flips the user's photo from right to left
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -2d array of color constructs of the image user inputs
    public static void mirrorRight(Color[][] pixels){
        for (int i = 0; i < pixels.length; i++) {
            for (int j = pixels[i].length/2; j < pixels[i].length; j++) {
               
                Color rightSide = pixels [i][j];
                int mirrorJ= pixels[i].length/2 - (j - pixels[i].length/2);
                pixels[i][mirrorJ] = rightSide;
            }
        }
    }

    //Behavior:
    //  -This method inverts the color of the user's image
    //Returns:
    //  -does not return anything
    //Parameters:
    //  -2d array of color constructs of the image that the user inputs
    public static void negative(Color[][] pixels){
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                Color originalColor = pixels[i][j];
                int red = originalColor.getRed();
                int green = originalColor.getGreen();
                int blue = originalColor.getBlue();
                
                Color newColor = new Color(255 - red, 255 - green, 255 - blue);
                pixels[i][j] = newColor;
            }
        }
    }

    //Behavior:
    //  -helper method which asks for user's desired rgb values
    //Returns:
    //  -returns an array of rgb int values
    //Parameters:
    //  -scanner console to obtain user's rgb value
    public static int [] rgbValues (Scanner console){
        System.out.print("Enter Red value: ");
        int redVal = console.nextInt();
        System.out.print("Enter Green value: ");
        int greenVal = console.nextInt();
        System.out.print("Enter Blue value: ");
        int blueVal = console.nextInt();

        int [] rgb = {redVal, greenVal, blueVal};
        return rgb;
    }
    
    //Behavior:
    //  -helper method that applys users filters
    //Returns:
    //  -nothing
    //Parameters:
    //  -int row to modify index of pixel, int col to modify index of pixel
    //  -int[] rgbprompts for user rgb values, 2d array
    //  -of color constructs of the image that the user inputs
    public static void applyFilter(int row, int col, int[] rgbPrompts, Color[][] pixels){
        Color originalColor = pixels[row][col];
        int red = originalColor.getRed();
        int green = originalColor.getGreen();
        int blue = originalColor.getBlue();
        
        Color newColor = new Color(Math.min(red + rgbPrompts[0], 255), 
                                    Math.min(green + rgbPrompts[1], 255), 
                                    Math.min(blue + rgbPrompts[2], 255));
        pixels[row][col] = newColor;
    }
}
