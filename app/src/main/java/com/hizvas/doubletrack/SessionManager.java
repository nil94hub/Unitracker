package com.hizvas.doubletrack;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared Preferences
    static SharedPreferences pref;

    // Editor for Shared preferences
    //SharedPreferences.Editor editor;

    // Context
    //Context _context;

    // Shared pref mode
    //int PRIVATE_MODE = 0;

    static SessionManager app;

    // Sharedpref file name
    private static final String PREF_NAME = "unitracker";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String Mobile = "mobile";
    public static final String User = "user";
    public static final String Et_mobile = "et_mobile";
    public static final String Et_user = "et_user";
    public static final String KEY_VEHICLE = "vehicle";
    public static final String GCM_TOKEN = "gcm_token";


    // Email address (make variable public to access from outside)

    public static final String KEY_EMAIL = "email";
    public static final String FARE_UNIT = "unit";
    public static final String COST = "cost";
    public static final String LOGIN_AS = "login_as";
    public static final String USER_ID = "user_id";
    public static final String Origin_lat = "origin_lat";
    public static final String Origin_long = "origin_long";
    public static final String Destination_lat = "destination_lat";
    public static final String Destination_long = "destination_long";
    public static String KEY = "key";
    public static final String USER = "user";
    public static final String NotStatus = "notstatus";
    public static final String VTYPE = "vtype";
    public static final String Date = "date";
    public static final String Time = "time";
    public static final String VEH_STATS = "vehicle status";
    public static final String FARE = "fare";
    public static final String RIDE_ID = "ride_id";
    public static final String DRIVER_ID = "driver_id";
    public static final String RIDE_SELECTION = "ride_selection";

    public static final String LOGIN_EMAIL = "login_email";
    public static final String LOGIN_NAME = "login_name";
    public static final String TOKENID = "tokenid";




    public static final String FROM = "from";

    public static String getTO(String F) {
        return TO;
    }

    public static final String TO = "to";

    public static final String RIDESTATUS = "ridestatus";

    public static final String RIDEDURATION = "rideduration";
    public static final String RIDEPRIO= "rideprio";
    public static final String DUEPAYMENT= "duepayment";
    public static final String DISTANCE= "DISTANCE";
    public static final String PICKUPLOCATION= "pickuplocation";
    public static final String DROPLOCATION= "droplocation";
    public static final String DUE= "due";
    public static final String CURRENTRIDE_FARE= "currentride_fare";
    public static final String TEMPID= "temp_id";
    public static final String TOTALFARE= "totalfare";


    // vehicle info variables

    public static final String lsDate = "lsDate";
    public static final String lsTime = "lsTime";
    public static final String YEAR = "year";
    public static final String COLOR = "color";

    public static final String DRivingLicence = "licence";
    public static final String VehicleInsurance = "insurance";
    public static final String VehicleNo = "no";
    public static final String VehiclePermit = "permit";
    public static final String VehicleRegistartion = "registration";
    private int name;
    private String avatar;
    private String email;


    public static void setGcmToken(String gcmToken) {

        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(GCM_TOKEN, gcmToken);
        prefsEditor.commit();
    }

    public SessionManager() {
    }

    public static void initialize(Context context) {
        if (pref == null)
            pref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }



    /*public static SessionManager getInstance(Context context) {
        if (app == null) {
            app = new SessionManager();

        }
        app.setrPref();
        return (app);
    }*/

    public String getGcmToken() {
        return pref.getString(GCM_TOKEN, null);
    }

    public static void setolat(String lat) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Origin_lat, lat);
        editor.commit();
    }

    public static String getolat() {

        return pref.getString(Origin_lat, null);
    }
    public static void setolong(String lng) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Origin_long, lng);
        editor.commit();
    }
    public static String getlsDate() {

        return pref.getString(lsDate, null);
    }
    public static void setlsDate(String date) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(lsDate, date);
        editor.commit();
    }
    public static String getlsTime() {

        return pref.getString(lsTime, null);
    }
    public static void setlsTime(String time) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(lsTime, time);
        editor.commit();
    }

    public static String getolong() {

        return pref.getString(Origin_long, null);
    }
    public static void setdlat(String lat) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Destination_lat, lat);
        editor.commit();
    }

    public static String getdlat() {

        return pref.getString(Destination_lat, null);
    }
    public static void setdlong(String lng) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Destination_long, lng);
        editor.commit();
    }

    public static String getdlong() {

        return pref.getString(Destination_long, null);
    }




    public static void setIsLogin() {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putBoolean(IS_LOGIN, true);
        prefsEditor.commit();

    }

    public static void setMobile(String mobile) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Mobile, mobile);
        prefsEditor.commit();
    }

    public static String getMobile() {
        return pref.getString(Mobile, null);
    }
    public static void setEt_user(String et_user) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Et_user, et_user);
        prefsEditor.commit();
    }

    public static String getEt_user() {
        return pref.getString(Et_user, null);
    }
    public static void setEt_mobile(String et_mobile) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Et_mobile, et_mobile);
        prefsEditor.commit();
    }

    public static String getEt_mobile() {
        return pref.getString(Et_mobile, null);
    }
    public static void setUser(String user) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(User, user);
        prefsEditor.commit();
    }


    public static String getUser() {
        return pref.getString(User, null);
    }
 /*   public SharedPreferences getPref() {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        return pref;
    }

    public SharedPreferences.Editor getDb() {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        return editor;
    }*/




    public static String getDistance() {

        return pref.getString(DISTANCE, null);
    }

    public static void setDistance(String distance) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(DISTANCE, distance);
        prefsEditor.commit();
    }

    public static String getUnit() {

        return pref.getString(FARE_UNIT, null);
    }

    public static String getNotStatus() {
        return pref.getString(NotStatus, null);
    }

    public static void setNotStatus(String notStatus) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(NotStatus,notStatus);
        prefsEditor.commit();
    }

    public static String getTokenid() {
        return pref.getString(TOKENID, null);
    }

    public static void setTokenid(String tokenid) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(TOKENID,tokenid);
        prefsEditor.commit();
    }

    public static String getPickuplocation() {
        return pref.getString(PICKUPLOCATION, null);
    }

    public static void setPickuplocation(String pickuplocation) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(PICKUPLOCATION,pickuplocation);
        prefsEditor.commit();
    }

    public static String getDroplocation() {
        return pref.getString(DROPLOCATION, null);
    }

    public static void setDroplocation(String droplocation) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(DROPLOCATION,droplocation);
        prefsEditor.commit();
    }

    public static String getTotalfare() {
        return pref.getString(TOTALFARE, null);
    }

    public static void setTotalfare(String totalfare) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(TOTALFARE,totalfare);
        prefsEditor.commit();
    }

    public static String getRideSelection() {
        return pref.getString(RIDE_SELECTION, null);
    }

    public static void setRideSelection(String rideSelection) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(RIDE_SELECTION,rideSelection);
        prefsEditor.commit();
    }

    public static String getDriverId() {
        return pref.getString(DRIVER_ID, null);
    }

    public static void setDriverId(String driverId) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(DRIVER_ID,driverId);
        prefsEditor.commit();
    }

    public static String getTempid() {
        return pref.getString(TEMPID, null);
    }

    public static void setTempid(String tempid) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(TEMPID,tempid);
        prefsEditor.commit();
    }

    public static String getDue() {
        return pref.getString(DUE, null);
    }

    public static void setDue(String due) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(DUE,due);
        prefsEditor.commit();
    }

    public static String getCurrentrideFare() {
        return pref.getString(CURRENTRIDE_FARE, null);
    }

    public static void setCurrentrideFare(String currentrideFare) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(CURRENTRIDE_FARE, currentrideFare);
        prefsEditor.commit();
    }

    public static String getRideprio() {
        return pref.getString(RIDEPRIO, null);
    }

    public static void setRideprio(String rideprio) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(RIDEPRIO,rideprio);
        prefsEditor.commit();
    }

    public static String getDuepayment() {
        return pref.getString(DUEPAYMENT, null);
    }

    public static void setDuepayment(String duepayment) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(DUEPAYMENT,duepayment);
        prefsEditor.commit();
    }

    public static String getRideduration() {
        return pref.getString(RIDEDURATION, null);
    }

    public static void setRideduration(String rideduration) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(RIDEDURATION,rideduration);
        prefsEditor.commit();
    }

    public static String getRideId() {
        return pref.getString(RIDE_ID, null);
    }

    public static void setRideId(String rideId) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(RIDE_ID,rideId);
        prefsEditor.commit();
    }


    public static String getDate() {
        return pref.getString(Date, null);
    }

    public static void setDate(String date) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Date,date);
        prefsEditor.commit();
    }

    public static String getTime() {
        return pref.getString(Time, null);
    }

    public static void setTime(String time) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(Time,time);
        prefsEditor.commit();
    }

    public static String getFROM() {
        return pref.getString(FROM, null);
    }

    public static void setFROM(String from) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(FROM, from);
        prefsEditor.commit();
    }


    public static String getTO() {
        return pref.getString(TO, null);
    }

    public static void setTO(String to) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(TO,to);
        prefsEditor.commit();
    }

    public static String getLoginEmail() {
        return pref.getString(LOGIN_EMAIL, null);
    }

    public static void setLoginEmail(String loginEmail) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(LOGIN_EMAIL,loginEmail);
        prefsEditor.commit();
    }

    public static String getLoginName() {
        return pref.getString(LOGIN_NAME, null);
    }

    public static void setLoginName(String loginName) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(LOGIN_NAME,loginName);
        prefsEditor.commit();
    }


    public static String getVtype() {
        return pref.getString(VTYPE, null);
    }

    public static void setVtype(String vtype) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(VTYPE,vtype);
        prefsEditor.commit();
    }

    public static String getFare() {
        return pref.getString(FARE, null);
    }

    public static void setFare(String fare) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(FARE,fare);
        prefsEditor.commit();
    }







    /*  *//**
     * Create login session
     *//*
    public void createLoginSession(String name, String email, String user, String avatar, String mobile, String vehicle) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_VEHICLE, vehicle);
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(USER_ID, user);
        editor.putString(AVATAR, avatar);


        // commit changes
        editor.commit();
    }

    public void setVehicleInfo(String brand, String model, String year, String color, String licence,
                               String insurance, String no, String permit, String registration) {

        editor.putString(BRAND, brand);
        editor.putString(MODEL, model);
        editor.putString(YEAR, year);
        editor.putString(COLOR, color);

        editor.putString(DRivingLicence, licence);
        editor.putString(VehicleInsurance, insurance);
        editor.putString(VehicleNo, no);
        editor.putString(VehiclePermit, permit);
        editor.putString(VehicleRegistartion, registration);
        editor.commit();
    }

    */

    /**
     * Check login method wil checkky user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin(Context _context) {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }




    /**
     * Clear session details
     */


    public static void logoutUser(Context _context) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putBoolean(IS_LOGIN, false);
        prefsEditor.clear();
        prefsEditor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }

    /**
     * Quick checkky for login
     **/
    // Get Login State
    public static boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }




}
