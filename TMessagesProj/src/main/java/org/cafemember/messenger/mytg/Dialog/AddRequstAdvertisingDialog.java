package org.cafemember.messenger.mytg.Dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.CategoryChannel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.adapter.SpinnerAdapter;
import org.cafemember.messenger.mytg.fragments.MyChannelFragment;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mohammad on 05/09/2016.
 */

@SuppressLint("ValidFragment")
public class AddRequstAdvertisingDialog extends DialogFragment {
    private TextView txtAlertDialogAddMember;

    public static AddRequstAdvertisingDialog newInstance(long id) {
        return new AddRequstAdvertisingDialog(id);
    }

    public long id;

    @SuppressLint("ValidFragment")
    public AddRequstAdvertisingDialog(long id) {
        super();
        this.id = id;
    }

    //  TextInputLayout layPriceAddMember;
    EditText edtPriceAddMember;
    EditText edtDescriptionAddMember;
    EditText edtAdminLinkAddMember;
    LinearLayout submitAddMemberLayout;
    Spinner spinnerAddMember;
    String description;
    int price = -789;
    int category_id = 0;
    String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.add_requst_advertising_dialog, container, false);

        // layPriceAddMember = (TextInputLayout) v.findViewById(R.id.layPriceAddMember);
        edtPriceAddMember = (EditText) v.findViewById(R.id.edtPriceAddMember);
        edtDescriptionAddMember = (EditText) v.findViewById(R.id.edtDescriptionAddMember);
        edtAdminLinkAddMember = (EditText) v.findViewById(R.id.edtAdminLinkAddMember);
        submitAddMemberLayout = (LinearLayout) v.findViewById(R.id.submitAddMemberLayout);
        spinnerAddMember = (Spinner) v.findViewById(R.id.spinnerAddMember);
        txtAlertDialogAddMember = (TextView) v.findViewById(R.id.txtAlertDialogAddMember);
        txtAlertDialogAddMember.setVisibility(View.INVISIBLE);
        initializeSpinner(getActivity());

        submitAddMemberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkparams()) {
                    return;
               }
               Commands.addChannel((int) id, category_id, price, link, description, new OnResponseReadyListener() {
                   @Override
                   public void OnResponseReady(boolean error, JSONObject data, String message) {
                      if(error) {
                          txtAlertDialogAddMember.setText(message);
                          txtAlertDialogAddMember.setVisibility(View.VISIBLE);
                      }else {
                          Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.successMessage), Toast.LENGTH_SHORT).show();
                          dismiss();
                      }
                   }
               });


            }
        });


        return v;
    }

    private boolean checkparams() {
        description = edtDescriptionAddMember.getText().toString();
        link = edtAdminLinkAddMember.getText().toString();
        if (edtPriceAddMember.getText().toString().length() > 0) {
            try {
                price = Integer.parseInt(edtPriceAddMember.getText().toString());
            } catch (Exception e) {
                txtAlertDialogAddMember.setText(R.string.errorInputNumberAddMember);
                txtAlertDialogAddMember.setVisibility(View.VISIBLE);
                return false;
            }

        }
        if (description != null && link != null && description.length() > 5 && link.length() > 5 && price != -789 && category_id != 0) {
            return true;
        }
        txtAlertDialogAddMember.setText(R.string.errorCompliteAddMember);
        txtAlertDialogAddMember.setVisibility(View.VISIBLE);
        return false;
    }

    private void initializeSpinner(final Context context) {

        final ArrayList<CategoryChannel> cats = new ArrayList<>();

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
                                category_id = cats.get(position).id;
//                                loadMore(category);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinnerAddMember.setOnItemSelectedListener(listener);
                        SpinnerAdapter customAdapter = new SpinnerAdapter(context, cats);
                        spinnerAddMember.setAdapter(customAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
