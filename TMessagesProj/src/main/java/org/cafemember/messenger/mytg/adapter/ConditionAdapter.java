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
    ArrayList<String> condition;


    public ConditionAdapter(Context context, ArrayList<String> condition) {
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
        String item = condition.get(position);

//        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        holder.myCustomEditTextListener.updatePosition(position);
        holder.edtCondition.setText(condition.get(position));
        holder.edtCondition.setHint(hin);



    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_list_dialog, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup, new MyCustomEditTextListener());
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public EditText edtCondition;

        public MyCustomEditTextListener myCustomEditTextListener;

        public RecyclerViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);

            this.edtCondition = (EditText) v.findViewById(R.id.edtCondition);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.edtCondition.addTextChangedListener(myCustomEditTextListener);


        }


    }

    public ArrayList<String> getcondition() {
        return condition;

    }
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            condition.remove(position);
            String text = editable.toString();
            condition.add(position, text);
            // no op
        }
    }

}

