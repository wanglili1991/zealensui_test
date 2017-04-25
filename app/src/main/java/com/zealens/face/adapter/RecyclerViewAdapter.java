package com.zealens.face.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.user.UserInfo;
import com.zealens.face.R;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.PortraitView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/11/13
 * in BlaBla by Kyle
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    @IntDef({Type.loginAlready, Type.toLogin})
    public @interface Type {
        int loginAlready = 0;
        int toLogin = 1;
    }

    private List<UserInfo> mUserList;
    private boolean mReverseOrder;

    public RecyclerViewAdapter(@Nullable List<UserInfo> list, boolean reverseOrder) {
        mUserList = list == null ? new ArrayList<>(0) : list;
        mReverseOrder = reverseOrder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.change_player_item_layout, parent, false), false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= mUserList.size()) {
            ViewUtil.hide(holder.mDividerStart, holder.mDividerEnd);
            return;
        }
        UserInfo info = mUserList.get(position);
        holder.mNickName.setText(info.nickName);
        ViewUtil.hide(mReverseOrder ? holder.mDividerEnd : holder.mDividerStart);
    }

    @Override
    public int getItemCount() {
        return mUserList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position != getItemCount() - 1 ? Type.loginAlready : Type.toLogin;
    }

    @SuppressWarnings("ResourceType")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        PortraitView mPortraitView;
        TextView mNickName;
        View mDividerStart;
        View mDividerEnd;

        public ViewHolder(View itemView) {
            this(itemView, true);
        }

        public ViewHolder(View itemView, boolean wholeClickResponse) {
            super(itemView);

            if (wholeClickResponse) {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            mPortraitView = (PortraitView) itemView.findViewById(R.id.portrait_view);
            mNickName = (TextView) itemView.findViewById(R.id.nick_name);
            mDividerStart = itemView.findViewById(R.id.divider_start);
            mDividerEnd = itemView.findViewById(R.id.divider_end);
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