package org.cafemember.messenger.mytg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
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

import okio.ByteString;

/**
 * Created by Masoud on 6/2/2016.
 */
public class MySuggestAdsAdapter extends ArrayAdapter

{
    private JSONArray history;
    private MySuggestAds fragment;
    private int index=1;


    public MySuggestAdsAdapter(Context context, int resource, JSONArray objects, MySuggestAds fragment) {
        super(context, resource);
        history = objects;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        final JSONObject coin = getItem(position);
        HistoryViewHolder viewHolder;
        LayoutInflater vi = LayoutInflater.from(getContext());
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
            viewHolder.txtNosuggest.setVisibility(View.GONE);

            final String name = coin.getString("name");
            int id = coin.getInt("id");
            int price = coin.getInt("price");
            String title = coin.getString("title");
            String desc = coin.getString("desc");
            String byteString = coin.getString("byteString");
            String category = coin.getString("category");
            String adsPrice = coin.getString("adsPrice");
            String adsId = coin.getString("adsId");
            String adsUser = coin.getString("adsUser");
            String adsRule = coin.getString("adsRule");
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
                String[] prices = adsPrice.split("~");
                String[] users = adsUser.split("~");
                String[] ids = adsId.split("~");
                String[] rules = adsRule.split("~");
                for (int i = 0; i < ids.length; i++) {
                    View child = vi.inflate(R.layout.item_suggest, null);
                    TextView childPrice = (TextView) child.findViewById(R.id.txtItemPrice);
                    TextView childmobile1 = (TextView) child.findViewById(R.id.txtMobile1);
                    TextView childmobile2 = (TextView) child.findViewById(R.id.txtMobile2);
                    TextView txtShowSuggest = (TextView) child.findViewById(R.id.txtShowSuggest);
                    childPrice.setText(prices[i]);
                    String mob = "0" + prices[i].substring(2, 5);
                    childmobile1.setText("0" + users[i].substring(2, 5));
                    childmobile2.setText("0" + users[i].substring(9));


                    final String option = rules[i];
                    final int idSggest = Integer.parseInt(ids[i]);
                    final ViewGroup ve = viewHolder.laySuggest;
                    final int numberSuggest=i++;
                    txtShowSuggest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogAddMember(idSggest, option, ve,numberSuggest);
                        }
                    });


                    viewHolder.laySuggest.addView(child,numberSuggest);

                }


            } else {
                viewHolder.txtNosuggest.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
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

    public void showDialogAddMember(int id, String options, ViewGroup view,int numberSuggest) {
        DialogFragment newFragment = SuggestionDialog.newInstance(id, options, view, numberSuggest);
        Activity f = fragment.getParentActivity();
        if (f instanceof LaunchActivity) {
            newFragment.show(((LaunchActivity) f).getSupportFragmentManager(), "dialog");
        }

    }
}
