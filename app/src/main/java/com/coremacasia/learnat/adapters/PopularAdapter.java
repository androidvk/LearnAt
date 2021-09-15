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
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.activities.CourseViewer;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;
import com.coremacasia.learnat.utility.MyStore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Holder> {
    private Activity activity;
    private static final String TAG = "PopularAdapter";
    private ArrayList<String> list = new ArrayList<>();
    private CategoryDashboardHelper dataModel;
    public void setDataModel(CategoryDashboardHelper dataModel) {
        this.dataModel = dataModel;
        list=dataModel.getPopular();
    }



    public PopularAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_popular,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PopularAdapter.Holder holder, int position) {
        String course_id = list.get(position);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {
                holder.tTitle.setText(helper.getTitle());
               /* ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
                for (MentorHelper helper1 : mentorList) {
                    if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                        holder.tMentorName.setText(helper1.getName());
                    }
                }*/
               /* if (helper.isIs_live()) {
                    holder.tLive.setVisibility(View.VISIBLE);
                } else {
                    holder.tLive.setVisibility(View.GONE);
                }*/
                new ImageSetterGlide().defaultImg(holder.itemView.getContext(), helper.getThumbnail(),
                        holder.imageView);
                holder.mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.context.startActivity(new Intent(holder.context,
                                CourseViewer.class)
                                .putExtra("courseId",helper.getCourse_id())
                                .putExtra("category",helper.getCategory_id()));
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
        private static final String TAG = "PopularHolder";
        private Holder holder;
        private int position;
        private ImageView imageView;
        private TextView tTitle;
        private View mainView;
        private Context context;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tTitle=itemView.findViewById(R.id.textView42);
            imageView=itemView.findViewById(R.id.imageView14);
            mainView=itemView.findViewById(R.id.mainView);
            context= itemView.getContext();
        }

    }
}
