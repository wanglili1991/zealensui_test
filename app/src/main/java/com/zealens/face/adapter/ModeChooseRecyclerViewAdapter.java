package com.zealens.face.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.base.modechoose.SideEffectUnit;
import com.zealens.face.common.SimpleCallbackWithArg;

import junit.framework.Assert;

/**
 * Created on 2016/11/13
 * in BlaBla by Kyle
 */

public class ModeChooseRecyclerViewAdapter extends RecyclerView.Adapter<ModeChooseRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = ModeChooseRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private String[] mTagStringArr;
    @Rule.Side
    private int mSide;
    private SimpleCallbackWithArg mCallbackWithArg;
    @Rule.TrainMode
    private int mSelectedPosition = Rule.TrainMode.UNSET;
    private int[] mEnabledModes;
    private boolean mEnableAll = true;

    public ModeChooseRecyclerViewAdapter(Context context, @Rule.Side int side, SimpleCallbackWithArg callbackWithArg) {
        mContext = context;
        mTagStringArr = context.getResources().getStringArray(R.array.TrainModeChoose);
        mSide = side;
        mCallbackWithArg = callbackWithArg;
        int len = context.getResources().getInteger(R.integer.mode_choose_total_count);
        mEnabledModes = new int[len];
        Assert.assertEquals(mTagStringArr.length, len);
    }

    public void enableItem(boolean all, int[] modes) {
        mEnableAll = all;
        mEnabledModes = modes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.train_button_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        @Rule.TrainMode
        int adapterPos = holder.getAdapterPosition();
        @DrawableRes
        int bgRes;
        switch (adapterPos) {
            default:
            case Rule.TrainMode.SERVICE:
                bgRes = R.drawable.train_button_effect_service;
                break;
            case Rule.TrainMode.ALTERNATE_SERVICE:
                bgRes = R.drawable.train_button_effect_alternate_service;
                break;
            case Rule.TrainMode.FOREHAND:
                bgRes = R.drawable.train_button_effect_forehand;
                break;
            case Rule.TrainMode.FOREHAND_DOWN_THE_LINE:
                bgRes = mSide == Rule.Side.A ? R.drawable.train_button_effect_forehand_down_the_line : R.drawable.train_button_effect_forehand_down_the_line_player_b;
                break;
            case Rule.TrainMode.FOREHAND_CROSS_COURT:
                bgRes = mSide == Rule.Side.A ? R.drawable.train_button_effect_forehand_cross_court : R.drawable.train_button_effect_forehand_cross_court_player_b;
                break;
            case Rule.TrainMode.BACKHAND:
                bgRes = R.drawable.train_button_effect_backhand;
                break;
            case Rule.TrainMode.BACKHAND_DOWN_THE_LINE:
                bgRes = mSide == Rule.Side.A ? R.drawable.train_button_effect_backhand_down_the_line : R.drawable.train_button_effect_backhand_down_the_line_player_b;
                break;
            case Rule.TrainMode.BACKHAND_CROSS_COURT:
                bgRes = mSide == Rule.Side.A ? R.drawable.train_button_effect_backhand_cross_court : R.drawable.train_button_effect_backhand_cross_court_player_b;
                break;
            case Rule.TrainMode.VOLLEY:
                bgRes = R.drawable.train_button_effect_volley;
                break;
            case Rule.TrainMode.FREE_STYLE:
                bgRes = R.drawable.train_button_effect_free_style;
                break;
            case Rule.TrainMode.MULTIPLE_BALLS:
                bgRes = R.drawable.train_button_effect_multiple_balls;
                break;
        }
        holder.mMode.setBackgroundResource(bgRes);
        holder.mMode.setText(mTagStringArr[adapterPos]);

        if (mEnableAll) holder.mMode.setEnabled(true);
        else holder.mMode.setEnabled(containedInArray(mEnabledModes, position));

        if (mSelectedPosition != adapterPos) holder.mMode.setChecked(false);
        holder.mMode.setOnClickListener(((v) -> {
            boolean isChecked = ((CheckBox)v).isChecked();
            mSelectedPosition = isChecked ? adapterPos : Rule.TrainMode.UNSET;
            mCallbackWithArg.onRefresh(new SideEffectUnit(mSide, adapterPos, isChecked));
            v.post(this::notifyDataSetChanged);
        }));
    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getInteger(R.integer.mode_choose_total_count);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private boolean containedInArray(int[] arr, int check) {
        if (arr == null || arr.length == 0) return false;
        for (int i : arr) if (i == check) return true;
        return false;
    }

    @SuppressWarnings("ResourceType")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CheckBox mMode;

        public ViewHolder(View itemView) {
            this(itemView, true);
        }

        public ViewHolder(View itemView, boolean wholeClickResponse) {
            super(itemView);

            if (wholeClickResponse) {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            mMode = (CheckBox) itemView.findViewById(R.id.mode);
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