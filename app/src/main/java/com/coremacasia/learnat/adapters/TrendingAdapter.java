package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.fragments.InsideCourse;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.Holder> {
    private static final String TAG = "UpcomingAdapter";
    private CategoryDashboardHelper dataModel;
    private Context activity;
    private ArrayList<String> list = new ArrayList<>();

    public TrendingAdapter(Context activity) {
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
        View view = layoutInflater.inflate(R.layout.list_trending_courses, parent, false);
        return new Holder(view);
    }

    private String imageLink;

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrendingAdapter.Holder holder, int position) {
        String course_id = list.get(position);



        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                holder.tTitle.setText(helper.getTitle());

                ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        holder.tMentorName.setText(helper1.getName());
                        imageLink = helper1.getImage();
                    }
                }

                if (helper.isIs_live()) {
                    holder.tLive.setVisibility(View.VISIBLE);
                } else {
                    holder.tLive.setVisibility(View.GONE);
                }


                holder.tDescription.setText(helper.getDesc());
                String wallpaper = "https://learnat.in/wp-content/uploads/2021/08/17879-scaled-e1628793009125.jpg";
                new ImageSetterGlide().defaultImg(holder.itemView.getContext(), wallpaper,
                        holder.imageView);

                new ImageSetterGlide().defaultImg(holder.itemView.getContext(), imageLink,
                        holder.teacherPng);

                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: " );
                        Bundle bundle = new Bundle();
                        bundle.putString("cat",helper.getCategory_id());
                        bundle.putString("from", "trending");
                        bundle.putString("courseId", helper.getCourse_id());
                        FragmentManager manager = ((AppCompatActivity) holder.context)
                                .getSupportFragmentManager();

                        FragmentTransaction fragmenttransaction =
                                manager.beginTransaction();
                        InsideCourse frag = new InsideCourse();
                        frag.setArguments(bundle);
                        fragmenttransaction.replace(android.R.id.content, frag)
                                .addToBackStack(frag.TAG);
                        fragmenttransaction.commit();
                    }
                });

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
        private TextView tTitle, tMentorName, tLive, tDescription;
        private ImageView imageView, teacherPng;
        private View mainView;
        private Context context;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tTitle = itemView.findViewById(R.id.textView24);
            tMentorName = itemView.findViewById(R.id.textView45);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            teacherPng = itemView.findViewById(R.id.imageView15);
            tDescription = itemView.findViewById(R.id.textView43);
            mainView = itemView.findViewById(R.id.mainView);
        }

    }
}
