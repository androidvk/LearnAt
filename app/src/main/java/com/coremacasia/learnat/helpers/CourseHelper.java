package com.coremacasia.learnat.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseHelper {
    private String added_by;
    private String course_id;
    private String mentor_id;
    private String subject_id;
    private String thumbnail;
    private String desc;
    private String course_price,course_lang;
    private Date start_date;

    public String getCourse_price() {
        return course_price;
    }

    public String getCourse_lang() {
        return course_lang;
    }

    public Date getStart_date() {
        return start_date;
    }

    private List<LectureHelper> lectures=new ArrayList<>();

    public List<LectureHelper> getLectures() {
        return lectures;
    }

    public String getDesc() {
        return desc;
    }

    private String title;

    public String getCategory_id() {
        return category_id;
    }

    private String category_id;
    private boolean is_individual,is_live,is_visible;

    public String getAdded_by() {
        return added_by;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIs_individual() {
        return is_individual;
    }

    public boolean isIs_live() {
        return is_live;
    }

    public boolean isIs_visible() {
        return is_visible;
    }
}
