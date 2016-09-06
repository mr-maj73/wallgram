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

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.adapter.ConditionAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mohammad on 05/09/2016.
 */

@SuppressLint("ValidFragment")
public class AddRequstAdvertisingDialog extends DialogFragment {
    public static AddRequstAdvertisingDialog newInstance(long id) {
        return new AddRequstAdvertisingDialog(id);
    }

    public long id;

    @SuppressLint("ValidFragment")
    public AddRequstAdvertisingDialog(long id) {
        super();
        this.id = id;
    }

    TextInputLayout layPrice;
    EditText edtPriceAddMember;
    EditText edtDescriptionAddMember;
    TextView txtErrorDialog;
    LinearLayout layMoreCondition;
    TextView txtSubmitDialog;
    RecyclerView listCondition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setLayout(200, 400);
        View v = inflater.inflate(R.layout.advertising_dialog, container, false);
        //  View tv = v.findViewById(R.id.text);
        //   ((TextView)tv).setText("This is an instance of MyDialogFragment");
        layPrice = (TextInputLayout) v.findViewById(R.id.layPrice);
        edtPriceAddMember = (EditText) v.findViewById(R.id.edtPriceAddMember);
        edtDescriptionAddMember = (EditText) v.findViewById(R.id.edtDescriptionAddMember);
        txtErrorDialog = (TextView) v.findViewById(R.id.txtErrorDialog);
        layMoreCondition = (LinearLayout) v.findViewById(R.id.layMoreCondition);
        txtSubmitDialog = (TextView) v.findViewById(R.id.txtSubmitDialog);
    




        layMoreCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return v;
    }
}
