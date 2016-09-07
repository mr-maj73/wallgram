package org.cafemember.messenger.mytg.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.ImageReceiver;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Channel;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.Dialog.AddRequstAdvertisingDialog;
import org.cafemember.messenger.mytg.Dialog.AdvertisingDialog;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.fragments.MyChannelFragment;
import org.cafemember.messenger.mytg.listeners.OnJoinSuccess;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.tgnet.TLRPC;
import org.cafemember.ui.Components.AvatarDrawable;
import org.cafemember.ui.DialogsActivity;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Masoud on 6/2/2016.
 */
public class MyChannelsAdapter extends ArrayAdapter {


    private final MyChannelFragment myChannelFragment;
    private final DialogsActivity dialogsActivity;
    private ArrayList<Channel> channels;
    private AlertDialog alertDialog;

    public MyChannelsAdapter(Context context, int resource, ArrayList<Channel> objects, MyChannelFragment myChannelFragment, DialogsActivity dialogsActivity) {
        super(context, resource, objects);
        channels = objects;
        this.myChannelFragment = myChannelFragment;
        this.dialogsActivity = dialogsActivity;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        final Channel channel = getItem(position);
        MyChannelViewHolder viewHolder;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.my_channel_item, parent, false);
            viewHolder = new MyChannelViewHolder();
            viewHolder.name = (TextView) v.findViewById(R.id.name);
            viewHolder.title = (TextView) v.findViewById(R.id.title);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            viewHolder.add = (Button) v.findViewById(R.id.reserve);
            viewHolder.selectMemebers = (Button) v.findViewById(R.id.selectMemebers);
            v.setTag(viewHolder);
        } else {
            viewHolder = (MyChannelViewHolder) v.getTag();
        }


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
//                Log.e("MY",channel.name+"Has Online");
                TLRPC.FileLocation photo = null;
                photo = channel.photo;
                viewHolder.avatarImage.setImage(photo, "50_50", viewHolder.avatarDrawable, null, false);
                bitmap = viewHolder.avatarImage.getBitmap();
                if (bitmap != null) {
                    Commands.updateChannel(channel, bitmap);

                } else {
                    final MyChannelViewHolder cvh = viewHolder;
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
                }
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
        viewHolder.selectMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myChannelFragment.setLoader(View.VISIBLE);
//                Commands.removeChannel(channel, new OnJoinSuccess() {
//                    @Override
//                    public void OnResponse(boolean ok) {
//                        myChannelFragment.setLoader(View.GONE);
//                        if(ok){
//                            remove(channel);
//                            notifyDataSetChanged();
//                        }
//                    }
//                });
                Log.d("MyChannel", "OnClick Triggerd");

//                Log.d("COMMAND","AddChannel Triggerd");
                final int channelId = (int) channel.id;
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext());

                builder.setTitle(LocaleController.getString("MemberBegirTitle", R.string.MemberBegirTitle));

                            /*builder.setItems(Defaults.MEMBERS_COUNT , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Commands.addChannel(chat,Integer.parseInt(Defaults.MEMBERS_COUNT[which]));
                                }
                            });*/
                ReserveAdapter reserveAdapter = new ReserveAdapter(getContext(), R.layout.adapter_buy_coin, channel);
                reserveAdapter.setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("COMMAND","OnClick Triggerd 2");
                        final int count = Integer.parseInt(Defaults.MEMBERS_COUNT[which]);
                        Bitmap b = null;
                        b = channel.getBitMap();
                        myChannelFragment.setLoader(View.VISIBLE);
                        Commands.addChannel(channel, count, b, dialogsActivity, myChannelFragment);
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                builder.setAdapter(reserveAdapter, null);
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                builder.setCancelable(true);
                alertDialog = builder.create();
                dialogsActivity.showDialog(alertDialog);


            }
        });
        final MyChannelViewHolder holder = viewHolder;
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogAddMember(30);
            }
        });
        FontManager.instance().setTypefaceImmediate(v);
        return v;

    }

    @Override
    public Channel getItem(int position) {
        return channels.get(position);
    }

    public class MyChannelViewHolder {

        TextView name;
        ImageView image;
        Button add;
        Button selectMemebers;
        TextView title;
        ImageReceiver avatarImage;
        AvatarDrawable avatarDrawable;

    }

    public void showDialogAddMember(long id) {
        DialogFragment newFragment = AddRequstAdvertisingDialog.newInstance(id);
        newFragment.show(myChannelFragment.getFragmentManager(), "dialog");

    }
}
