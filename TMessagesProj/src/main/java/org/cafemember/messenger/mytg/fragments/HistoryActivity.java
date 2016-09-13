package org.cafemember.messenger.mytg.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.cafemember.messenger.mytg.adapter.PageOrdersAdapter;
import org.cafemember.ui.LaunchActivity;
import org.json.JSONException;
import org.json.JSONObject;
import org.cafemember.messenger.AndroidUtilities;
import org.cafemember.messenger.ApplicationLoader;
import org.cafemember.messenger.LocaleController;
import org.cafemember.messenger.MessagesController;
import org.cafemember.messenger.NotificationCenter;
import org.cafemember.messenger.R;
import org.cafemember.messenger.UserConfig;
import org.cafemember.messenger.mytg.Commands;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.CoinsAdapter;
import org.cafemember.messenger.mytg.adapter.HistoryAdapter;
import org.cafemember.messenger.mytg.listeners.OnResponseReadyListener;
import org.cafemember.messenger.mytg.util.Defaults;
import org.cafemember.tgnet.ConnectionsManager;
import org.cafemember.tgnet.RequestDelegate;
import org.cafemember.tgnet.TLObject;
import org.cafemember.tgnet.TLRPC;
import org.cafemember.ui.ActionBar.ActionBar;
import org.cafemember.ui.ActionBar.ActionBarMenu;
import org.cafemember.ui.ActionBar.ActionBarMenuItem;
import org.cafemember.ui.ActionBar.BaseFragment;
import org.cafemember.ui.ChangeNameActivity;
import org.cafemember.ui.Components.AvatarDrawable;
import org.cafemember.ui.Components.LayoutHelper;

/**
 * Created by Masoud on 7/19/2016.
 */
public class HistoryActivity extends BaseFragment {
//    private Context context;

    /*private EditText firstNameField;
    private EditText lastNameField;
    private View headerLabelView;
    private View doneButton;*/

    private final static int done_button = 1;
    private EditText firstNameField;
    ViewPager viewPager;

    @Override
    public View createView(final Context context) {

        Log.i("mohammadddddddddd", "dddddddddddd");
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("HistoryTitle", R.string.HistoryTitle));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fragmentView = inflater.inflate(R.layout.history_fragment, null);
        TabLayout tabLayout = (TabLayout) fragmentView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabOrdersAdvertising), 0);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabOrdersMemberShip), 1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) fragmentView.findViewById(R.id.pager);
        Activity f = HistoryActivity.this.getParentActivity();
        if (f instanceof LaunchActivity) {
            FragmentManager manager = ((LaunchActivity) f).getSupportFragmentManager();
            final PageOrdersAdapter adapter = new PageOrdersAdapter(manager, tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


//        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        /*TLRPC.User user = MessagesController.getInstance().getUser(UserConfig.getClientUserId());
        if (user == null) {
            user = UserConfig.getCurrentUser();
        }*/
//
//        final ProgressBar loader = new ProgressBar(context);
//        final ListView listView = new ListView(context);
//
//        FrameLayout farme = new FrameLayout(context);
//        fragmentView = farme;
//        fragmentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        loader.setVisibility(View.VISIBLE);
//        listView.setBackgroundResource(R.color.my_background);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            listView.setDivider(context.getDrawable(R.drawable.transparent));
//        }
//        else {
//            listView.setDivider(context.getResources().getDrawable(R.drawable.transparent));
//        }
//        farme.addView(listView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
//        farme.addView(loader, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT,LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 24, 24, 24, 0));
//
//
//        /*fragmentView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });*/
//        Commands.getHistory(new OnResponseReadyListener() {
//            @Override
//            public void OnResponseReady(boolean error, JSONObject data, String message) {
//                if(!error){
//                    try {
//                        listView.setAdapter(new HistoryAdapter(context,R.layout.adapter_history,data.getJSONArray("data")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    loader.setVisibility(View.GONE);
//
//                }
//                else {
//                    loader.setVisibility(View.GONE);
//                    Toast.makeText(context,"خطا در دریافت اطلاعات",Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        FontManager.instance().setTypefaceImmediate(fragmentView);

        return fragmentView;
    }


}
