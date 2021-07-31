package com.coremacasia.learnat.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.helpers.PopularHelper;
import com.coremacasia.learnat.utility.ImageSetterGlide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Holder> {
    private Activity activity;
    private static final String TAG = "PopularAdapter";
    private ArrayList<PopularHelper> list = new ArrayList<>();
    private CommonDataModel dataModel;
    public void setDataModel(CommonDataModel dataModel) {
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
        holder.onBind(holder,position);
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
        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            tTitle=itemView.findViewById(R.id.textView42);
            imageView=itemView.findViewById(R.id.imageView14);
        }

        public void onBind(Holder holder, int position) {
            this.holder = holder;
            this.position = position;
            PopularHelper helper=list.get(position);
            tTitle.setText(helper.getName()+" by "+helper.getMentor());
            new ImageSetterGlide().defaultImg(itemView.getContext(),helper.getImage(),imageView);

        }
    }
}
