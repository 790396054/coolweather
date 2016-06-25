package com.hdty.app.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hdty.app.coolweather.model.City;
import com.hdty.app.coolweather.model.County;
import com.hdty.app.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     */
    private static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    private static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     * @param context
     */
   private CoolWeatherDB(Context context){
       CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
       dbHelper.getWritableDatabase();
   }

    /**
     * 提供共有的获取实例的方法
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将 Province 添加到数据库表中
     * @param province
     */
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert(DB_NAME,null,values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息。
     * @return list
     */
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        cursor.close();//关闭 cursor
        return list;
    }

    /**
     * 将 City 添加到数据库中
     * @param city
     */
    public void saveCity(City city){
        ContentValues values = new ContentValues();
        values.put("city_code",city.getCityCode());
        values.put("city_name",city.getCityName());
        values.put("province_id",city.getProvinceId());
        db.insert(DB_NAME,null,values);
    }

    /**
     * 从数据库读取某省下所有的城市信息。
     * @return
     */
    public List<City> loadCity(){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            city.setProvinceId(cursor.getShort(cursor.getColumnIndex("province_id")));
            list.add(city);
        }while (cursor.moveToNext());
        return list;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[] { String.valueOf(cityId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
