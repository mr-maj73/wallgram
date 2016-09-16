package org.cafemember.messenger.mytg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.ChannelOrderMemberShip;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/09/2016.
 */

public class OrdersMembershipAdapter extends
        RecyclerView.Adapter<OrdersMembershipAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<ChannelOrderMemberShip> members;
    private Context context;


    public OrdersMembershipAdapter(Context context, ArrayList<ChannelOrderMemberShip> members) {
        this.context = context;
        this.members = members;


//            this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return (null != members ? members.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ChannelOrderMemberShip memeber = members.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder


        mainHolder.title.setText(memeber.name);
        mainHolder.txtDate.setText(memeber.date);
//        Log.e("DATE",memeber.date);
        mainHolder.imageChannel.setImageBitmap(memeber.getBitMap());
        mainHolder.txtOrderNumber.setText(memeber.total + " عضو ");
        String left = "";
        if (Integer.parseInt(memeber.left) > 0) {
            left = " " + "(" + " خارج شده " + memeber.left + ")";
        }
        mainHolder.txtNumberDone.setText(" عضو " + memeber.done + left);
        //       mainHolder.txtDate.setText(memeber.date);

        if (Integer.parseInt(memeber.status) == 0) {
            mainHolder.status.setText("جاری");
            mainHolder.status.setTextColor(Color.parseColor("#a8a8a8"));
        } else if (Integer.parseInt(memeber.status) == 1) {
            Log.i("mohammad", "noConnect");
            mainHolder.status.setText("انجام شده");
            mainHolder.status.setTextColor(Color.parseColor("#39b54a"));

        } else {
            mainHolder.status.setText("بلاک شده");
            mainHolder.status.setTextColor(Color.parseColor("#d84152"));
        }


        //       mainHolder.imageChannel.setImageBitmap(memeber.getBitMap());
//        mainHolder.imgMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/"+channelAdv.admin_link));
//                context.startActivity(telegram);
//            }
//        });


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_orders_membership, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public TextView title;
        public TextView txtOrderNumber;
        public TextView txtDone;
        public TextView txtNumberDone;
        public TextView txtDate;
        public TextView status;
        public ImageView imageChannel;


        public RecyclerViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.title);
            this.txtOrderNumber = (TextView) view.findViewById(R.id.txtOrderNumber);
            this.status = (TextView) view.findViewById(R.id.txtStatus);
            this.txtDone = (TextView) view.findViewById(R.id.txtDone);
            this.txtNumberDone = (TextView) view.findViewById(R.id.txtNumberDone);
            this.txtDate = (TextView) view.findViewById(R.id.txtDate);
            this.imageChannel = (ImageView) view.findViewById(R.id.imageChannel);
            FontManager.instance().setTypefaceImmediate(view);


        }


    }


}

