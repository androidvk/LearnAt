package com.coremacasia.learnat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnat.R;
import com.coremacasia.learnat.databinding.ListCoursePriceBinding;
import com.coremacasia.learnat.helpers.PriceDurationHelper;

import java.util.ArrayList;

public class PriceDurationAdapter extends RecyclerView.Adapter<PriceDurationAdapter.Holder> {
    private final FragmentActivity activity;
    private final ArrayList<PriceDurationHelper> price_durationList;
    private static final String TAG = "PriceDurationAdapter";

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

    private int lastCheckedPosition = 1;
    private OnPriceClickListener onPriceClickListener;

    public interface OnPriceClickListener {
        void onPriceClick(PriceDurationHelper helper);
    }

    public void onPriceClick(OnPriceClickListener onPriceClickListener) {
        this.onPriceClickListener = onPriceClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PriceDurationHelper helper = price_durationList.get(position);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int copyOfLastCheckedPosition = lastCheckedPosition;
                lastCheckedPosition = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPosition);
                if (lastCheckedPosition != -1) {
                    PriceDurationHelper helper1 = price_durationList.get(lastCheckedPosition);
                    onPriceClickListener.onPriceClick(helper1);
                }
            }
        });
        holder.radioButton.setChecked(position == lastCheckedPosition);
        holder.tPrice.setText(holder.context.getString(R.string.rupee_sign) + helper.getPrice());
        holder.tDuration.setText(helper.getDuration() + " " + helper.getDuration_unit());


    }

    @Override
    public int getItemCount() {
        return price_durationList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private static final String TAG = "PriceDurationAdapter";
        private Context context;
        private TextView tPrice, tDuration;
        private RadioButton radioButton;
        private View mainView;

        public Holder(@NonNull ListCoursePriceBinding itemView) {
            super(itemView.getRoot());
            tPrice = itemView.tPrice;
            tDuration = itemView.tDuration;
            radioButton = itemView.radioButton;
            context = itemView.getRoot().getContext();
            mainView = itemView.mainView;


        }
    }
}
