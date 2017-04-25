package com.zealens.face.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zealens.face.R;

import junit.framework.Assert;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class PlayerStateImageView extends AppCompatImageView {
    @IntDef({Status.serve, Status.receive, Status.in, Status.out})
    public @interface Status {
        int receive = 0;
        int serve = 1;
        int in = 2;
        int out = 3;
    }

    private int[] RESOURCE_ARR = {
            R.drawable.match_score_board_receiver,
            R.drawable.match_score_board_server,
            R.drawable.match_score_board_in,
            R.drawable.match_score_board_out,
    };

    public PlayerStateImageView(Context context) {
        super(context, null);
        init(context, null, 0);
    }

    public PlayerStateImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    public PlayerStateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setImageResource(RESOURCE_ARR[Status.receive]);
        Assert.assertEquals(RESOURCE_ARR.length, Status.class.getFields().length);
    }

    public void switchStatus(@Status int status) {
        setImageResource(RESOURCE_ARR[status]);
    }
}
