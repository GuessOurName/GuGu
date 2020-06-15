package Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.gugu_client.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMainViewPager;
import Util.ImageManager;
import View.LayoutChats;
import View.LayoutContacts;
import View.LayoutMoments;
import View.TitleBar;

public class AtyMain extends AppCompatActivity {

    private Context context;
    private TitleBar titleBar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabLayout.Tab> tabList;
    private LayoutChats chats;
    private LayoutContacts contacts;
    private LayoutMoments moments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_main);
        initViews();
    }


    private void initViews() {
        context=this;
        titleBar = (TitleBar) findViewById(R.id.tb_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        tabLayout = (TabLayout) findViewById(R.id.tl_main);

        //对主界面下方Tab添加碎片
        tabList = new ArrayList<>();
        AdapterMainViewPager adapter = new AdapterMainViewPager(getSupportFragmentManager());
        chats=new LayoutChats();
        contacts=new LayoutContacts();
        moments=new LayoutMoments();
        adapter.addFragment(chats);
        adapter.addFragment(contacts);
        adapter.addFragment(moments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabList.add(tabLayout.getTabAt(0));
        tabList.add(tabLayout.getTabAt(1));
        tabList.add(tabLayout.getTabAt(2));
        tabList.get(0).setIcon(R.drawable.msgselected).setText("Chats");
        tabList.get(1).setIcon(R.drawable.contactsunselected).setText("Contacts");
        tabList.get(2).setIcon(R.drawable.momentunselected).setText("Moments");

        //设置主界面下方Tab选择事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabList.get(tab.getPosition()).setIcon(ImageManager.imageID[tab.getPosition() + 3]);
                tabLayout.setTabTextColors(
                        ContextCompat.getColor(AtyMain.this, R.color.colorBlack),
                        ContextCompat.getColor(AtyMain.this, R.color.colorBlue)
                );
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabList.get(tab.getPosition()).setIcon(ImageManager.imageID[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //设置主界面Title左右按钮监听事件
        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                Toast.makeText(AtyMain.this, "Left Button Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void rightButtonClick() {
                Intent intent = new Intent(context, AtyAdd.class);
                startActivityForResult(intent,0);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null){
            Toast.makeText(this, "Add Error", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = data.getExtras();
        String STATE = bundle.getString("STATE");
        if(STATE.equals("1")){
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            contacts.initView();
        }
        else{
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }
}
