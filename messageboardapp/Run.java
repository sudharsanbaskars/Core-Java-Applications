package sudharsan.zoho_questions.messageboardapp;

import sudharsan.zoho_questions.messageboardapp.dao.DataBase;
import sudharsan.zoho_questions.messageboardapp.logics.Application;
import sudharsan.zoho_questions.messageboardapp.logics.GroupLogic;
import sudharsan.zoho_questions.messageboardapp.pojos.Group;
import sudharsan.zoho_questions.messageboardapp.pojos.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Run {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DataBase db = new DataBase();

        System.out.println("Welcome to the message board\n");

        while (true) {
            System.out.println("0. Exit");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("Select the option");
            String firstOption = scan.next();

            switch (firstOption) { //
                case "1" : {  // signup
                    Application.signUp(db);
                    break;
                }

                case "3" : // displaying the users in the app
                        db.getUsersInApp().forEach((i, j) -> System.out.println(i));
                        break;

                case "0" : {  // exiting the app
                    System.out.println("Exiting the app!!");
                    System.exit(0);
                }

                case "2" : {  // sign in
                    User currUser;

                    while (true) {
                        System.out.print("\tEnter the user name: ");
                        String userName = input.readLine();

                        System.out.print("\tEnter the password: ");
                        String password = input.readLine();

                        if (db.getUsersInApp().containsKey(userName)) { // if username is available
                            String userPassword = db.getUsersInApp().get(userName).getPassword();

                            if (!password.equals(userPassword)) { // validating the password
                                System.out.println("Your password is mismatching!!! Please try again!!!");
                            } else {
                                ////////// going inside the app
                                currUser = db.getUsersInApp().get(userName);
                                while (true) { //
                                    System.out.println();
                                    System.out.println("Welcome " + currUser.getUserName());
                                    System.out.println("\t1. Create new Post");
                                    System.out.println("\t2. View all Posts");
                                    System.out.println("\t3. Edit My Post Settings");
                                    System.out.println("\t4. Create Group");
                                    System.out.println("\t5. View all Groups");
                                    System.out.println("\t6. Delete my posts");
                                    System.out.println("\t7. Logout");
                                    System.out.print("Enter the option: ");

                                    try {
                                        int subOption = Integer.parseInt(input.readLine());
                                        if (subOption == 7) { // logout
                                            System.out.println("User logged out successfully!!!!");
                                            break;

                                        } else if (subOption == 1) { // create new post logic
                                            Application.createNewPostLogic(currUser, db);

                                        } else if (subOption == 2) { // view all posts
                                            Application.viewAllPostsLogic(currUser, db);

                                        } else if (subOption == 3) { // edit setting
                                            Application.editSettingLogic(currUser, db);

                                        } else if (subOption == 4) { // group creation
                                            GroupLogic.groupCreationLogic(currUser, db);

                                        } else if(subOption == 6){
                                            Application.deletePostLogic(currUser, db);

                                        } else if (subOption == 5) { // view all group and select it
                                            ArrayList<String> myGroups = db.getAllGroupsForUser(currUser);//currUser.getGroups();

                                            if (myGroups.isEmpty()) {
                                                System.out.println("Seems you are not in any of the group");

                                            } else {
                                                System.out.println("View of All Groups of " + currUser.getUserName());
                                                for (int i = 0; i < myGroups.size(); i++) {
                                                    System.out.println((i + 1) + ". " + myGroups.get(i));
                                                }

                                                System.out.print("Select the option to enter the group:(0 for back) ");
                                                int groupSelectionOption = Integer.parseInt(input.readLine());


                                                if (groupSelectionOption < 0 || groupSelectionOption > myGroups.size()) {
                                                    System.out.println("Please Select a valid option!!!!");
                                                } else if (groupSelectionOption == 0) {
                                                    System.out.println("Moving back!");
                                                } else {
                                                    Group currGroup = db.getGroupByName(myGroups.get(groupSelectionOption - 1));
                                                    System.out.println();

                                                    GroupLogic.displayAllGroupOperationsLogic(currUser, currGroup, db); // display all group operation
                                                }
                                            }
                                        }
                                    }
                                    catch (Exception e){
                                        System.out.println("Please select a given option!!");
                                    }
                                }// end of the second while loop after user sign in
                                //break;
                            }
                        } else { // if username is not available
                            System.out.println("Seems you are not signed up!! please sign up before signing in");
                            break;
                        }
                        break;
                    }
                }
                break;

                default : System.out.println("Please enter a valid option");
            }
        } // end of the first while loop
    } // end of the main method
}