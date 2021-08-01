package com.coremacasia.learnat.commons.offlineData;

public class OfflineRepo {
    private static final String TAG = "OfflineRepo";

    private OnTaskComplete onTaskComplete;

    public OfflineRepo(OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }
    public void getData(){
        onTaskComplete.data(xxHelper.getXtraHelper());
    }

    public interface OnTaskComplete{
        void data(XtraHelper helper);
        void error(Exception e);
    }
}
