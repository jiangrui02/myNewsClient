package com.jr.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityContainer {

	public static List<Activity> mActivities = new ArrayList<Activity>();
	public static void add(Activity activity)
	{
		mActivities.add(activity);
	}
	public static void finish() {
		// TODO Auto-generated method stub
      for(int i=mActivities.size()-1;i>=0;i--)
    	  {mActivities.get(i).finish();
          mActivities.remove(i);
    	  }

	}
}
