package org.cafemember.messenger.mytg.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cafemember.messenger.mytg.listeners.Refrashable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.MessagesController;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Channel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.ChannelsAdapter;
import org.cafemember.messenger.mytg.listeners.OnChannelReady;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.tgnet.ConnectionsManager;
import org.cafemember.tgnet.RequestDelegate;
import org.cafemember.tgnet.TLObject;
import org.cafemember.tgnet.TLRPC;
import org.cafemember.ui.DialogsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masoud on 6/2/2016.
 */
@SuppressLint("ValidFragment")
public class ChannelsFragment extends Fragment implements Refrashable, SwipeRefreshLayout.OnRefreshListener {
    private ChannelsAdapter adapter;

    private final DialogsActivity dialogsActivity;
    private LinearLayout loading;
    private ListView channelsLis;
    private View layout;
    private long lastCheck = 0;

    private SwipeRefreshLayout swiper;

    @SuppressLint("ValidFragment")
    public ChannelsFragment(DialogsActivity dialogsActivity){
        this.dialogsActivity = dialogsActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.channels_layout, null);
        channelsLis = (ListView)layout.findViewById(R.id.channelsList);
        loading = (LinearLayout)layout.findViewById(R.id.holder);
        loading.setVisibility(View.GONE);
        swiper = (SwipeRefreshLayout) layout.findViewById(R.id.swip);

        adapter = new ChannelsAdapter(getContext(),R.layout.channel_item,new ArrayList<Channel>(),ChannelsFragment.this, dialogsActivity);
        channelsLis.setAdapter(adapter);
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeColors(R.color.colorPrimary,R.color.colorPrimaryDark);
        FontManager.instance().setTypefaceImmediate(layout);

        return layout;

    }

    public void loadMore(){
        /*if(layout == null){
            layout = inflater.inflate(R.layout.channels_layout, null);

            FontManager.instance().setTypefaceImmediate(layout);
        }*/

        long now = System.currentTimeMillis();
        /*if(now - 5000 > lastCheck) {
                                        Log.e("ASD1", "yES");*/

            lastCheck = now;
        if(!swiper.isRefreshing()){
            loading.setVisibility(View.VISIBLE);
        }
            Commands.getNewChannels(new OnResponseReadyListener() {
                @Override
                public void OnResponseReady(boolean error, JSONObject data, String message) {
                    if(!swiper.isRefreshing()){
                        loading.setVisibility(View.GONE);
                    }                    if (!error) {
                        layout.findViewById(R.id.error).setVisibility(View.GONE);
                        MessagesController controller = MessagesController.getInstance();
                        try {
                            JSONArray channelsId = data.getJSONArray("data");
                            final ArrayList<Channel> channels = new ArrayList<>();
                            int size = channelsId.length();

                            adapter.clear();
//                            Log.e("ASD", size + "");
                            for (int i = 0; i < size; i++) {
//                            TLRPC.TL_contacts_search req = new TLRPC.TL_contacts_search();
                                JSONObject item = channelsId.getJSONObject(i);
                                final int cu = i;
                                final Channel currentChannel = new Channel(item.getString("name"), item.getInt("tg_id"));

                                String byteString = null;
                                if(item.has("title")  && item.getString("title").length() > 0){
                                    currentChannel.title = item.getString("title");
                                }
                                if(item.has("byteString")  && item.getString("byteString").length() > 0){
//                                    Log.e("MyCh",currentChannel.name+" Has Byte");
                                    byteString = item.getString("byteString");
                                    currentChannel.setPhoto(byteString);
                                        adapter.add(currentChannel);
//                                    channelReady = true;
                                }
                                else {
                                    final int last = i;
                                    Defaults.getInstance().loadChannel(currentChannel, new OnChannelReady() {
                                        @Override
                                        public void onReady(Channel channel, boolean isOk) {
                                            Log.e("CH", cu + " : " + isOk);
                                            if (isOk) {
                                                adapter.add(currentChannel);
                                                //channels.add(currentChannel);
                                                adapter.notifyDataSetChanged();
                                            }
                                      /*  else {
                                            currentChannel.title = currentChannel.name;
                                            adapter.add(currentChannel);
                                            //channels.add(currentChannel);
                                            adapter.notifyDataSetChanged();
                                        }*/
                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        TextView tv = (TextView) layout.findViewById(R.id.error);
                        if(message != null && message.length() > 0){
                            tv.setText(message);
                        }
                        tv.setVisibility(View.VISIBLE);
                    }
                    if(swiper.isRefreshing()){
                        swiper.setRefreshing(false);
                    }
                }
            });


    }

    public void setLoader(int visibility){
        loading.setVisibility(visibility);
    }

    @Override
    public void onResume() {
        super.onResume();
//        swiper.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void refresh() {
//        loadMore();
    }

    @Override
    public void onRefresh() {
        loadMore();
    }
}
