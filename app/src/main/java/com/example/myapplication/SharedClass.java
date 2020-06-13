package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class SharedClass {
    private static final String PREFERENCES_NAME = "루틴 리스트";
    private static final String PREFERENCES_NAME2 = "하루일과 리스트";
    private static final String PREFERENCES_NAME3 = "통계 기록 데이터";


    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getPref(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME2, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getPrefStatistic(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME3, Context.MODE_PRIVATE);
    }

    // 루틴 리스트 저장 메서드
    public static void saveRoutine(Context context, ArrayList<RoutinePlanItem> list, String key){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(list);
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    // 루틴 리스트 불러오는 메서드
    public static ArrayList<RoutinePlanItem> loadRoutine(Context context, String key){

        SharedPreferences sharedPreferences = getPreferences(context);
        String json = sharedPreferences.getString(key, "");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Type type = new TypeToken<ArrayList<RoutinePlanItem>>(){}.getType();
        ArrayList<RoutinePlanItem> list = gson.fromJson(json, type);

        if(list == null){
            list = new ArrayList<>();
        }

        return list;
    }

    // 날짜 저장하고 불러오는 메서드
    public static void savePickedDate(Context context, String PickedDate, String key){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, PickedDate);
        editor.apply();
    }

    public static void savePickedDay(Context context, String PickedDay, String key){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, PickedDay);
        editor.apply();
    }

    public static String loadPickedDate(Context context, String key) {
        SharedPreferences sharedPreferences = getPreferences(context);
        String pickedDate = sharedPreferences.getString(key, null);

        if(pickedDate == null){
            pickedDate = "";
        }
        return pickedDate;
    }

    public static String loadPickedDay(Context context, String key) {
        SharedPreferences sharedPreferences = getPreferences(context);
        String pickedDay = sharedPreferences.getString(key, null);
        if(pickedDay == null){
            pickedDay = "";
        }
        return pickedDay;
    }

//    // 해당 날짜의 routine, plan 데이터 저장 메서드. (key는 해당 날짜)
//    public static void saveOneDayRoutine(Context context, ArrayList<RoutinePlanItem> list, int key){
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String json = gson.toJson(list);
//        SharedPreferences sharedPreferences = getPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(String.valueOf(key), json);
//        editor.apply();
//    }

    public static void saveOneDayPlan(Context context, ArrayList<RoutinePlanItem> list, int key){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(list);
        SharedPreferences sharedPreferences = getPref(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(key), json);
        editor.apply();
    }


    public static ArrayList<RoutinePlanItem> loadOneDayPlan(Context context, int key){

        SharedPreferences sharedPreferences = getPref(context);
        String json = sharedPreferences.getString(String.valueOf(key), "");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Type type = new TypeToken<ArrayList<RoutinePlanItem>>(){}.getType();
        ArrayList<RoutinePlanItem> list = gson.fromJson(json, type);
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }


    public static void removeAllData(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveIntDate(Context context, int DateInt, String key){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, DateInt);
        editor.apply();
    }

    public static int loadIntDay(Context context, String key) {
        SharedPreferences sharedPreferences = getPreferences(context);
        int DateInt = sharedPreferences.getInt(key, 0);

        return DateInt;
    }

    public static void saveIPTPlan(Context context, ArrayList<RoutinePlanItem> list){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(list);
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ipt plan", json);
        editor.apply();
    }

    public static ArrayList<RoutinePlanItem> loadIPTPlan(Context context){

        SharedPreferences sharedPreferences = getPreferences(context);
        String json = sharedPreferences.getString("ipt plan", "");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Type type = new TypeToken<ArrayList<RoutinePlanItem>>(){}.getType();
        ArrayList<RoutinePlanItem> list = gson.fromJson(json, type);

        if(list == null){
            list = new ArrayList<>();
        }

        return list;
    }

    public static ArrayList<String> getAll(Context context){
        SharedPreferences sharedPreferences = getPref(context);
        Map<String,?> keys = sharedPreferences.getAll();
        ArrayList<String> keyArray = new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            keyArray.add(entry.getKey());
//            Log.e("하루일과 리스트", entry.getKey());
        }


        return keyArray;
    }

    /**
     *
     * TimeRecord 액티비티에 보여지는 plan List
     *
     */
    public static void saveStatisticData(Context context, ArrayList<RoutinePlanItem> list, int key){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(list);
        SharedPreferences sharedPreferences = getPrefStatistic(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(key), json);
        editor.apply();
    }


    public static ArrayList<RoutinePlanItem> loadStatisticData(Context context, int key){

        SharedPreferences sharedPreferences = getPrefStatistic(context);
        String json = sharedPreferences.getString(String.valueOf(key), "");
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Type type = new TypeToken<ArrayList<RoutinePlanItem>>(){}.getType();
        ArrayList<RoutinePlanItem> list = gson.fromJson(json, type);
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }


    public static ArrayList<String> getStatisticDataAll(Context context){
        SharedPreferences sharedPreferences = getPrefStatistic(context);
        Map<String,?> keys = sharedPreferences.getAll();
        ArrayList<String> keyArray = new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            keyArray.add(entry.getKey());
//            Log.e("하루일과 리스트", entry.getKey());
        }


        return keyArray;
    }
}
