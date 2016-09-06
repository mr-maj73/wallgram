package org.cafemember.messenger.mytg.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mohammad on 04/09/2016.
 */

public class ConditionAdapter extends
        RecyclerView.Adapter<ConditionAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter

    private Context context;
    private String[] hints;
    Map<Integer, String> condition;


    public ConditionAdapter(Context context, Map<String, String> condition) {
        this.context = context;
        this.condition = condition;
        hints = context.getResources().getStringArray(R.array.condition);


    }

    @Override
    public int getItemCount() {
        return (null != condition ? condition.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final String hin = hints[position];

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        final String key = "" + position;
        mainHolder.edtCondition.setHint(hin);
        Log.i("mohammad", "1" + condition.get("" + position) + "  " + position);
        mainHolder.edtCondition.setText(condition.get("" + position));
        Log.i("mohammad", "2" + condition.get("" + position) + "  " + position);
        mainHolder.edtCondition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
    //            Log.i("mohammad", "3"+condition.get("" + position)+"  "+position);
                condition.put(key, mainHolder.edtCondition.getText().toString());
    //            Log.i("mohammad","4"+ condition.get("" + position)+"  "+position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_list_dialog, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public EditText edtCondition;


        public RecyclerViewHolder(View view) {
            super(view);

            this.edtCondition = (EditText) view.findViewById(R.id.edtCondition);


        }


    }

    public Map<String, String> getcondition() {
        return condition;

    }


}

