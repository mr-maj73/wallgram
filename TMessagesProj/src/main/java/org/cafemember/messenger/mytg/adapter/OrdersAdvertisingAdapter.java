package org.cafemember.messenger.mytg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.ChannelOrderAdvertising;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.Dialog.SuggestionDialog;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.ui.LaunchActivity;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/09/2016.
 */

public class OrdersAdvertisingAdapter extends
        RecyclerView.Adapter<OrdersAdvertisingAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<ChannelOrderAdvertising> advChannels;
    private FragmentActivity context;



    public OrdersAdvertisingAdapter(FragmentActivity context, ArrayList<ChannelOrderAdvertising> advChannels) {
        this.context = context;
        this.advChannels = advChannels;


    }

    @Override
    public int getItemCount() {
        return (null != advChannels ? advChannels.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ChannelOrderAdvertising channelAdv = advChannels.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder



        mainHolder.reportLayout.setVisibility(View.GONE);
        mainHolder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mainHolder.reportLayout.getVisibility()==View.VISIBLE){
                    mainHolder.reportLayout.setVisibility(View.GONE);
                }
            }
        });
        if (channelAdv.name.length() > 0) {
            mainHolder.title.setText(channelAdv.name);
        } else {
            mainHolder.title.setText(channelAdv.title);
        }

//        Log.i("momhammad","ddddddddddd");
        mainHolder.txtCoindeal.setText(channelAdv.price);
        mainHolder.txtOrderpicker.setText("0" +channelAdv.user.substring(2, 5)+"***"+channelAdv.user.substring(8));
        mainHolder.txtDate.setText(channelAdv.date);
        mainHolder.imageChannel.setImageBitmap(channelAdv.getBitMap());
        int type = Integer.parseInt(channelAdv.type);
        if (type == 0) {
            mainHolder.status.setText("در انتظار تایید");
            mainHolder.status.setTextColor(Color.parseColor("#a8a8a8"));
        } else if (type == 1) {
            Log.i("mohammad", "noConnect");
            mainHolder.status.setText("انجام شده");
            mainHolder.status.setTextColor(Color.parseColor("#39b54a"));

        }else if (type == 2) {
            Log.i("mohammad", "noConnect");
            mainHolder.status.setText("رد شده");
            mainHolder.status.setTextColor(Color.parseColor("#d84152"));

        } else {
            mainHolder.status.setText("پذیرفته شده");
            mainHolder.status.setTextColor(Color.parseColor("#39b54a"));
        }
        if(type==1){
            mainHolder.imgMore.setVisibility(View.VISIBLE);
        }else{
            mainHolder.imgMore.setVisibility(View.GONE);
        }
        mainHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainHolder.reportLayout.getVisibility() == View.GONE) {
                    mainHolder.reportLayout.setVisibility(View.VISIBLE);
                } else {
                    mainHolder.reportLayout.setVisibility(View.GONE);
                }
            }
        });
        mainHolder.imageChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/"+channelAdv.admin_link));
                context.startActivity(telegram);
            }
        });
        mainHolder.txtShowCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                advertisingFragment.showDialog(channelAdv.id);
                DialogFragment newFragment = SuggestionDialog.newInstance(0, channelAdv.rule, null, 0, null, 0);
                if (context instanceof LaunchActivity) {
                    newFragment.show(((LaunchActivity) context).getSupportFragmentManager(), "dialog");
                }
            }
        });
        mainHolder.txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainHolder.reportLayout.getVisibility() == View.VISIBLE) {
                    alertDialogReporter(Integer.parseInt(channelAdv.id),mainHolder.reportLayout);

                }
            }
        });

        mainHolder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainHolder.reportLayout.getVisibility() == View.VISIBLE) {
                    mainHolder.reportLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_orders_advertising, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public TextView title;
        public TextView txtOrderpicker;
        public TextView txtCoindeal;
        public TextView txtShowCondition;
        public TextView txtReport;
        public TextView txtDate;
        public ImageView imageChannel;
        public ImageView imgMore;
        public LinearLayout reportLayout;
        public CardView cardOrderAdv;
        public RelativeLayout backLayout;
        public TextView status;


        public RecyclerViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.title);;
            this.status = (TextView) view.findViewById(R.id.txtStatus);
            this.txtOrderpicker = (TextView) view.findViewById(R.id.txtOrderpicker);
            this.txtCoindeal = (TextView) view.findViewById(R.id.txtCoindeal);
            this.txtShowCondition = (TextView) view.findViewById(R.id.txtShowCondition);
            this.txtDate = (TextView) view.findViewById(R.id.txtDate);
            this.txtReport = (TextView) view.findViewById(R.id.txtReport);
            this.imageChannel = (ImageView) view.findViewById(R.id.imageChannel);
            this.imgMore = (ImageView) view.findViewById(R.id.imgMore);
            this.reportLayout = (LinearLayout) view.findViewById(R.id.reportLayout);
            this.backLayout = (RelativeLayout) view.findViewById(R.id.backLayout);

            this.cardOrderAdv = (CardView) view.findViewById(R.id.cardOrderAdv);
            FontManager.instance().setTypefaceImmediate(view);


        }


    }

    private void alertDialogReporter(final int channel_id, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("گزارش آگهی");

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint("دلیل گزارش");
        input.setPadding(8,0,8,0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(20, 10, 20, 10);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);


        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                Commands.reportAdv( channel_id, m_Text, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        Toast.makeText(context, error ? "خطا در گزارش آگهی" : "آگهی گزارش شد", Toast.LENGTH_SHORT).show();
                    }
                });
                view.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                view.setVisibility(View.GONE);
            }
        });

        builder.show();

    }



}

