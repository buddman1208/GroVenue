package kr.edcan.grovenue.model;

/**
 * Created by Junseok on 2016-09-30.
 */
public class Star {
    private int score;
    private String title, content, spot;
    private User owner;

    public Star(int score, String title, String content, User owner, String spot) {
        this.score = score;
        this.content = content;
        this.owner = owner;
        this.spot = spot;
    }


    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getOwner() {
        return owner;
    }

    public String getSpot() {
        return spot;
    }
}
