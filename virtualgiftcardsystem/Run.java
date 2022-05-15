package sudharsan.zoho_questions.virtualgiftcardsystem;

import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.Bank;
import sudharsan.zoho_questions.virtualgiftcardsystem.pojos.CustomerAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;

public class Run {

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Bank bankDb = new Bank();

        while (true){ // start
            System.out.println("---------------Virtual Gift Card System-------------");
            System.out.println("1. Add a Customer Account");
            System.out.println("2. Account Summary of All Customer");
            System.out.println("3. View All Customers");
            System.out.println("4. Gift Card Summary");
            System.out.println("5. Transaction Summary");
            System.out.println("6. Exit");

            System.out.print("Enter an Option: ");

            try {
                int option = Integer.parseInt(input.readLine());

                if (option == 1){ // add a new customer
                    Application.addNewAccount(bankDb);

                } else if (option == 2){ // display account summary
                    System.out.println("----------------------------------------------------");
                    bankDb.displayEachCustomerBalance();
                    System.out.println("----------------------------------------------------");

                } else if (option == 3){ // display customers
                    List<Integer> customers = bankDb.getCustomerAccounts();
                    if (customers.isEmpty()){
                        System.out.println("No customer till now!!!");

                    } else {
                        CustomerAccount currCustomerAccount;
                        System.out.println("Customer Accounts: ");
                        for (int i=0; i<customers.size(); i++){
                            System.out.println("\t"+(i+1)+". "+ bankDb.getCustomerAccountById(customers.get(i)) + "("+customers.get(i)+")");
                        }

                        System.out.print("Select an Account:(0 for back) ");
                        try {
                            int customerSelectionOption = Integer.parseInt(input.readLine());
                            if (customerSelectionOption == 0) System.out.println("Moving back!!");
                            else if (customerSelectionOption < 0 || customerSelectionOption > customers.size()){
                                System.out.println("Please select a valid option!!!");

                            } else {
                                currCustomerAccount = bankDb.getCustomerAccountById(customers.get(customerSelectionOption-1));

                                Application.displayCustomerLogic(currCustomerAccount, bankDb);

                            }
                        } catch (Exception e){
                            System.out.println("Please select from given option!!!");
                        }
                    }

                } else if (option == 4){ // view gift card summary
                    System.out.println("----------------------------------------");
                    bankDb.displayGiftCardSummary();
                    System.out.println("-----------------------------------------");

                } else if (option == 5){ // transaction summary
                    System.out.println("---------------------------------------");
                    bankDb.displayTransactionSummary();
                    System.out.println("---------------------------------------");

                } else if (option == 6){ // exit the app
                    System.out.println("Exiting the app");
                    System.exit(0);

                } else {
                    System.out.println("Please a valid input!!!!");
                }

            } catch (Exception e){
                System.out.println("Please select from given option!!!");
            }

        }//end of first while
    }
}
