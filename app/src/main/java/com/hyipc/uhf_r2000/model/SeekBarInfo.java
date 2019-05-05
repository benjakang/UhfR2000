package com.hyipc.uhf_r2000.model;

import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by ting on 2017/9/27.
 */

public class SeekBarInfo {

    private SeekBar seekBar;
    private TextView tvProgress;
    private TextView tvMin;
    private TextView tvMax;
    private String[] values;
    private String currValue;

    public void update() {
        tvProgress.setText(currValue);
        tvMin.setText(values[0]);
        tvMax.setText(values[values.length - 1]);

        for (int i = 0; i < values.length; i++) {
            if (currValue.equalsIgnoreCase(values[i])) {
                int progress = i;
                seekBar.setProgress(progress);
            }
        }
    }

    public void onProgressChanged(int progress) {
        int index = progress;
        setCurrValue(values[index]);
        update();
    }

    public SeekBarInfo() {
        super();
    }

    public SeekBarInfo(SeekBar seekBar, TextView tvProgress, TextView tvMin, TextView tvMax, String[] values, String currValue) {
        super();
        this.seekBar = seekBar;
        this.tvProgress = tvProgress;
        this.tvMin = tvMin;
        this.tvMax = tvMax;
        this.values = values;
        this.currValue = currValue;
        this.seekBar.setMax(values.length - 1);
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public TextView getTvProgress() {
        return tvProgress;
    }

    public void setTvProgress(TextView tvProgress) {
        this.tvProgress = tvProgress;
    }

    public TextView getTvMin() {
        return tvMin;
    }

    public void setTvMin(TextView tvMin) {
        this.tvMin = tvMin;
    }

    public TextView getTvMax() {
        return tvMax;
    }

    public void setTvMax(TextView tvMax) {
        this.tvMax = tvMax;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
        this.seekBar.setMax(values.length - 1);
    }

    public String getCurrValue() {
        return currValue;
    }

    public void setCurrValue(String currValue) {
        this.currValue = currValue;
    }

}
