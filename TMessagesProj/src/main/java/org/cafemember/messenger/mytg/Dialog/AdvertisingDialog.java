package org.cafemember.messenger.mytg.Dialog;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.adapter.ConditionAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mohammad on 05/09/2016.
 */

public class AdvertisingDialog extends DialogFragment {
    public static AdvertisingDialog newInstance() {
        return new AdvertisingDialog();
    }

    public static Map<String, String> condition = new HashMap<String, String>();
    ConditionAdapter adapter;
    int i = 1;
    TextInputLayout layPrice;
    EditText edtPriceDialog;
    TextView txtErrorDialog;
    LinearLayout layMoreCondition;
    TextView txtSubmitDialog;
    RecyclerView listCondition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.advertising_dialog, container, false);
        //  View tv = v.findViewById(R.id.text);
        //   ((TextView)tv).setText("This is an instance of MyDialogFragment");
        layPrice = (TextInputLayout) v.findViewById(R.id.layPrice);
        edtPriceDialog = (EditText) v.findViewById(R.id.edtPriceDialog);
        txtErrorDialog = (TextView) v.findViewById(R.id.txtErrorDialog);
        layMoreCondition = (LinearLayout) v.findViewById(R.id.layMoreCondition);
        txtSubmitDialog = (TextView) v.findViewById(R.id.txtSubmitDialog);
        listCondition = (RecyclerView) v.findViewById(R.id.listCondition);

        condition.put("" + 0, "");
        condition.put("" + i++, "");
        condition.put("" + i++, "");

        listCondition.setHasFixedSize(true);
        listCondition.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ConditionAdapter(getActivity(), condition);
        listCondition.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        layMoreCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < 10) {
                                       condition.put("" + i++, "");
                               adapter.notifyDataSetChanged();
                    listCondition.smoothScrollToPosition(i);
                }
            }
        });
        txtSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPriceDialog.getText().toString() == null || edtPriceDialog.getText().toString().length() < 2) {
                    txtErrorDialog.setText(R.string.errorDialogComplite);
                    return;
                }
                String cond="";
                Log.i("mohammad","dddddd");
                 Collection<String> conditions = adapter.getcondition().values();
                List<String> list = new ArrayList<String>(conditions);

                for (int i = 0; i < list.size(); i++) {
                   if(list.get(i)!=null&&list.get(i).length()>1){
                       Log.i("mohammad","dddddd"+list.get(i));
                       cond=cond+"$$$"+list.get(i);
                   }
                }
                Log.i("mohammad",cond);
            }

        });


        return v;
    }
}
