package ru.jokerconf.jokerconf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_TALKS = "SHARED_TALKS";
    private static final String SHARED_TAGS = "SHARED_TAGS";
    private static final String SHARED_DATES = "SHARED_DATES";
    private static final String SHARED_AGENDA = "SHARED_AGENDA";
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private final static String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    private int mPage;

    private EventsAdapter eventsAdapter1;
    private EventsAdapter eventsAdapter2;
    private AgendaAdapter agendaAdapter;

    private RecyclerView recyclerView;

    private List<Event> restoredTalks = new ArrayList<>();
    private List<Filter> restoredTags = new ArrayList<>();
    private List<String> restoredDates = new ArrayList<>();
    private List<Event> filteredTalks = new ArrayList<>();

    private class EventsList {
        List<Event> eventList;

        public EventsList(List<Event> eventList) {
            this.eventList = eventList;
        }
    }
    private List<Event> talksByDay1 = new ArrayList<>();
    private List<Event> talksByDay2 = new ArrayList<>();

    private List<EventsList> talksByDay = new ArrayList<>();


    private List<String> savedFilters = new ArrayList<>();
    private List<Event> restoredAgenda = new ArrayList<>();

    private FiltersFragmentDialog filtersDialog;

    EventsAdapter.OnItemClickListener onItemClickListener = new EventsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(@NonNull Event event, int position) {
            openTalkDetails(getContext(), event.getId());
        }
    };

    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null ){
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       update();
    }

    public void update(){
            talkToShow();
            recyclerView.getAdapter().notifyDataSetChanged();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        Context context = getContext();

        FloatingActionButton fba = view.findViewById(R.id.fab);
        fba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtersDialog = new FiltersFragmentDialog();
                filtersDialog.setStyle(DialogFragment.STYLE_NO_TITLE,0);
                filtersDialog.setTargetFragment(PageFragment.this, Activity.RESULT_OK);
                assert getFragmentManager() != null;
                filtersDialog.show(getFragmentManager(),
                        filtersDialog.getClass().getName());
            }
        });

        assert context != null;
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        //получить программы докладов, даты
        if (sharedPref.contains(SHARED_TALKS)) {
            //Toast.makeText(getContext(),"Talks exist", Toast.LENGTH_SHORT).show();
            Type listTalkType = new TypeToken<List<Event>>() {}.getType();
            String s = sharedPref.getString(SHARED_TALKS, "");
            restoredTalks = gson.fromJson(s, listTalkType);
        }
        if (sharedPref.contains(SHARED_TAGS)) {
            //Toast.makeText(getContext(),"Talks exist", Toast.LENGTH_SHORT).show();
            Type tagsType = new TypeToken<List<Filter>>() {}.getType();
            String tagsString = sharedPref.getString(SHARED_TAGS, "");
            restoredTags = gson.fromJson(tagsString, tagsType);

        }
        if (sharedPref.contains(SHARED_DATES)) {
            Type datesType = new TypeToken<List<String>>() {}.getType();
            String d = sharedPref.getString(SHARED_DATES,"");
            restoredDates = gson.fromJson(d,datesType);
        }
        // фильтрация докладов и разделение по дням
        talkToShow();

        if (sharedPref.contains(SHARED_AGENDA)){
            Type agendaType = new TypeToken<List<Event>>() {}.getType();
            String ag = sharedPref.getString(SHARED_AGENDA,"");
            restoredAgenda = gson.fromJson(ag,agendaType);
            }

        recyclerView = view.findViewById(R.id.talks_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < restoredDates.size(); i++){

        }

        if (mPage == 1){
            eventsAdapter1 = new EventsAdapter(talksByDay1, R.layout.event_item,
                    onItemClickListener, restoredTalks, context);
            recyclerView.setAdapter(eventsAdapter1);
            RecyclerSectionItemDecoration sectionItemDecoration1 =
                    new RecyclerSectionItemDecoration(
                            5,
                            true,
                            getSectionCallback(talksByDay1));
            recyclerView.addItemDecoration(sectionItemDecoration1);

        } else if (mPage == 2 && mPage != restoredDates.size() + 1){
            eventsAdapter2 = new EventsAdapter(talksByDay2, R.layout.event_item,
                    onItemClickListener, restoredTalks, context);
            recyclerView.setAdapter(eventsAdapter2);
            RecyclerSectionItemDecoration sectionItemDecoration2 =
                    new RecyclerSectionItemDecoration(
                            5,
                            true,
                            getSectionCallback(talksByDay2));
            recyclerView.addItemDecoration(sectionItemDecoration2);


        } else if (mPage == restoredDates.size() + 1) { // mPage == 3
            fba.setVisibility(View.GONE);
            agendaAdapter = new AgendaAdapter(restoredAgenda,R.layout.agenda_item);
            recyclerView.setAdapter(agendaAdapter);
            AgendaItemStickyHeader agendaItemDecoration =
                    new AgendaItemStickyHeader(
                            5,
                            true,
                            getAgendaSectionCallback(restoredAgenda));
            recyclerView.addItemDecoration(agendaItemDecoration);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (recyclerView != null) {
            talkToShow();
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    } // end of onStart

    private AgendaItemStickyHeader.SectionCallback getAgendaSectionCallback(final List<Event> events) {
        return new AgendaItemStickyHeader.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                if (position == 0) {
                    return true;
                } else if (!events.get(position).getDay()
                        .equals(events.get(position-1).getDay())){
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                String dateStr = events.get(position).getDay();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateToShow = new SimpleDateFormat("d");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dayOfWeek = new SimpleDateFormat("E");

                Date parsingDate = new Date();
                try {
                    parsingDate = ft.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                StringBuilder d = new StringBuilder(dateToShow.format(parsingDate));
                d.append(" ");
                d.append(dayOfWeek.format(parsingDate));

                return d.toString();
            }
        };

    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<Event> events) {
        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                if (position == 0) {
                    return true;
                } else if (!events.get(position).getBegin()
                        .equals(events.get(position-1).getBegin())){
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public CharSequence getSectionHeader(int position) {
                return events.get(position).getBegin();
            }
        };
    }

    private void openTalkDetails(Context context, String eventID){
        Intent talkActivityIntent = new Intent(context, TalkDetailsActivity.class);
        talkActivityIntent.putExtra(EXTRA_EVENT_ID, eventID);
        startActivity(talkActivityIntent);
    }

    public static int countFiltered(List<Filter> filtersG, List<Event> events){

        List<String> filters = new ArrayList<>();
        for (Filter fG: filtersG){
            if (fG.getIsChecked()){
                filters.add(fG.getFilterName());
            }
        }
        int eventNum = events.size();
        int count = 0;
        boolean filtersExist = filters != null &&
                ((!filters.contains("is Starred") && filters.size() > 0) ||
                        (filters.contains("is Starred") && filters.size() > 1));
        boolean justStarred = (filters != null && filters.contains("is Starred"));
        for (int i = 0; i < eventNum; i++){
            boolean display = true;
            if (filtersExist){
                boolean isFiltered = false;
                int tagsNum = events.get(i).getTags().size();
                for (int j = 0; j < tagsNum; j++){
                    if (filters.contains(events.get(i).getTags().get(j))) {
                        isFiltered = true;
                    }
                }
                if (!isFiltered){
                    display = false;
                }
            }
            if (justStarred) {
                display = display && events.get(i).getIsStarred();
            }
            if (display){
                count++;
            }
        }
        return count;
    }

    private void talkFilter(){
        int talksNum = restoredTalks.size();
        int restoredTagsNum = restoredTags.size();
        savedFilters.clear();
        for (int i = 0; i < restoredTagsNum; i++){
            if (restoredTags.get(i).getIsChecked()){
                savedFilters.add(restoredTags.get(i).getFilterName());
            }
        }

        boolean filtersExist = savedFilters != null &&
                ((!savedFilters.contains("is Starred") && savedFilters.size() > 0) ||
                        (savedFilters.contains("is Starred") && savedFilters.size() > 1));
        boolean justStarred = (savedFilters != null && savedFilters.contains("is Starred"));

        for (int i = 0; i < talksNum; i++){
            boolean display = true;
            if (filtersExist){
                boolean isFiltered = false;
                int tagsNum = restoredTalks.get(i).getTags().size();
                for (int j = 0; j < tagsNum; j++){
                    if (savedFilters.contains(restoredTalks.get(i).getTags().get(j))) {
                        isFiltered = true;
                    }
                }
                if (!isFiltered){
                    display = false;
                }
            }
            if (justStarred) {
                display = display && restoredTalks.get(i).getIsStarred();
            }

            if (display){
                filteredTalks.add(restoredTalks.get(i));
            }
        }
    } //end talkFilter

    private void talkToShow(){
        // получение сохраненных фильтров
        if (sharedPref.contains(SHARED_TAGS)) {
            Gson gson =  new Gson();
            Type filtersType = new TypeToken<List<Filter>>() {}.getType();
            String f = sharedPref.getString(SHARED_TAGS, "");
            restoredTags = gson.fromJson(f, filtersType);
        }
        int restoredTagsNum = restoredTags.size();
        savedFilters.clear();
        for (int i = 0; i < restoredTagsNum; i++){
            if (restoredTags.get(i).getIsChecked()){
                savedFilters.add(restoredTags.get(i).getFilterName());
            }
        }
        filteredTalks.clear();

        talksByDay.clear();

        talksByDay1.clear();
        talksByDay2.clear();
        // фильтрация данных
        talkFilter();
        // распределение по дням
        int talkSize = filteredTalks.size();
        int daysNumber = restoredDates.size();
        for (int i = 0; i < talkSize; i ++){
            if (restoredDates.get(0).equals(filteredTalks.get(i).getDay())){
                talksByDay1.add(filteredTalks.get(i));
            }
            if ((restoredDates.size() > 1) && restoredDates.get(1).equals(filteredTalks.get(i).getDay())){
                talksByDay2.add(filteredTalks.get(i));
            }
        }

        for (int i = 0; i < talkSize; i ++){
            for (int j = 0; j < daysNumber; j++){
                List<Event> dayJ = new ArrayList<>();
                if (restoredDates.get(j).equals(filteredTalks.get(i).getDay())){
                    dayJ.add(filteredTalks.get(i));
                }
                talksByDay.add(new EventsList(dayJ));
            }
        }
    }// end talkToShow
}
