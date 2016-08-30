/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.cafemember.messenger.mytg.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.cafemember.messenger.FileLog;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.FAQAdapter;
import org.cafemember.messenger.mytg.adapter.HelpAdapter;
import org.cafemember.ui.ActionBar.ActionBar;
import org.cafemember.ui.ActionBar.ActionBarMenu;
import org.cafemember.ui.ActionBar.BaseFragment;
import org.cafemember.ui.Components.LayoutHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HelpActivity extends BaseFragment {

    /*private View doneButton;
    private TextView checkTextView;
    private Context context;
    final String Share_Text = "دریافت سکه رایگان !!!\n" +
            "این برنامه  را به دوستانتان معرفی کنید و سکه رایگان دریافت کنید!\n" +
            "هر نفر که عضو برنامه شود و شما را به عنوان  معرف اعلام کند ، به شما 1000 سکه ویو  رایگان تعلق میگیرد.\n" +
            "\n" +
            " شما میتوانید با زدن دکمه  زیر  ، دعوت نامه خود را برای دوستانتان در تمام  برنامه های اجتماعی ( تلگرام ، اینستاگرام ، sms و... ) ارسال کنید . در این دعوت نامه شناسه شما به عنوام معرف قرار دارد .";

    private final static int done_button = 1;

    @Override
    public View createView(Context context) {
        this.context = context;
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("MenuHelp", R.string.MenuHelp));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();




        fragmentView = new ScrollView(context);
        LinearLayout layout = new LinearLayout(context);
        ((LinearLayout) layout).setOrientation(LinearLayout.VERTICAL);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        TextView helpTextView = new TextView(context);
        helpTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        helpTextView.setTextColor(0xff212121);
        helpTextView.setGravity(Gravity.RIGHT);
//        helpTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ShareText", R.string.ShareText)));
        helpTextView.setText(LocaleController.getString("MyHelpText", R.string.MyHelpText));
//        helpTextView.setText(Share_Text);
        layout.addView(helpTextView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 24, 10, 24, 0));
        ((ScrollView) fragmentView).addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0,0,0,0));


        FontManager.instance().setTypefaceImmediate(fragmentView);

        return fragmentView;
    }*/


    @Override
    public View createView(final Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("MenuHelp", R.string.MenuHelp));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
//        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        /*TLRPC.User user = MessagesController.getInstance().getUser(UserConfig.getClientUserId());
        if (user == null) {
            user = UserConfig.getCurrentUser();
        }*/

        final ProgressBar loader = new ProgressBar(context);
        final ListView listView = new ListView(context);

        FrameLayout farme = new FrameLayout(context);
        fragmentView = farme;
        fragmentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loader.setVisibility(View.VISIBLE);
        listView.setBackgroundResource(R.color.my_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setDivider(context.getDrawable(R.drawable.transparent));
        }
        else {
            listView.setDivider(context.getResources().getDrawable(R.drawable.transparent));
        }
        farme.addView(loader, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,36, 24, 24, 24, 0));
        farme.addView(listView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        /*fragmentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        ArrayList<JSONObject> faqlist = new ArrayList<>();
        String [][] faqArray = {
                {"1-در صورتی که وارد بخش سکه رایگان شدید و لیست کانال ها خالی بود به این دلیل است که شما موقتا تا 24 ساعت توسط تلگرام بلاک شده اید و دلیل آن کاملا نامعلوم است و جزع سیاست های تلگرام می باشد و هیچ ارتباطی با برنامه ندارد.",
                ""},
                {"2-در بخش خرید سکه بسته های اخر بیشترین تخفیف را خورده اند پیشنهاد ما به شما خرید بسته های اخر می باشد",
                ""},
                {"3-برای افزایش سکه میتوانید به بخش خرید سکه بروید.",
                ""},
                {"4-در قسمت سفارش عضو میتوانید کانال هایتان را ثبت کنید و برای هر کدام جداگانه ممبر سفارش دهید.",
                ""},
                {"5-در قسمت سکه رایگان میتوانید با عضو شدن در کانال ها سکه رایگان بدست اورید",
                ""}
        };
        for(String [] object: faqArray){
            JSONObject js = new JSONObject();
            try {
                js.put("q",object[0]);
                js.put("a",object[1]);
                faqlist.add(js);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final JSONArray faq = new JSONArray(faqlist);
        listView.setAdapter(new HelpAdapter(context,R.layout.adapter_history,faq));
        loader.setVisibility(View.GONE);



        FontManager.instance().setTypefaceImmediate(fragmentView);

        return fragmentView;
    }




}
