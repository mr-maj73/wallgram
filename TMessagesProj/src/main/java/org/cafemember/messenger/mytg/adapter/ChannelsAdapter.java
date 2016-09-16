package org.cafemember.messenger.mytg.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.ImageReceiver;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Channel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.ChannelsFragment;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.tgnet.TLRPC;
import org.cafemember.ui.Components.AvatarDrawable;
import org.cafemember.ui.DialogsActivity;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Masoud on 6/2/2016.
 */
public class ChannelsAdapter extends ArrayAdapter {


    private final ChannelsFragment channelsFragment;
    private final DialogsActivity dialogsActivity;
    private ArrayList<Channel> channels;

    public ChannelsAdapter(Context context, int resource, ArrayList<Channel> objects, ChannelsFragment channelsFragment, DialogsActivity dialogsActivity) {
        super(context, resource, objects);
        channels = objects;
        this.channelsFragment = channelsFragment;
        this.dialogsActivity = dialogsActivity;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        final Channel channel = getItem(position);
        final ChannelViewHolder viewHolder;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.channel_item, parent, false);
            viewHolder = new ChannelViewHolder();
            viewHolder.name = (TextView) v.findViewById(R.id.name);
            viewHolder.title = (TextView) v.findViewById(R.id.title);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            viewHolder.join = (TextView) v.findViewById(R.id.join);
            viewHolder.report = (Button) v.findViewById(R.id.report);
            viewHolder.reportLayout = (LinearLayout) v.findViewById(R.id.reportLayout);
            viewHolder.imgMore = (ImageButton) v.findViewById(R.id.imgMore);
            viewHolder.backLayout = (RelativeLayout) v.findViewById(R.id.backLayout);
            viewHolder.txtReport = (TextView) v.findViewById(R.id.txtReport);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ChannelViewHolder) v.getTag();
        }
        viewHolder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.reportLayout.getVisibility() == View.VISIBLE) {
                    viewHolder.reportLayout.setVisibility(View.GONE);
                }

            }
        });
        viewHolder.reportLayout.setVisibility(View.GONE);
        viewHolder.avatarImage = new ImageReceiver(v);
        viewHolder.avatarDrawable = new AvatarDrawable();
        viewHolder.avatarImage.setRoundRadius(AndroidUtilities.dp(26));
        int avatarLeft = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 13 : 9);
        int avatarTop = AndroidUtilities.dp(10);
        viewHolder.avatarImage.setImageCoords(avatarLeft, avatarTop, AndroidUtilities.dp(52), AndroidUtilities.dp(52));
        viewHolder.avatarDrawable.setInfo((int) channel.id, channel.name, null, channel.id < 0);
        Bitmap bitmap = null;
        if (channel.hasPhoto) {
//            Log.e("MY",channel.name+"Has Photo");
            bitmap = channel.getBitMap();
        } else {
//            Log.e("MY",channel.name+"Has NOT Photo");
            if (channel.photo != null) {
                final ChannelViewHolder cvh = viewHolder;
                Defaults.getInstance().loadPhoto((long)channel.photo.dc_id, channel.photo.volume_id,channel.photo.secret,channel.photo.local_id, new Handler(){
                    @Override
                    public void handleMessage(Message message) {
                        if (message.getData() != null && message.getData().getParcelable("bmp") != null) {
                            cvh.image.setImageBitmap((Bitmap) message.getData().getParcelable("bmp"));
                        }
                    }
                });
//                Log.e("MY",channel.name+"Has Online");
                /*TLRPC.FileLocation photo = null;
                photo = channel.photo;
                viewHolder.avatarImage.setImage(photo, "50_50", viewHolder.avatarDrawable, null, false);
                bitmap = viewHolder.avatarImage.getBitmap();
                if (bitmap != null) {
                    Commands.updateChannel(channel, bitmap);
                } else {
                    final ChannelViewHolder cvh = viewHolder;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = null;
                            do {
                                bitmap = cvh.avatarImage.getBitmap();
                                if (bitmap != null) {
                                    dialogsActivity.getParentActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cvh.image.setImageBitmap(cvh.avatarImage.getBitmap());
                                        }
                                    });

                                    Commands.updateChannel(channel, bitmap);
                                    break;

                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } while (bitmap == null);
                        }
                    });
                    t.start();
                }*/
            }

        }
        if (bitmap != null) {
//            Log.e("MY",channel.name+"Ready");
            viewHolder.image.setImageBitmap(bitmap);
        } else {
//            Log.e("MY",channel.name+"Default");
            viewHolder.image.setImageResource(R.drawable.default_channel_icon);
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/" + channel.name));
                getContext().startActivity(telegram);
            }
        });
        viewHolder.name.setText(channel.name);
        viewHolder.title.setText(channel.title);
        /*if(viewHolder.refresh != null){
            viewHolder.refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    channelsFragment.loadMore();
                }
            });
        }*/
        viewHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.reportLayout.getVisibility() == View.GONE) {
                    viewHolder.reportLayout.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.reportLayout.setVisibility(View.GONE);
                }
            }
        });


        viewHolder.txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.reportLayout.getVisibility() == View.VISIBLE) {
                    alertDialogReporter((int)channel.id,viewHolder.reportLayout);

                }
            }
        });

        viewHolder.reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.reportLayout.setVisibility(View.GONE);
            }
        });
        viewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelsFragment.setLoader(View.VISIBLE);
                Commands.join(channel, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        channelsFragment.setLoader(View.GONE);
                        if (!error) {
                            remove(channel);
                            notifyDataSetChanged();
//                            Log.e("COUNT",getCount()+"");
                            if (getCount() == 0) {
                                channelsFragment.loadMore();
//                                Toast.makeText(getContext(),"فعلا کانالی برای نمایش وجود نداره لطفا دقایقی دیگر مراجعه کنید",Toast.LENGTH_LONG).show();
                            }
                            /*if(Defaults.getInstance().openOnJoin()){
                                dialogsActivity.showChannel(channel.id);
                            }*/
                        } else {
                            if (message == null || message.length() == 0) {

                                Toast.makeText(getContext(), "خطا در عضویت کانال", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
            }
        });
        FontManager.instance().setTypefaceImmediate(v);
        return v;

    }

    @Override
    public Channel getItem(int position) {
        return channels.get(position);
    }

    public class ChannelViewHolder {

        TextView name;
        ImageView image;
        TextView join;
        Button report;
        LinearLayout reportLayout;
        TextView txtReport;
        RelativeLayout backLayout;
        ImageButton imgMore;
        TextView title;
        ImageReceiver avatarImage;
        AvatarDrawable avatarDrawable;

    }

    private void alertDialogReporter(final int channel_id, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("گزارش کانال");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setHint("دلیل گزارش");
        input.setPadding(8,0,8,0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(20, 10, 20, 10);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);


        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                Commands.report( channel_id, m_Text, new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        Toast.makeText(getContext(), error ? "خطا در گزارش کانال" : "کانال گزارش شد", Toast.LENGTH_SHORT).show();
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
