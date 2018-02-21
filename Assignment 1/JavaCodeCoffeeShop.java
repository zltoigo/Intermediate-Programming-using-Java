/*
Name: Zoe Toigo
Class name: CS401
Semester: Spring 2018
Assignment 1: Java Code Coffee Shop Program

User chooses items from a menu.  Discounts and tax
are applied for a "cart" of different quantities of 
different items.  
*/

import java.util.Scanner;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class JavaCodeCoffeeShop {

	public static void main(String[] args) {
		// Declare variables
		String name; 		 	//customer name
		String itemName; 	 	//name of item chosen
		double itemCost;		//price of item chosen
		double totalCost = 0;	//running total cost
		
		// Declare constant variables
		final double taxPercent = 0.07;  	// constant sales tax
		final double coffeePrice = 1.50;	// price of coffee
		final double lattePrice = 3.50;		// price of latte
		final double cappPrice = 2.00;		// price of cappuccino
		final double espressoPrice = 2.50;	// price of espresso
		final double sconePrice = 2.50;		// price of scone
		final double muffinPrice = 3.00;	// price of muffin
		
		//set up menu
		String menuNames[] = {"Coffee", "Latte", "Cappuccino", 
		"Espresso", "Scone", "Muffin"};
		double menuPrices[]= {coffeePrice, lattePrice, cappPrice, espressoPrice, sconePrice, muffinPrice};
		
		// Instantiate keyboard object
		Scanner keyboard = new Scanner(System.in);
		
		// Prompt user to enter name.
		System.out.print("What is your name? ");
		name = keyboard.nextLine();
		System.out.printf("\nWelcome to Java Code Coffee Shop, %s!\n\n", name);	
		
		// Print menu
		System.out.println("Here is our menu: ");
		for(int i = 0; i < 6; i++) {
			// print list number
			System.out.print(i+1);
			//print product name
			System.out.print(". " + menuNames[i]); 
			// print cost
			System.out.printf(":\t$%.2f\n", menuPrices[i]); 
		}
		
		// Order the first item
		System.out.print("\nPlease enter a menu number: ");
		int choice = keyboard.nextInt();
		System.out.print("Please enter the quantity: ");
		int quantity = keyboard.nextInt();
		
		// Store the name and cost of item chosen
		itemName = menuNames[choice-1];
		itemCost = menuPrices[choice-1];
		
		// Update the total cost of the order
		totalCost += (itemCost * quantity);
		
		//if choice is greater than four, then a pastry was chosen
		if(choice > 4) {
			System.out.print("Would you like a coffee or espresso drink to go with your order?  Enter 1 for \"yes\" or 2 for \"no\": ");
			
			// Help the user buy a beverage if s/he answered yes
			if(keyboard.nextInt() == 1) {
				System.out.print("Which beverage would you like? " );
				int itemNumber = keyboard.nextInt();
				System.out.print("And how many?");
				int addQuantity = keyboard.nextInt();
				totalCost += menuPrices[itemNumber-1] * addQuantity;
			}
		// executes if a drink was chosen from the menu
		} else {
			System.out.print("Would you like a scone or muffin to go with your " + 
			itemName.toLowerCase() + "(s)? Enter 1 for \"yes\" or 0 for \"no\": ");
			
			// Help the user buy a pastry if s/he answered yes
			if (keyboard.nextInt() == 1) {
				System.out.print("Which pastry would you like? " );
				int itemNumber = keyboard.nextInt();

				//check if the user actually chose a pastry
				while(itemNumber != 5 && itemNumber != 6){
					System.out.print("The item you selected is not a pastry. Which pastry would you like? " );
					itemNumber = keyboard.nextInt();
				}
				
				// get pastry quantity
				System.out.printf("And how many %ss? ", menuNames[itemNumber-1].toLowerCase());
				int addQuantity = keyboard.nextInt();
				totalCost += menuPrices[itemNumber-1] * addQuantity;
			}
		}
		
		System.out.printf("Total before discount and tax is: $%.2f\n",totalCost);
		
		// apply discount if total cost greater than $10
		if(totalCost > 10) {
			double discount = totalCost*0.10;
			System.out.printf("Discount is: $%.2f\n",discount);
			totalCost = totalCost - discount;
			System.out.printf("Price after discount is: $%.2f \n", totalCost);
		}
		
		// calculate tax
		double tax = totalCost*taxPercent;
		System.out.printf("Tax: $%.2f \n", tax);
		
		// add tax to total cost
		totalCost += tax;
		System.out.printf("Total: $%.2f \n", totalCost);
		
		// Help the user pay
		System.out.print("Enter your payment amount: ");
		double paymentAmount = keyboard.nextDouble();
		
		// Calculate change or reject transaction
		if(paymentAmount < totalCost) {
			System.out.println("I'm sorry, your payment is insufficient." + 
			"Please stop by again in the future, " + name + "!");
		} else {
			System.out.printf("Thank you for your purchase, " + name +
			"! Your change is $%.2f. Have an awesome day!", paymentAmount-totalCost);
		}

		System.exit(1);
	}

}