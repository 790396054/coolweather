package com.hdty.app.coolweather.util;

import android.content.Context;
import java.text.SimpleDateFormat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.hdty.app.coolweather.db.CoolWeatherDB;
import com.hdty.app.coolweather.model.City;
import com.hdty.app.coolweather.model.County;
import com.hdty.app.coolweather.model.Province;

import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/6/25.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param db
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB db,String response){
        if(!TextUtils.isEmpty(response)){
            String [] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces){
                    String [] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到数据库中
                    db.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * @param db
     * @param response
     * @param provinceId
     * @return
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB db,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String [] allCities = response.split(",");
            if(allCities != null && allCities.length > 0){
                for(String c : allCities){
                    String [] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    db.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * @param db
     * @param response
     * @param cityId
     * @return
     */
    public synchronized static boolean handleCountiesResponse(CoolWeatherDB db,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String [] counties = response.split(",");
            if(counties != null && counties.length > 0){
                for (String c : counties){
                    String [] array = c.split("\\|");
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    db.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的 JSON 数据，并将解析出来的结果存到本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context,String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,weatherDesp, publishTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中.
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,
                              String weatherDesp, String publishTime){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }


}
