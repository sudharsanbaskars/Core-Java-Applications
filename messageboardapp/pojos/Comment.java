package sudharsan.zoho_questions.messageboardapp.pojos;


public class Comment {
    private User commentedBy;
    private String comment;

    @Override
    public String toString() {
        return "Comment{" +
                "commentedBy=" + commentedBy +
                ", comment='" + comment + '\'' +
                '}';
    }

    public Comment(User user, String comment){
        this.commentedBy = user;
        this.comment = comment;
    }

    public User getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(User commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
