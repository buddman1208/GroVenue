package kr.edcan.grovenue.model;

import java.util.ArrayList;

/**
 * Created by 최예찬 on 2016-10-10.
 */
public class SpotResult {
    private ArrayList<Spot> result;
    private boolean hasMore;

    public SpotResult(ArrayList<Spot> result, boolean hasMore) {
        this.result = result;
        this.hasMore = hasMore;
    }

    public ArrayList<Spot> getResult() {
        return result;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
