package ru.jokerconf.jokerconf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private static final String SHARED_TALKS = "SHARED_TALKS";
    private static final String SHARED_PREF = "JOKER_SHARED_PREF";

    private final List<Event> events;
    private final List<Event> eventsFullList;
    private final int         rowLayout;
    private Context context;

    @NonNull
    private EventsAdapter.OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onItemClick(@NonNull Event event, int position);
    }

    private final View.OnClickListener internalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Event event = (Event) v.getTag();
            if (event != null) {
                int position = events.indexOf(event);
                onItemClickListener.onItemClick(event,position);
            }
        }
    };


    private CompoundButton.OnCheckedChangeListener internalOnCheckedChangeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getTag() != null) {
                int position = (int) buttonView.getTag();

                if (events.get(position).getIsStarred() != isChecked){
                    events.get(position).setIsStarred(isChecked);
                    for (int i = 0; i < eventsFullList.size(); i++){
                        if (eventsFullList.get(i).getId().equals(events.get(position).getId())){
                            eventsFullList.get(i).setIsStarred(isChecked);
                        }
                    }
                    Gson gson = new Gson();
                    String talksJson = gson.toJson(eventsFullList);

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SHARED_TALKS, talksJson);
                    editor.apply();
                    editor.commit();

                }
            }
        }
    };

    public EventsAdapter(List<Event> events, int rowLayout,
                         EventsAdapter.OnItemClickListener onItemClickListener,
                         List<Event> eventsFullList, Context context) {

        this.events = events;
        this.rowLayout = rowLayout;
        this.onItemClickListener =  onItemClickListener;
        this.eventsFullList = eventsFullList;
        this.context = context;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        String t = event.getBegin();
        holder.time.setText(t);
        holder.title.setOnClickListener(internalClickListener);
        holder.title.setTag(event);
        holder.title.setText(event.getTitle());
        holder.datePlace.setText(event.getDay());
        holder.datePlace.append( " / " + event.getPlace());

        holder.toggleButton.setTag(position);
        holder.toggleButton.setVisibility(View.VISIBLE);
        holder.toggleButton.setChecked(event.getIsStarred());
        holder.toggleButton.setOnCheckedChangeListener(internalOnCheckedChangeListener);

        SpannableStringBuilder tagsList = new SpannableStringBuilder();
        for (int i = 0; i < event.getTags().size(); i++){
            tagsList.append(event.getTags().get(i));
            if (i < event.getTags().size()-1){
                tagsList.append(", ");
            }
        }
        tagsList.setSpan(new BulletSpan(14, Color.GREEN), 0, tagsList.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.eventTagsList.setText(tagsList);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView datePlace;
        private TextView time;
        private TextView title;
        private ToggleButton toggleButton;
        private TextView eventTagsList;

        public ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.event_time);
            title = view.findViewById(R.id.event_title);
            datePlace = view.findViewById(R.id.event_date_place);
            toggleButton = itemView.findViewById(R.id.set_starred);
            toggleButton.setVisibility(View.INVISIBLE);
            eventTagsList = view.findViewById(R.id.event_tags);
        }
    }
}
