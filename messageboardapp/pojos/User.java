package sudharsan.zoho_questions.messageboardapp.pojos;

import sudharsan.zoho_questions.messageboardapp.dao.DataBase;

import java.util.Objects;

public class User {
    private final int id;
    private String userName;
    private String password;
    private String email;

    //private ArrayList<Integer> userPosts = new ArrayList<>();
    //private ArrayList<String> groupsIn = new ArrayList<>();

    private static int counter = 0;
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.id = ++counter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId(){
        return this.id;
    }

    public void setUserPostById(int idx, Post post, DataBase db){
        db.getPostById(idx).setCommentSetting(post.getCommentSetting());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUserName().equals(user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }
}
