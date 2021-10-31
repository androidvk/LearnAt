package com.coremacasia.learnat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.activities.CourseViewer;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Inside_courseAdapter extends RecyclerView.Adapter<Inside_courseAdapter.Holder> {


    private ArrayList<CourseHelper> list;

    public Inside_courseAdapter(Context activity) {

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_trending_courses, parent, false);
        return new Inside_courseAdapter.Holder(view);
    }
    private String imageLink;
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
       CourseHelper helper = list.get(position);

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
                Gson gson = new Gson();
                String myJson = gson.toJson(helper);
                holder.context.startActivity(new Intent(holder.context,
                        CourseViewer.class)
                        .putExtra("helper",myJson)
                        .putExtra("courseId",helper.getCourse_id())
                        .putExtra("category",helper.getCategory_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataModel(ArrayList<CourseHelper> list) {

        this.list = list;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TrendingAdapter.Holder holder;
        private int position;
        private static final String TAG = "InsideCourseHolder";
        private TextView tTitle, tMentorName, tLive, tDescription;
        private ImageView imageView, teacherPng;
        private View mainView;
        private Context context;
        public Holder(@NonNull View itemView) {
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
