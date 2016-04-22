package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Prediction;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.meweatherDB;
import model.City;
import model.County;
import model.Province;

public class Utility {
	
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(
			meweatherDB meweatherDB,String response){
		if (!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0){
				for (String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// ���������������ݴ��浽Province��
					meweatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��м�����
	 */
	public static boolean handleCitiesResponse(
			meweatherDB meweatherDB,String response, int provinceId){
		if (!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//���������������ݴ��浽City��
					meweatherDB.saveCity(city);
				}
				return true;
				
			}
		}
		return false;
	}
	
	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	
	public static boolean handleCountiesResponse(
			meweatherDB meweatherDB,String response, int cityId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0){
				for (String c : allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county .setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//���������������ݴ��浽County��
					meweatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �������������ص�JSON����,���������������ݴ��浽����
	 */
	public static void handleWeatherResponse(Context context, String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveweatherInfo(context, cityName, weatherCode,
					temp1, temp2, weatherDesp,publishTime);
		} catch (JSONException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ������������������������Ϣ���浽SharePreferences�ļ���
	 */
	public static void saveweatherInfo(Context context, String cityName,
			String weatherCode, String temp1,String temp2, String weatherDesp,String
			publishTime){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��",
							Locale.CHINA);
					SharedPreferences.Editor editor = PreferenceManager
							.getDefaultSharedPreferences(context).edit();
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
