package kr.edcan.grovenue.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityFilterBinding;
import kr.edcan.grovenue.model.SpotQuery;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.view.RoundTextView;

public class FilterActivity extends BaseActivity {

    ActivityFilterBinding binding;
    ArrayList<RoundTextView> purposeTag;
    ArrayList<RoundTextView> budgetTag;
    ArrayList<RoundTextView> maxDistanceTypeTag;
    ArrayList<RoundTextView> minScoreTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;

        ab.setDisplayHomeAsUpEnabled(true);
        setTitle("검색 필터");
    }


    private void setDefault() {
        purposeTag = new ArrayList<>(Arrays.asList(binding.filterPurpose0, binding.filterPurpose1, binding.filterPurpose2, binding.filterPurpose3, binding.filterPurpose4, binding.filterPurpose5, binding.filterPurpose6, binding.filterPurpose7));
        budgetTag = new ArrayList<>(Arrays.asList(binding.filterBudget0, binding.filterBudget1, binding.filterBudget2));
        maxDistanceTypeTag = new ArrayList<>(Arrays.asList(binding.filterMaxDistance0, binding.filterMaxDistance1, binding.filterMaxDistance2, binding.filterMaxDistance3, binding.filterMaxDistance4));
        minScoreTag = new ArrayList<>(Arrays.asList(binding.filterMinScore1, binding.filterMaxScore2, binding.filterMinScore3, binding.filterMinScore4, binding.filterMinScore5));
        for (final RoundTextView c : purposeTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.toggleFillEnabled();
                    changePurposeData(c);
                }
            });
        }
        for (final RoundTextView c : budgetTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.toggleFillEnabled();
                    changeBudgetData(c);
                }
            });
        }
        for (final RoundTextView c : maxDistanceTypeTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.toggleFillEnabled();
                    changeMaxDistanceData(c);
                }
            });
        }
        for (final RoundTextView c : minScoreTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.toggleFillEnabled();
                    changeMinScoreData(c);
                }
            });
        }

        syncFromSpotQuery();

    }

    
    private void syncFromSpotQuery(){
        SpotQuery query = DataManager.INSTANCE.getSpotQuery();

        int purpose = query.getPurpose();
        for(int i=0; i<purposeTag.size(); i++){
            int mask = 1 << i;
            purposeTag.get(i).setFillEnabled((mask & purpose) == mask);
        }
        int budget = query.getBudget();
        for(int i=0; i<budgetTag.size(); i++){
            budgetTag.get(i).setFillEnabled(i == budget);
        }
        int maxDistanceType = query.getMaxDistanceType();
        for(int i = 0; i< maxDistanceTypeTag.size(); i++){
            maxDistanceTypeTag.get(i).setFillEnabled(i == maxDistanceType);
        }
        int minScore = query.getMinScore();
        for(int i = 0; i< minScoreTag.size(); i++){
            minScoreTag.get(i).setFillEnabled(i + 1 == minScore);
        }
    }
    private void changePurposeData(RoundTextView c) {
        SpotQuery query = DataManager.INSTANCE.getSpotQuery();
        int purpose = query.getPurpose();
        purpose ^= 1 << purposeTag.indexOf(c);
        query.setPurpose(purpose);
        DataManager.INSTANCE.saveSpotQuery();
    }

    private void changeBudgetData(RoundTextView c) {
        SpotQuery query = DataManager.INSTANCE.getSpotQuery();
        int budget = query.getBudget();
        int tag = budgetTag.indexOf(c);
        if (budget != tag){
            if(budget != -1) budgetTag.get(budget).setFillEnabled(false);
            query.setBudget(tag);
        }
        else query.setBudget(-1);
        DataManager.INSTANCE.saveSpotQuery();
    }

    private void changeMaxDistanceData(RoundTextView c) {
        SpotQuery query = DataManager.INSTANCE.getSpotQuery();
        int maxDistanceType = query.getMaxDistanceType();
        int tag = maxDistanceTypeTag.indexOf(c);
        if (maxDistanceType != tag){
            if(maxDistanceType != -1) maxDistanceTypeTag.get(maxDistanceType).setFillEnabled(false);
            query.setMaxDistanceType(tag);
        }
        else query.setMaxDistanceType(-1);
        DataManager.INSTANCE.saveSpotQuery();
    }


    private void changeMinScoreData(RoundTextView c) {
        SpotQuery query = DataManager.INSTANCE.getSpotQuery();
        int minScore = query.getMinScore();
        int tag = minScoreTag.indexOf(c);
        if (minScore-1 != tag){
            if(minScore != -1) minScoreTag.get(minScore-1).setFillEnabled(false);
            query.setMinScore(tag+1);
        }
        else query.setMinScore(-1);
        DataManager.INSTANCE.saveSpotQuery();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
