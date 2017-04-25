package com.zealens.face.base.fakedialog;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.zealens.face.R;
import com.zealens.face.base.Rule;
import com.zealens.face.common.ClickCallback;
import com.zealens.face.core.CoreContext;
import com.zealens.face.domain.module.Video;
import com.zealens.face.domain.umpire.CorrectComponents;
import com.zealens.face.util.ViewUtil;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class LastScoreIncorrectDialogPresenterImpl extends DialogPresenterImpl implements DialogPresenter, ClickCallback {

    @Rule.ChannelMode
    private int mChannelMode;
    private CorrectComponents mCorrectComponents;

    public LastScoreIncorrectDialogPresenterImpl(CoreContext coreContext, ViewGroup rootVG
            , @IntRange(from = 1) @LayoutRes int layoutRes, @Rule.ChannelMode int mode, CorrectComponents functionToolBox) {
        super(coreContext, rootVG, layoutRes, null);
        setClickCallback(this);

        mChannelMode = mode;
        mCorrectComponents = functionToolBox;
    }

    @Override
    public void loadDialogPage() {
        /*
         * verify callback
         * yes-- competitor won;
         * no -- cancel last score,replay
         */
        super.loadDialogPage();
        ViewUtil.setClickListener((v -> replayVideo(Rule.Side.A)), mDialogLayout.findViewById(R.id.replay_side_a));
        ViewUtil.setClickListener((v -> replayVideo(Rule.Side.B)), mDialogLayout.findViewById(R.id.replay_side_b));
    }

    private void replayVideo(@Rule.Side int side) {
        Video[] videos = mCorrectComponents.getVideoOfLastScore();
        if (side == Rule.Side.A) {
        } else {
        }
        removeDialogPage();
    }

    @Override
    public void onYes() {
        mCorrectComponents.reverseLastScoreJudge();
    }

    @Override
    public void onNo() {
        mCorrectComponents.cancelLastScore();
    }

    @Override
    public void onClose() {

    }
}
