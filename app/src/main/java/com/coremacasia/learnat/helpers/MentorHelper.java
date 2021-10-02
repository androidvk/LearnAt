package com.coremacasia.learnat.helpers;

import java.util.ArrayList;

public class MentorHelper {
    private String category,image,mentor_id,name;

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public String getName() {
        return name;
    }
    public ArrayList<String> subjects=new ArrayList<>();
    public ArrayList<String> getSubjects() {
        return subjects;
    }
}
