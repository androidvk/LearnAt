package com.coremacasia.learnat.commons;

public class CommonData {
    public static CommonDataModel getCommonDataModel() {
        return commonDataModel;
    }

    public static void setCommonDataModel(CommonDataModel commonDataModel) {
        CommonData.commonDataModel = commonDataModel;
    }

    private static CommonDataModel commonDataModel;


}
