package ru.jokerconf.jokerconf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class InfoActivity extends AppCompatActivity{

    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_INFO = "SHARED_INFO";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //openInfoActivity
                    return true;
                case R.id.navigation_dashboard:
                    //openMainActivity
                    setResult(RESULT_OK);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //openMapActivity
                    Intent mapActivityIntent = new Intent(InfoActivity.this, MapActivity.class);
                    startActivityForResult(mapActivityIntent,0);
                    setResult(RESULT_OK);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        if (sharedPref.contains(SHARED_INFO)){
            Gson gson =  new Gson();
            Type infoType = new TypeToken<InfoOld>() {}.getType();
            String gInfo = sharedPref.getString(SHARED_INFO,"");
            InfoOld restoredInfo = gson.fromJson(gInfo,infoType);

            String confTitle = restoredInfo.getTitle();
            String startDate = restoredInfo.getStartDate();
            String endDate = restoredInfo.getEndDate();
            String place = restoredInfo.getPlace();
            String wifiNet = restoredInfo.getWifiNet();
            String wifiPswd = restoredInfo.getWifiPass();

            TextView confTitleText = findViewById(R.id.conf_title);
            confTitleText.setText(confTitle);
            confTitleText.append(" " + startDate + " - " + endDate + " " + place);

            TextView wifiNetwork = findViewById(R.id.wifi);
            wifiNetwork.append(wifiNet);
            TextView wifiPassword = findViewById(R.id.wifi_pass);
            wifiPassword.append(wifiPswd);

            String about = restoredInfo.getAbout();
            TextView aboutTextView = findViewById(R.id.about_text);


            aboutTextView.setText(about);


        }


    }
}
