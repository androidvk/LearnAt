package com.coremacasia.learnat.helpers;
public class UserHelper {
    private String name;
    private String image;
    private String name_small;
    private String firebase_id;
    private String m_number;
    private String email;
    private String type,mentor_id;

    public String getType() {
        return type;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public String getPreferred_type1() {
        return preferred_type1;
    }

    private String preferred_type1;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getName_small() {
        return name_small;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public String getM_number() {
        return m_number;
    }

    public String getEmail() {
        return email;
    }
}
