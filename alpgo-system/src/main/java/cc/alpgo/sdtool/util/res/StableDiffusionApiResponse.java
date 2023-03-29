package cc.alpgo.sdtool.util.res;

import java.util.List;
import java.util.Map;

public class StableDiffusionApiResponse {
    private List<Object> data;
    private boolean is_generating;
    private double duration;
    private double average_duration;

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

