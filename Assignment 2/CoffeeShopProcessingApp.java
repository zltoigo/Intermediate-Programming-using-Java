/***************
Zoe Toigo
Spring 2018
CS 0401
Assignment 2
****************/

import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class CoffeeShopProcessingApp {
    public static void main(String[] args) throws IOException {
        // For accepting keyboard input.
        Scanner keyboard = new Scanner(System.in);

        /* Each order's details are stored in an arrray including the following
            details: {[order number],[name for the order],[coffee quantity],
            [latte quantity],[cappuccino quantity],[espresso quantity],
            [scone quantity],[muffin quantity]} 

            orders contains all of the order details arrays.  
        */
        String[][] orders = new String[10][];

        /* Each element of the array corresponds to an order.  Program requires that 
        there are <10 orders. The status of order n is stored in orderstatuses[n-1].
        Status codes:
        1 = received
        2 = processing begun
        3 = finished  processing
        */
        int[] orderStatuses = new int[10];

        // The processing menu continues to re-appear after the end of each activity 
        // until runAgain is false.
        boolean runAgain = true;

        System.out.println("Welcome to the Java Code Coffee Shop Processing program!");
        
        do {
            // Display the processing menu and return the user's choice.
            int chosenProcess = getProcessingResponse(keyboard);
            
            // Based on that choice, do something
            switch(chosenProcess) {
                case 1:
                    viewOrders();
                    break;
                case 2:
                    receiveOrders(orders, orderStatuses);
                    break;
                case 3:
                    beginProcessingOrder(orderStatuses, keyboard);
                    break;
                case 4: 
                    finishProcessingOrder(orderStatuses, keyboard, orders);
                    break;
                case 5:
                    viewOrderHistory(orders, orderStatuses);
                    break;
                case 6:
                    runReport(keyboard, orders);
                    break;
                default:
                    runAgain = exitApp();
                    break;
            }            
        }while(runAgain==true);
    }


    public static int getProcessingResponse(Scanner keyboard) {
        System.out.println("\n\n-- Processing Menu --" +
                            "\n 1. View orders." +
                            "\n 2. Receive orders." +
                            "\n 3. Begin processing an order." +
                            "\n 4. Complete processing an order." +
                            "\n 5. View order history." +
                            "\n 6. Run a report." +
                            "\n 7. Exit");
        System.out.print("Enter an action: ");
        int choice = keyboard.nextInt();
        //absorb the return button press 
        keyboard.nextLine();
        return choice;
    }

    public static void viewOrders() throws IOException {
        File ordersFile = new File("orders.txt");

        if(!ordersFile.exists()){
            System.out.println("An orders file does not exist.");
            return;
        }
        
        Scanner fileReader = new Scanner(ordersFile);

        if(!fileReader.hasNext()){
            System.out.println("There are not any unprocessed orders right now.");
            return;
        }

        System.out.println("\n---Current Orders---");
        while(fileReader.hasNext()) {
            System.out.println(fileReader.nextLine());
        }

        fileReader.close();
    }

    public static void receiveOrders(String[][] orders, 
                                    int[] orderStatuses) throws IOException {
        File ordersFile = new File("orders.txt");   
        Scanner pendingOrders;

        if(!ordersFile.exists()){
            return;
        }

        pendingOrders = new Scanner(ordersFile);

        if(!pendingOrders.hasNext()){
            System.out.println("There are not any orders to receive.");
            pendingOrders.close();
            return;
        }
        
        String[] order = new String[10];
        int i = 0;

        // read in and process each order
        while(pendingOrders.hasNext()){
            order = pendingOrders.nextLine().split(",");
            orders[i] = order;
            updateOrderStatus(orderStatuses, Integer.parseInt(order[0]), 1);
            System.out.printf("Order %d has been received.\n", Integer.parseInt(order[0]));
            i++;
        }

        eraseFile("orders.txt");
        pendingOrders.close();
    }

    // remove all contents from a given file
    public static void eraseFile(String filename) throws IOException {
        PrintWriter targetFileWriter = new PrintWriter(filename);
        targetFileWriter.print("");
        targetFileWriter.close();
    }

    public static void beginProcessingOrder(int[] orderStatuses, 
                                            Scanner keyboard) throws IOException {
        int[] emptyArray = new int[10];
        if(Arrays.equals(orderStatuses, emptyArray)) {
            System.out.println("There are not any received orders.");
            return;
        }

        int orderToProcess = getOrderToProcess(keyboard, 1, orderStatuses);      
        System.out.printf("\nOrder %d is now processing.", orderToProcess);  
        updateOrderStatus(orderStatuses, orderToProcess, 2);
    }

    public static void updateOrderStatus(int[] orderStatuses, int orderNumber, 
                                        int newStatus) throws IOException {
       orderStatuses[orderNumber-1]=newStatus;
       File statusFile = new File("status.txt");
       PrintWriter statusWriter = new PrintWriter(statusFile);
       // since file will be overwriten each time this method is called,
       // we need to loop through the whole array.
       for(int i = 0; i<orderStatuses.length; i++){
           switch(orderStatuses[i]){
                case 1:
                    statusWriter.printf("Order %d has been received.", i+1);
                    statusWriter.println("");
                    break;
                case 2:
                    statusWriter.printf("Order %d is now processing.", i+1);
                    statusWriter.println("");
                    break;
                case 3:
                    statusWriter.printf("Order %d is finished processing.", i+1);
                    statusWriter.println("");
                    break;
                default:
                    break;
           }
       }

       statusWriter.close();
    }

    public static int getOrderToProcess(Scanner keyboard, int status, int[] orderStatuses) {
        int listIndex = 1;

        switch(status) {
            case 1: 
                System.out.println("\nChoose one of the received orders to process:");
                for(int i = 0; i<orderStatuses.length; i++) {
                    if(orderStatuses[i] == 1){
                        System.out.printf("%d. Order %d\n", listIndex, i+1);
                        listIndex++;
                    }
                }
                System.out.print("\nWhich order would you like to start processing? ");
                break;
            default:
                System.out.println("\nHere are the orders that are processing: ");
                for(int i=0; i<orderStatuses.length; i++){
                    if(orderStatuses[i]==2){
                        System.out.printf("%d. Order %d\n", listIndex, i+1);
                        listIndex++;
                    }
                }
                System.out.print("\nWhich order would you like to finish processing? ");
        }        
        return Integer.parseInt(keyboard.nextLine()); 
    }

    public static void finishProcessingOrder(int[] orderStatuses, Scanner keyboard, String[][] orders) throws IOException {
        int[] emptyArray = new int[10];
        if(Arrays.equals(orderStatuses, emptyArray)) {
            System.out.println("There are not any received orders.");
            return;
        }

        int orderToProcess = getOrderToProcess(keyboard, 2, orderStatuses);     
        System.out.printf("\nOrder %d is finished processing.", orderToProcess);   
        updateOrderStatus(orderStatuses, orderToProcess, 3);
        writeOrderHistory(orderStatuses, orders);
    }

    public static void writeOrderHistory(int[] orderStatuses, String[][] orders) throws IOException{
        File orderHistoryFile = new File("order-history.txt");
        PrintWriter historyWriter = new PrintWriter(orderHistoryFile);
        for(int i=0; i<orderStatuses.length; i++) {
            if(orderStatuses[i]==3){
                String[] orderDetails = orders[i];
                for (int detailsIndex = 0; detailsIndex < orderDetails.length; detailsIndex++){
                    if(detailsIndex != orderDetails.length-1) {
                        historyWriter.print(orderDetails[detailsIndex]+",");
                    } else { 
                        historyWriter.println(orderDetails[detailsIndex]);
                    }
                }
            }
        }
        historyWriter.close();
    }

    public static void viewOrderHistory(String[][] orders, int[] orderStatuses) {
        System.out.println("\n---Order History---");
        System.out.printf("\n%-10s%8s%6s%12s%10s%6s%7s","Customer", "Coffee", "Latte", "Cappuccino", "Espresso", "Scone", "Muffin");
        System.out.printf("\n%-10s%8s%6s%12s%10s%6s%7s","--------", "------", "-----", "----------", "--------", "-----", "------");
        for(int i=0; i < orders.length; i++) {
            if(orders[i]!=null && orderStatuses[i]==3) {
                String[] orderDetails = orders[i];
                System.out.printf("\n%-10s%8s%6s%12s%10s%6s%7s",orderDetails[1], orderDetails[2], orderDetails[3],
                                                                orderDetails[4], orderDetails[5], orderDetails[6],
                                                                orderDetails[7]);
            }
        }

    }

    public static void runReport(Scanner keyboard, String[][]orders) throws IOException {
        // read orders history into orders array in case this is the first action taken
        // after loading the program
        updateOrdersArray(orders);
        printReportMenu();
        int reportMenuChoice = getReportChoice(keyboard);

        switch (reportMenuChoice) {
            //print order history for a specific customer
            case 1:
                runCustomerReport(keyboard, orders);
                break;
            //print the toal number sold for a product type
            default:
                int totalSold = getTotalSold(orders, reportMenuChoice);
                printProductReport(reportMenuChoice, totalSold);
                break;
        }
    }

    //used to force the array to fill if the 
    public static void updateOrdersArray(String[][] orders) throws IOException{
        File orderHistory = new File("order-history.txt");
        if (orderHistory.exists()){
            String[] order = new String[10];
            Scanner arrayFiller = new Scanner(orderHistory);
            int i = 0;

            // read in and process each order
            while(arrayFiller.hasNext()){
                order = arrayFiller.nextLine().split(",");
                orders[i] = order;
                i++;
            }
        }
    }

    public static void printReportMenu(){
        System.out.println("\n--- Report ---");
        System.out.println("1. Customer");
        System.out.println("2. Coffee");
        System.out.println("3. Lattee");
        System.out.println("4. Cappuccino");
        System.out.println("5. Espresso");
        System.out.println("6. Scone");
        System.out.println("7. Muffin");
    }

    public static int getReportChoice(Scanner keyboard){
        System.out.print("Choose a field: ");
        int choice = keyboard.nextInt();
        keyboard.nextLine();
        return choice;
    }

    public static int getTotalSold(String[][] orders, int productMenuNumber){
        int productCount=0;
        for(int i=0; i < orders.length; i++) {
            if(orders[i]!=null){
                productCount+=Integer.parseInt(orders[i][productMenuNumber]);
            } 
        }
        return productCount;
    }

    public static void printProductReport(int productMenuNumber, int totalSold){
        String[] products = {"Coffee", "Latte", "Cappuccino", "Espresso", "Scone", "Muffin"};
        System.out.printf("%-10s%13s\n", "Item:",products[productMenuNumber-2]);
        System.out.printf("%-10s%12s\n", "Total Sold:",totalSold);
    }

    public static void runCustomerReport(Scanner keyboard, String[][] orders){
        System.out.print("\nEnter the customer's name: ");
        String customerName = keyboard.nextLine();
        int[] customerProductAggregate = new int[7];

        for (int i=0; i < orders.length; i++) {
            if(orders[i]==null){
                break;
            }

            if(customerName.equals(orders[i][1])) 
                updateCustomerAggregate(orders[i], customerProductAggregate); 
        }

        //if customer's total products ordered == 0
        if(customerProductAggregate[6] == 0){ 
            System.out.println("There are not any orders associated with that name.");
            return;
        }

        printCustomerReport(customerProductAggregate);
    }

    public static void updateCustomerAggregate(String[] orderDetails, int[]customerProductAggregate){
        for(int i=2; i < orderDetails.length; i++){
            //update individual product totals
            customerProductAggregate[i-2] += Integer.parseInt(orderDetails[i]);
            //update total items
            customerProductAggregate[customerProductAggregate.length-1] += Integer.parseInt(orderDetails[i]);
        }   

    }

    public static void printCustomerReport(int[] customerProductAggregate){
        System.out.printf("\n%-8s%6s%12s%10s%6s%7s%12s","Coffee", "Latte", "Cappuccino", "Espresso", "Scone", "Muffin", "Total Items");
                    System.out.printf("\n%-8s%6s%12s%10s%6s%7s%12s", "------", "-----", "----------", "--------", "-----", "------", "-----------");
                    System.out.printf("\n%-8d%6d%12d%10d%6d%7d%12d", customerProductAggregate[0], customerProductAggregate[1],
                                                                    customerProductAggregate[2], customerProductAggregate[3], customerProductAggregate[4],
                                                                    customerProductAggregate[5],customerProductAggregate[6]);
    }

    public static boolean exitApp() {
        System.out.println("\nGoodbye!");
        return false;
    }

}
