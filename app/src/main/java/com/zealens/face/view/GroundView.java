package com.zealens.face.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zealens.face.R;
import com.zealens.face.util.ViewUtil;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class GroundView extends FrameLayout {

    @IntDef({
            Zoo.side_a_backyard_bottom_half_red,
            Zoo.side_a_backyard_bottom_yellow,
            Zoo.side_a_whole_blue,
            Zoo.side_a_backyard_half_red,
            Zoo.side_a_backyard_yellow,
            Zoo.side_a_service_area_bottom_inner,
            Zoo.side_a_service_area_bottom_blue,
            Zoo.side_a_service_area_bottom_outer_red,
            Zoo.side_b_whole_top,
            Zoo.side_b_backyard_top_half_yellow,
            Zoo.side_b_backyard_top,
            Zoo.side_b_whole,
            Zoo.side_b_backyard_bottom_half_blue,
            Zoo.side_b_backyard_bottom_half_red,
            Zoo.side_b_whole_bottom,
            Zoo.side_b_backyard_bottom,
            Zoo.side_b_backyard,
            Zoo.side_b_backyard_half,
            Zoo.side_b_service_area_top_outer,
            Zoo.side_b_service_area_top_inner,
            Zoo.side_b_service_area_bottom,
            Zoo.side_b_service_area_bottom_inner,
            Zoo.side_b_service_area_bottom_outer,
            Zoo.player_backyard_half_yellow,
            Zoo.player_backyard_half_blue,
            Zoo.player_backyard_half_yellow_bottom,
            Zoo.player_backyard_half_blue_bottom,
            Zoo.player_blue_bottom_line,
            Zoo.player_yellow_bottom_line,
            Zoo.side_b_service_area_top,
    })
    public @interface Zoo {
        int side_a_backyard_bottom_half_red = 0;
        int side_a_backyard_bottom_yellow = 1;
        int side_a_whole_blue = 2;
        int side_a_backyard_half_red = 3;
        int side_a_backyard_yellow = 4;
        int side_a_service_area_bottom_inner = 5;
        int side_a_service_area_bottom_blue = 6;
        int side_a_service_area_bottom_outer_red = 7;
        int side_b_whole_top = 8;
        int side_b_backyard_top_half_yellow = 9;
        int side_b_backyard_top = 10;
        int side_b_whole = 11;
        int side_b_backyard_bottom_half_blue = 12;
        int side_b_backyard_bottom_half_red = 13;
        int side_b_whole_bottom = 14;
        int side_b_backyard_bottom = 15;
        int side_b_backyard = 16;
        int side_b_backyard_half = 17;
        int side_b_service_area_top_outer = 18;
        int side_b_service_area_top_inner = 19;
        int side_b_service_area_bottom = 20;
        int side_b_service_area_bottom_inner = 21;
        int side_b_service_area_bottom_outer = 22;
        int player_backyard_half_yellow = 23;
        int player_backyard_half_blue = 24;
        int player_backyard_half_yellow_bottom = 25;
        int player_backyard_half_blue_bottom = 26;
        int player_blue_bottom_line = 27;
        int player_yellow_bottom_line = 28;
        int side_b_service_area_top = 29;
    }

    private final int[] RESOURCE_IDS = {
            R.id.zone_side_a_backyard_bottom_half_red,
            R.id.zone_side_a_backyard_bottom_yellow,
            R.id.zone_side_a_whole_blue,
            R.id.zone_side_a_backyard_half_red,
            R.id.zone_side_a_backyard_yellow,
            R.id.zone_side_a_service_area_bottom_inner,
            R.id.zone_side_a_service_area_bottom_blue,
            R.id.zone_side_a_service_area_bottom_outer_red,
            R.id.zone_side_b_whole_top,
            R.id.zone_side_b_backyard_top_half_yellow,
            R.id.zone_side_b_backyard_top,
            R.id.zone_side_b_whole,
            R.id.zone_side_b_backyard_bottom_half_blue,
            R.id.zone_side_b_backyard_bottom_half_red,
            R.id.zone_side_b_whole_bottom,
            R.id.zone_side_b_backyard_bottom,
            R.id.zone_side_b_backyard,
            R.id.zone_side_b_backyard_half,
            R.id.zone_side_b_service_area_top_outer,
            R.id.zone_side_b_service_area_top_inner,
            R.id.zone_side_b_service_area_bottom,
            R.id.zone_side_b_service_area_bottom_inner,
            R.id.zone_side_b_service_area_bottom_outer,
            R.id.zone_player_backyard_half_yellow,
            R.id.zone_player_backyard_half_blue,
            R.id.zone_player_backyard_half_yellow_bottom,
            R.id.zone_player_backyard_half_blue_bottom,
            R.id.zone_player_blue_bottom_line,
            R.id.zone_player_yellow_bottom_line,
            R.id.zone_side_b_service_area_top,
    };

    private View[] RESOURCE_VIEWS;


    public GroundView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public GroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public GroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.playground_layout, this);
        int filedLen = Zoo.class.getFields().length;
        RESOURCE_VIEWS = new View[filedLen];
        for (int i = 0; i < filedLen; i++) {
            RESOURCE_VIEWS[i] = findViewById(RESOURCE_IDS[i]);
        }
    }

    public void modeDisplay(@Zoo int[] modeRes) {
        ViewUtil.hide(RESOURCE_VIEWS);
        for (int index : modeRes)
            ViewUtil.show(RESOURCE_VIEWS[index]);
    }
}
