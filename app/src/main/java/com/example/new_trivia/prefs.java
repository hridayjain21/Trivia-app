package com.example.new_trivia;

import android.app.Activity;
import android.content.SharedPreferences;

public class prefs {
    private String HIGHEST_SCORE = "highest_score";
    private SharedPreferences preferences;

    public prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }
     public void save_highest_score(int score){
        int current = score;
        int highest = preferences.getInt(HIGHEST_SCORE,0);
        if(current>highest){
            preferences.edit().putInt(HIGHEST_SCORE,current).apply();
        }
     }
     public int get_highest_score(){
        return preferences.getInt(HIGHEST_SCORE,0);
     }
}
