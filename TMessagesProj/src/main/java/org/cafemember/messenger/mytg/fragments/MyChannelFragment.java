package org.cafemember.messenger.mytg.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.ApplicationLoader;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.MessageObject;
import org.cafemember.messenger.MessagesController;
import org.cafemember.messenger.MessagesStorage;
import org.cafemember.messenger.NotificationCenter;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Channel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.ChannelsAdapter;
import org.cafemember.messenger.mytg.adapter.MyChannelsAdapter;
import org.cafemember.messenger.mytg.listeners.OnChannelReady;
import org.cafemember.messenger.mytg.listeners.OnJoinSuccess;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.listeners.Refrashable;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.messenger.support.widget.LinearLayoutManager;
import org.cafemember.messenger.support.widget.RecyclerView;
import org.cafemember.tgnet.TLRPC;
import org.cafemember.ui.Adapters.DialogsAdapter;
import org.cafemember.ui.ChatActivity;
import org.cafemember.ui.Components.LayoutHelper;
import org.cafemember.ui.Components.RecyclerListView;
import org.cafemember.ui.DialogsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Masoud on 6/2/2016.
 */
@SuppressLint("ValidFragment")
public class MyChannelFragment extends Fragment implements Refrashable, SwipeRefreshLayout.OnRefreshListener  {
    private MyChannelsAdapter adapter;
    private final DialogsActivity dialogsActivity;
    private LinearLayout loading;
    private ListView channelsLis;
    private View layout;
    private FloatingActionButton  newChannel;
    private long lastCheck = 0;
    private SwipeRefreshLayout swiper;
    private TextView errorHolder;

    @SuppressLint("ValidFragment")
    public MyChannelFragment(DialogsActivity dialogsActivity){
        this.dialogsActivity = dialogsActivity;
    }

    public void setLoader(int visibility){
        loading.setVisibility(visibility);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.my_channels_layout, null);
        channelsLis = (ListView)layout.findViewById(R.id.channelsList);
        loading = (LinearLayout)layout.findViewById(R.id.holder);
        errorHolder = (TextView) layout.findViewById(R.id.error);
        loading.setVisibility(View.GONE);

        swiper = (SwipeRefreshLayout) layout.findViewById(R.id.swip);
        newChannel = (FloatingActionButton) layout.findViewById(R.id.newChannel);
        newChannel.attachToListView(channelsLis, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
//                Log.d("ListViewFragment", "onScrollDown()");
            }

            @Override
            public void onScrollUp() {
//                Log.d("ListViewFragment", "onScrollUp()");
            }
        }, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.d("ListViewFragment", "onScrollStateChanged()");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d("ListViewFragment", "onScroll()");
            }
        });







        adapter = new MyChannelsAdapter(getContext(),R.layout.my_channel_item,new ArrayList<Channel>(),MyChannelFragment.this,dialogsActivity);
        channelsLis.setAdapter(adapter);
        newChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChannelActivity addChannelActivity = new AddChannelActivity();
                addChannelActivity.setChannelAddListener(new OnJoinSuccess() {
                    @Override
                    public void OnResponse(boolean ok) {
                        loadMore();
                    }
                });
                dialogsActivity.presentFragment(addChannelActivity);
            }
        });
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeColors(R.color.colorPrimary,R.color.colorPrimaryDark);

        FontManager.instance().setTypefaceImmediate(layout);

        return layout;

    }

    public void loadMore(){




        long now = System.currentTimeMillis();
       /* if(now - 5000 > lastCheck){
            Log.e("ASD2", "yES")*/;
            lastCheck = now;
        if(!swiper.isRefreshing()){
            loading.setVisibility(View.VISIBLE);
        }
            Commands.getMyChannels(new OnResponseReadyListener() {
                @Override
                public void OnResponseReady(boolean error, JSONObject data, String message) {
                    if(!swiper.isRefreshing()){
                        loading.setVisibility(View.GONE);
                    }
                    if (!error) {
                        errorHolder.setVisibility(View.GONE);
                        MessagesController controller = MessagesController.getInstance();
                        try {
                            JSONArray channelsId = data.getJSONArray("data");
                            final ArrayList<Channel> channels = new ArrayList<>();
                            int size = channelsId.length();

                            adapter.clear();
                            for (int i = 0; i < size; i++) {
//                            TLRPC.TL_contacts_search req = new TLRPC.TL_contacts_search();
                                JSONObject item = channelsId.getJSONObject(i);
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
                                }
                                else {
                                Defaults.getInstance().loadChannel(currentChannel, new OnChannelReady() {
                                    @Override
                                    public void onReady(Channel channel, boolean isOk) {
                                        if (isOk) {
                                            adapter.add(currentChannel);
                                            //channels.add(currentChannel);
                                            adapter.notifyDataSetChanged();
                                        }
                                        else {
                                            currentChannel.title = currentChannel.name;
                                            adapter.add(currentChannel);
                                            //channels.add(currentChannel);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }

                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorHolder.setVisibility(View.VISIBLE);
                    }
                    if(swiper.isRefreshing()){
                        swiper.setRefreshing(false);
                    }
                }
            });

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
