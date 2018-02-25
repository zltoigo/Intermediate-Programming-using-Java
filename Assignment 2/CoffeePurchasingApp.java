/***************
Zoe Toigo
Spring 2018
CS 0401
Assignment 2
****************/

import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class CoffeePurchasingApp {
    public static void main(String[] args) throws IOException{       
        //INITIALIZE VARIABLES
        
        //controls whether the purchasing menu is shown again at the end of an action
        boolean runAgain = true;
        
        //available products and prices
        String[][] products = {
            {"Coffee", "1.50"},
            {"Latte", "3.50"},
            {"Cappuccino", "3.25"},
            {"Espresso", "2.00"},
            {"Scone", "2.50"},
            {"Muffin", "3.00"}
        };

        PrintWriter ordersWriter = null;

        //available coupon codes that will give a 20% discount
        String[] couponCodes = {"COFFEE1", "LATTE23", "I<3JAVA", "SCONEZ", "MUFFIN!LOLOL"};
        
        /* will store {[order number],[name for the order],[coffee quantity],
            [latte quantity],[cappuccino quantity],[espresso quantity],
            [scone quantity],[muffin quantity]}
        */
        String[] orderDetails = new String[8];
        
        //String array initially fills with null.  Fill with "0" to avoid errors.
        Arrays.fill(orderDetails,"0");   
        
        // Scanner to accept keyboard input from user
        Scanner keyboard = new Scanner(System.in);
        boolean firstOrder = true;

        System.out.print("Welcome to the Java Code Coffee Shop Purchasing App!");
        
        // will continue looping until user choose to exit.
        do {
            int purchaseMenuChoice = getPurchasingMenuResponse(keyboard);
            switch (purchaseMenuChoice) {
                case 1:
                    if(firstOrder) { 
                        File ordersFile = new File("orders.txt");
                        ordersWriter = new PrintWriter(ordersFile);
                        firstOrder = false;
                    }                  
                   placeAnOrder(orderDetails, keyboard, products, couponCodes, ordersWriter);
                    break;
                case 2:
                    checkOrderStatus();
                    break;
                // If the user wants to quit, set the flag to null and perform exit actions
                case 3: 
                    if(ordersWriter!=null) {
                        ordersWriter.close();
                    }
                    runAgain = false;
                    break;
                default: 
                    break;
            }
        }while(runAgain==true);
    } 

    public static int getPurchasingMenuResponse(Scanner keyboard) {
        System.out.println("\n\n-- Purchasing Menu --" +
                            "\n 1. Place an order." +
                            "\n 2. Check order status." +
                            "\n 3. Exit");
        System.out.print("Enter an action: ");
        int choice = keyboard.nextInt();
        //absorb the return button press 
        keyboard.nextLine();
        return choice;
    };

    // customer can add items to cart, apply coupons, and view order summary
    public static void placeAnOrder(String[] orderDetails, Scanner keyboard, 
    String[][] products, String[] acceptableCoupons, PrintWriter ordersWriter) throws IOException {
        // update the order number to start a new order
        orderDetails[0]=Integer.toString(Integer.parseInt(orderDetails[0])+1);
        // set indexes 1-7 to zero to start a new record    
        Arrays.fill(orderDetails,1,7,"0");
        displayProductMenu(products);
        orderDetails[1] = getCustomerName(keyboard);
        addItemsToCart(orderDetails, keyboard);
        boolean hasValidCoupon = getCouponCode(keyboard, acceptableCoupons);
        printOrderSummary(orderDetails,products, hasValidCoupon);
        submitOrderConfirmation(orderDetails, keyboard, ordersWriter);
    }

    public static void displayProductMenu(String[][] products) {
        System.out.print("\nHere is our menu:");
        // each row of the products 2d array includes a name and price column
        for (int i = 0; i < products.length; i++) {
            for(int j = 0; j < products[i].length; j++) {
                //print product name
                if(j==0) {
                    System.out.printf("\n%d. %-10s",i+1, products[i][j]);
                //print product price
                } else {
                    System.out.printf(" $%-8.2f", Double.parseDouble(products[i][j]));
                }

            }
        }
    };

    public static String getCustomerName(Scanner keyboard){
        System.out.print("\n\nWhat is your name?: ");
        String customerName = keyboard.nextLine();
        return customerName;
    }

    public static void addItemsToCart(String[] orderDetails, Scanner keyboard){
        int orderedItem = addItem(orderDetails, keyboard);
    
        //Based on first item ordered, offer a product of the opposite type 
        //The first four items on menu are drinks.
        if(orderedItem<4){
            System.out.print("\nWould you like to add a pastry? Enter 1 for yes or 0 for no: ");
        } else {
            System.out.print("\nWould you like to add a coffee or espresso drink? Enter 1 or yes or 0 for no: ");
        }

        int addAnswer = keyboard.nextInt();
        keyboard.nextLine();
        // if they answered yes, add the item.
        if(addAnswer == 1) {
            int secondOrderedItem = addItem(orderDetails, keyboard);
        }

    }

    public static int addItem(String[] orderDetails, Scanner keyboard) {
        System.out.print("What would you like?: ");
        //Coffee is ordered by entering "1" based on the menu, latte is "2", etc.
        int itemNumber = keyboard.nextInt(); 
        keyboard.nextLine();
        System.out.print("How many?: ");
        // However, coffee quantity is stored in orderDetails[2], latte quantity in orderDetails[3], etc.
        orderDetails[itemNumber+1] = keyboard.nextLine();
        return itemNumber;
    }

    public static boolean getCouponCode(Scanner keyboard, String[] acceptableCoupons) {
        System.out.print("Enter a coupon code (or press Enter to skip): ");
        String couponEntry = keyboard.nextLine();
        //check if coupon code is valid
        if(couponEntry.isEmpty()) {
            return false;
        } else if(Arrays.asList(acceptableCoupons).contains(couponEntry)) {
            System.out.print("\nYour order has been accepted. You get 20% off!");
            return true;
        } else {
            System.out.print("\nSorry, that code is invalid.");
            return false;
        }
        
    }

    //prints a table of items, quantity ordered, price for all items of that type, cost before discount,
    //discount amount, and price after discount
    public static void printOrderSummary(String[] orderDetails, String[][] products, boolean hasCoupon) {
        //start the printed list at 1
        double orderCost = 0.0;
        double discount = 0.0;
        System.out.printf("\n\n--- Order %s Summary ---", orderDetails[0]);
        for (int i = 2; i < orderDetails.length; i++) {
            if(orderDetails[i]!= "0") {
                String itemName = products[i-2][0];
                double itemPrice = Double.parseDouble(products[i-2][1]);
                int itemQuantity = Integer.parseInt(orderDetails[i]);
                //linePrice is the total cost for all products of one type. ex: all coffees ordered
                double linePrice = itemPrice*itemQuantity;
                orderCost+= linePrice;
                System.out.printf("\n%-2d %-12s $%-6.2f", itemQuantity, itemName, linePrice);
            }
        }

        if(hasCoupon) {
            discount = orderCost*.2;
        }

        System.out.printf("\n\n%-15s $%-7.2f", "Total Cost:", orderCost);
        if(discount>0.0) {
            System.out.printf("\n%-15s $%-7.2f", "20% Discount:", discount);
        }
        System.out.printf("\n%-15s $%-7.2f", "Final cost:",orderCost-discount);
    }

    //Asks the user to confirm order, and if yes writes the order to a file
    public static void submitOrderConfirmation(String[] orderDetails, Scanner keyboard, PrintWriter ordersWriter) throws IOException {
        System.out.print("\n\nEnter 1 to confirm or 0 to cancel: ");
        if(keyboard.nextInt() == 1) {
            writeOrderDetailsToFile(orderDetails, ordersWriter);
        }
        keyboard.nextLine();
    }

    //writes orderDetails array (separated by commas) to the output file
    public static void writeOrderDetailsToFile(String[] orderDetails, PrintWriter ordersWriter) throws IOException{
        for(int i = 0; i < orderDetails.length; i++){
            //if it is not the last item on the list
            if(i != orderDetails.length-1) {
                ordersWriter.print(orderDetails[i]+",");
            } else { 
                ordersWriter.println(orderDetails[i]);
            }
        }
        System.out.printf("\nOrder %s has been placed.", orderDetails[0]);
    }
     
    public static void checkOrderStatus() throws IOException {
        File ordersFile = new File("status.txt");
        System.out.println("\nOrder Status: ");
        if(!ordersFile.exists()){
            System.out.println("No orders are in at this time.");
            return;
        }
        
        Scanner fileReader = new Scanner(ordersFile);
        if(!fileReader.hasNext()){
            System.out.println("The status file exists but is empty.");
            fileReader.close();
            return;
        }

        while(fileReader.hasNext()) {
            System.out.println(fileReader.nextLine());
        }
        
        fileReader.close();
    }

    // leaveste app
    public static boolean exitApp(PrintWriter ordersWriter) {
        System.out.println("Goodbye!");
        ordersWriter.close();
        return false;
    } 
}