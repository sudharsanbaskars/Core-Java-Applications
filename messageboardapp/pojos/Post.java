package sudharsan.zoho_questions.messageboardapp.pojos;

import sudharsan.zoho_questions.messageboardapp.logics.Application;
import sudharsan.zoho_questions.messageboardapp.dao.DataBase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Post {

    private String heading;
    private String content;
    private int commentSetting;
    private int likeSetting;
    private final User postAuthor;
    private boolean isGlobalPost = true;
    private final LocalDateTime dateTime;
    private Integer groupIdBelongsTo = null;
    private int likes=0;
    private int dislikes=0;

    private ArrayList<Integer> likedBy = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Integer> dislikedBy = new ArrayList<>();
   // private HashMap<String, Integer> likedOrDislikedCounts = new HashMap<>();
    private Set<Integer> likesDislikesCount = new LinkedHashSet<>();

    private static int count = 0;
    private final int ID;


    public static HashMap<Integer, String> commentOptions= new HashMap<>();
    public static HashMap<Integer, String> likeOptions = new HashMap<>();

    public Post(User user, String heading, String content, int commentSetting, int likeSetting, DataBase db) {

        this.heading = heading;
        this.content = content;
        this.commentSetting = commentSetting;
        this.postAuthor = user;
        this.likeSetting = likeSetting;
        this.dateTime = LocalDateTime.now();
        this.ID = Application.generateUniqueIdForPost(db);
    }

    public Post(User user, String heading, String content, int commentSetting, int likeSetting, Integer groupIdBelongsTo, DataBase db) {
        this.heading = heading;
        this.content = content;
        this.commentSetting = commentSetting;
        this.postAuthor = user;
        this.likeSetting = likeSetting;
        this.dateTime = LocalDateTime.now();
        this.ID = Application.generateUniqueIdForPost(db);
        this.groupIdBelongsTo = groupIdBelongsTo;
    }

    public LocalDateTime getDataTime(){
        return this.dateTime;
    }

    public void setPostScope(boolean res){
        this.isGlobalPost = res;
    }

    public boolean isGlobalPost(){
        return this.isGlobalPost;
    }

    public Integer getGroupIdBelongsTo(){
        return this.groupIdBelongsTo;
    }

    public int getLikes(){
        return this.likes;
    }

    public int getDislikes(){
        return this.dislikes;
    }

    public boolean getPostScope(){
        return this.isGlobalPost;
    }

    public boolean isAlreadyLikedBy(User user){
        return likedBy.contains(user.getUserId() * -1);
    }

    public boolean containsUserInLikeDislike(User user){
        return this.likesDislikesCount.contains(user.getUserId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Post p = (Post)o;
        return p.getID() == this.ID;
    }

    @Override
    public int hashCode() {
        return this.ID;
    }

    public int getID(){
        return this.ID;
    }

    //////// for comments
    public ArrayList<Comment> getAllComments(){
        return this.comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
    ////////

    public boolean isUserCommented(User user){
        for (Comment c : this.comments){
            if (c.getCommentedBy().equals(user)){
                return true;
            }
        }
        return false;
    }

    public void inverseUserIdInDisliked(User user){
        if (this.dislikedBy.contains((Integer) user.getUserId())){
            int idx = dislikedBy.indexOf(user.getUserId());
            this.dislikedBy.set(idx, user.getUserId()*-1);
        }
    }

    public void inverseUserIdInLiked(User user){
        if (this.likedBy.contains(user.getUserId())){
            int idx = likedBy.indexOf(user.getUserId());
            this.likedBy.set(idx, user.getUserId()*-1);
        }
    }


    public void like(User likedUser){
        this.likes++;
        this.likedBy.add(likedUser.getUserId());
    }

    public void disLike(User user){
        this.dislikedBy.add(user.getUserId());
        this.dislikes++;
    }

    public ArrayList<Integer> getLikedUsers(){
        return this.likedBy;
    }

    public ArrayList<Integer> getDislikedUsers(){
        return this.dislikedBy;
    }

    public boolean isLikedBy(User user){
        return this.likedBy.contains(user.getUserId());
    }

    public boolean isDislikedBy(User user){
        return this.dislikedBy.contains(user.getUserId());
    }

    public void addInDislikeLikeSet(User user){
        this.likesDislikesCount.add(user.getUserId());
    }

    public void removeDislikedBy(User user){
        if (isDislikedBy(user)){
            this.dislikedBy.remove((Integer) user.getUserId());
            this.dislikes--;
        }
    }

    public String getScopeMessage(DataBase db){
        if (this.isGlobalPost) return "Global Post";
         else return db.getGroupById(this.groupIdBelongsTo).getGroupName();
    }

    public void removeLikedBy(User user){
        if (this.likedBy.contains((Integer) user.getUserId())){
            this.likedBy.remove((Integer) user.getUserId());
            this.likes--;
        }
    }

    public void reduceLikes(){
        this.likes--;
    }

    public void reduceDislikes(){
        this.dislikes--;
    }


    public User getPostAuthor(){
        return this.postAuthor;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentSetting() {
        return commentSetting;
    }

    public int getLikeSetting(){
        return this.likeSetting;
    }

    public void setCommentSetting(int commentSetting) {
        this.commentSetting = commentSetting;
    }
    public void setLikeSetting(int likeSetting){
        this.likeSetting = likeSetting;
    }

    static {
        commentOptions.put(1, "allow all comments");
        commentOptions.put(2, "block all comments");
        commentOptions.put(3, "allow one-time comments");

        likeOptions.put(1, "Allow Likes and dislikes");
        likeOptions.put(2, "Disallow likes and dislikes");
    }



    @Override
    public String toString() {
        return "Post{" +
                "heading='" + heading + '\'' +
                ", content='" + content + '\'' +
                ", commentSetting=" + commentSetting +
                ", postAuthor=" + postAuthor +
                ", likes=" + likes +
                ", ID=" + ID +
                '}';
    }
}
