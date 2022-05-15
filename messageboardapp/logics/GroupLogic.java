package sudharsan.zoho_questions.messageboardapp.logics;

import sudharsan.zoho_questions.messageboardapp.dao.DataBase;
import sudharsan.zoho_questions.messageboardapp.pojos.Group;
import sudharsan.zoho_questions.messageboardapp.pojos.Post;
import sudharsan.zoho_questions.messageboardapp.pojos.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GroupLogic {
    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void displayAllGroupOperationsLogic(User currUser, Group currGroup, DataBase db) {
        while (true) {
            System.out.println("Welcome to the " + currGroup.getGroupName());

            // add user to the group if the current user is the post author
            if (currUser.equals(currGroup.getOwner())) {
                System.out.println("\t1. Add user to this group");
                System.out.println("\t2. View All posts from this group");
                System.out.println("\t3. View all members of the group");
                System.out.println("\t4. Remove User from the group");
                System.out.println("\t5. Delete Post from group");
                System.out.print("select the option(0 for back page): ");

                try {
                    int groupOption = Integer.parseInt(input.readLine().trim());

                    if (groupOption == 1) { // add user to the group
                        addGroupLogic(currUser, currGroup, db); // add group

                    } else if (groupOption == 2) { // display all posts in group
                        viewAllPostsFromGroupLogic(currUser, currGroup, db);

                    } else if (groupOption == 0) {
                        break; // exit

                    } else if (groupOption == 3) { // view all the members of the group
                        System.out.println("Members in group: ");
                        System.out.println("\t-> " + currGroup.getOwner().getUserName() + "(Owner)");
                        currGroup.getUserInGroup().stream().filter(j -> !j.equals(currGroup.getOwner().getUserId())).forEach(i -> System.out.println("\t-> " + db.getUserNameById(i)));

                    } else if (groupOption == 4) { // remove user from group
                        removeUserFromGroupLogic(currGroup, db);

                    } else if (groupOption == 5) { // delete post from group
                        deletePostFromGroup(currGroup, db);

                    } else {
                        System.out.println("Please select a valid option!!!!");
                    }
                } catch (Exception e) {
                    System.out.println("Please select a valid option from given options!!!!");
                }

            } else { // curr user is not the owner of this group
                System.out.println("\t1. View All posts");
                System.out.println("\t2. View all members of the group");
                System.out.print("select the option(0 for back page): ");

                try {
                    int groupOption = Integer.parseInt(input.readLine().trim());

                    if (groupOption == 1) { // view all posts from the group
                        viewAllPostsFromGroupLogic(currUser, currGroup, db);

                    } else if (groupOption == 2) { // view of all members
                        System.out.println("Members in group: ");
                        System.out.println("\tGroup Owner: " + currGroup.getOwner().getUserName());
                        currGroup.getUserInGroup().stream().filter(j -> !j.equals(currGroup.getOwner().getUserId())).forEach(i -> System.out.println("\t-> " + db.getUserNameById(i)));

                    } else if (groupOption == 0) {
                        break;

                    } else {
                        System.out.println("Please select a valid option!!!!");
                    }
                } catch (Exception e) {
                    System.out.println("Please Select a valid option from given options!!!");
                }
            }
        }
    }



    public static void deletePostFromGroup(Group currGroup, DataBase db) throws IOException { // delete post from user
        System.out.println("Which post do you want to delete: ");
        List<Integer> currGroupPosts = db.getAllPostsFromGroup(currGroup);

        if (currGroupPosts.isEmpty()) {
            System.out.println("No Post till now!!");

        } else {
            for (int j = 0; j < currGroupPosts.size(); j++) {
                System.out.println("\t" + (j + 1) + ". " + db.getPostById(currGroupPosts.get(j)).getHeading() + "(Created at " + Application.formatDateTime(db.getPostById(currGroupPosts.get(j)).getDataTime()) + ")");
            }
            // select the post
            System.out.print("Select the post to remove:(type 0 for back) ");
            int groupPostSelectOption = Integer.parseInt(input.readLine());

            if (groupPostSelectOption == 0) {
                System.out.println("Moving back!");
            }
            if (groupPostSelectOption < 0 || groupPostSelectOption > currGroupPosts.size()) {
                System.out.println("Please select a valid option!!!!");
            } else {
                Post selectGroupPost = db.getPostById(currGroupPosts.get(groupPostSelectOption - 1));
                db.removePostFromGroup(selectGroupPost);
                System.out.println("Post deleted successfully!!!!");
            }
        }
    }


    public static void removeUserFromGroupLogic(Group currGroup, DataBase db) throws IOException { // remove user from the group
        System.out.print("Please enter the Name of the User to remove: ");

        String userNameToRemove = input.readLine();

        if (!currGroup.containsUser(db.getUserByName(userNameToRemove))) {
            System.out.println("Seems this user is not available in this group!!!");
        } else if (userNameToRemove.equals(currGroup.getOwner().getUserName())) {
            System.out.println("Group Owner cannot be removed");
        } else {
            currGroup.removeUser(db.getUserByName(userNameToRemove));
            System.out.println("User removed from group successfully!!!");
        }
    }

    public static void viewAllPostsFromGroupLogic(User currUser, Group currGroup, DataBase db) throws IOException { // view all the post in group
        System.out.println("View all Posts");
        System.out.println("Posts: ");
        List<Integer> currGroupPosts = db.getAllPostsFromGroup(currGroup);

        if (currGroupPosts.isEmpty()) {
            System.out.println("No Post till now!!");

        } else {
            for (int j = 0; j < currGroupPosts.size(); j++) {
                System.out.println("\t" + (j + 1) + ". " + db.getPostById(currGroupPosts.get(j)).getHeading() + "(Created at " + Application.formatDateTime(db.getPostById(currGroupPosts.get(j)).getDataTime()) + ")");
            }
            // select the post
            System.out.print("Select the post:(type 0 for back) ");
            int groupPostSelectOption = Integer.parseInt(input.readLine());

            //if (groupPostSelectOption == 0) return;
            if (groupPostSelectOption < 0 || groupPostSelectOption > currGroupPosts.size()) {
                System.out.println("Please select a valid option!!!!");
            } else {
                Post selectGroupPost = db.getPostById(currGroupPosts.get(groupPostSelectOption - 1));

                Application.display(currUser, selectGroupPost, db);

            }
        }
    }

    public static void addGroupLogic(User currUser, Group currGroup, DataBase db) throws IOException { // add user to the group
        System.out.print("Type the user names to add into the group ");
        String user = input.readLine();

        if (currGroup.getUserInGroup().stream().anyMatch(k -> db.getUserNameById(k).equals(user)) || user.equals(currGroup.getOwner().getUserName())) {
            System.out.println(currGroup.getUserInGroup()); //verify
            System.out.println("Seems this user is already available inside this group");
        } else if (!db.getUsersInApp().containsKey(user)) {
            System.out.println(db.getUsersInApp());
            System.out.println("Seems this user in not signed up yet!! please signup");
        } else {
            User userToAdd = db.getUsersInApp().get(user);
            currGroup.addUser(userToAdd);
            System.out.println("User added successfully!!!!!");
            System.out.println();
        }
    }


    public static void groupCreationLogic(User currUser, DataBase db) throws IOException { // creating group
        System.out.print("Enter the unique group name: ");
        String str = input.readLine();
        ArrayList<User> users = new ArrayList<>();
        if (db.containsGroupWithName(str)) { // if group already present
            System.out.println("A Group with this name is already present!! Please try another name");

        } else {
            System.out.println("Enter the users to add in this group: (type 'stop' to stop)");
            while (true) {
                String name = input.readLine();
                if (name.equals("stop")) break;

                if (!db.getUsersInApp().containsKey(name)) {
                    System.out.println("Please enter a valid username who signed up in this app!!!");
                } else if (name.equals(currUser.getUserName())) {
                    System.out.println("No need to include your name!!!!");
                } else {
                    //User u = db.getUsersInApp().get(name);
                    users.add(db.getUserByName(name));
                }
            }
            users.add(currUser);
            Group newGroup = new Group(str, users, currUser, db); // creating a new group object
            db.addGroup(newGroup); // adding the group to the db

            System.out.println("Group created successfully!!!!");
        }
    }


}
