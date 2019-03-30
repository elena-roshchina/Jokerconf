package ru.jokerconf.jokerconf;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    private final String URL = "https://yandex.ru/maps/?z=12&ll=30.35741838466146,59.76381354798114&l=map&origin=jsapi_2_1_69&from=api-maps&um=constructor:f566841c3a42b552bca1bf9cf63edac02baaa1c9f35cc74d9a85555b655d4173";

    private final String LOCATION_RU ="Joker 2018 " +
            "19-20 октября\n\n" +
            "Конгрессно-выставочный\nцентр ЭКСПОФОРУМ\n" +
            "Россия, Санкт-Петербург,\nПетербургское шоссе, 64/1";
    private final String LOCATION_ENG = "Joker 2018 " +
            "19-20 october\n\n" +
            "Convention and Exhibition Centre EXPOFORUM\n" +
            "Russia, Saint-Petersburg,\nPeterburgskoye sh. 64/1";

    private final String YANDEX_BUTTON_RU = "на Яндекс.карте";

    private final String YANDEX_BUTTON_EN = "see on Yandex.Map";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //openInfoActivity();
                    Intent infoActivityIntent = new Intent(MapActivity.this, InfoActivity.class);
                    startActivityForResult(infoActivityIntent,0);
                    setResult(RESULT_OK);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    //openMainActivity();
                    setResult(RESULT_OK);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    //openMapActivity();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String lang =(Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")) ? "ru" : "en";

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView about = findViewById(R.id.location);

        Button yandexButton = findViewById(R.id.seeYandexMap);

        if (lang.equals("ru")){
            about.setText(LOCATION_RU);
            yandexButton.setText(YANDEX_BUTTON_RU);
        } else {
            about.setText(LOCATION_ENG);
            yandexButton.setText(YANDEX_BUTTON_EN);
        }

        yandexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (URLUtil.isValidUrl(URL)){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(URL));
                    startActivity(browserIntent);
                }
            }
        });

    }
}
