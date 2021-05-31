package com.coremacasia.learnat.startItems;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.utility.ImageSetterGlide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CourseMenuAdapter extends RecyclerView.Adapter<CourseMenuAdapter.Holder> {
    private Context activity;
    private ArrayList<menu_list> listHelper;
    private static final String TAG = "CourseMenuAdapter";
    public CourseMenuAdapter(Context activity, ArrayList<menu_list> listHelper) {
        Log.e(TAG, "CourseMenuAdapter: " );
        this.activity = activity;
        this.listHelper = listHelper;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_course_menu,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseMenuAdapter.Holder holder,
                                 int position) {
        holder.onBind(holder,position);
    }

    @Override
    public int getItemCount() {
        return listHelper.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "Holder";
        private TextView tName,tDesc;
        private ImageView iThumbnail;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName=itemView.findViewById(R.id.tName);
            tDesc=itemView.findViewById(R.id.tDesc);
            iThumbnail=itemView.findViewById(R.id.iCourseThumbnail);
        }

        public void onBind(Holder holder, int position) {
            menu_list list=listHelper.get(position);

            tName.setText(list.getName_en());
            tDesc.setText(list.getDesc_en());
            new ImageSetterGlide().defaultImg(itemView.getContext(),list.getImage(),iThumbnail);
        }
    }
}
