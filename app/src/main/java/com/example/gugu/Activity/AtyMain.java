package com.example.gugu.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.gugu.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMainViewPager;
import Util.ImageManager;
import View.LayoutChats;
import View.LayoutContacts;
import View.LayoutMoments;
import View.LayoutMyPage;
import View.TitleBar;

public class AtyMain extends AppCompatActivity {

    private ConstraintLayout cslayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabLayout.Tab> tabList;
    private TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_main);
        initViews();
    }

    private void initViews() {
        cslayout = (ConstraintLayout) findViewById(R.id.cl_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        tabLayout = (TabLayout) findViewById(R.id.tl_main);
        titleBar = (TitleBar) findViewById(R.id.title_main);
        tabList = new ArrayList<>();

        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                System.out.println("SUCCESS 200");
            }

            @Override
            public void rightButtonClick() {
                System.out.println("SUCCESS 201");
            }
        });

        AdapterMainViewPager adapter = new AdapterMainViewPager(getSupportFragmentManager());

        adapter.addFragment(new LayoutChats());
        adapter.addFragment(new LayoutContacts());
        adapter.addFragment(new LayoutMoments());
        adapter.addFragment(new LayoutMyPage());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabList.add(tabLayout.getTabAt(0));
        tabList.add(tabLayout.getTabAt(1));
        tabList.add(tabLayout.getTabAt(2));
        tabList.add(tabLayout.getTabAt(3));
        tabList.get(0).setIcon(R.drawable.msgunselected).setText("Chats");
        tabList.get(1).setIcon(R.drawable.contactsunselected).setText("Contacts");
        tabList.get(2).setIcon(R.drawable.momentunselected).setText("Moments");
        tabList.get(3).setIcon(R.drawable.icon).setText("Mypage");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()<3) {
                    tabList.get(tab.getPosition()).setIcon(ImageManager.imageID[tab.getPosition() + 3]);
                    tabLayout.setTabTextColors(
                            ContextCompat.getColor(AtyMain.this, R.color.colorBlack),
                            ContextCompat.getColor(AtyMain.this, R.color.colorBlue)
                    );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabList.get(tab.getPosition()).setIcon(ImageManager.imageID[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(1).select();

    }
}
