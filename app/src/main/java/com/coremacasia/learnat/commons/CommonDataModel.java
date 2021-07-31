package com.coremacasia.learnat.commons;

import com.coremacasia.learnat.helpers.CategoryHelper;
import com.coremacasia.learnat.helpers.CourseHelper;
import com.coremacasia.learnat.helpers.MentorHelper;
import com.coremacasia.learnat.helpers.PopularHelper;
import com.coremacasia.learnat.helpers.SubjectHelper;
import com.coremacasia.learnat.helpers.UpcomingHelper;

import java.util.ArrayList;

public class CommonDataModel {
    public CommonDataModel(){}

    public ArrayList<CategoryHelper> getCategory() {
        return category;
    }

    private ArrayList<PopularHelper> popular=new ArrayList<>();

    public ArrayList<PopularHelper> getPopular() {
        return popular;
    }
    public ArrayList<UpcomingHelper> upcoming=new ArrayList<>();

    public ArrayList<UpcomingHelper> getUpcoming() {
        return upcoming;
    }

    private ArrayList<CategoryHelper> category;

    private ArrayList<String> course_id;

    public ArrayList<String> getSubject_id() {
        return subject_id;
    }

    private ArrayList<String> subject_id;

    public ArrayList<String> getCourse_id() {
        return course_id;
    }

    public ArrayList<CourseHelper> getAll_courses() {
        return all_courses;
    }

    private ArrayList<CourseHelper> all_courses;

    private ArrayList<MentorHelper> mentors;

    private ArrayList<SubjectHelper> all_subjects;

    public ArrayList<SubjectHelper> getAll_subjects() {
        return all_subjects;
    }

    public ArrayList<MentorHelper> getMentors() {
        return mentors;
    }
}
