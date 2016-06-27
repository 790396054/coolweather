package com.hdty.app.coolweather.util;

import android.text.TextUtils;

import com.hdty.app.coolweather.db.CoolWeatherDB;
import com.hdty.app.coolweather.model.City;
import com.hdty.app.coolweather.model.County;
import com.hdty.app.coolweather.model.Province;

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
}
