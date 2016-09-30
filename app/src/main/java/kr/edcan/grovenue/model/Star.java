package kr.edcan.grovenue.model;

/**
 * Created by Junseok on 2016-09-30.
 */
public class Star {
    private int score;
    private String content, owner, spot;

    public Star(int score, String content, String owner, String spot) {
        this.score = score;
        this.content = content;
        this.owner = owner;
        this.spot = spot;
    }

    public int getScore() {
        return score;
    }

    public String getContent() {
        return content;
    }

    public String getOwner() {
        return owner;
    }

    public String getSpot() {
        return spot;
    }
}
