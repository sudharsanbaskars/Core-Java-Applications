package sudharsan.zoho_questions.tripmanagement;

import sudharsan.zoho_questions.tripmanagement.dao.DataBase;
import sudharsan.zoho_questions.tripmanagement.pojos.ExpenseDetails;
import sudharsan.zoho_questions.tripmanagement.pojos.Member;
import sudharsan.zoho_questions.tripmanagement.pojos.Owe;
import sudharsan.zoho_questions.tripmanagement.pojos.Trip;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Run {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        DataBase db = new DataBase(); // initializing the database

        System.out.println("                                 Trip Management System!!!");
        System.out.println("-------------------------------------------------------------------------------------------");

        while (true){
            System.out.println("Main Menu");
            System.out.println();
            System.out.println("1. Add user to the Application");
            System.out.println("2. Add Trip");
            System.out.println("3. Enter Trip");
            System.out.println("4. Exit");

            System.out.println("Please Enter the Option!");
            String input = scan.next();

            if (input.equals("1")){
                System.out.println("Please Enter the user name: ");
                String userName = scan.next();
                db.addUser(userName); // adding the user to the database

                System.out.println("User Added Succesfully!!!");
                System.out.println("-------------------------------------------------------------------------------------------");

            } else if(input.equals("2")){
                System.out.println("Please Enter the Trip Name: ");
                String tripName = scan.next();

                System.out.println("Please Enter the Start Of the Trip: ");
                String startDate = scan.next();

                System.out.println("Please Enter the End of the Trip: ");
                String endDate = scan.next();

                System.out.println("Please Enter the Maximum people who are part of the Trip: ");
                int numOfMembers = scan.nextInt();

                Trip trip = new Trip(tripName, startDate, endDate, numOfMembers);

                db.addTrip(trip); // adding the trip to the database

                System.out.println("Trip Added !!!!");
                System.out.println("-------------------------------------------------------------------------------------------");

            } else if(input.equals("3")){

                System.out.println("Trips in the System are: ");
                List<String> names = db.getALlTrips();
                for(int i=0; i<names.size(); i++){
                    System.out.println((i+1) + ". " + names.get(i));
                }


                System.out.println("Enter the Option: ");

                 /////////////
                    int option = scan.nextInt();
                    if (option-1 < names.size()){
                        Trip currTrip = db.getTripByIndex(option-1);

                        while (true) { // selecting the trip
                            System.out.println();
                            System.out.println("Welcome to the " + names.get(option-1));
                            System.out.println("Trip Menu");
                            System.out.println("1. Add Users To the Trip");
                            System.out.println("2. Add Expense");
                            System.out.println("3. Display All Expenses");
                            System.out.println("4. Display All Owes");
                            System.out.println("5. Settle up owe");
                            System.out.println("6. Show Expenses Summary");
                            System.out.println("7. Show owe settlement History");
                            System.out.println("8. Show members in the Trip");
                            System.out.println("9. Return to Main menu");
                            System.out.println("10. Remove a member from the trip");

                            System.out.println();
                            System.out.println("Enter the Option: ");

                            int sub_option = scan.nextInt();
                            ///////

                            while (true) {
                                if (sub_option == 1) { // adding users to the trip

                                    System.out.println("Who do you want to add the users to the Trip: " );
                                    System.out.println("Enter 0 for Back: ");
                                    //db.addUser();
                                    List<Member> persons = currTrip.getAvailableMembers();

                                    for (int i = 0; i < persons.size(); i++) {
                                        System.out.println((i + 1) + ". " + persons.get(i));
                                    }

                                    System.out.println("Option: ");
                                    int personOption = scan.nextInt(); ;
//                                    try {
//                                        personOption = scan.nextInt();
//                                    } catch (Exception e){
//                                        System.out.println("PLease enter a valid input: ");
//                                    }

                                    if (personOption == 0) break;

                                    if ((personOption - 1) > persons.size()-1) System.out.println("Enter the valid option ");
                                    else {
                                        //Trip trip =
                                        if (currTrip.getMembersInTrip().size() < currTrip.getTripSize()) {
                                            db.addUserToTrip(currTrip, persons.get(personOption - 1));

                                            System.out.println(currTrip.getAvailableMemberByIndex(personOption - 1) + " added to the trip " + currTrip.getTripName() + " Successfully!!");
                                            currTrip.removeAvailableMembers((personOption - 1));
                                        } else {
                                            System.out.println("Trip member size if full");
                                            break;
                                        }
                                        //persons.remove(personOption-1);
                                    }

                                } else if(sub_option == 2){ // adding expenses
                                    System.out.println("Choose the person who paid: " + " Type 0 for Back Page");
                                    List<Member> membersInTrip = currTrip.getMembersInTrip();
                                    for (int i=0; i<membersInTrip.size(); i++){
                                        System.out.println((i+1) + ". " + membersInTrip.get(i));
                                    }

                                    // person who paid
                                    System.out.println("Person: ");
                                    int personIdWhoPaid = scan.nextInt() - 1;
                                    if (personIdWhoPaid == -1) break;
                                    if (personIdWhoPaid > membersInTrip.size()-1){
                                        System.out.println("Select a valid input: ");
                                    } else {
                                        String personWhoPaid = currTrip.getMembersInTripByIndex(personIdWhoPaid).getName();

                                        System.out.println("Name of the Expense: ");
                                        String nameOfExpense = scan.next();

                                        System.out.println("Total Amount Paid: ");
                                        int amountPaid = scan.nextInt();

                                        ExpenseDetails expense = new ExpenseDetails(personWhoPaid, nameOfExpense, amountPaid);
                                        currTrip.addExpense(expense);
                                        currTrip.createOwes(expense); // creating the owes
                                    }
                                   // break;
                                }
                                  else if(sub_option == 4){ // displaying owes
                                    currTrip.displayOwes();
                                    //System.out.println();
                                    break;

                                } else if(sub_option == 5){ // settle up owe
                                    // System.out.println();
                                     List<Owe> owes = currTrip.getAllOwes();

                                   // Set<String> pendingOwesNames = currTrip.getPendingOwes();
                                    //Set<String> pendingOwesName = new LinkedHashSet<>();

                                    for (int i=0; i<owes.size(); i++){
                                        System.out.println((i+1) + ". " + owes.get(i).getOweFrom() + " to " + owes.get(i).getOweTo() +" "+ owes.get(i).getAmount());
                                    }

//                                    int k=1;
//                                    for (String str : pendingOwesNames){
//                                        System.out.println((k++) + ". " + str);
//                                    }

                                    int oweSettleOption = scan.nextInt();

                                    if ((oweSettleOption-1) > owes.size()-1 || (oweSettleOption-1) <= -1)
                                        System.out.println("Please select a valid option!!!");
                                    else {
                                        Owe settleOwe = owes.get((oweSettleOption-1));

                                        System.out.println("Enter the amount to pay ");

                                        double amt = scan.nextDouble();
                                        if (amt > settleOwe.getAmount()){
                                            System.out.println("You are trying to pay more money than the owe!! Please enter a valid amount!!!");
                                        } else if (amt == settleOwe.getAmount()){
                                            String message = settleOwe.getOweFrom() + " settles " + settleOwe.getAmount() + " amount to "
                                                    + settleOwe.getOweTo() + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                                            System.out.println(message);

                                            currTrip.addOweHistory(message);
                                            currTrip.deleteOweByIndex((oweSettleOption-1));

                                        }else { // settle up only the partial amount
                                            String message = settleOwe.getOweFrom() + " settles " + amt + " amount to "
                                                    + settleOwe.getOweTo() + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                                            currTrip.setOweByIndex((oweSettleOption-1), settleOwe.getAmount()-amt);
                                            currTrip.addOweHistory(message);
                                            System.out.println(message);
                                        }
                                    }
                                    break;
                                } else if (sub_option == 6){ // summary
                                    System.out.println("---------------------------------------------------------------");
                                    System.out.println("Amount Spent so far: " + currTrip.getTripExpenses());
                                    System.out.println();
                                    System.out.println("Expenses Sheet");
                                    currTrip.memberExpenseMap.forEach((i,j) -> System.out.println("Person " + i + " spent " + j));
                                    System.out.println();
                                   // System.out.println("Who owes How Much");
                                   // currTrip.membersOwesMap.forEach((i,j) -> System.out.println("Person " + i+" owes " + j));
                                    System.out.println("----------------------------------------------------------------");

 //                                    List<ExpenseDetails> expenses = currTrip.getAllTripExpenses();
//                                    for (ExpenseDetails e : expenses){
//                                        System.out.println(e);
//                                    }
                                    break;

                                } else if(sub_option == 3){ // displaying all the expenses
                                    currTrip.printExpenseDetails();
                                    System.out.println();
                                    break;
                                } else if (sub_option == 8){
                                      currTrip.showMembers();
                                      break;
                                }else if(sub_option == 9) break;
                                  else if(sub_option == 10){ // remove a member from the trip
                                    System.out.println("Select a user to delete:  Type 0 for back!!");
                                    List<Member> membersInTrip = currTrip.getMembersInTrip();
                                      for (int i=0; i<membersInTrip.size(); i++){
                                          System.out.println((1+i) + ". "+membersInTrip.get(i));
                                      }

                                      System.out.println("Option: ");
                                      int userIdxTORemove = scan.nextInt()-1;
                                      if (userIdxTORemove == -1) break;
                                      currTrip.removeMemberFromTrip(userIdxTORemove);
                                      //break;

                                }
                                  else if(sub_option == 7){ // show settlement history
                                    System.out.println("----------------------------------------------------------------");

                                    System.out.println("Showing Owes Settlement History: ");
                                      List<String> list = currTrip.getOwesHistory();
                                      //System.out.println(list);
                                      for (int i=0; i<list.size(); i++){
                                          System.out.println((i+1) + ". " + list.get(i));
                                      }
                                    System.out.println("----------------------------------------------------------------");
                                    break;
                                }
                                else {
                                    System.out.println("Enter a valid input: ");
                                    System.out.println();
                                    break;
                                }
                               // break;
                            }
                            if (sub_option == 9) {
                                System.out.println("-------------------------------------------------------------------------------------------");
                                break;
                            }
                        }
                    } else {
                        System.out.println("Please enter a valid input");
                        System.out.println();
                    }
                } else if(input.equals("4")) {
                     System.exit(0);
                } else {
                //break;
                     System.out.println("Enter a valid Input!!!");
                }
          } // end of the application
    } // end of the main method
} // end of the class


