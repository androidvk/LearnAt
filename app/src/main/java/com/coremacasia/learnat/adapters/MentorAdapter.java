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
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.Holder> {
    private static final String TAG = "MentorAdapter";
    private ArrayList<MentorHelper> list=new ArrayList<>();
    private CommonDataModel dataModel;
    private Context activity;

    public void setDataModel(CommonDataModel dataModel) {
        this.dataModel = dataModel;
        list=dataModel.getMentors();
    }

    public MentorAdapter(Context activity) {

        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_mentor,parent,false    );
       return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MentorAdapter.Holder holder, int position) {
        holder.onBind(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "MentorHolder";
        private ImageView imageView;
        private TextView tName;
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tName=itemView.findViewById(R.id.textView40);
            imageView=itemView.findViewById(R.id.imageView12);
        }

        public void onBind(Holder holder, int position) {
            MentorHelper helper=list.get(position);
            tName.setText(helper.getName());
            new ImageSetterGlide().defaultImg(itemView.getContext(),helper.getImage(),imageView);

        }
    }
}
