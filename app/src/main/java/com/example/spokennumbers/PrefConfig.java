package com.example.spokennumbers;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String NIGHT_MODE = "is_night_mode";
    public static final String DELAYTIME = "delayTime";
    public static final String INCTIME = "incTime";
    public static final String ISFEMALE = "isFemale";
    public static final String ISDECIMAL = "isDecimal";
    public static final String EVALMODE = "isEvalMode";
    public static final String HIGHSCORE = "highScore";

    public PrefConfig(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void saveData(String delayTimeText, String incTimeText, boolean femaleChecked, boolean decimalChecked, boolean isEvalMode, boolean isNightMode){
        editor = sharedPreferences.edit();
        editor.putString(DELAYTIME, delayTimeText);
        editor.putString(INCTIME, incTimeText);
        editor.putBoolean(ISFEMALE, femaleChecked);
        editor.putBoolean(ISDECIMAL, decimalChecked);
        editor.putBoolean(EVALMODE, isEvalMode);
        editor.putBoolean(NIGHT_MODE, isNightMode);
        editor.apply();
    }

    public String loadDataDelayTime(){
        return (String)sharedPreferences.getString(DELAYTIME, "1.00");
    }

    public String loadDataIncTime(){
        return (String)sharedPreferences.getString(INCTIME, "0.25");
    }

    public boolean loadDataFemaleChecked(){
        return (boolean) sharedPreferences.getBoolean(ISFEMALE, true);
    }

    public boolean loadDataDecimalChecked(){
        return (boolean)sharedPreferences.getBoolean(ISDECIMAL, true);
    }

    public boolean loadEvalModeChecked(){
        return (boolean)sharedPreferences.getBoolean(EVALMODE, true);
    }

    public void saveHighScore(int newScore){
        editor = sharedPreferences.edit();
        int oldHighScore = loadHighScore();
        if(newScore > oldHighScore){
            editor.putInt(HIGHSCORE, newScore);
            editor.apply();
        }
    }

    public int loadHighScore(){
        return (int)sharedPreferences.getInt(HIGHSCORE, 0);
    }

    public boolean loadNightModeChecked(){
        return (boolean)sharedPreferences.getBoolean(NIGHT_MODE, false);
    }
    public void saveNightModeChecked(boolean isNightMode){
        editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE, isNightMode);
        editor.apply();
    }
}
