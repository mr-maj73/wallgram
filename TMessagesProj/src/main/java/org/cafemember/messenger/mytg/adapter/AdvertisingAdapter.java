package org.cafemember.messenger.mytg.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.ChannelAdvertising;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.AdvertisingFragment;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/09/2016.
 */

public class AdvertisingAdapter extends
        RecyclerView.Adapter<AdvertisingAdapter.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<ChannelAdvertising> advChannels;
    private Context context;
    private AdvertisingFragment advertisingFragment;


    public AdvertisingAdapter(Context context, ArrayList<ChannelAdvertising> advChannels, AdvertisingFragment advertisingFragment) {
        this.context = context;
        this.advChannels = advChannels;
        this.advertisingFragment = advertisingFragment;

//            this.listener = listener;

    }

    @Override
    public int getItemCount() {
        return (null != advChannels ? advChannels.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ChannelAdvertising channelAdv = advChannels.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder


        mainHolder.textPriceAdver.setText("" + channelAdv.price);

        if (channelAdv.title != null && channelAdv.title.length() > 1) {
            mainHolder.textTitleAdv.setText(channelAdv.title);
        } else {
            mainHolder.textTitleAdv.setText(channelAdv.name);
        }
        mainHolder.textDescription.setText(channelAdv.description);
        mainHolder.imgChannalAdv.setImageBitmap(channelAdv.getBitMap());
        mainHolder.layConnectAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/"+channelAdv.admin_link));
                context.startActivity(telegram);
            }
        });
        mainHolder.laySubmitAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisingFragment.showDialog(channelAdv.id);
            }
        });
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


        mainHolder.txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainHolder.reportLayout.getVisibility() == View.VISIBLE) {
                    alertDialogReporter((int)channelAdv.id,mainHolder.reportLayout);

                }
            }
        });
        mainHolder.advertisingLayoutSliding.setOnClickListener(new View.OnClickListener() {
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
                R.layout.advertising_item, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // View holder for gridview recycler view as we used in listview

        public TextView textPriceAdver;
        public TextView textDescription;
        public TextView textTitleAdv;
        public ImageView imgChannalAdv;
        public ImageView imgMore;
        LinearLayout reportLayout;
        TextView txtReport;
        public LinearLayout laySubmitAdv;
        public LinearLayout layConnectAdv;
        public CardView advertisingLayoutSliding;


        public RecyclerViewHolder(View view) {
            super(view);

            this.textPriceAdver = (TextView) view.findViewById(R.id.textPriceAdver);
            this.textDescription = (TextView) view.findViewById(R.id.textDescription);
            this.textTitleAdv = (TextView) view.findViewById(R.id.textTitleAdv);
            this.imgChannalAdv = (ImageView) view.findViewById(R.id.imgChannalAdv);
            this.imgMore = (ImageView) view.findViewById(R.id.imgOptionAdv);
            this.laySubmitAdv = (LinearLayout) view.findViewById(R.id.laySubmitAdv);
            this.reportLayout = (LinearLayout) view.findViewById(R.id.reportLayout);
            this.txtReport = (TextView) view.findViewById(R.id.txtReport);
            this.layConnectAdv = (LinearLayout) view.findViewById(R.id.layConnectAdv);
            this.advertisingLayoutSliding = (CardView) view.findViewById(R.id.advertisingLayoutSliding);
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
                Commands.reportWall( channel_id, m_Text, new OnResponseReadyListener() {
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

