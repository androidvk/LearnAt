package com.coremacasia.learnat.commons;

import com.coremacasia.learnat.startItems.CategoryHelper;
import com.coremacasia.learnat.ui.home.mentors.MentorHelper;
import com.coremacasia.learnat.ui.home.popular.PopularHelper;
import com.coremacasia.learnat.ui.home.subjects.SubjectHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel(){}
    public CommonDataModel( ArrayList<CategoryHelper> category) {

        this.category = category;
    }



    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<CategoryHelper> category;

    public ArrayList<SubjectHelper> getSubjects() {
        return subjects;
    }

    private ArrayList<SubjectHelper> subjects;
    private ArrayList<MentorHelper> mentors;
    private ArrayList<PopularHelper> popular;

    public ArrayList<PopularHelper> getPopular() {
        return popular;
    }

    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }
}
