package org.cafemember.messenger.mytg.adapter;

/**
 * Created by mohammad on 04/09/2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.CategoryChannel;
import org.cafemember.messenger.mytg.FontManager;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryChannel> cats;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, ArrayList<CategoryChannel> cats) {
        this.context = applicationContext;
        this.cats = cats;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cats.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        CategoryChannel cat = cats.get(i);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setText(cat.name);
        FontManager.instance().setTypefaceImmediate(view);

        return view;
    }
}