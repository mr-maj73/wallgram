package org.cafemember.messenger.mytg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Dialog.AddRequstAdvertisingDialog;
import org.cafemember.messenger.mytg.Dialog.SuggestionDialog;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.MySuggestAds;
import org.cafemember.messenger.mytg.util.FileConvert;
import org.cafemember.ui.LaunchActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okio.ByteString;

/**
 * Created by Masoud on 6/2/2016.
 */
public class MySuggestAdsAdapter extends ArrayAdapter

{
    private JSONArray history;
    private MySuggestAds fragment;
    private int index = 1;
    LayoutInflater vi;

    public MySuggestAdsAdapter(Context context, int resource, JSONArray objects, MySuggestAds fragment) {
        super(context, resource);
        history = objects;
        this.fragment = fragment;
        vi = LayoutInflater.from(getContext());
        jsonInitialize();

    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final JSONObject coin = getItem(position);
        HistoryViewHolder viewHolder;

        if (v == null) {

            // vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_mysuggest_ads, parent, false);
            viewHolder = new HistoryViewHolder();
            viewHolder.txtCategoty = (TextView) v.findViewById(R.id.txtCategoty);
            viewHolder.textDescription = (TextView) v.findViewById(R.id.textDescription);
            viewHolder.textTitleAdv = (TextView) v.findViewById(R.id.textTitleAdv);
            viewHolder.textPriceAdver = (TextView) v.findViewById(R.id.textPriceAdver);
            viewHolder.txtNosuggest = (TextView) v.findViewById(R.id.txtNosuggest);
            viewHolder.imgOptionAdv = (ImageView) v.findViewById(R.id.imgOptionAdv);
            viewHolder.imgChannalAdv = (ImageView) v.findViewById(R.id.imgChannalAdv);
            viewHolder.laySuggest = (LinearLayout) v.findViewById(R.id.laySuggest);
            v.setTag(viewHolder);
        } else {
            viewHolder = (HistoryViewHolder) v.getTag();
        }
        try {

            viewHolder.laySuggest.removeAllViews();
            final String name = coin.getString("name");
            int id = coin.getInt("id");
            int price = coin.getInt("price");
            String title = coin.getString("title");
            String desc = coin.getString("desc");
            String byteString = coin.getString("byteString");
            String category = coin.getString("category");
            final JSONArray adsPrice = coin.getJSONArray("adsPrice");
            final JSONArray adsId = coin.getJSONArray("adsId");
            final JSONArray adsUser = coin.getJSONArray("adsUser");
            final JSONArray adsRule = coin.getJSONArray("adsRule");
            viewHolder.txtCategoty.setText(category);
            viewHolder.textDescription.setText("توضیحات : " + desc);
            if (name.length() > 0) {
                viewHolder.textTitleAdv.setText(name);
            } else {
                viewHolder.textTitleAdv.setText(title);
            }
            viewHolder.textPriceAdver.setText("" + price);
            if (byteString != null && byteString.length() > 0) {
                viewHolder.imgChannalAdv.setImageBitmap(FileConvert.getBitmapFromString(byteString));
            }


            if (adsUser.length() > 0) {
                for (int i = 0; i < adsUser.length(); i++) {
                    View child = vi.inflate(R.layout.item_suggest, null);
                    TextView childPrice = (TextView) child.findViewById(R.id.txtItemPrice);
                    TextView childmobile1 = (TextView) child.findViewById(R.id.txtMobile1);
                    TextView childmobile2 = (TextView) child.findViewById(R.id.txtMobile2);
                    TextView txtShowSuggest = (TextView) child.findViewById(R.id.txtShowSuggest);
                    childPrice.setText((String) adsPrice.get(i));
                    childmobile1.setText("0" + ((String) adsUser.get(i)).substring(2, 5));
                    childmobile2.setText(((String) adsUser.get(i)).substring(9));


                    final String option = (String) adsRule.get(i);
                    final int idSggest = Integer.parseInt((String) adsId.get(i));
                    final ViewGroup ve = viewHolder.laySuggest;
                    final int numberSuggest = i;
                    txtShowSuggest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogAddMember(idSggest, option, ve, numberSuggest, MySuggestAdsAdapter.this, position);
                        }
                    });


                    viewHolder.laySuggest.addView(child, numberSuggest);

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        FontManager.instance().setTypefaceImmediate(v);
        return v;

    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return history.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return history.length();
    }

    public class HistoryViewHolder {

        TextView textTitleAdv;
        TextView txtCategoty;
        TextView textDescription;
        TextView textPriceAdver;
        TextView txtNosuggest;
        ImageView imgOptionAdv;
        ImageView imgChannalAdv;
        LinearLayout laySuggest;
    }

    public void showDialogAddMember(int id, String options, ViewGroup view, int numberSuggest, MySuggestAdsAdapter mySuggestAdsAdapter, int object) {
        DialogFragment newFragment = SuggestionDialog.newInstance(id, options, view, numberSuggest, mySuggestAdsAdapter, object);
        Activity f = fragment.getParentActivity();
        if (f instanceof LaunchActivity) {
            newFragment.show(((LaunchActivity) f).getSupportFragmentManager(), "dialog");
        }

    }

    private void jsonInitialize() {
        try {
            for (int i = 0; i < history.length(); i++) {
                JSONObject js = (JSONObject) history.get(i);
                String[] prices = js.getString("adsPrice").split("~");
                String[] users = js.getString("adsUser").split("~");
                String[] ids = js.getString("adsId").split("~");
                String[] rules = js.getString("adsRule").split("~");
                JSONArray jsonArray1 = new JSONArray();
                JSONArray jsonArray2 = new JSONArray();
                JSONArray jsonArray3 = new JSONArray();
                JSONArray jsonArray4 = new JSONArray();
                for (int j = 0; j < users.length; j++) {

                    jsonArray1.put(prices[j]);
                    jsonArray2.put(users[j]);
                    jsonArray3.put(ids[j]);
                    jsonArray4.put(rules[j]);

                }
                js.put("adsPrice", jsonArray1);
                js.put("adsUser", jsonArray2);
                js.put("adsId", jsonArray3);
                js.put("adsRule", jsonArray4);

                Log.i("jasonRference", history.toString(8));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void removeItem(int object, int id) {
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<String> list3 = new ArrayList<String>();
        ArrayList<String> list4 = new ArrayList<String>();
        try {
            JSONObject js = (JSONObject) history.get(object);

            JSONArray adsPrice = js.getJSONArray("adsPrice");
            JSONArray adsId = js.getJSONArray("adsId");
            JSONArray adsUser = js.getJSONArray("adsUser");
            JSONArray adsRule = js.getJSONArray("adsRule");

            int len = adsPrice.length();
            if (adsPrice != null) {
                for (int i = 0; i < len; i++) {
                    list1.add(adsPrice.get(i).toString());
                    list2.add(adsId.get(i).toString());
                    list3.add(adsUser.get(i).toString());
                    list4.add(adsRule.get(i).toString());
                }
            }
            list1.remove(id);
            list2.remove(id);
            list3.remove(id);
            list4.remove(id);
            js.put("adsPrice", new JSONArray(list1));
            js.put("adsUser", new JSONArray(list2));
            js.put("adsId", new JSONArray(list3));
            js.put("adsRule", new JSONArray(list4));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
