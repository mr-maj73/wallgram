package org.cafemember.messenger.mytg.ui;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cafemember.messenger.R;
import org.cafemember.messenger.mytg.FontManager;
import org.cafemember.messenger.mytg.adapter.ViewPagerAdapter;
import org.cafemember.messenger.mytg.listeners.Refrashable;
import org.cafemember.ui.DialogsActivity;

/**
 * Created by Masoud on 6/2/2016.
 */
public class Views {

    public static View getTabLayout(final FragmentActivity context, DialogsActivity dialogsActivity, View dialogsLayout) {

        View lay = context.getLayoutInflater().inflate(R.layout.main_layout, null);
        //lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));*/

        TabLayout tabLayout = (TabLayout) lay.findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(Color.WHITE);
        final MyViewPager viewPager = (MyViewPager) lay.findViewById(R.id.viewpager);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context.getSupportFragmentManager(), dialogsActivity, dialogsLayout);
        viewPager.setAdapter(viewPagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*Fragment page = context.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
                if(page != null && page instanceof Refrashable){
                    ((Refrashable)page).refresh();
                }*/
                Refrashable refrashable = (Refrashable) viewPagerAdapter.getItem(position);
                refrashable.refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TabLayout.Tab telegram = tabLayout.newTab();
        final TabLayout.Tab myChannels = tabLayout.newTab().setText(R.string.myChannelFragmentLeybel);
        final TabLayout.Tab ChannelsFragment = tabLayout.newTab().setText(R.string.channelsFragmentLeybel);
        final TabLayout.Tab coinFragmentTab = tabLayout.newTab().setText(R.string.coinFragmentLeybel);
        final TabLayout.Tab advertisingFragmentTab = tabLayout.newTab().setText(R.string.advertisingFragmentLeybel);
        final TabLayout.Tab transfare = tabLayout.newTab();

        tabLayout.addTab(ChannelsFragment, 0);
        tabLayout.addTab(advertisingFragmentTab, 1);
        tabLayout.addTab(myChannels, 2);
        tabLayout.addTab(coinFragmentTab, 3);

//        telegram.setIcon(R.drawable.ic_message);
//        channels.setText("خرید سکه");
       /* View tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView text = (TextView) tabOne.findViewById(R.id.text);
        ImageView icon = (ImageView) tabOne.findViewById(R.id.icon);
        text.setText(R.string.coinFragmentLeybel);
//        icon.setImageResource(R.drawable.buy_coin);
        coinFragmentTab.setCustomView(tabOne);


//        telegram.setIcon(درج اگهی);
//        channels.setText("اگهی");
        View tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        text = (TextView) tabTwo.findViewById(R.id.text);
        icon = (ImageView) tabTwo.findViewById(R.id.icon);
        text.setText(R.string.advertisingFragmentLeybel);
//        icon.setImageResource(R.drawable.buy_coin);
        advertisingFragmentTab.setCustomView(tabTwo);

//        posts.setText("کانال های من");
//        joinCoins.setText("سفارش عضو");

        View tabThree = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        text = (TextView) tabThree.findViewById(R.id.text);
        icon = (ImageView) tabThree.findViewById(R.id.icon);
        text.setText(R.string.myChannelFragmentLeybel);
//        icon.setImageResource(R.drawable.member);
        myChannels.setCustomView(tabThree);


        View tabFour = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        text = (TextView) tabFour.findViewById(R.id.text);
        icon = (ImageView) tabFour.findViewById(R.id.icon);
        text.setText(R.string.channelsFragmentLeybel);
//        icon.setImageResource(R.drawable.free_coin);
        ChannelsFragment.setCustomView(tabFour);
*/


//        View tabFour =  LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        text = (TextView)tabFour.findViewById(R.id.text);
//        icon = (ImageView)tabFour.findViewById(R.id.icon);
//        text.setText("انتقال سکه");
////        icon.setImageResource(R.drawable.free_coin);
//
//        transfare.setCustomView(tabFour);

        /*channels.setIcon(R.drawable.free_coin);
        posts.setIcon(R.drawable.member);

        joinCoins.setIcon(R.drawable.buy_coin);*/
//        viewCoins.setIcon(R.drawable.ic_coin_eye);


//        tabLayout.addTab(telegram, 0);

//        tabLayout.setTabMode(ViewGroup.);
//        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.abc_primary_text_material_dark));

        FontManager.instance().setTypefaceImmediate(tabLayout);

        viewPager.setOffscreenPageLimit(3);
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

        for(int i=0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(25, 0, 25, 0);
            tab.requestLayout();
        }

        return lay;

    }


}
