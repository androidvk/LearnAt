package com.coremacasia.learnat.helpers;

import java.util.Date;

public class SubsHelper {
    private Date start_date,end_date,subs_date;
    private int subs_price,cashback_received,discount_received;

    private String referral;

    public String getCourse_id() {
        return course_id;
    }

    private String course_id;

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public Date getSubs_date() {
        return subs_date;
    }

    public int getSubs_price() {
        return subs_price;
    }

    public int getCashback_received() {
        return cashback_received;
    }

    public int getDiscount_received() {
        return discount_received;
    }

    public String getReferral() {
        return referral;
    }

    public String getReferred_by() {
        return referred_by;
    }

    private String referred_by;
}
