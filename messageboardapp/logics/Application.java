package sudharsan.zoho_questions.messageboardapp.logics;

import sudharsan.zoho_questions.messageboardapp.dao.DataBase;
import sudharsan.zoho_questions.messageboardapp.pojos.Comment;
import sudharsan.zoho_questions.messageboardapp.pojos.Post;
import sudharsan.zoho_questions.messageboardapp.pojos.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Application {

    static Scanner scan = new Scanner(System.in);
    static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void signUp(DataBase db) throws  Exception{
        while (true){
            System.out.print("\tEnter User Name: ");
            String userName = input.readLine();

            if (db.getUsersInApp().containsKey(userName)) {
                System.out.println("\tThe User name  already available in our system!! Please try again with other unique username\n");
            } else {
                System.out.print("\tEnter the email: ");
                String email = input.readLine();
                if (db.containsEmail(email)) {
                    System.out.println("\tThe Email already available in our system!! Please try again with other unique email\n");

                } else if (!isValidEmail(email)){
                    System.out.println("\tPlease Enter a valid email id\n");
                } else {
                    System.out.print("\tEnter the password: ");
                    String password = input.readLine();

                    System.out.print("\tConfirm Password: ");
                    String rePassword = input.readLine();

                    if (!password.equals(rePassword)) {
                        System.out.println("\tPlease Enter the same password!!!!\n");
                    } else {
                        User newUser = new User(userName, email, password);
                        db.insertUserToApp(newUser);
                        System.out.println("User added to the app successfully!!!!\n");
                        break;
                    }
                }
            }
        }
    }

    public static void viewAllPostsLogic(User currUser, DataBase db){ // view all posts available

        List<Integer> currAvailablePosts = db.getAllPostsForUser(currUser);

        if (currAvailablePosts.isEmpty()) {
            System.out.println("No Post Yet!!!!");
        } else {
            System.out.println("Viewing All the Posts: ");

            for (int i = 0; i < currAvailablePosts.size(); i++) {
                Post postToDisplay = db.getPostById(currAvailablePosts.get(i));
                System.out.println("\t" + (i + 1) + ". " + postToDisplay.getHeading() + "(" + postToDisplay.getScopeMessage(db) + ") posted on " + Application.formatDateTime(postToDisplay.getDataTime()));
            }

            System.out.println();
            System.out.print("Select an option:(0 for back) ");

            try {
                int postSelectionOption = Integer.parseInt(input.readLine().trim());

                if (postSelectionOption == 0) {
                } else if (postSelectionOption <= 0 || postSelectionOption - 1 >= currAvailablePosts.size()) {
                    System.out.println("Please enter a valid option!!!!");
                } else {
                    Post currSelectedPost = db.getPostById(currAvailablePosts.get(postSelectionOption - 1));
                    Application.display(currUser, currSelectedPost, db);
                }
            } catch (Exception e){
                System.out.println("Please select a valid option from given options!!!");
            }
        }
    }



    public static void editSettingLogic(User currUser, DataBase db){ // edit the post settings

        List<Integer> currUserPosts = db.getAllPostsByUser(currUser);
        if (currUserPosts.isEmpty()) {
            System.out.println("You Have Not Posted Anything!!!!!");

        } else {
            System.out.println("My Posts: ");

            for (int i = 0; i < currUserPosts.size(); i++) {
                System.out.println("\t" + (i + 1) + ". " + db.getPostById(currUserPosts.get(i)).getHeading());
            }

            System.out.print("Select an option to modify the setting: ");
            try {
                int modifySettingOption = Integer.parseInt(input.readLine());
                if (modifySettingOption <= 0 || modifySettingOption - 1 > currUserPosts.size() - 1) {
                    System.out.println("Please enter a valid input!!!!");
                    //break;
                } else {
                    Post modifySettingPost = db.getPostById(currUserPosts.get(modifySettingOption - 1));

                    System.out.println("Title: " + modifySettingPost.getHeading());
                    System.out.println("Comments: " + Post.commentOptions.get(modifySettingPost.getCommentSetting()));
                    System.out.println("Likes: " + Post.likeOptions.get(modifySettingPost.getLikeSetting()));

                    System.out.print("Do you want to change the comment settings??(y/n): ");
                    String changeOrNot = input.readLine();
                    if (changeOrNot.equals("y")) {

                        System.out.println("\t1. " + Post.commentOptions.get(1));
                        System.out.println("\t2. " + Post.commentOptions.get(2));
                        System.out.println("\t3. " + Post.commentOptions.get(3));

                        System.out.print("Select to set the comment setting: ");
                        int commentOption = Integer.parseInt(input.readLine());

                        modifySettingPost.setCommentSetting(commentOption);

                        System.out.println("Setting Modified to " + Post.commentOptions.get(commentOption) + " successfully!!!!!"); //commentOption
                    }
                    // editing the likes settings

                    System.out.print("Do you want to change the like settings??(y/n): ");
                    String likeChangeOrNot = input.readLine();

                    if (likeChangeOrNot.equals("y")) {
                        System.out.println("1. Allow Likes");
                        System.out.println("2. Disallow Likes");

                        System.out.print("Select option to change the settings: ");
                        int likeSelectedOption = Integer.parseInt(input.readLine());

                        if (likeSelectedOption > 2 || likeSelectedOption < 1)
                            System.out.println("Please select a valid option!!!");
                        else {
                            modifySettingPost.setLikeSetting(likeSelectedOption);
                            System.out.println("Setting Modified to " + Post.likeOptions.get(likeSelectedOption) + " successfully!!!!!");
                        }
                    }
                }
            } catch (Exception e){
                System.out.println("Please select from given options!!!");
            }
        }
    }


    public static void createNewPostLogic(User currUser, DataBase db) throws IOException { // create new logic
        { // create new post
            System.out.print("\tEnter the post Heading: ");
            String postHeading = input.readLine();

            System.out.print("\tEnter the Post Content: ");
            String postContent = input.readLine();
            Post newPost;

            while (true) {
                System.out.println("\t1. Allow all comments");
                System.out.println("\t2. Block All comments");
                System.out.println("\t3. Allow one-time comments");
                System.out.print("Select to set comment settings (0 for back): ");

                try {
                    int commentSetting = Integer.parseInt(input.readLine());

                    if (commentSetting == 0) break;
                    else if (commentSetting > 3 || commentSetting < 0) {
                        System.out.println("Please select a valid option!!!!");
                    } else {
                        System.out.println("\t1. Allow Likes");
                        System.out.println("\t2. Disallow Likes");
                        System.out.print("select to set like settings: ");

                        try {
                            int likeSetting = Integer.parseInt(input.readLine());

                            if (likeSetting <= 0 || likeSetting > 2) {
                                System.out.println("Please select a valid option!!!!");
                            } else {
                                // newPost = new Post(currUser, postHeading, postContent, commentSetting, likeSetting);
                                System.out.print("Do you want to post on global?????(y/n): ");
                                String choice = input.readLine();

                                if (choice.equals("y")){ // create global post
                                    newPost = new Post(currUser, postHeading, postContent, commentSetting, likeSetting, db); // global post

                                    db.addNewPost(newPost); // adding new post to the db

                                    System.out.println("Post created successfully!!!\n");

                                } else if (choice.equals("n")) { // create group post
                                    List<String> groups = db.getAllGroupsForUser(currUser);
                                    while (true) {
                                        System.out.println("Available Groups");
                                        if (groups.isEmpty()){
                                            System.out.println("Groups are empty!!!");
                                            break;
                                        } else {
                                            for (int i = 0; i < groups.size(); i++) {
                                                System.out.println("\t" + (i + 1) + ". " + groups.get(i));
                                            }

                                            System.out.print("Enter the option:(0 for back) ");

                                            try {
                                                int opt = Integer.parseInt(input.readLine());

                                                if (opt == 0) {
                                                    break;
                                                } else {
                                                    String currSelectedGroupName = groups.get(opt - 1);
                                                    newPost = new Post(currUser, postHeading, postContent, commentSetting, likeSetting, db.getGroupByName(currSelectedGroupName).getGroupID(), db); // group post
                                                    newPost.setPostScope(false);

                                                    db.addNewPost(newPost);
                                                    groups.remove(opt-1);
                                                    System.out.println("Post created and added to your group successfully!!!!");
                                                    //break;
                                                }

                                            } catch (Exception e) {
                                                System.out.println("Please select a given option!!!");
                                            }
                                        }
                                        /////////
                                    }
                                } else {
                                    System.out.println("Please select a valid input!!");
                                }// end of new while
                                break;
                            }
                        } catch (Exception e){
                            System.out.println("Please select a option from given options!");
                        }// for putting the post in specified group
                    }
                } catch (Exception e){
                    System.out.println("Please select a option from given options!!!");
                }
            }
            // post creation over
        }
    }

    public static void deletePostLogic(User currUser, DataBase db){
        List<Integer> currUserPosts = db.getAllPostsByUser(currUser);
        if (currUserPosts.isEmpty()) {
            System.out.println("You Have Not Posted Anything!!!!!");

        } else {
            System.out.println("My Posts: ");

            for (int i = 0; i < currUserPosts.size(); i++) {
                System.out.println("\t" + (i + 1) + ". " + db.getPostById(currUserPosts.get(i)).getHeading());
            }

            System.out.print("Select an option to delete: ");
            try {
                int deletePostOption = Integer.parseInt(input.readLine());
                if (deletePostOption <= 0 || deletePostOption - 1 > currUserPosts.size() - 1) {
                    System.out.println("Please enter a valid input!!!!");
                    //break;
                } else {
                    Post modifySettingPost = db.getPostById(currUserPosts.get(deletePostOption - 1));

                    System.out.print("Do you want to surely delete this post??(y/n): ");
                    String changeOrNot = input.readLine();
                    if (changeOrNot.equals("y")) {
                        db.deletePostFromDb((Integer) modifySettingPost.getID());
                        System.out.println("Post deleted successfully!!!!!");
                    }

                }
            } catch (Exception e){
                System.out.println("Please select from given options!!!");
            }
        }
    }



    public static void display(User currUser, Post currGroupPost, DataBase db) throws IOException {
        while (true) {
            System.out.println();
            System.out.println("Heading: " + currGroupPost.getHeading());
            System.out.println("Posted on: " + formatDateTime(currGroupPost.getDataTime()));
            System.out.println("Content: " + currGroupPost.getContent());
            System.out.println("Author: " + currGroupPost.getPostAuthor());
            System.out.println("Likes: " + currGroupPost.getLikes() + "\t Dislikes: " + currGroupPost.getDislikes());


            // display who liked and disliked this post only for post owner
            if (currUser.equals(currGroupPost.getPostAuthor())) {
                List<Integer> likedUsers = currGroupPost.getLikedUsers();
                if (!likedUsers.isEmpty()) {
                    System.out.println("Users Who Liked this Post: ");
                    for (int i = 0; i < likedUsers.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". " + db.getUserNameById(likedUsers.get(i)));
                    }
                }

                List<Integer> disLikedUsers = currGroupPost.getDislikedUsers();
                if (!disLikedUsers.isEmpty()) {
                    System.out.println("Users Who Disliked this Post: ");
                    for (int i = 0; i < disLikedUsers.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". " + db.getUserNameById(disLikedUsers.get(i)));
                    }
                }
            }

            System.out.println("Comments: ");
            if (!currGroupPost.getAllComments().isEmpty()) {
                currGroupPost.getAllComments().forEach(i -> System.out.println("\t-> " + i.getCommentedBy() + ": " + i.getComment()));
            }


            if (currGroupPost.getCommentSetting() == 2) { // block all content
                System.out.println("--------Comment section of this particular post is blocked!!!----------");

            } else if (currGroupPost.getCommentSetting() == 3) { // allow one time comment
                if (currGroupPost.isUserCommented(currUser)) {
                    System.out.println("----------You have already commented on this post!!--------------------");
                }
            }

            int totalFlag = 0, likeFlag = 0, commentFlag = 0;

            if (currGroupPost.getCommentSetting() == 1 || (currGroupPost.getCommentSetting() == 3 && !currGroupPost.isUserCommented(currUser))) {
                totalFlag += 1;
                commentFlag += 1;
            }

            if ((currGroupPost.getLikeSetting() == 1) && !(currGroupPost.containsUserInLikeDislike(currUser))) { // (currGroupPost.isDislikedBy(currUser)) && (currGroupPost.isLikedBy(currUser))
                totalFlag += 1;
                likeFlag += 1;
            }

            if (totalFlag == 2) { // display both like and comment
                if (currGroupPost.isDislikedBy(currUser)) System.out.println("1. Comment | 2. Like | 3. Back");
                else if (currGroupPost.isLikedBy(currUser)) System.out.println("1. Comment | 2. DisLike | 3. Back");
                else System.out.println("1. Comment | 2. Like | 3. Dislike | 4. Back");
                try {
                    System.out.print("Select a option: ");
                    int option = Integer.parseInt(input.readLine());

                    if (option == 1) addComment(currUser, currGroupPost, db);
                    else if (option == 2) likeLogic(currUser, currGroupPost, option);
                    else if (option == 3 && !currGroupPost.isLikedBy(currUser) && !currGroupPost.isDislikedBy(currUser)) likeLogic(currUser, currGroupPost, option);
                    else if (option == 3 && (currGroupPost.isDislikedBy(currUser) || currGroupPost.isLikedBy(currUser))) break;
                    else if (option == 4) break;
                    else System.out.println("Please Select a valid option!!!");

                } catch (Exception e) {
                    System.out.println("Please Select a valid option provided!!");
                }

            } else if (totalFlag == 1) { // either to display like or comment
                if (commentFlag == 1) { // if want to display comment alone
                    System.out.println("1. Comment | 2. Go Back");
                    try {
                        System.out.print("Select a option: ");
                        int option = Integer.parseInt(input.readLine());

                        if (option == 1) addComment(currUser, currGroupPost, db);
                        else if (option == 2) break;
                        else System.out.println("Please Select a valid option!!!");
                    } catch (Exception e) {
                        System.out.println("Please Select a valid option provided!!");
                    }

                } else { // display like alone
                    if (currGroupPost.isDislikedBy(currUser)) System.out.println("1. Like | 2. Back");
                    else if (currGroupPost.isLikedBy(currUser)) System.out.println("1. DisLike | 2. Back");
                    else System.out.println("1. Like | 2. Dislike | 3. Back");
                    try {
                        System.out.print("Select a option: ");
                        int option = Integer.parseInt(input.readLine());

                        if (option == 1) likeLogic(currUser, currGroupPost, option + 1);
                        else if (option == 2 && !currGroupPost.isLikedBy(currUser) && !currGroupPost.isDislikedBy(currUser)) likeLogic(currUser, currGroupPost, option+1);
                        else if (option == 2 && (currGroupPost.isDislikedBy(currUser) || currGroupPost.isLikedBy(currUser))) break;
                        else if (option ==3) break;
                        else System.out.println("Please Select a valid option!!!");
                    } catch (Exception e) {
                        System.out.println("Please Select a valid option provided!!");
                    }
                }

            } else { // neither like nor comment
                System.out.println("1. Go Back");
                try {
                    System.out.print("Select a option: ");
                    int option = Integer.parseInt(input.readLine());

                    if (option == 1) break;
                    else System.out.println("Please Select a valid option!!!");
                } catch (Exception e) {
                    System.out.println("Please Select a valid option provided!!");
                }
            }
        }
    }

    public static void likeLogic(User currUser, Post currPost, int option) {
        if (!currPost.isLikedBy(currUser) && !currPost.isDislikedBy(currUser)) { // if not liked as well as disliked
            if (option == 2) likePost(currPost, currUser);
            else if (option == 3) dislikePost(currPost, currUser);

        } else if (!currPost.isDislikedBy(currUser) && option == 2) dislikePost(currPost, currUser);

        else if (!currPost.isLikedBy(currUser) && option == 2) likePost(currPost, currUser);
    }

   public static void likePost(Post currPost, User currUser) {
        if (currPost.isLikedBy(currUser)) {
            System.out.println("This post has been liked by you already!!!");

        } else {
            currPost.like(currUser);
            if (currPost.isDislikedBy(currUser)) {
                //currPost.inverseUserIdInDisliked(currUser); // change the user id to its negative
                // currPost.reduceDislikes();
                currPost.addInDislikeLikeSet(currUser);
                currPost.removeDislikedBy(currUser);
            }
            System.out.println("Post has been liked successfully!!!");
        }
    }

    public static void addComment(User currUser, Post currPost, DataBase db) throws IOException {
        System.out.print("Enter your Comment: ");
        String message = input.readLine().trim();
        Comment comment = new Comment(currUser, message);
        //db.updateUserInDB(currUser); // changing the updated user to the database
        currPost.addComment(comment);
        System.out.println("Comment added successfully!!!!\n");
    }


    public static void dislikePost(Post currPost, User currUser) {
        if (currPost.isDislikedBy(currUser)) {
            System.out.println("This post has been disliked by you already!!!");
        } else {
            currPost.disLike(currUser);
            if (currPost.isLikedBy(currUser)) {
                //currPost.inverseUserIdInLiked(currUser);
                // currPost.reduceLikes();
                currPost.removeLikedBy(currUser);
                currPost.addInDislikeLikeSet(currUser);
            }
            System.out.println("Post disliked!!!");
        }
    }

    public static String formatDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static final Random random = new Random();

    public static Integer generateUniqueIdForGroup(DataBase db){
        int res;
        while (true){
            res = random.nextInt(9999);
            if (!db.getGroups().containsKey(res)) break;
        }
        return res;
    }

    public static Integer generateUniqueIdForPost(DataBase db){
        int res;
        while (true){
            res = random.nextInt(9999);
            if (!db.getAllPosts().containsKey(res)) break;
        }
        return res;
    }


    public static boolean isValidEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[a-zA-Z]{2,3}$";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.matches()) return true;
        else return false;
    }

}






//    public static void signUpLogic(DataBase db) throws IOException { // sign up
//        System.out.print("\tEnter User Name: ");
//        String userName = input.readLine();
//
//        System.out.print("\tEnter the email: ");
//        String email = input.readLine();
//
//        System.out.print("\tEnter the password: ");
//        String password = input.readLine();
//
//        System.out.print("\tConfirm Password: ");
//        String rePassword = input.readLine();
//
//        // validate
//        if (!password.equals(rePassword)) {
//            System.out.println("Please Enter the same password!!!!");
//
//        } else if (db.getUsersInApp().containsKey(userName) || db.containsEmail(email)) {
//            System.out.println("The User name or email already available in our system!! Please try again with other unique username or email");
//
//        } else if (!isValidEmail(email)){
//            System.out.println("Please Enter a valid email id");
//
//        }else {
//            User newUser = new User(userName, email, password);
//            db.insertUserToApp(newUser);
//            System.out.println("User added to the app successfully!!!!\n");
//        }
//    }

