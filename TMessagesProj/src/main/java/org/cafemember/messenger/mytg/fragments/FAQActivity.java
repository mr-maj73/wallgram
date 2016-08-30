package org.cafemember.messenger.mytg.fragments;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.FAQAdapter;
import org.cafemember.messenger.mytg.adapter.HistoryAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.ui.ActionBar.ActionBar;
import org.cafemember.ui.ActionBar.ActionBarMenu;
import org.cafemember.ui.ActionBar.BaseFragment;
import org.cafemember.ui.Components.LayoutHelper;

import java.util.ArrayList;

/**
 * Created by Masoud on 7/19/2016.
 */
public class FAQActivity extends BaseFragment {
//    private Context context;

    /*private EditText firstNameField;
    private EditText lastNameField;
    private View headerLabelView;
    private View doneButton;*/

    private final static int done_button = 1;
    private EditText firstNameField;

    @Override
    public View createView(final Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("MenuFAQ", R.string.MenuFAQ));
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
                {"2-اگر اعضایی که به کانالم اضافه میشوند بعد از گرفتن سکه کانالم را ترک کنند تکلیف چیست؟",
                "در صورتی که در ده روز آینده کسی کانال را ترک کند تعداد 2 سکه از حسابشون کسر میشه و 1 سکه به حساب شما افزوده میشه"},
                {"3-چرا سکه های من منفی شده اند؟",
                "در صورتی که قبل از ده روز کانالی که عضو شدین ترک کنید به ازای هر کانال 2 سکه از شما کسر میشود"},
                {"4-ممبرهایی که این برنامه اضافه میکند واقعی هستند؟ ",
                "تمامی کاربرانی که عضو کانال شما میشوند کاربران واقعی همین برنامه هستند."},
                {"5-چطور میتوانم اعضا را از ترک کانال منع کنم؟",
                "برای حفظ کاربر میتوانید مطالب جذاب در کانال خود قرار دهید"},
                {"6-چطوری میتوانم کانالم را در لیست درخواست ممبر قرار دهم؟",
                "وارد بخش سفارش عضو شوید و پس از ثبت کانال خود میتوانید برای کانالتان ممبر سفارش دهید.اگر سکه کافی نداشته باشید میتوانید با رفتن به بخش خرید سکه اقدام به خرید سکه کنید یا با عضو شدن در کانال های دیگران سکه رایگان بدست اورید."},
                {"7-چرا هنگامی که در روز عضو کانال های زیادی میشوم برنامه پیغام میدهد و اجازه ی عضو شدن داده نمیشود؟",
                "اگر بیش از 50 کانال در در یک ساعت عضو شید تلگرام حداقل برای ده ساعت اکانت شما را محدود میکند بنابراین باید حداقل  ده ساعت تا حداکثر 1 روز صبر کنید تا محدودیت تان برطرف شود."},
                {"8-در صورت داشتن سوال یا وجود مشکل چگونه با شما ارتباط برقرار کنم؟",
                "میتوانید از بخش پشتیبانی در نرم افزار به id پشتیبانی پیام دهید و در کمتر از 24 ساعت جواب خود را دریافت کنید."},
                {"9-اگر سکه خریداری کنم ولی به حسابم اضافه نشود چکار کنم؟",
                        "در صورتی که خرید کرده اید اما سکه ای دریافت نکردید به منوی کشویی برنامه رفته و بر روی پشتیبانی کلیک کنید تا به مدیریت پشتیبانی برنامه در تلگرام وصل شوید و مقدار خرید و توکن خرید(که توسط کافه بازار به ایمیل شما ارسال شده است) خود را برای ما ارسال کنید در صورت صحت خرید در کمتر از 24 ساعت سکه های شما اضافه خواهد شد."},

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
                        listView.setAdapter(new FAQAdapter(context,R.layout.adapter_history,faq));
                    loader.setVisibility(View.GONE);



        FontManager.instance().setTypefaceImmediate(fragmentView);

        return fragmentView;
    }



}
