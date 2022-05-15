package sudharsan.zoho_questions.messageboardapp.pojos;


import sudharsan.zoho_questions.messageboardapp.logics.Application;
import sudharsan.zoho_questions.messageboardapp.dao.DataBase;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Group {
    private Integer groupID;
    private String GroupName;
    private User owner;
    private ArrayList<Integer> userInGroup = new ArrayList<>();
   // private ArrayList<Integer> groupPosts = new ArrayList<>();

    public Group(String groupName, ArrayList<User> userObjectInGroup, User owner, DataBase db) {
        GroupName = groupName;
        this.userInGroup = (ArrayList<Integer>) userObjectInGroup.stream().map(User::getUserId).collect(Collectors.toList());
        this.groupID = Application.generateUniqueIdForGroup(db);
        this.owner = owner;

    }


    public boolean containsUser(User name){
        return userInGroup.contains(name.getUserId());
    }


    public User getOwner(){
        return this.owner;
    }

    public void addUser(User user){
        this.userInGroup.add(user.getUserId());
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public ArrayList<Integer> getUserInGroup() {
        return userInGroup;
    }

    public void setUserInGroup(ArrayList<Integer> userInGroup) {
        this.userInGroup = userInGroup;
    }

    @Override
    public String toString() {
        return "Group{" +
                "GroupName='" + GroupName + '\'' +
                '}';
    }


    public void removeUser(User userName){
        this.userInGroup.remove((Integer) userName.getUserId());
    }

}
