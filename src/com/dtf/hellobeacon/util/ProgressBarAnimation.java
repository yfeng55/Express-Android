package com.dtf.hellobeacon.util;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;


public class ProgressBarAnimation extends Animation{
    private ProgressBar progressBar;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

    
    public static ProgressBarAnimation setAnimation(ProgressBar b, int from, int to){
    	
    	ProgressBarAnimation anim = new ProgressBarAnimation(b, from, to);
    	anim.setDuration(1000);
    	    	
    	return anim;
    }
    
    
    
}