package kr.edcan.grovenue.model;

/**
 * Created by 최예찬 on 2016-10-10.
 */
public class SpotQuery {
    private int purpose = 0;
    private int budget = -1;
    private int maxDistanceType = -1;
    private int minScore = -1;

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getMaxDistanceType() {
        return maxDistanceType;
    }

    public void setMaxDistanceType(int maxDistanceType) {
        this.maxDistanceType = maxDistanceType;
    }

    public int getMaxDistance(){
        switch (maxDistanceType){
            case 0:
                return 100;
            case 1:
                return 500;
            case 2:
                return 1000;
            case 3:
                return 10000;
            case 4:
                return 50000;
            default:
                return 1000000;
        }
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }
}
