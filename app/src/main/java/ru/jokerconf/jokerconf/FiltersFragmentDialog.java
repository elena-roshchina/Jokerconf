package ru.jokerconf.jokerconf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FiltersFragmentDialog extends DialogFragment implements View.OnClickListener {
    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_TAGS = "SHARED_TAGS";
    private static final String SHARED_TALKS = "SHARED_TALKS";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<Filter> restoredTags;
    private List<Event> restoredTalks;
    private RecyclerView recyclerView;

    private Button resetButton;
    private ImageButton hideFiltersButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //getDialog().setTitle("FILTERS");
        View view = inflater.inflate(R.layout.filters_dialog_fragment, null);
        final TextView eventCountText = view.findViewById(R.id.dialog_filters);
        eventCountText.setText("Filters");
        resetButton = view.findViewById(R.id.dialog_filters_reset);
        gson =  new Gson();
        sharedPref = Objects.requireNonNull(getContext())
                .getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        if (sharedPref.contains(SHARED_TALKS)) {
            Type listTalkType = new TypeToken<List<Event>>() {}.getType();
            String s = sharedPref.getString(SHARED_TALKS, "");
            restoredTalks = gson.fromJson(s, listTalkType);
        } else {
            restoredTalks = new ArrayList<>();
        }

        if (sharedPref.contains(SHARED_TAGS)){
            Type typeTags = new TypeToken<List<Filter>>() {}.getType();

            String gTags = sharedPref.getString(SHARED_TAGS,"");
            restoredTags = gson.fromJson(gTags,typeTags);

            int count = PageFragment.countFiltered(restoredTags, restoredTalks);
            String msg = count + " Events";
            eventCountText.setText(msg);

            recyclerView = view.findViewById(R.id.tags_recycle_view);;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            int c = 0;
            for (Filter f:restoredTags){
                if (f.getIsChecked()){
                    c++;
                }
            }
            if (c == 0){
                resetButton.setVisibility(View.INVISIBLE);
            } else {
                resetButton.setVisibility(View.VISIBLE);
            }

            TagsAdapter.OnItemClickListener onItemClickListener = new TagsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    int c = 0;
                    for (Filter f:restoredTags){
                        if (f.getIsChecked()){
                            c++;
                        }
                    }
                    if (c == 0){
                        resetButton.setVisibility(View.INVISIBLE);
                    } else {
                        resetButton.setVisibility(View.VISIBLE);
                    }

                    int count = PageFragment.countFiltered(restoredTags, restoredTalks);
                    String msg = count + " Events";
                    eventCountText.setText(msg);
                }
            };

            TagsAdapter adapter = new TagsAdapter(restoredTags, R.layout.tag_filter_item,
                    onItemClickListener, Objects.requireNonNull(getContext()));
            recyclerView.setAdapter(adapter);
        }


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Filter fG: restoredTags){
                    fG.setIsChecked(false);
                }
                resetButton.setVisibility(View.INVISIBLE);
                recyclerView.getAdapter().notifyDataSetChanged();

                int count = (restoredTalks != null) ? restoredTalks.size() : 0;
                String msg = count + " Events";
                eventCountText.setText(msg);
            }
        });


        hideFiltersButton = view.findViewById(R.id.dialog_filters_hide);
        // сохраняет измененные фильтры
        hideFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson =  new Gson();
                String filtersJson = gson.toJson(restoredTags);

                saveToShared(Objects.requireNonNull(getContext()), SHARED_TAGS, filtersJson);

                Intent intent = new Intent();
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                }
                dismiss();
            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {
        //Save filters and close fragment

    }

    private void saveToShared(Context context, String key, String savedString) {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString(key, savedString);
        editor.apply();
        editor.commit();
    }
}
