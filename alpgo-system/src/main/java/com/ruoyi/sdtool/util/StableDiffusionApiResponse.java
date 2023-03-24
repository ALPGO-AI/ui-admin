package com.alpgo.sdtool.util;


import java.util.List;

public class StableDiffusionApiResponse {
    private List<Object> data;
    private boolean is_generating;
    private double duration;
    private double average_duration;

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public boolean isIs_generating() {
        return is_generating;
    }

    public void setIs_generating(boolean is_generating) {
        this.is_generating = is_generating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getAverage_duration() {
        return average_duration;
    }

    public void setAverage_duration(double average_duration) {
        this.average_duration = average_duration;
    }
}
