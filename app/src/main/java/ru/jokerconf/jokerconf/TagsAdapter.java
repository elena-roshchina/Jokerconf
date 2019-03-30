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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private static final String SHARED_PREF = "JOKER_SHARED_PREF";
    private static final String SHARED_TAGS = "SHARED_TAGS";

    private final List<Filter> tags;
    private final int          rowLayout;
    private Context context;


    @NonNull
    private TagsAdapter.OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onItemClick();
    }

    private final View.OnClickListener internalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Filter selectedTag = (Filter) v.getTag();
            if (selectedTag != null) {
                int position = tags.indexOf(selectedTag);
                onItemClickListener.onItemClick();
            }
        }
    };


    private CheckBox.OnCheckedChangeListener internalBoxOnCheckedChangeListener
            = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getTag() != null) {
                int position = (int) buttonView.getTag();

                if (tags.get(position).getIsChecked() != isChecked){
                    tags.get(position).setIsChecked(isChecked);

                    Gson gson = new Gson();
                    String talksJson = gson.toJson(tags);

                    SharedPreferences sharedPreferences = context.getSharedPreferences(
                            SHARED_PREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SHARED_TAGS, talksJson);
                    editor.apply();
                    editor.commit();
                }
            }
            onItemClickListener.onItemClick();
        }
    };


    public TagsAdapter(List<Filter> tags, int rowLayout,
                       OnItemClickListener onItemClickListener, Context context) {
        this.tags = tags;
        this.rowLayout = rowLayout;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagsAdapter.ViewHolder holder, int position) {
        //String tagText = tags.get(position);
        //holder.tagText.setText(tagText);
        Filter selectedTag = tags.get(position);

        String tagText = selectedTag.getFilterName();
        holder.tagText.setText(tagText);

        holder.checkBox.setTag(position);
        holder.checkBox.setVisibility(View.VISIBLE);
        holder.checkBox.setChecked(selectedTag.getIsChecked());
        holder.checkBox.setOnCheckedChangeListener(internalBoxOnCheckedChangeListener);

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tagText;
        private CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            tagText = view.findViewById(R.id.tag_filter_item_text);
            checkBox = itemView.findViewById(R.id.tag_checked);
        }
    }

}
