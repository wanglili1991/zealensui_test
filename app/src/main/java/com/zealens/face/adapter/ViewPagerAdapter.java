package com.zealens.face.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;

/**
 * Created on 2017/3/10
 * in BlaBla by Kyle
 */

public class ViewPagerAdapter extends PagerAdapter {
    private String[] mStringRes;

    public ViewPagerAdapter(String[] mResources) {
        this.mStringRes = mResources;
    }

    @Override
    public int getCount() {
        return mStringRes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.mode_choose_item_layout, container, false);
        TextView imageView = (TextView) itemView.findViewById(R.id.description);
        imageView.setText(mStringRes[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}