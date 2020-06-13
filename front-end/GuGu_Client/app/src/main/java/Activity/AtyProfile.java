package Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;
import View.TitleBar;

public class AtyProfile extends AppCompatActivity {

    private TitleBar tbProfile;
    private EditText etUsername;
    private EditText etSign;
    private EditText etPassword;
    private EditText etInsurePassword;
    private Button btnSubmit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_profile);

        initViews();
    }

    private void initViews() {
        tbProfile = (TitleBar) findViewById(R.id.tb_profile);
        etUsername = (EditText) findViewById(R.id.et_profile_username);
        etSign = (EditText) findViewById(R.id.et_profile_sign);
        etPassword = (EditText) findViewById(R.id.et_profile_password);
        etInsurePassword = (EditText) findViewById(R.id.et_profile_insure_password);
        btnSubmit = (Button) findViewById(R.id.btn_profile_submit);

        tbProfile.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }

            @Override
            public void rightButtonClick() { }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AtyProfile.this, "submit success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
