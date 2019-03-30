package ru.jokerconf.jokerconf;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private final String ORG = "org";
    private final String BREAK = "break";
    private final String MEAL = "meal";
    private final String PARTY = "party";
    private final String SECTION = "talk section";
    private final String OTHER = "other";

    private final List<Event> items; // of agenda
    private final int         rowLayout;

    public AgendaAdapter(List<Event> items, int rowLayout) {
        this.items = items;
        this.rowLayout = rowLayout;
    }

    @Override
    public AgendaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new AgendaAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AgendaAdapter.ViewHolder holder, int position) {
        Event item = items.get(position);
        String dateStr = item.getDay();
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

        holder.date.setText(d.toString());
        holder.title.setText(item.getTitle());
        StringBuilder timeStr = new StringBuilder(item.getBegin());
        timeStr.append(" - ");
        timeStr.append(item.getEnd());
        holder.time.setText(timeStr.toString());
        String kind = item.getKind();
        switch (kind){
            case ORG:
                holder.img.setImageResource(R.drawable.baseline_how_to_reg_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorORG);
                break;
            case BREAK:
                holder.img.setImageResource(R.drawable.baseline_free_breakfast_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorBREAK);
                break;
            case MEAL:
                holder.img.setImageResource(R.drawable.baseline_restaurant_menu_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorBREAK);
                break;
            case SECTION:
                holder.img.setImageResource(R.drawable.baseline_people_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorSECTION);
                break;
            case PARTY:
                holder.img.setImageResource(R.drawable.baseline_local_bar_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorOTHER);
                break;
            case OTHER:
                holder.img.setImageResource(R.drawable.baseline_brightness_3_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorOTHER);
                break;
            default:
                holder.img.setImageResource(R.drawable.baseline_people_black_18dp);
                holder.layout.setBackgroundResource(R.color.colorWHITE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup layout;
        private TextView date;
        private ImageView img;
        private TextView time;
        private TextView title;

        public ViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.agenda_inner_layout);
            date = view.findViewById(R.id.agenda_date);
            img = view.findViewById(R.id.agenda_icon);
            title = view.findViewById(R.id.agenda_title);
            time = view.findViewById(R.id.agenda_time);
        }
    }
}
