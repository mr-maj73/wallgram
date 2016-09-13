package org.cafemember.messenger.mytg.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.ChannelOrderAdvertising;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/09/2016.
 */

public class OrdersAdvertisingAdapter extends
        RecyclerView.Adapter<OrdersAdvertisingAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<ChannelOrderAdvertising> advChannels;
    private Context context;



    public OrdersAdvertisingAdapter(Context context, ArrayList<ChannelOrderAdvertising> advChannels) {
        this.context = context;
        this.advChannels = advChannels;


    }

    @Override
    public int getItemCount() {
        return (null != advChannels ? advChannels.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ChannelOrderAdvertising channelAdv = advChannels.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder



        mainHolder.reportLayout.setVisibility(View.GONE);
        mainHolder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mainHolder.reportLayout.getVisibility()==View.VISIBLE){
                    mainHolder.reportLayout.setVisibility(View.GONE);
                }
            }
        });
        if (channelAdv.name.length() > 0) {
            mainHolder.title.setText(channelAdv.name);
        } else {
            mainHolder.title.setText(channelAdv.title);
        }

        Log.i("momhammad","ddddddddddd");
        mainHolder.txtCoindeal.setText(" سکه " + channelAdv.price);
        mainHolder.txtOrderpicker.setText("0" +channelAdv.user.substring(2, 5)+"***"+channelAdv.user.substring(9));
        mainHolder.txtDate.setText(channelAdv.date);
        mainHolder.imageChannel.setImageBitmap(channelAdv.getBitMap());
        if(Integer.parseInt(channelAdv.type)==2){
            mainHolder.imgMore.setVisibility(View.VISIBLE);
        }else{
            mainHolder.imgMore.setVisibility(View.GONE);
        }
        mainHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHolder.reportLayout.setVisibility(View.VISIBLE);
            }
        });
//        mainHolder.imgMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/"+channelAdv.admin_link));
//                context.startActivity(telegram);
//            }
//        });
        mainHolder.txtShowCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // advertisingFragment.showDialog(channelAdv.id);
            }
        });


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_orders_advertising, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public TextView title;
        public TextView txtOrderpicker;
        public TextView txtCoindeal;
        public TextView txtShowCondition;
        public TextView txtDate;
        public ImageView imageChannel;
        public ImageView imgMore;
        public LinearLayout reportLayout;
        public CardView cardOrderAdv;
        public RelativeLayout backLayout;


        public RecyclerViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.title);
            this.txtOrderpicker = (TextView) view.findViewById(R.id.txtOrderpicker);
            this.txtCoindeal = (TextView) view.findViewById(R.id.txtCoindeal);
            this.txtShowCondition = (TextView) view.findViewById(R.id.txtShowCondition);
            this.txtDate = (TextView) view.findViewById(R.id.txtDate);
            this.imageChannel = (ImageView) view.findViewById(R.id.imageChannel);
            this.imgMore = (ImageView) view.findViewById(R.id.imgMore);
            this.reportLayout = (LinearLayout) view.findViewById(R.id.reportLayout);
            this.backLayout = (RelativeLayout) view.findViewById(R.id.backLayout);

            this.cardOrderAdv = (CardView) view.findViewById(R.id.cardOrderAdv);
            FontManager.instance().setTypefaceImmediate(view);


        }


    }


}

