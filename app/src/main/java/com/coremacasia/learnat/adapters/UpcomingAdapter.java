package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.helpers.UpcomingHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;
import com.coremacasia.learnat.utility.kMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.Holder> {
    private static final String TAG = "UpcomingAdapter";
    private CategoryDashboardHelper dataModel;
    private Context activity;
    private ArrayList<String> list = new ArrayList<>();

    public UpcomingAdapter(Context activity) {
        this.activity = activity;
    }

    public void setDataModel(CategoryDashboardHelper dataModel) {
        this.dataModel = dataModel;
        list = dataModel.getTrending();
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_upcoming_courses, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UpcomingAdapter.Holder holder, int position) {
        String course_id = list.get(position);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                holder.tTitle.setText(helper.getTitle());
                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        holder.tMentorName.setText(helper1.getName());
                    }
                }
                if (helper.isIs_live()) {
                    holder.tLive.setVisibility(View.VISIBLE);
                } else {
                    holder.tLive.setVisibility(View.GONE);
                }
                new ImageSetterGlide().defaultImg(holder.itemView.getContext(), helper.getThumbnail(),
                        holder.imageView);


            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Holder holder;
        private int position;
        private static final String TAG = "UpcomingHolder";
        private TextView tTitle, tMentorName, tLive;
        private ImageView imageView;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tTitle = itemView.findViewById(R.id.textView43);
            tMentorName = itemView.findViewById(R.id.textView44);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
        }

    }
}
