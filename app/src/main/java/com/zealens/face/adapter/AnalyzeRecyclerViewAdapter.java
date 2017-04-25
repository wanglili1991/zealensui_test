package com.zealens.face.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.util.ViewUtil;

import junit.framework.Assert;

/**
 * Created on 2016/11/13
 * in BlaBla by Kyle
 */

public class AnalyzeRecyclerViewAdapter extends RecyclerView.Adapter<AnalyzeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = AnalyzeRecyclerViewAdapter.class.getSimpleName();

    @IntDef({Indicator.aces, Indicator.double_faults, Indicator.first_server_in, Indicator.first_server_won
            , Indicator.second_server_won, Indicator.break_point_won, Indicator.net_points_won, Indicator.winner,})
    public @interface Indicator {
        int aces = 0;
        int double_faults = 1;
        int first_server_in = 2;
        int first_server_won = 3;
        int second_server_won = 4;
        int break_point_won = 5;
        int net_points_won = 6;
        int winner = 7;
    }

    @IntDef({IndicatorTraining.success_times, IndicatorTraining.total_times, IndicatorTraining.success_ratio, IndicatorTraining.max_speed
            , IndicatorTraining.average_speed,})
    public @interface IndicatorTraining {
        int success_times = Indicator.aces;
        int total_times = Indicator.double_faults;
        int success_ratio = Indicator.first_server_in;
        int max_speed = Indicator.first_server_won;
        int average_speed = Indicator.second_server_won;
    }

    @IntDef({Rule.Team.TAN, Rule.Team.RED, TextViewIndex.center,})
    public @interface TextViewIndex {
        int left = Rule.Team.TAN;
        int right = Rule.Team.RED;
        int center = 2;
    }

    private Context mContext;
    private String[] mTagStringArr;
    private TennisBase.MatchBoxScore mLeftScore;
    private TennisBase.MatchBoxScore mRightScore;
    private TennisBase.DrillBoxScore mDrillLeftScore;
    private TennisBase.DrillBoxScore mDrillRightScore;
    @Rule.ChannelMode
    private int mChanelMode;
    private boolean mIsMatchMode;

    public AnalyzeRecyclerViewAdapter(Context context, TennisBase.BaseBoxScore[] matchBoxScores
            , @Rule.ChannelMode int mode) {
        mContext = context;
        mIsMatchMode = Rule.isMatchChannel(mode);
        mTagStringArr = context.getResources().getStringArray(
                mIsMatchMode ? R.array.AnalyzeIndicators : R.array.AnalyzeIndicatorsTraining);
        Assert.assertEquals(mTagStringArr.length, context.getResources().getInteger(
                mIsMatchMode ? R.integer.analyze_indicator_count : R.integer.analyze_indicator_training_count));

        if (mode != Rule.ChannelMode.TRAINING) {
            mLeftScore = (TennisBase.MatchBoxScore) matchBoxScores[TextViewIndex.left];
            mRightScore = (TennisBase.MatchBoxScore) matchBoxScores[TextViewIndex.right];
        } else {
            mDrillLeftScore = (TennisBase.DrillBoxScore) matchBoxScores[TextViewIndex.left];
            mDrillRightScore = (TennisBase.DrillBoxScore) matchBoxScores[TextViewIndex.right];
        }

        mChanelMode = mode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.analyze_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViews[TextViewIndex.center].setText(mTagStringArr[position]);
        int startValue = 0;
        int endValue = 0;
        switch (position) {
            case Indicator.aces:
                startValue = mIsMatchMode ? mLeftScore.ace : mDrillLeftScore.successfulTimes;
                endValue = mIsMatchMode ? mRightScore.ace : mDrillRightScore.successfulTimes;
                break;
            case Indicator.double_faults:
                startValue = mIsMatchMode ? mLeftScore.doubleFaultsNum : mDrillLeftScore.totalTimes;
                endValue = mIsMatchMode ? mRightScore.doubleFaultsNum : mDrillRightScore.totalTimes;
                break;
            case Indicator.first_server_in:
                startValue = mIsMatchMode ? mLeftScore.n1stServeSNum : mDrillLeftScore.successRatio;
                endValue = mIsMatchMode ? mRightScore.n1stServeSNum : mDrillRightScore.successRatio;
                break;
            case Indicator.first_server_won:
                startValue = mIsMatchMode ? mLeftScore.n1stSocreSNum : mDrillLeftScore.maxSpeed;
                endValue = mIsMatchMode ? mRightScore.n1stSocreSNum : mDrillRightScore.maxSpeed;
                break;
            case Indicator.second_server_won:
                startValue = mIsMatchMode ? mLeftScore.n2ndServeSNum : mDrillLeftScore.averageSpeed;
                endValue = mIsMatchMode ? mRightScore.n2ndServeSNum : mDrillRightScore.averageSpeed;
                break;
//            case Indicator.break_point_won:
//                startValue = mLeftScore.break_point_won;
//                endValue = mRightScore.ace;
//                break;
//            case Indicator.net_points_won:
//                startValue = mLeftScore.net_points_won;
//                endValue = mRightScore.ace;
//                break;
            case Indicator.winner:
                startValue = mLeftScore.winners;
                endValue = mRightScore.winners;
                break;
            default:
                break;
        }
        boolean needAppend = needPercentageAppend(mIsMatchMode, position);
        customText(holder.mTextViews[TextViewIndex.left],
                needAppend ? String.valueOf(startValue) + mContext.getResources().getString(R.string.percent_char)
                        : String.valueOf(startValue));
        customText(holder.mTextViews[TextViewIndex.right],
                needAppend ? String.valueOf(endValue) + mContext.getResources().getString(R.string.percent_char)
                        : String.valueOf(endValue));

        if (!mIsMatchMode)
            ViewUtil.invisible(holder.mTextViews[TextViewIndex.right]);
    }

    private boolean needPercentageAppend(boolean matchMode, int position) {
        if (matchMode) return position == Indicator.first_server_in ||
                position == Indicator.first_server_won ||
                position == Indicator.second_server_won;
        else return position == IndicatorTraining.success_ratio;
    }

    private void customText(TextView tv, String str) {
        tv.setText(str);
    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getInteger(
                mIsMatchMode ? R.integer.analyze_indicator_count : R.integer.analyze_indicator_training_count);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressWarnings("ResourceType")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView[] mTextViews;

        public ViewHolder(View itemView) {
            this(itemView, true);
        }

        public ViewHolder(View itemView, boolean wholeClickResponse) {
            super(itemView);

            if (wholeClickResponse) {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            mTextViews = new TextView[3];
            mTextViews[TextViewIndex.left] = (TextView) itemView.findViewById(R.id.indicator_start_text);
            mTextViews[TextViewIndex.right] = (TextView) itemView.findViewById(R.id.indicator_end_text);
            mTextViews[TextViewIndex.center] = (TextView) itemView.findViewById(R.id.indicator_center_text);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}