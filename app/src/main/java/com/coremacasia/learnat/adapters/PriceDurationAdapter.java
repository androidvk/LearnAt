package com.coremacasia.learnat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.databinding.ListCoursePriceBinding;
import com.coremacasia.learnat.helpers.PriceDurationHelper;

import java.util.ArrayList;

public class PriceDurationAdapter extends RecyclerView.Adapter<PriceDurationAdapter.Holder> {
    private final FragmentActivity activity;
    private final ArrayList<PriceDurationHelper> price_durationList;

    public PriceDurationAdapter(FragmentActivity activity, ArrayList<PriceDurationHelper> price_durationList) {

        this.activity = activity;
        this.price_durationList = price_durationList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ListCoursePriceBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return price_durationList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull ListCoursePriceBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
