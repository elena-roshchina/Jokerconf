package ru.jokerconf.jokerconf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TalkDetailsActivity extends AppCompatActivity {

    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_TALKS = "SHARED_TALKS";
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private final static String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private String pdfUrl = "";
    private String videoUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_details);
        Intent intent = getIntent();

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        TextView talkTitle = findViewById(R.id.talk_details_title);
        TextView dateTimeLangLevel = findViewById(R.id.talk_date_time_lang_level);
        TextView talkPlace = findViewById(R.id.talk_details_place);
        TextView talkAbout = findViewById(R.id.talk_details_about);

        TextView tags = findViewById(R.id.tags_list);


        Gson gson = new Gson();
        List<Event> restoredTalks = new ArrayList<>();
        sharedPref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPref.contains(SHARED_TALKS)) {
            //Toast.makeText(getContext(),"Talks exist", Toast.LENGTH_SHORT).show();
            Type listTalkType = new TypeToken<List<Event>>() {}.getType();
            String s = sharedPref.getString(SHARED_TALKS, "");
            restoredTalks = gson.fromJson(s, listTalkType);
        }
        for (Event t: restoredTalks){
            if (t.getId().equals(intent.getStringExtra(EXTRA_EVENT_ID))){
                talkTitle.setText(t.getTitle());
                String dateTimeLang = t.getDay() + " " + t.getBegin()  + " / " +
                        t.getLanguage();
                dateTimeLangLevel.setText(dateTimeLang);
                talkPlace.setText(t.getPlace());
                talkAbout.setText(t.getDescLong());

                videoUrl = t.getVideo();
                pdfUrl = t.getPdf();

                ImageButton videoButton = findViewById(R.id.video);
                videoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (URLUtil.isValidUrl(videoUrl)){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(videoUrl));
                            startActivity(browserIntent);
                        }
                    }
                });
                ImageButton pdfButton = findViewById(R.id.pdf);
                pdfButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (URLUtil.isValidUrl(pdfUrl)){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(pdfUrl));
                            startActivity(browserIntent);
                        }
                    }
                });

                ImageView speakerPortrait = findViewById(R.id.speaker_portrait);
                Picasso.get().load(t.getSpkList().get(0).getPicture()).into(speakerPortrait);
                TextView speakerName = findViewById(R.id.speaker_name);
                speakerName.setText(t.getSpkList().get(0).getName());
                TextView affiliation = findViewById(R.id.affiliation);
                affiliation.setText(t.getSpkList().get(0).getAffiliation());
                TextView speakerInfo = findViewById(R.id.speaker_info);
                speakerInfo.setText(t.getSpkList().get(0).getDescription());

                if (t.getSpkList().size() > 1){
                    ImageView speakerPortrait2 = findViewById(R.id.speaker_portrait2);
                    Picasso.get().load(t.getSpkList().get(1).getPicture()).into(speakerPortrait2);
                    TextView speakerName2 = findViewById(R.id.speaker_name2);
                    speakerName2.setText(t.getSpkList().get(1).getName());
                    TextView affiliation2 = findViewById(R.id.affiliation2);
                    affiliation2.setText(t.getSpkList().get(1).getAffiliation());
                    TextView speakerInfo2 = findViewById(R.id.speaker_info2);
                    speakerInfo2.setText(t.getSpkList().get(1).getDescription());

                }

                StringBuilder tagStr = new StringBuilder();
                for (int i = 0; i < t.getTags().size(); i++){
                    tagStr.append(t.getTags().get(i));
                    if (i < t.getTags().size()-1){
                        tagStr.append(", ");
                    }
                }
                SpannableStringBuilder tagsList = new SpannableStringBuilder(tagStr);
                tagsList.setSpan(new BulletSpan(14, Color.GREEN), 0, tagsList.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tags.setText(tagsList);

            }
        }


    }
}
