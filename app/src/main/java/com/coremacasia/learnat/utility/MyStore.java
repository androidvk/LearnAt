package com.coremacasia.learnat.utility;


import com.coremacasia.learnat.commons.CommonDataModel;
import com.coremacasia.learnat.commons.all_courses.CourseModel;
import com.coremacasia.learnat.helpers.CategoryDashboardHelper;
import com.coremacasia.learnat.helpers.UserHelper;

public class MyStore {
    public static CommonDataModel getCommonData() {
        return commonData;
    }
    public static UserHelper userData;
    public static CategoryDashboardHelper categoryDashboardHelper;

    public static CategoryDashboardHelper getCategoryDashboardHelper() {
        return categoryDashboardHelper;
    }

    public static void setCategoryDashboardHelper(CategoryDashboardHelper categoryDashboardHelper) {
        MyStore.categoryDashboardHelper = categoryDashboardHelper;
    }

    public static UserHelper getUserData() {
        return userData;
    }

    public static void setUserData(UserHelper userData) {
        MyStore.userData = userData;
    }

    public static void setCommonData(CommonDataModel commonData) {
        MyStore.commonData = commonData;
    }

    public static CommonDataModel commonData;

    public static CourseModel getCourseData() {
        return courseData;
    }

    public static void setCourseData(CourseModel courseData) {
        MyStore.courseData = courseData;
    }

    public static CourseModel courseData;

}
