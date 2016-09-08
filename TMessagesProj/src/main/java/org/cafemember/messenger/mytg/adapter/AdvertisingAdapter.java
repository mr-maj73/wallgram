package org.cafemember.messenger.mytg.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import org.cafemember.messenger.mytg.Channel;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/09/2016.
 */

public class AdvertisingAdapter extends
        RecyclerView.Adapter<AdvertisingAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<ChannelAdvertising> advChannels;
    private Context context;
    private AdvertisingFragment advertisingFragment;


    public AdvertisingAdapter(Context context, ArrayList<ChannelAdvertising> advChannels, AdvertisingFragment advertisingFragment) {
        this.context = context;
        this.advChannels = advChannels;
        this.advertisingFragment = advertisingFragment;

//            this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return (null != advChannels ? advChannels.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ChannelAdvertising channelAdv = advChannels.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder


        mainHolder.textPriceAdver.setText("" + channelAdv.price);
        Log.i("mohammad", "11111111" + channelAdv.price);
        if (channelAdv.title != null && channelAdv.title.length() > 1) {
            mainHolder.textTitleAdv.setText(channelAdv.title);
        } else {
            mainHolder.textTitleAdv.setText(channelAdv.name);
        }
        mainHolder.textDescription.setText(channelAdv.description);
        mainHolder.imgChannalAdv.setImageBitmap(channelAdv.getBitMap());
        mainHolder.layConnectAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/tefelgarddd"));
                context.startActivity(telegram);
            }
        });
        mainHolder.laySubmitAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisingFragment.showDialog(channelAdv.id);
            }
        });


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.advertising_item, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public TextView textPriceAdver;
        public TextView textDescription;
        public TextView textTitleAdv;
        public ImageView imgChannalAdv;
        public ImageView imgOptionAdv;
        public LinearLayout laySubmitAdv;
        public LinearLayout layConnectAdv;
        public CardView advertisingLayoutSliding;


        public RecyclerViewHolder(View view) {
            super(view);

            this.textPriceAdver = (TextView) view.findViewById(R.id.textPriceAdver);
            this.textDescription = (TextView) view.findViewById(R.id.textDescription);
            this.textTitleAdv = (TextView) view.findViewById(R.id.textTitleAdv);
            this.imgChannalAdv = (ImageView) view.findViewById(R.id.imgChannalAdv);
            this.imgOptionAdv = (ImageView) view.findViewById(R.id.imgOptionAdv);
            this.laySubmitAdv = (LinearLayout) view.findViewById(R.id.laySubmitAdv);
            this.layConnectAdv = (LinearLayout) view.findViewById(R.id.layConnectAdv);
            this.advertisingLayoutSliding = (CardView) view.findViewById(R.id.advertisingLayoutSliding);
            FontManager.instance().setTypefaceImmediate(view);


        }


    }


}

