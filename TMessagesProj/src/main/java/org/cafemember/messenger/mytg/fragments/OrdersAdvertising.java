package org.cafemember.messenger.mytg.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelOrderAdvertising;
import org.cafemember.messenger.mytg.ChannelOrderMemberShip;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.adapter.OrdersAdvertisingAdapter;
import org.cafemember.messenger.mytg.adapter.OrdersMembershipAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohammad on 11/09/2016.
 */
public class OrdersAdvertising extends Fragment
{
    private View layout;
    private RecyclerView orderadvertisingList;
    private LinearLayout loading;
    private LinearLayout errorLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.orders_advertising_fragment, null);
        loading = (LinearLayout) layout.findViewById(R.id.holder);
        errorLay = (LinearLayout) layout.findViewById(R.id.errorLay);
        errorLay.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        orderadvertisingList = (RecyclerView) layout.findViewById(R.id.listOrdersAdvertising);
        orderadvertisingList.setHasFixedSize(true);
        orderadvertisingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        intilizeMemberShip();

        return layout;

    }

    public void intilizeMemberShip() {
        Commands.getWallHistory(new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    errorLay.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }else {
                    try {
                        errorLay.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        ArrayList<ChannelOrderAdvertising> members = new ArrayList<ChannelOrderAdvertising>();
                        JSONArray channel = data.getJSONArray("data");
                        for (int i = 0; i < channel.length(); i++) {
                            ChannelOrderAdvertising member = new ChannelOrderAdvertising();
                            JSONObject object = channel.getJSONObject(i);
                            member.id = object.getString("id");
                            member.type = object.getString("type");
                            member.user = object.getString("user");
                            member.name = object.getString("name");
                            member.title = object.getString("title");
                            member.date = object.getString("date");
                            member.rule = object.getString("rule");
                            member.price = object.getString("price");
                            member.admin_link = object.getString("admin_link");
                            member.byteString = object.getString("byteString");
                            members.add(member);

                        }
                        Log.i("momhammad", "tttttttt");
                        OrdersAdvertisingAdapter adapter = new OrdersAdvertisingAdapter(getActivity(), members);
                        orderadvertisingList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        errorLay.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }


            }
        });
    }


}
