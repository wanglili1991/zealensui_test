package com.zealens.face.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.base.scorevideopanel.VideoSortAlgorithm;
import com.zealens.face.common.SimpleCallbackWithArg;
import com.zealens.face.domain.DomainConst;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;
import com.zealens.face.util.CollectionUtil;
import com.zealens.face.util.CompatUtil;
import com.zealens.face.util.ViewUtil;

import org.jetbrains.annotations.NonNls;

/**
 * Created on 2016/11/13
 * in BlaBla by Kyle
 */

public class VideoPanelRecyclerViewAdapter extends RecyclerView.Adapter<VideoPanelRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = VideoPanelRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private int mSelectedPosition;
    private boolean mSplitSection;
    private Video[] mVideos;
    private SimpleCallbackWithArg mCallbackWithArg;
    /**
     * @see Rule.Side for index
     */
    private final String[] TRAINING_STR;
    private final String SIDE_A_TAG_STR;
    private final Score mScore;

    public VideoPanelRecyclerViewAdapter(Context context, SimpleCallbackWithArg callbackWithArg
            , @NonNls Score score, @NonNull Video[] videos, boolean splitSection) {
        mContext = context;
        mCallbackWithArg = obj -> {
            if (obj instanceof Integer) {
                callbackWithArg.onRefresh(mVideos[(int) obj]);
            }
        };
        mScore = score;
        TRAINING_STR = new String[]{mContext.getString(R.string.training_video_inspect_a), mContext.getString(R.string.training_video_inspect_b)};
        SIDE_A_TAG_STR = DomainConst.SCORE_SPLIT + Rule.SideStr.A;
        mSplitSection = splitSection;
        assignVideos(videos);
    }

    public void assignVideos(Video[] videos) {
        mVideos = VideoSortAlgorithm.sortVideosAndInsertDivider(mScore, videos, mSplitSection);
        mSelectedPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = R.layout.video_panel_item_layout;
        if (mSplitSection) {
            switch (viewType) {
                case Rule.VideoDivider.JEWEL:
                case Rule.VideoDivider.GAME:
                    layoutRes = R.layout.video_panel_divider_item_layout;
                    break;
                default:
                    break;
            }
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false)
                , mCallbackWithArg);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean selected = mSelectedPosition == position;
        Video video = mVideos[position];
        int type = getItemViewType(position);
        if (type == Rule.VideoDivider.GENERAL) {
            if (!mSplitSection) {
                boolean aSide = video.path.contains(SIDE_A_TAG_STR);
                holder.part.setText(TRAINING_STR[aSide ? Rule.Side.A : Rule.Side.B]);
                ViewUtil.invisible(holder.lastingTime, holder.score);
            } else {
                if (!TextUtils.isEmpty(video.path)) {
                    if (Rule.isTagSpecial(video.tag)) {
                        String title;
                        switch (video.tag) {
                            default:
                            case Rule.VideoTag.WINNER:
                                title = mContext.getString(R.string.special_video_title_winner);
                                break;
                            case Rule.VideoTag.ACE:
                                title = mContext.getString(R.string.special_video_title_ace);
                                break;
                            case Rule.VideoTag.ABOVE_20_HITS:
                                title = mContext.getString(R.string.special_video_title_20_hits);
                                break;
                        }
                        holder.part.setText(title);
                    } else {
                        int boutSum = CollectionUtil.accumulateIntegerArray(video.score.bout);
                        if (boutSum == 0) {
                            boutSum = CollectionUtil.accumulateIntegerArray(mVideos[position - 1].score.bout) + 1;
                        }
                        holder.part.setText(String.format(CompatUtil.getLocale(mContext)
                                , mContext.getString(R.string.match_video_name), boutSum));
                    }
                    holder.lastingTime.setText(String.valueOf(video.timeLast));
                    String score = String.valueOf(video.score.bout[Rule.Team.TAN]) + DomainConst.SCORE_DIVIDER_2_DISPLAY
                            + String.valueOf(video.score.bout[Rule.Team.RED]);

                    int boutSum = CollectionUtil.accumulateIntegerArray(video.score.bout);
                    if (boutSum == 0) {
                        score = mContext.getString(R.string.special_video_title_winner);
                    }
                    holder.score.setText(score);
                }
            }
            holder.itemView.setSelected(selected);
        } else {
            if (type == Rule.VideoDivider.GAME) {
                int gameCount = CollectionUtil.accumulateIntegerArray(mVideos[position + 1].score.game) + 1;
                holder.dividerTag.setText(String.format(CompatUtil.getLocale(mContext)
                        , mContext.getString(R.string.match_video_name_game), gameCount));
            } else {
                holder.dividerTag.setText(R.string.special_video_divider_text);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mVideos.length;
    }

    @Override
    public int getItemViewType(int position) {
        return mVideos[position].dividerType;
    }

    @SuppressWarnings("ResourceType")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ViewGroup parent;
        TextView part;
        TextView score;
        TextView lastingTime;

        TextView dividerTag;
        SimpleCallbackWithArg callbackWithArg;

        public ViewHolder(View itemView, SimpleCallbackWithArg callbackWithArg) {
            this(itemView, callbackWithArg, true);
        }

        public ViewHolder(View itemView, SimpleCallbackWithArg callbackWithArg, boolean wholeClickResponse) {
            super(itemView);

            this.callbackWithArg = callbackWithArg;
            if (wholeClickResponse) {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            parent = (ViewGroup) itemView.findViewById(R.id.parent);
            part = (TextView) itemView.findViewById(R.id.part);
            score = (TextView) itemView.findViewById(R.id.score);
            lastingTime = (TextView) itemView.findViewById(R.id.lasting_time);
            dividerTag = (TextView) itemView.findViewById(R.id.divider_tag_text);

            ViewUtil.setClickListener(this, parent);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = getLayoutPosition();
            v.setSelected(true);
            notifyItemChanged(mSelectedPosition);
            callbackWithArg.onRefresh(mSelectedPosition);
//            v.requestFocus();
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}