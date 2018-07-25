package com.androiddev.myweatherapplication;


public class WeatherApiClient {

    private static final String TAG = WeatherApiClient.class.getSimpleName();
    private static final String WEATHER_API_BASE_URL = "http://api.openweathermap.org/data/" +
            "2.5/weather?units=metric&id=";
    private static final String FORECAST_API_BASE_URL = "http://api.openweathermap.org/data/" +
            "2.5/forecast?units=metric&id=";
    private static final String OWM_API_KEY = "1965d874c7bd7b95af8df41b4fa5ef0f";

    public static String GetForecastWeatherUrl(String relativeUrl){
        return WEATHER_API_BASE_URL + relativeUrl;
    }
    public static String GetCurrWeatherUrl(String relativeUrl){
        return WEATHER_API_BASE_URL + relativeUrl;
    }


    public static String getOwmApiKey() {
        return OWM_API_KEY;
    }
}
