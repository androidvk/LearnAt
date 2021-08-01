package com.coremacasia.learnat.helpers;

import java.util.ArrayList;

public class CategoryDashboardHelper {
    private ArrayList<CategoryHelper> category=new ArrayList<>();

    private ArrayList<String> course_id = new ArrayList<>();
    private ArrayList<String> trending = new ArrayList<>();
    private ArrayList<String> popular=new ArrayList<>();
    private ArrayList<String> mentors=new ArrayList<>();

    public ArrayList<String> getSubject_id() {
        return subject_id;
    }

    private ArrayList<String> subject_id=new ArrayList<>();
    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    public ArrayList<String> getCourse_id() {
        return course_id;
    }

    public ArrayList<String> getTrending() {
        return trending;
    }

    public ArrayList<String> getPopular() {
        return popular;
    }

    public ArrayList<String> getMentors() {
        return mentors;
    }
}
