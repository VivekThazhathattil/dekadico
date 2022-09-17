package com.example.spokennumbers;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";

    /* Spoken numbers module */
    private static final String NIGHT_MODE = "is_night_mode";
    public static final String DELAYTIME = "delayTime";
    public static final String INCTIME = "incTime";
    public static final String ISFEMALE = "isFemale";
    public static final String ISDECIMAL = "isDecimal";
    public static final String EVALMODE = "isEvalMode";
    public static final String HIGHSCORE = "highScore";

    /* Flash Anzan module */
    public static final String FLASH_ANZAN_NUM_ROWS = "flashAnzanNumRows";
    public static final String FLASH_ANZAN_NUM_DIGITS = "flashAnzanNumDigits";
    public static final String FLASH_ANZAN_NUM_TIMEOUT = "flashAnzanNumTimeout";
    public static final String FLASH_ANZAN_NUM_FLASH = "flashAnzanNumFlash";
    public static final String FLASH_ANZAN_SUBTRACTIONS = "flashAnzanSubtractions";
    public static final String FLASH_ANZAN_SPEECH_SYNTHESIS = "flashAnzanSpeechSynthesis";
    public static final String FLASH_ANZAN_CONTINUOUS_MODE = "flashAnzanContinuousMode";

    public static final String FLASH_ANZAN_NUM_ROWS_DEFAULT = "5";
    public static final String FLASH_ANZAN_NUM_DIGITS_DEFAULT = "3";
    public static final String FLASH_ANZAN_NUM_TIMEOUT_DEFAULT = "1000";
    public static final String FLASH_ANZAN_NUM_FLASH_DEFAULT = "1000";
    public static final boolean FLASH_ANZAN_SUBTRACTIONS_DEFAULT = false;
    public static final boolean FLASH_ANZAN_SPEECH_SYNTHESIS_DEFAULT = false;
    public static final boolean FLASH_ANZAN_CONTINUOUS_MODE_DEFAULT = false;

    public PrefConfig(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void saveData(String delayTimeText, String incTimeText, boolean femaleChecked,
                         boolean decimalChecked, boolean isEvalMode, boolean isNightMode){
        editor = sharedPreferences.edit();
            editor.putString(DELAYTIME, delayTimeText);
            editor.putString(INCTIME, incTimeText);
            editor.putBoolean(ISFEMALE, femaleChecked);
            editor.putBoolean(ISDECIMAL, decimalChecked);
            editor.putBoolean(EVALMODE, isEvalMode);
            editor.putBoolean(NIGHT_MODE, isNightMode);
        editor.apply();
    }

    public void saveDataFlashAnzan(String flashAnzanNumRows, String flashAnzanNumDigits,
                         String flashAnzanNumTimeout, String flashAnzanNumFlash,
                         boolean flashAnzanSubtractions, boolean flashAnzanSpeechSynthesis,
                         boolean flashAnzanContinuousMode){

        editor = sharedPreferences.edit();
            editor.putString(FLASH_ANZAN_NUM_ROWS, flashAnzanNumRows);
            editor.putString(FLASH_ANZAN_NUM_DIGITS, flashAnzanNumDigits);
            editor.putString(FLASH_ANZAN_NUM_TIMEOUT, flashAnzanNumTimeout);
            editor.putString(FLASH_ANZAN_NUM_FLASH, flashAnzanNumFlash);
            editor.putBoolean(FLASH_ANZAN_CONTINUOUS_MODE, flashAnzanContinuousMode);
            editor.putBoolean(FLASH_ANZAN_SPEECH_SYNTHESIS, flashAnzanSpeechSynthesis);
            editor.putBoolean(FLASH_ANZAN_SUBTRACTIONS, flashAnzanSubtractions);
        editor.apply();
    }

    public String loadFlashAnzanDataString(String requestedData){
        switch(requestedData){
            case FLASH_ANZAN_NUM_ROWS:
                return (String)sharedPreferences.getString(FLASH_ANZAN_NUM_ROWS,
                        FLASH_ANZAN_NUM_ROWS_DEFAULT);
            case FLASH_ANZAN_NUM_DIGITS:
                return (String)sharedPreferences.getString(FLASH_ANZAN_NUM_DIGITS,
                        FLASH_ANZAN_NUM_DIGITS_DEFAULT);
            case FLASH_ANZAN_NUM_TIMEOUT:
                return (String)sharedPreferences.getString(FLASH_ANZAN_NUM_TIMEOUT,
                        FLASH_ANZAN_NUM_TIMEOUT_DEFAULT);
            case FLASH_ANZAN_NUM_FLASH:
                return (String)sharedPreferences.getString(FLASH_ANZAN_NUM_FLASH,
                        FLASH_ANZAN_NUM_FLASH_DEFAULT);
        }
        return "";
    }

    public boolean loadFlashAnzanDataBoolean(String requestedData){
        switch(requestedData){
            case FLASH_ANZAN_SUBTRACTIONS:
                return (boolean)sharedPreferences.getBoolean(FLASH_ANZAN_SUBTRACTIONS, FLASH_ANZAN_SUBTRACTIONS_DEFAULT);
            case FLASH_ANZAN_SPEECH_SYNTHESIS:
                return (boolean)sharedPreferences.getBoolean(FLASH_ANZAN_SPEECH_SYNTHESIS, FLASH_ANZAN_SPEECH_SYNTHESIS_DEFAULT);
            case FLASH_ANZAN_CONTINUOUS_MODE:
                return (boolean)sharedPreferences.getBoolean(FLASH_ANZAN_CONTINUOUS_MODE, FLASH_ANZAN_CONTINUOUS_MODE_DEFAULT);
        }
        return false;
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
