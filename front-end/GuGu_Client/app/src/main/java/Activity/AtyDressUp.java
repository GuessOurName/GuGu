package Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterAvatar;
import Adapter.AdapterBackground;

import Util.ImageManager;
import Util.ImageMsg;
import View.TitleBar;

public class AtyDressUp extends AppCompatActivity {

    private TitleBar titleBar;
    private RecyclerView rvAvatar;
    private RecyclerView rvBackground;
    private Button btnSave;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_dress_up);

        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        titleBar = (TitleBar) findViewById(R.id.tb_dress_up);
        rvAvatar = (RecyclerView) findViewById(R.id.rv_avatar);
        rvBackground = (RecyclerView) findViewById(R.id.rv_background);
        btnSave = (Button) findViewById(R.id.btn_save);

        addAvatarView();

        addBackgroundView();

        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }

            @Override
            public void rightButtonClick() {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AtyDressUp.this, "saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addAvatarView() {
        List<ImageMsg> imageMsgs = new ArrayList<>();
        for (int anImagesAvatar : ImageManager.imagesAvatar) {
            ImageMsg imagemsg = new ImageMsg();
            imagemsg.setImageID(anImagesAvatar);
            imageMsgs.add(imagemsg);
        }

        AdapterAvatar avatarAdapter = new AdapterAvatar(this, imageMsgs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvAvatar.setLayoutManager(layoutManager);
        rvAvatar.setAdapter(avatarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addBackgroundView() {
        List<ImageMsg> imageMsgs = new ArrayList<>();
        for (int anImagesBackground : ImageManager.imagesBackground) {
            ImageMsg imagemsg = new ImageMsg();
            imagemsg.setImageID(anImagesBackground);
            imageMsgs.add(imagemsg);
        }

        AdapterBackground adapterBackground = new AdapterBackground(this, imageMsgs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvBackground.setLayoutManager(layoutManager);
        rvBackground.setAdapter(adapterBackground);
    }

}
