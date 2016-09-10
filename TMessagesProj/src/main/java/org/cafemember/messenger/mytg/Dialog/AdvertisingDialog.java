package org.cafemember.messenger.mytg.Dialog;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.ConditionAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mohammad on 05/09/2016.
 */

@SuppressLint("ValidFragment")
public class AdvertisingDialog extends DialogFragment {
    public static AdvertisingDialog newInstance(long id) {
        return new AdvertisingDialog(id);
    }

    public long id;

    @SuppressLint("ValidFragment")
    public AdvertisingDialog(long id) {
        super();
        this.id = id;
    }

    public ArrayList<String> condition = new ArrayList<String>();
    ConditionAdapter adapter;
    int i = 3;
//    TextInputLayout layPrice;
    EditText edtPriceDialog;
    TextInputLayout edtPriceWrapper;
    TextView txtErrorDialog;
    LinearLayout layMoreCondition;
    TextView txtSubmitDialog;
    RecyclerView listCondition;
    int price=-780;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setLayout(200, 400);
        View v = inflater.inflate(R.layout.advertising_dialog, container, false);
        //  View tv = v.findViewById(R.id.text);
        //   ((TextView)tv).setText("This is an instance of MyDialogFragment");
//        layPrice = (TextInputLayout) v.findViewById(R.id.layPrice);
        edtPriceDialog = (EditText) v.findViewById(R.id.edtPriceDialog);
        edtPriceWrapper= (TextInputLayout) v.findViewById(R.id.edtPriceWrapper);
        edtPriceWrapper.setHint(getString(R.string.coin_count_hint));
        txtErrorDialog = (TextView) v.findViewById(R.id.txtErrorDialog);
        layMoreCondition = (LinearLayout) v.findViewById(R.id.layMoreCondition);
        txtSubmitDialog = (TextView) v.findViewById(R.id.txtSubmitDialog);
        listCondition = (RecyclerView) v.findViewById(R.id.listCondition);

        condition.add("");
        condition.add("");
        condition.add("");


        listCondition.setHasFixedSize(true);
        listCondition.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ConditionAdapter(getActivity(), condition);
        listCondition.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        layMoreCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < 10) {
                    if (adapter.getcondition().get(i - 1).length() < 5) {
                        return;
                    }
                    i++;
                    condition.add("");
                    adapter.notifyDataSetChanged();
                    listCondition.smoothScrollToPosition(i);
                    if (i == 10) {
                        layMoreCondition.setVisibility(View.GONE);
                    }
                }
            }
        });
        txtSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPriceDialog.getText().toString() != null && edtPriceDialog.getText().toString().length()>0) {
                    try {
                        price = Integer.parseInt(edtPriceDialog.getText().toString());
                    }catch (Exception e){
                        txtErrorDialog.setText(R.string.errorDialogComplite);
                        return;
                    }

                }else {
                    return;
                }

                String cond = "";

                ArrayList<String> list = new ArrayList<String>();
                list = adapter.getcondition();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) != null && list.get(i).length() > 5) {
                        Log.i("mohammad", "dddddd" + list.get(i));
                        if (i == 0) {
                            cond = list.get(i);
                        } else {
                            cond = cond + "$$$" + list.get(i);
                        }

                    }
                    Log.i("mohammad", cond);

                }

                Commands.suggest((int) id, Integer.parseInt(edtPriceDialog.getText().toString()), cond, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        if (!error) {
                            Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.successMessage), Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            txtErrorDialog.setText(message);
                        }
                    }
                });

            }

        });

        FontManager.instance().setTypeface(v);
        return v;
    }
}
