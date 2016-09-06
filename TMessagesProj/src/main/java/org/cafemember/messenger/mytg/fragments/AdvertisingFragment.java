package org.cafemember.messenger.mytg.fragments;

import android.annotation.SuppressLint;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.TextView;


import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.CategoryChannel;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.Dialog.AdvertisingDialog;
import org.cafemember.messenger.mytg.adapter.AdvertisingAdapter;
import org.cafemember.messenger.mytg.adapter.SpinnerAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.listeners.Refrashable;
import org.cafemember.ui.DialogsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mohammad on 03/09/2016.
 */
public class AdvertisingFragment extends Fragment implements Refrashable, SwipeRefreshLayout.OnRefreshListener {

    private long lastCheck;
    private SwipeRefreshLayout swipAdv;
    private LinearLayout loading;
    private RecyclerView channelsListAdv;
    private ArrayList<ChannelAdvertising> channels = new ArrayList<ChannelAdvertising>();
    ;
    private AdvertisingAdapter adapter;
    private Spinner spinner;
    private int category = 0;


    private View layout;
    //   private MaterialSpinner spinner1;
    String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.advertising_fragment_layout, null);
        loading = (LinearLayout) layout.findViewById(R.id.holder);
        spinner = (Spinner) layout.findViewById(R.id.spinnerCatsChannel);

        loading.setVisibility(View.GONE);


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1 = (MaterialSpinner)layout.findViewById(R.id.spinner);
//        spinner1.setAdapter(adapter);


        swipAdv = (SwipeRefreshLayout) layout.findViewById(R.id.swipAdv);
        channelsListAdv = (RecyclerView) layout.findViewById(R.id.channelsListAdv);
        channelsListAdv.setHasFixedSize(true);
        channelsListAdv.setLayoutManager(new LinearLayoutManager(getActivity()));

//        adapter = new AdvertisingAdapter(G.context, G.flights, res, new Flight_Adapter_Listener() {
//            @Override
//            public void callback(FlightData flight) {
//                android.app.Fragment fragment = new WebsiteFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("link", flight.getLink());
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_container, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
//        channelsListAdv.setAdapter(adapter);// set adapter on recyclerview
//        adapter.notifyDataSetChanged();

        swipAdv.setOnRefreshListener(this);
        swipAdv.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        initializeSpinner(getActivity());
        loadMore(category);

        return layout;

    }

    public void loadMore(int category) {
        long now = System.currentTimeMillis();
        if (adapter != null) {
            channels.clear();
            adapter.notifyDataSetChanged();

        }


        lastCheck = now;
        if (!swipAdv.isRefreshing()) {
            loading.setVisibility(View.VISIBLE);
        }
        Commands.getWall(category, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!swipAdv.isRefreshing()) {
                    loading.setVisibility(View.GONE);
                }
                if (!error) {
                    layout.findViewById(R.id.error).setVisibility(View.GONE);

                    try {
                        JSONArray channelsId = data.getJSONArray("data");

                        int size = channelsId.length();

                        for (int i = 0; i < size; i++) {

                            JSONObject item = channelsId.getJSONObject(i);
                            final int cu = i;
                            final ChannelAdvertising currentChannel = new ChannelAdvertising(item.getString("name"), item.getInt("id"));
                            currentChannel.admin_link = item.getString("admin_link");
                            currentChannel.description = item.getString("desc");
                            currentChannel.price = item.getInt("price");
                            String byteString = null;
                            if (item.has("title") && item.getString("title").length() > 0) {
                                currentChannel.title = item.getString("title");
                            }
                            if (item.has("byteString") && item.getString("byteString").length() > 0) {
                                byteString = item.getString("byteString");
                                currentChannel.setPhoto(byteString);
                            }

                            channels.add(currentChannel);
                        }

                        adapter = new AdvertisingAdapter(getActivity(), channels, AdvertisingFragment.this);
                        channelsListAdv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    TextView tv = (TextView) layout.findViewById(R.id.error);
                    if (message != null && message.length() > 0) {
                        tv.setText(message);
                    }
                    tv.setVisibility(View.VISIBLE);
                }
                if (swipAdv.isRefreshing()) {
                    swipAdv.setRefreshing(false);
                }
            }
        });


    }

    private void initializeSpinner(final Context context) {

        final ArrayList<CategoryChannel> cats = new ArrayList<>();
        CategoryChannel cat = new CategoryChannel();
        cat.id = 0;
        cat.name = getResources().getString(R.string.all);
        cats.add(cat);
        Commands.categories(new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
                    try {
                        JSONArray channelsId = data.getJSONArray("data");
                        for (int i = 0; i < channelsId.length(); i++) {
                            JSONObject item = channelsId.getJSONObject(i);
                            CategoryChannel cat = new CategoryChannel();
                            cat.id = item.getInt("id");
                            cat.name = item.getString("name");
                            cats.add(cat);
                        }

                        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                category = cats.get(position).id;
                                loadMore(category);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinner.setOnItemSelectedListener(listener);
                        SpinnerAdapter customAdapter = new SpinnerAdapter(context, cats);
                        spinner.setAdapter(customAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        loadMore(category);
    }

    @Override
    public void refresh() {

    }


    public void showDialog() {
        DialogFragment newFragment = AdvertisingDialog.newInstance();
        newFragment.show(getFragmentManager(), "dialog");

    }


}


