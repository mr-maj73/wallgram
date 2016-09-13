package org.cafemember.messenger.mytg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import org.cafemember.messenger.mytg.adapter.ReserveAdapter;
import org.cafemember.messenger.mytg.fragments.MyChannelFragment;
import org.cafemember.messenger.mytg.util.FileConvert;
import org.cafemember.ui.DialogsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.ApplicationLoader;
import org.cafemember.messenger.FileLog;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.MessageObject;
import org.cafemember.messenger.MessagesController;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.listeners.OnChannelReady;
import org.cafemember.messenger.mytg.listeners.OnCoinsReady;
import org.cafemember.messenger.mytg.listeners.OnJoinSuccess;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.util.API;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.tgnet.ConnectionsManager;
import org.cafemember.tgnet.RequestDelegate;
import org.cafemember.tgnet.TLObject;
import org.cafemember.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Masoud on 6/1/2016.
 */
public class Commands {

    private static Context context = ApplicationLoader.applicationContext;
    private static int lastMessage = 0;
    private static AlertDialog visibleDialog;

    public static JSONArray JoinCoins;
    public static JSONArray ViewCoins;
    private static JSONObject Cats;
    private static int x = 0;

    public static void view(final int id) {

        if (lastMessage == id) {
            return;
        }
        lastMessage = id;
//        Toast.makeText(ApplicationLoader.applicationContext, id+" Marked.",Toast.LENGTH_SHORT).show();
        API.getInstance().run(String.format(Locale.ENGLISH, "/posts/view/%d", id), new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
                    loadCoins(data);
                } else {
//                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void join(final Channel channel, final OnJoinSuccess joinSuccess) {
        join(channel, new OnResponseReadyListener() {

            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                joinSuccess.OnResponse(!error);
            }
        });

    }

    public static void join(final Channel channel, final OnResponseReadyListener joinSuccess) {
        join(channel, joinSuccess, true);
    }

    public static void join(final Channel channel, final OnResponseReadyListener joinSuccess, final boolean joinServer) {
        if (channel.inputChannel == null) {
            Defaults.getInstance().loadChannel(channel, new OnChannelReady() {
                @Override
                public void onReady(final Channel channel, boolean isOK) {
                    if (isOK) {
                        TLRPC.TL_channels_joinChannel req = new TLRPC.TL_channels_joinChannel();
                        req.channel = channel.inputChannel;
                        ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
                            @Override
                            public void run(final TLObject response, final TLRPC.TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (error == null) {
                                            if (joinServer) {
                                                joinChannel(channel, joinSuccess);
                                            } else {
                                                joinSuccess.OnResponseReady(false, null, "عضویت مجدد انجام شد");
                                            }
                                        } else {
                                            joinSuccess.OnResponseReady(true, null, "خطا در عضویت کانال");
                                        }
                                    }
                                });
                            }
                        }, ConnectionsManager.RequestFlagFailOnServerErrors);
                    } else {
                        joinSuccess.OnResponseReady(true, null, "خطا در یافتن کانال");
                    }
                }
            });
        } else {
            TLRPC.TL_channels_joinChannel req = new TLRPC.TL_channels_joinChannel();
            req.channel = channel.inputChannel;
            ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
                @Override
                public void run(final TLObject response, final TLRPC.TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (error == null) {

                                if (joinServer) {
                                    joinChannel(channel, joinSuccess);
                                } else {
                                    joinSuccess.OnResponseReady(false, null, "عضویت مجدد انجام شد");
                                }
                            } else {
                                joinSuccess.OnResponseReady(true, null, "خطا در عضویت کانال");
                            }
                        }
                    });
                }
            }, ConnectionsManager.RequestFlagFailOnServerErrors);
        }


    }

    private static void joinChannel(final Channel channel, final OnResponseReadyListener joinSuccess) {
        TLRPC.FileLocation loc = channel.photo;
        String url;
        url = String.format(Locale.ENGLISH, "/channels/join/%d", channel.id);


        API.getInstance().post(url, "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
                    loadCoins(data);
                    joinSuccess.OnResponseReady(error, data, message);
                } else {
                    joinSuccess.OnResponseReady(error, data, message);
                }
            }
        });
    }

    public static void left() {

    }

    public static void report(int id, String reason, OnResponseReadyListener onResponseReadyListener) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/report/%d", id), "{\"reason\":\"" + reason + "\"}", onResponseReadyListener);
    }

    public static void coinsPrice(final OnResponseReadyListener onResponseReadyListener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/coin/getCoinsPrice"), onResponseReadyListener);
    }

    public static void defaultCoins(final OnJoinSuccess onJoinSuccess) {
        if (JoinCoins == null || ViewCoins == null) {
            API.getInstance().run(String.format(Locale.ENGLISH, "/coin/getDefaultCoins"), new OnResponseReadyListener() {
                @Override
                public void OnResponseReady(boolean error, JSONObject data, String message) {
                    if (!error) {
                        try {
                            data = data.getJSONObject("data");
                            ViewCoins = data.getJSONArray("viewCoins");
                            JoinCoins = data.getJSONArray("joinCoins");
                            onJoinSuccess.OnResponse(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        onJoinSuccess.OnResponse(false);
                    }
                }
            });
        } else {
            onJoinSuccess.OnResponse(true);
        }
    }

    public static void buy(String id) {
        Intent i = new Intent(context, PayActivityNivad.class);
//        Intent i = new Intent(context,PaymentActivity.class);
        i.putExtra("sku", id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void checkBoughtItem(String id, final OnJoinSuccess success) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/coin/buyCoin/%s", id), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponse(true);
                } else {
                    success.OnResponse(false);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void transfare(String user, int amount, int type, final OnResponseReadyListener onResponseReadyListener) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/coin/transfare/%d/%s/%d", type, user, amount), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
                    loadCoins(data);

                }
                onResponseReadyListener.OnResponseReady(error, data, message);
            }
        });

    }

    public static void login(String phone, final OnResponseReadyListener onResponseReadyListener) {
        if (phone != null) {

            if (phone.length() >= 10) {
                phone = "98" + phone.substring(phone.length() - 10);
            }
        }
        API.getInstance().post(String.format(Locale.ENGLISH, "/user/login/%s", phone), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
                    try {
                        loadCoins(data);
                        data = data.getJSONObject("data");
                        String token = data.getString("token");

                        if (data.has("channel_name")) {
                            String channelName = data.getString("channel_name");
                            Defaults.getInstance().setMyChannelName(channelName);
                        }
                        if (data.has("support")) {
                            String channelName = data.getString("support");
                            Defaults.getInstance().setSupport(channelName);
                        }
                        if (data.has("help_channel_id")) {
                            int helpcChannelId = data.getInt("help_channel_id");
                            Defaults.getInstance().setHelpChannelId(helpcChannelId);
                        }
                        if (data.has("channel_id")) {
                            int channelId = data.getInt("channel_id");
                            Defaults.getInstance().setMyChannelId(channelId);
                        }
                        Defaults.getInstance().setMyToken(token);
                        if (data.has("first")) {
                            Log.e("log", "Has First");
                            if (data.getBoolean("first")) {
                                Log.e("log", "Yess");
                                AlertDialog.Builder builder = null;
                                builder = new AlertDialog.Builder(context);
                                builder.setTitle("هدیه ثبت نام");
                                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("giftText2", R.string.giftText2)));

                                builder.setPositiveButton("تایید", null);
                                showAlertDialog(builder.create());
                            }

                        }
                        final Defaults def = Defaults.getInstance();
                        /*if(!def.isChannelSet()) {
                            def.loadMyChannel(new OnJoinSuccess() {
                                @Override
                                public void OnResponse(boolean ok) {
                                    if(ok){
                                        def.setChannelSet(true);
                                    }
                                    else {
                                        AlertDialog.Builder builder = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                                            builder = new AlertDialog.Builder(context, R.style.MyDialog);
                                        }
                                        else {
                                            builder = new AlertDialog.Builder(context);
                                        }
                                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                        builder.setMessage(LocaleController.getString("MyChannelError", R.string.MyChannelError)+"  \n@"+def.getMyChannelName());
                                        builder.setNegativeButton(LocaleController.getString("MyCancel", R.string.MyCancel), null);
                                        showAlertDialog(builder.create());
                                    }

                                }
                            });
                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (onResponseReadyListener != null) {
                    onResponseReadyListener.OnResponseReady(error, data, message);
                }
            }
        });
    }

    public static void ref(String phone, final OnResponseReadyListener onResponseReadyListener) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/user/ref/%s", phone), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {
//                    Toast.makeText(context, "معرف با موفقیت ثبت شد",Toast.LENGTH_LONG).show();
                    onResponseReadyListener.OnResponseReady(error, data, "معرف با موفقیت ثبت شد");
                } else {
                    onResponseReadyListener.OnResponseReady(error, data, message);
//                    Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static ConcurrentHashMap<Long, TLRPC.Dialog> dialogs_dict = new ConcurrentHashMap<>(100, 1.0f, 2);
    public static ArrayList<TLRPC.Dialog> dialogs = new ArrayList<>();

    public static void checkChannelsTrigger(final ArrayList<TLRPC.Dialog> dsf, final DialogsActivity dialogsActivity) {
        Log.e("CHK", "Checking Channels...");
        checkChannels(dsf, dialogsActivity);
        dialogs_dict.clear();
    }

    public static void checkChannels(final ArrayList<TLRPC.Dialog> dsf, final DialogsActivity dialogsActivity) {
//        Log.e("CHK","Start Checking Channels ...");


        TLRPC.TL_messages_getDialogs req = new TLRPC.TL_messages_getDialogs();
        req.limit = 100;
        req.offset_date = (int) (System.currentTimeMillis() / 1000);
        boolean found = false;
        for (int a = dialogs.size() - 1; a >= 0; a--) {
//        for (int a = 0 ; a < dialogs.size(); a++) {
            TLRPC.Dialog dialog = dialogs.get(a);
            int lower_id = (int) dialog.id;
            int high_id = (int) (dialog.id >> 32);
            if (lower_id != 0 && high_id != 1 && dialog.top_message > 0) {
                MessageObject message = MessagesController.getInstance().dialogMessage.get(dialog.id);
                req.offset_date = dialog.last_message_date;
                break;
               /* if (message != null && message.getId() > 0) {
                    req.offset_date = Math.max(dialog.last_message_date_i, message.messageOwner.date);
//                    req.offset_id = message.messageOwner.id;
                    int id;
                    if (message.messageOwner.to_id.channel_id != 0) {
                        id = -message.messageOwner.to_id.channel_id;
                    } else if (message.messageOwner.to_id.chat_id != 0) {
                        id = -message.messageOwner.to_id.chat_id;
                    } else {
                        id = message.messageOwner.to_id.user_id;
                    }
//                    req.offset_peer = MessagesController.getInstance().getInputPeer(id);
                    found = true;
                    break;
                }*/
            }
        }
        req.offset_peer = new TLRPC.TL_inputPeerEmpty();
        if (!found) {
        }
        ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
            @Override
            public void run(TLObject response, TLRPC.TL_error error) {
                if (error == null) {
                    final TLRPC.messages_Dialogs dialogsRes = (TLRPC.messages_Dialogs) response;
                    final HashMap<Integer, TLRPC.Chat> chatsDict = new HashMap<>();
                    final HashMap<Long, TLRPC.Dialog> new_dialogs_dict = new HashMap<>();
                    final HashMap<Long, MessageObject> new_dialogMessage = new HashMap<>();
                    final HashMap<Integer, Integer> notImportantDates = new HashMap<>();
                    final HashMap<Integer, TLRPC.User> usersDict = new HashMap<>();

                    for (int a = 0; a < dialogsRes.users.size(); a++) {
                        TLRPC.User u = dialogsRes.users.get(a);
                        usersDict.put(u.id, u);
                    }
                    for (int a = 0; a < dialogsRes.chats.size(); a++) {
                        TLRPC.Chat c = dialogsRes.chats.get(a);
                        chatsDict.put(c.id, c);
                    }
                    for (int a = 0; a < dialogsRes.messages.size(); a++) {
                        TLRPC.Message message = dialogsRes.messages.get(a);
                        if (message.to_id.channel_id != 0) {
                            if (!MessageObject.isImportant(message)) {
                                notImportantDates.put(-message.to_id.channel_id, message.date);
                            }
                            TLRPC.Chat chat = chatsDict.get(message.to_id.channel_id);
                            if (chat != null && chat.left/* && !chat.megagroup*/) {
                                continue;
                            }
                            if (chat != null && chat.megagroup) {
                                message.flags |= TLRPC.MESSAGE_FLAG_MEGAGROUP;
                            }
                        } else if (message.to_id.chat_id != 0) {
                            TLRPC.Chat chat = chatsDict.get(message.to_id.chat_id);
                            if (chat != null && chat.migrated_to != null) {
                                continue;
                            }
                        }
                        if (message.post && !message.out) {
                            message.media_unread = true;
                        }
                        MessageObject messageObject = new MessageObject(message, usersDict, chatsDict, false);
                        MessageObject currentMessage = new_dialogMessage.get(messageObject.getDialogId());
                        if (currentMessage == null || messageObject.isMegagroup() || messageObject.isImportant()) {
                            new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                        }
                    }
                    for (int a = 0; a < dialogsRes.dialogs.size(); a++) {
                        TLRPC.Dialog d = dialogsRes.dialogs.get(a);
                        if (d.id == 0 && d.peer != null) {
                            if (d.peer.user_id != 0) {
                                d.id = d.peer.user_id;
                            } else if (d.peer.chat_id != 0) {
                                d.id = -d.peer.chat_id;
                            } else if (d.peer.channel_id != 0) {
                                d.id = -d.peer.channel_id;
                            }
                        }
                        if (d.id == 0) {
                            continue;
                        }
                        if (d.last_message_date == 0) {
                            MessageObject mess = new_dialogMessage.get(d.id);
                            if (mess != null) {
                                d.last_message_date = mess.messageOwner.date;
                            }
                        }
                        if (d.last_message_date_i == 0 && d.top_not_important_message != 0) {
                            Integer date = notImportantDates.get((int) d.id);
                            if (date != null) {
                                d.last_message_date_i = date;
                            }
                        }
                        if (d instanceof TLRPC.TL_dialogChannel) {
                            TLRPC.Chat chat = chatsDict.get(-(int) d.id);
                            if (chat != null && chat.megagroup) {
                                d.top_message = Math.max(d.top_message, d.top_not_important_message);
                                d.unread_count = Math.max(d.unread_count, d.unread_not_important_count);
                            }
                            if (chat != null && chat.left/* && !chat.megagroup*/) {
                                continue;
                            }
                        } else if ((int) d.id < 0) {
                            TLRPC.Chat chat = chatsDict.get(-(int) d.id);
                            if (chat != null && chat.migrated_to != null) {
                                continue;
                            }
                        }
                        new_dialogs_dict.put(d.id, d);
                    }
                    boolean added = false;
                    for (HashMap.Entry<Long, TLRPC.Dialog> pair : new_dialogs_dict.entrySet()) {
                        Long key = pair.getKey();
                        TLRPC.Dialog value = pair.getValue();
                        TLRPC.Dialog currentDialog = dialogs_dict.get(key);
                        if (currentDialog == null) {
                            added = true;
                            dialogs_dict.put(key, value);
                        }
                    }
                    dialogs.clear();
                    dialogs.addAll(dialogs_dict.values());
                    Collections.sort(dialogs, new Comparator<TLRPC.Dialog>() {
                        @Override
                        public int compare(TLRPC.Dialog tl_dialog, TLRPC.Dialog tl_dialog2) {
                            if (tl_dialog.last_message_date == tl_dialog2.last_message_date) {
                                return 0;
                            } else if (tl_dialog.last_message_date < tl_dialog2.last_message_date) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    if (added) {
                        Log.e("CHK", "Added: " + dialogs_dict.size());
                        checkChannels(null, dialogsActivity);
                        return;
                    }
                    Log.e("CHK", "Done With: " + dialogs.size());

                    if (dialogs != null && dialogs.size() > 0)
                        getJoinedChannels(new OnResponseReadyListener() {
                            @Override
                            public void OnResponseReady(boolean error, JSONObject data, String message) {
//                Log.e("CHK","Error: "+error);
                                if (!error) {
                                    JSONArray channelsId = null;
                                    try {
                                        channelsId = data.getJSONArray("data");
                                        int size = channelsId.length();
                                        HashMap<Long, Channel> lastChannels = new HashMap<Long, Channel>();
                                        for (int i = 0; i < size; i++) {
                                            JSONObject item = channelsId.getJSONObject(i);
                                            Channel currentChannel = new Channel(item.getString("name"), item.getInt("tg_id"));
                                            lastChannels.put(currentChannel.id, currentChannel);
                                        }
                                        for (TLRPC.Dialog dialog : dialogs) {
                                            if (dialog instanceof TLRPC.TL_dialogChannel) {
                                                long id = -dialog.id;
                                                lastChannels.remove(id);
                                            }
                                        }
                                        if (lastChannels.size() > 0) {
//                        if(true) {
//                            Log.e("CHK","Create");
                                            String[] items = new String[lastChannels.size()];
                                            final ArrayList<Channel> ids = new ArrayList<>();
                                            int i = 0;
                                            for (Channel channel : lastChannels.values()) {
                                                items[i] = channel.name;
                                                ids.add(channel);
                                                i++;
                                            }
                                            final boolean[] checkedItems = new boolean[items.length];
                                            AlertDialog.Builder builder = null;
                                            builder = new AlertDialog.Builder(dialogsActivity.getParentActivity());
//                            }
                                            int coin = items.length * 2;
                                            builder.setTitle("شما کانال های زیر را ترک کردید و " + coin + " سکه از دست دادید");

                                            builder.setItems(items, null);
                                            builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    /*for (int j = 0 ; j < checkedItems.length ; j++){
                                                        checkedItems[j]=false;
                                                    }
                                    reJoin(ids, checkedItems, new OnJoinSuccess() {
                                        @Override
                                        public void OnResponse(boolean ok) {

                                                        }
                                                    });*/
                                                    if (visibleDialog != null) {
                                                        visibleDialog.dismiss();
                                                        visibleDialog = null;
                                                    }
                                                }
                                            });
                                            builder.setCancelable(true);
                                            final AlertDialog al = builder.create();
                                            for (int j = 0; j < checkedItems.length; j++) {
                                                checkedItems[j] = false;
                                            }
                                            reJoin(ids, checkedItems, new OnJoinSuccess() {
                                                @Override
                                                public void OnResponse(boolean ok) {
                                                    if (ok) {
                                                        dialogsActivity.showDialog(al);
                                                    }
                                                }
                                            });

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                }
            }
        });

    }

    public static void reJoin(ArrayList<Channel> ids, boolean[] checkedItems, final OnJoinSuccess onJoinSuccess) {
        final JSONArray channelsToLeft = new JSONArray();
        ArrayList<Channel> channelsToReJoin = new ArrayList<>();
        int size = ids.size();
        int reJoinSize = 0;
        for (int i = 0; i < size; i++) {
            /*if(checkedItems[i]){
                channelsToReJoin.add(ids.get(i));
                reJoinSize++;
            }
            else {
*/
            channelsToLeft.put(ids.get(i).id);
//            }
        }/*
            final int joins = reJoinSize;
            for(final Channel ch: channelsToReJoin){
                            join(ch, new OnResponseReadyListener() {
                                int joinCompletes = 0;
                                int steps = 0;
                                @Override
                                public void OnResponseReady(boolean error, JSONObject data, String message) {
                                    steps ++;
                                    if(!error){
                                        joinCompletes++;

                                    }
                                    if (steps == joins) {

                                        if(joinCompletes == joins) {
                                            try {
//                                                Log.e("LEFT",channelsToLeft.toString(2));
                                                API.getInstance().post("/channels/leftAll", channelsToLeft.toString(2), new OnResponseReadyListener() {
                                                    @Override
                                                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                                                        if (!error) {
                                                            loadCoins(data);
                                                        }
                                                        onJoinSuccess.OnResponse(!error);
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            onJoinSuccess.OnResponse(false);
                                        }

                                    }
                                }
                            },false);

                        }*/

        if (reJoinSize == 0) {
            try {
//                Log.e("LEFT",channelsToLeft.toString(2));
                API.getInstance().post("/channels/leftAll", channelsToLeft.toString(2), new OnResponseReadyListener() {
                    @Override
                    public void OnResponseReady(boolean error, JSONObject data, String message) {
                        if (!error) {
                            loadCoins(data);
                        }
                        onJoinSuccess.OnResponse(!error);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static void addChannel(final TLRPC.Chat channel, int count) {
        int channelId = channel.id;
        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/add/%d/%s/%d", channelId, channel.username, count), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, channel.username + " اضافه شد", Toast.LENGTH_LONG).show();
                    loadCoins(data);

                }
            }
        });


    }

    public static void addMyChannel(final Channel channel, final OnResponseReadyListener onJoinSuccess) {
        int channelId = (int) channel.inputChannel.channel_id;

        String body;
        body = "{\"title\":\"" + (channel.title == null ? "" : channel.title) + "\"}";
        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/addMy/%d/%s", channelId, channel.name), body, onJoinSuccess);


    }


    public static void updateChannel(final Channel channel, final Bitmap bitmap) {
        int channelId = (int) channel.id;

        String body;
        body = "{\"byteString\":\"" + FileConvert.getStringFromBitmap(bitmap) + "\", \"title\":\"" + (channel.title == null ? "" : channel.title) + "\"}";

        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/update/%s", channel.name), body, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
//                Log.e("UPDT",channel.name+": "+!error);
            }
        });


    }

    public static void addChannel(final Channel channel, int count, final Bitmap bitmap, final DialogsActivity dialogsActivity, final MyChannelFragment myChannelFragment) {
        int channelId = (int) channel.id;
        TLRPC.FileLocation loc = channel.photo;

        String body = "";
        if (channel.hasPhoto) {
            body = "";
        } else {
            body = "{\"byteString\":\"" + FileConvert.getStringFromBitmap(bitmap) + "\"}";
        }
        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/add/%d/%s/%d", channelId, channel.name, count), body, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, channel.title + " اضافه شد", Toast.LENGTH_LONG).show();
                    loadCoins(data);
                    /*AlertDialog.Builder builder ;
                    builder = new AlertDialog.Builder(dialogsActivity.getParentActivity());

                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));

                    builder.setMessage(LocaleController.getString("MemberBegirAlert", R.string.MemberBegirAlert));
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.setCancelable(true);
                    dialogsActivity.showDialog(builder.create());*/

                }
                myChannelFragment.setLoader(View.GONE);
            }
        });


    }

    public static void addChannel(final Channel channel, final Bitmap bitmap) {
//        Log.d("COMMAND","AddChannel Triggerd");
        final int channelId = (int) channel.id;


        AlertDialog.Builder builder;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            builder = new AlertDialog.Builder(context);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, R.style.MyDialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(LocaleController.getString("MemberBegirTitle", R.string.MemberBegirTitle));

                            /*builder.setItems(Defaults.MEMBERS_COUNT , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Commands.addChannel(chat,Integer.parseInt(Defaults.MEMBERS_COUNT[which]));
                                }
                            });*/
        ReserveAdapter reserveAdapter = new ReserveAdapter(context, R.layout.adapter_buy_coin, channel);
        reserveAdapter.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d("COMMAND","OnClick Triggerd 2");
                final int count = Integer.parseInt(Defaults.MEMBERS_COUNT[which]);
                Commands.addChannel(channel, count, bitmap, null, null);
                if (visibleDialog != null) {
                    visibleDialog.dismiss();
                    visibleDialog = null;
                }
            }
        });
        builder.setAdapter(reserveAdapter, null);
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setCancelable(true);
        showAlertDialog(builder.create());


    }

    public static void removeChannel(final Channel channel, final OnJoinSuccess onJoinSuccess) {
        int channelId = (int) channel.id;
        API.getInstance().post(String.format(Locale.ENGLISH, "/channels/remove/%d", channelId), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                onJoinSuccess.OnResponse(!error);
                if (error) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, channel.title + " حذف شد", Toast.LENGTH_LONG).show();
                    loadCoins(data);

                }
            }
        });


    }


    public static void addPost(MessageObject message, int count) {

        int id = message.getId();
        int channel_id = message.messageOwner.to_id.channel_id;
        TLRPC.Chat chat = MessagesController.getInstance().getChat(channel_id);
//        long access_hash = chat.access_hash;
        CharSequence text = message.caption != null ? message.caption : (message.messageText != null ? message.messageText : "None");
        String data = "{\"text\":\"" + text + "\"}";
        final String channel_name = chat.username;

        API.getInstance().post(String.format(Locale.ENGLISH, "/posts/add/%s/%d/%d", channel_name, id, count), data, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "پست شما از " + channel_name + " اضافه شد", Toast.LENGTH_LONG).show();
                    loadCoins(data);
                }
            }
        });


    }

    private static void loadCoins(JSONObject data) {
        boolean isView = false;
        String key = "joinCoins";
        try {
            data = data.getJSONObject("data");
            if (data.has("viewCoins")) {
                key = "viewCoins";
                isView = true;
            } else if (data.has("joinCoins")) {
                key = "joinCoins";
                isView = false;
            } else {
                return;
            }
            int coins = data.getInt(key);
            if (isView) {
                ApplicationLoader.setViewCoins(coins);
            } else {
                ApplicationLoader.setJoinCoins(coins);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void loadCoins(final OnCoinsReady onCoinsReady) {
//        if( ApplicationLoader.joinCoins == 0){
        ApplicationLoader.setJoinCoins(Defaults.getInstance().getMyCoin(), true);
        API.getInstance().run(String.format(Locale.ENGLISH, "/coin"), new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (error) {
                    onCoinsReady.onCoins(0, 0);
                } else {
                    try {
                        data = data.getJSONObject("data");
                        int viewCoins = data.getInt("viewCoins");
                        int joinCoins = data.getInt("joinCoins");
                        Defaults.getInstance().setMyCoin(joinCoins);

//                            ApplicationLoader.setViewCoins(viewCoins);
//                            ApplicationLoader.setJoinCoins(joinCoins);
                        onCoinsReady.onCoins(viewCoins, joinCoins);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        /*}
        else {
            ApplicationLoader.setJoinCoins(Defaults.getInstance().getMyCoin(),true);
            onCoinsReady.onCoins(ApplicationLoader.viewCoins,ApplicationLoader.joinCoins);
        }*/


    }

    public static void getNewChannels(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/channels"), listener);
    }

    public static void getMyChannels(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/channels/getMy"), listener);
    }

    public static void getJoinedChannels(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/channels/self"), listener);
    }

    public static void getHistory(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/user/history"), listener);
    }

    public static Dialog showAlertDialog(AlertDialog dialog) {
//        Log.d("COMMAND","Show Alert");
        try {
            if (visibleDialog != null) {
                visibleDialog.dismiss();
                visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        try {
            visibleDialog = dialog;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                visibleDialog.getListView().setDivider(context.getDrawable(R.drawable.transparent));
            } else {
                visibleDialog.getListView().setDivider(context.getResources().getDrawable(R.drawable.transparent));
            }
            visibleDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            visibleDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            visibleDialog.setCanceledOnTouchOutside(false);
            visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    visibleDialog = null;
                }
            });
            FontManager.instance().setTypefaceImmediate(visibleDialog.getCurrentFocus());
            visibleDialog.show();
            return visibleDialog;
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static void getWallHistory(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/wall/history"), listener);
    }

    public static void mySuggests(OnResponseReadyListener listener) {
        API.getInstance().run(String.format(Locale.ENGLISH, "/wall/mySuggests"), listener);
    }

    public static void getWall(int id, OnResponseReadyListener listener) {
        String cat = id == 0 ? "":"/"+id;
        API.getInstance().run(String.format(Locale.ENGLISH, "/wall/get"+cat), listener);
    }

    public static void categories(final OnResponseReadyListener listener) {
        if (Cats == null ) {
            API.getInstance().run(String.format(Locale.ENGLISH, "/wall/cats"), new OnResponseReadyListener() {
                @Override
                public void OnResponseReady(boolean error, JSONObject data, String message) {
                    if (!error) {
                            Cats = data;
                            listener.OnResponseReady(false,Cats,"");
                    } else {
                        listener.OnResponseReady(true,data,message);
                    }
                }
            });
        } else {
            listener.OnResponseReady(false,Cats,"");
        }
    }


    public static void acceptAd(int id, final OnResponseReadyListener success) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/wall/accept/%d", id), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponseReady(false,null,message);
                } else {
                    success.OnResponseReady(true,null,message);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void reportAd(int id, final OnResponseReadyListener success) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/wall/report/%d", id), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponseReady(false,null,message);
                } else {
                    success.OnResponseReady(true,null,message);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void suggest(int wallId,int price,String rule, final OnResponseReadyListener success) {
        String body = String.format(Locale.ENGLISH, "{\"wall_id\":%d, \"price\":%d, \"rule\":\"%s\"}",wallId, price, rule);
        API.getInstance().post(String.format(Locale.ENGLISH, "/wall/suggest"), body, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponseReady(false,null,message);
                } else {
                    success.OnResponseReady(true,null,message);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void addChannel(int channelId,int categoryId,int price,String admin_link,String description, final OnResponseReadyListener success) {
        String body = String.format(Locale.ENGLISH, "{\"channel_id\":%d,\"cat_id\":%d, \"price\":%d,\"admin_link\":\"%s\", \"desc\":\"%s\"}",channelId,categoryId, price, admin_link,description);
        API.getInstance().post(String.format(Locale.ENGLISH, "/wall/addChannel"), body, new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponseReady(false,null,message);
                } else {
                    success.OnResponseReady(true,null,message);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static void rejectAd(int id, final OnResponseReadyListener success) {
        API.getInstance().post(String.format(Locale.ENGLISH, "/wall/reject/%d", id), "", new OnResponseReadyListener() {
            @Override
            public void OnResponseReady(boolean error, JSONObject data, String message) {
                if (!error) {

                    loadCoins(data);
                    success.OnResponseReady(false,null,message);
                } else {
                    success.OnResponseReady(true,null,message);
                    Toast.makeText(ApplicationLoader.applicationContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
