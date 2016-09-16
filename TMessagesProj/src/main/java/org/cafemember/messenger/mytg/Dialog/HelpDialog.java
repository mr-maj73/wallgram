package org.cafemember.messenger.mytg.Dialog;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.MySuggestAdsAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * Created by mohammad on 05/09/2016.
 */

@SuppressLint("ValidFragment")
public class HelpDialog extends DialogFragment {
    private int id;
    private String options;
    private String title;
    private ScrollView scrollViewSuggest;
    LinearLayout accseptSuggestLay;
    LinearLayout rejectSuggestLay;
    String[] rules;
    private LinearLayout layoutSuggest;
    public static HelpDialog newInstance(String options, String title) {
        return new HelpDialog(options,title);
    }


    @SuppressLint("ValidFragment")
    public HelpDialog(String options, String title) {
        super();
        this.options = options;
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.show_suggest, container, false);

        accseptSuggestLay = (LinearLayout) v.findViewById(R.id.accseptSuggestLay);
        rejectSuggestLay = (LinearLayout) v.findViewById(R.id.rejectSuggestLay);
        layoutSuggest = (LinearLayout) v.findViewById(R.id.layoutSuggest);
        TextView titleText = (TextView) v.findViewById(R.id.title);
        titleText.setText(title);
        scrollViewSuggest = (ScrollView) v.findViewById(R.id.scrollViewSuggest);

        rejectSuggestLay.setVisibility(View.GONE);
        accseptSuggestLay.setVisibility(View.GONE);
        if (options.length() > 0) {
            rules = options.split("\n");

            for(int i = 0; i < rules.length; i++) {
                View child = inflater.inflate(R.layout.show_suggest_item, null);
                TextView txtSuggestItem = (TextView) child.findViewById(R.id.txtSuggestItem);
                txtSuggestItem.setText(rules[i]);
                layoutSuggest.addView(child);

            }
        }
        accseptSuggestLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();


            }
        });
        FontManager.instance().setTypefaceImmediate(v);

        return v;
    }


}
