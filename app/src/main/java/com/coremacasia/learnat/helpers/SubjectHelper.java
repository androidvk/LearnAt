package com.coremacasia.learnat.helpers;

import java.util.Date;

public class SubjectHelper   {
    private String subject_id;

    public String getSubject_id() {
        return subject_id;
    }


    private String title;
    private String subject_code;
    private String desc;
    private String category;
    private String added_by,updated_by;
    private Date update_time;

    public String getAdded_by() {
        return added_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public String getIcon() {
        return icon;
    }

    private String icon;

    public String getTitle() {
        return title;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }
}
