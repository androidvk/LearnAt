package com.coremacasia.learnat.utility;

import android.content.Context;

import com.coremacasia.learnat.R;

public class Getter {
    public String getCategoryName(Context context, String cat) {
        if (cat.equals(RMAP.comp_exam)) {
            return context.getString(R.string.competitive_exams);
        } else if (cat.equals(RMAP.ent_exam)) {
            return context.getString(R.string.entrance_exam);

        } else if (cat.equals(RMAP.itcoding)) {
            return context.getString(R.string.it_coding);
        } else if (cat.equals(RMAP.sc_school)) {
            return context.getString(R.string.science_school);
        } else if (cat.equals(RMAP.comm)) {
            return context.getString(R.string.commerce);
        } else {
            return "Null";
        }
    }
}
