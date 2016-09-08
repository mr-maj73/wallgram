package org.cafemember.messenger.mytg.Dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.CategoryChannel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.MySuggestAdsAdapter;
import org.cafemember.messenger.mytg.adapter.SpinnerAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mohammad on 05/09/2016.
 */

@SuppressLint("ValidFragment")
public class SuggestionDialog extends DialogFragment {
    private TextView txtAlertDialogAddMember;
    private int id;
    private String options;
    private ViewGroup view;
    private ScrollView scrollViewSuggest;
    LinearLayout accseptSuggestLay;
    LinearLayout rejectSuggestLay;
    String[] rules;
    int numberSuggest;
    private LinearLayout layoutSuggest;
    MySuggestAdsAdapter mySuggestAdsAdapter;
    int object;
    public static SuggestionDialog newInstance(int id, String options, ViewGroup view, int numberSuggest,MySuggestAdsAdapter mySuggestAdsAdapter,int object) {
        return new SuggestionDialog(id, options, view, numberSuggest,mySuggestAdsAdapter, object);
    }


    @SuppressLint("ValidFragment")
    public SuggestionDialog(int id, String options, ViewGroup view, int numberSuggest,MySuggestAdsAdapter mySuggestAdsAdapter,int object) {
        super();
        this.id = id;
        this.options = options;
        this.view = view;
        this.numberSuggest = numberSuggest;
        this.mySuggestAdsAdapter = mySuggestAdsAdapter;
        this. object = object;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.show_suggest, container, false);

        accseptSuggestLay = (LinearLayout) v.findViewById(R.id.accseptSuggestLay);
        rejectSuggestLay = (LinearLayout) v.findViewById(R.id.rejectSuggestLay);
        layoutSuggest = (LinearLayout) v.findViewById(R.id.layoutSuggest);
        scrollViewSuggest = (ScrollView) v.findViewById(R.id.scrollViewSuggest);

        if (options.length() > 0) {
            rules = options.split("@@@");

            for (int i = 0; i < rules.length; i++) {
                View child = inflater.inflate(R.layout.show_suggest_item, null);
                TextView txtSuggestItem = (TextView) child.findViewById(R.id.txtSuggestItem);
                txtSuggestItem.setText(rules[i]);
                layoutSuggest.addView(child);
            }
        }
        accseptSuggestLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commands.acceptAd(id, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        if (!error) {
                            mySuggestAdsAdapter.removeItem(object,numberSuggest);
                            view.removeViewAt(numberSuggest);
                            dismiss();
                        } else {
                            Toast.makeText(getActivity(), "vdvfv", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        rejectSuggestLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commands.rejectAd(id, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        if (!error) {
                            mySuggestAdsAdapter.removeItem(object,numberSuggest);
                            view.removeViewAt(numberSuggest);
                            dismiss();
                        } else {
                            Toast.makeText(getActivity(), "vdvfv", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        FontManager.instance().setTypefaceImmediate(v);

        return v;
    }


}
