package sudharsan.zoho_questions.messageboardapp.dao;

import sudharsan.zoho_questions.messageboardapp.pojos.Group;
import sudharsan.zoho_questions.messageboardapp.pojos.Post;
import sudharsan.zoho_questions.messageboardapp.pojos.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DataBase {

    private HashMap<String, User> users = new HashMap<>();
    private HashMap<Integer, Post> posts = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();

    ////////////////////////optimized
    public List<Integer> getAllPostsByUser(User user){
        List<Integer> postsByUser = new ArrayList<>();

        for (Post post : this.posts.values()){
            if (post.getPostAuthor().equals(user)){

                if (!post.isGlobalPost()) {
                    if(getGroupById(post.getGroupIdBelongsTo()).containsUser(user)){ // for removed users
                        postsByUser.add(post.getID());
                    }
                } else postsByUser.add(post.getID());
            }
        }

        Collections.sort(postsByUser, (i,j) -> {
            return getPostById(j).getDataTime().compareTo(getPostById(i).getDataTime());
        });
        return postsByUser;
    }

    public boolean containsGroupWithName(String name){
        for (Group g : this.groups.values()){
            if (g.getGroupName().equals(name)) return true;
        }
        return false;
    }

    public Group getGroupByName(String name){
        for (Group g : this.groups.values()){
            if (g.getGroupName().equals(name)){
                return g;
            }
        }
        return null;
    }

    public ArrayList<String> getAllGroupsForUser(User user){
        ArrayList<String> list = new ArrayList<>();

        for (Group group : this.groups.values()){
            if (group.containsUser(user)){
                list.add(group.getGroupName());
            }
        }
        return list;
    }

    public ArrayList<Integer> getAllPostsFromGroup(Group group){
        ArrayList<Integer> posts = new ArrayList<>();

        for (Post post : this.posts.values()){
            if (!post.isGlobalPost() && post.getGroupIdBelongsTo().equals(group.getGroupID())){
                posts.add(post.getID());
            }
        }
        Collections.sort(posts, (i,j) -> {
            return getPostById(j).getDataTime().compareTo(getPostById(i).getDataTime());
        });
        return posts;
    }

    public void removePostFromGroup(Post post){
        this.posts.remove(post.getID());
    }

    public ArrayList<Integer> getAllPostsForUser(User user){
        ArrayList<Integer> list = new ArrayList<>();

        for (Post post : this.posts.values()){
            if (post.isGlobalPost()){
                list.add(post.getID());
            } else {
                if (this.groups.get(post.getGroupIdBelongsTo()).containsUser(user)){
                    list.add(post.getID());
                }
            }
        }

        Collections.sort(list, (i,j) -> {
            return getPostById(j).getDataTime().compareTo(getPostById(i).getDataTime());
        });
        return list;
    }

    ////////////////////////end optimized

    public void deletePostFromDb(Integer id){
        this.posts.remove(id);
    }

    public Group getGroupById(Integer id){
        return this.groups.get(id);
    }

    public void addGroup(Group newGroup){
        this.groups.put(newGroup.getGroupID(), newGroup);
    }

//    public Group getGroupByName(String name){
//        return this.groups.get(name);
//    }

    public Post getPostById(int id){
        return this.posts.get(id);
    }

    public String getUserNameById(int id){
        for (User user : this.users.values()){
            if (user.getUserId() == id){
                return user.getUserName();
            }
        }

        return null;
    }

    public boolean containsEmail(String email){
        return this.users.values().stream().map(user -> (User) user).anyMatch(us -> us.getEmail().equals(email));
    }

    public User getUserByName(String name){
        return this.users.get(name);
    }

    public void addNewPost(Post newPost) {
        this.posts.put(newPost.getID(), newPost);
    }
////////////////////////////////////
    public HashMap<Integer, Group> getGroups(){
        return this.groups;
    }

    public void insertUserToApp(User newUser){
        this.users.put(newUser.getUserName(), newUser);
    }

    public HashMap<String, User> getUsersInApp(){
        return this.users;
    }

    public HashMap<Integer, Post> getAllPosts(){
        return this.posts;
    }

    public void replacePostById(int id, Post p){
        for (Post post: this.posts.values()){
            if (post.equals(p)){
                posts.replace(post.getID(), p);
            }
        }
    }

    public void updateUserInDB(User user){
        this.users.replace(user.getUserName(), user);
    }

}
