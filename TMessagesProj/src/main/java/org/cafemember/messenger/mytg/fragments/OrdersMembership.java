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
import org.cafemember.messenger.mytg.ChannelOrderMemberShip;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.adapter.OrdersMembershipAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohammad on 11/09/2016.
 */
public class OrdersMembership extends Fragment {
    private View layout;
    private RecyclerView listMemberShip;
    private LinearLayout loading;
    private LinearLayout errorLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.orders_membership_fragment, null);
        loading = (LinearLayout) layout.findViewById(R.id.holder);
        errorLay = (LinearLayout) layout.findViewById(R.id.errorLay);
        errorLay.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        listMemberShip = (RecyclerView) layout.findViewById(R.id.listMemberShip);
        listMemberShip.setHasFixedSize(true);
        listMemberShip.setLayoutManager(new LinearLayoutManager(getActivity()));

        intilizeMemberShip();
        return layout;

    }

    public void intilizeMemberShip() {
        Commands.getHistory(new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    errorLay.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

                }
                {
                    try {
                        errorLay.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        ArrayList<ChannelOrderMemberShip> members = new ArrayList<ChannelOrderMemberShip>();
                        JSONArray channel = data.getJSONArray("data");
                        for (int i = 0; i < channel.length(); i++) {
                            ChannelOrderMemberShip member = new ChannelOrderMemberShip();
                            JSONObject object = channel.getJSONObject(i);
                            member.status = object.getString("status");
                            member.total = object.getString("total");
                            member.name = object.getString("name");
                            member.done = object.getString("done");
//                        member.date = object.getString("date");
                            member.left = object.getString("left");
                            member.type = object.getString("type");
                            //                      member.byteString = object.getString("byteString");
                            members.add(member);

                        }

                        OrdersMembershipAdapter adapter = new OrdersMembershipAdapter(getActivity(), members);
                        listMemberShip.setAdapter(adapter);
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
