package d.pintoptech.liftnetwork.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CHARMING on 7/13/2018.
 */

public class UserInfo {
    private static final String TAG = UserInfo.class.getSimpleName();
    private static final String PREF_NAME = "login_data";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DOB = "dob";
    private static final String KEY_PASSPORT = "passport";

    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_HELPED = "helped";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LOG_TYPE = "log_type";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_PASSPORT_TWO = "passport_two";
    private static final String KEY_PASSPORT_THREE = "passport_three";
    private static final String KEY_PASS_ONE = "id1";
    private static final String KEY_PASS_TWO = "id2";
    private static final String KEY_PASS_THREE = "id3";
    private static final String KEY_HELP_WITH = "help_with";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_OCCUPATION = "occupation";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfo(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL,email);
        editor.apply();
    }

    public void setPhone(String phone){
        editor.putString(KEY_PHONE,phone);
        editor.apply();
    }

    public void setName(String name){
        editor.putString(KEY_NAME,name);
        editor.apply();
    }

    public void setType(String type){
        editor.putString(KEY_TYPE,type);
        editor.apply();
    }

    public void setDob(String dob){
        editor.putString(KEY_DOB,dob);
        editor.apply();
    }

    public void setPassport(String passport){
        editor.putString(KEY_PASSPORT,passport);
        editor.apply();
    }

    public void setKeyAddress(String address){
        editor.putString(KEY_ADDRESS,address);
        editor.apply();
    }

    public void setKeyCity(String city){
        editor.putString(KEY_CITY,city);
        editor.apply();
    }

    public void setKeyState(String state){
        editor.putString(KEY_STATE,state);
        editor.apply();
    }

    public void setKeyCountry(String country){
        editor.putString(KEY_COUNTRY,country);
        editor.apply();
    }

    public void setKeyCreatedAt(String createdAt){
        editor.putString(KEY_CREATED_AT,createdAt);
        editor.apply();
    }

    public void setKeyHelped(String helped){
        editor.putString(KEY_HELPED,helped);
        editor.apply();
    }

    public void setKeyStatus(String status){
        editor.putString(KEY_STATUS,status);
        editor.apply();
    }

    public void setKeyAbout(String about){
        editor.putString(KEY_ABOUT,about);
        editor.apply();
    }

    public void setKeyPassportTwo(String passportTwo){
        editor.putString(KEY_PASSPORT_TWO,passportTwo);
        editor.apply();
    }

    public void setKeyPassportThree(String passportThree){
        editor.putString(KEY_PASSPORT_THREE,passportThree);
        editor.apply();
    }

    public void setKeyLogType(String logType){
        editor.putString(KEY_LOG_TYPE,logType);
        editor.apply();
    }

    public void setKeyPassOne(String passOne){
        editor.putString(KEY_PASS_ONE,passOne);
        editor.apply();
    }

    public void setKeyPassTwo(String passTwo){
        editor.putString(KEY_PASS_TWO,passTwo);
        editor.apply();
    }

    public void setKeyHelpWith(String helpWith){
        editor.putString(KEY_HELP_WITH,helpWith);
        editor.apply();
    }

    public void setKeyGender(String gender){
        editor.putString(KEY_GENDER,gender);
        editor.apply();
    }

    public void setKeyCategory(String category){
        editor.putString(KEY_CATEGORY,category);
        editor.apply();
    }

    public void setKeyPassThree(String passThree){
        editor.putString(KEY_PASS_THREE,passThree);
        editor.apply();
    }

    public void setKeyOccupation(String occupation){
        editor.putString(KEY_OCCUPATION,occupation);
        editor.apply();
    }

    public  String getKeyEmail(){
        return prefs.getString(KEY_EMAIL, "");
    }

    public  String getKeyPhone(){
        return prefs.getString(KEY_PHONE, "");
    }

    public  String getKeyName(){
        return prefs.getString(KEY_NAME, "");
    }

    public  String getKeyType(){
        return prefs.getString(KEY_TYPE, "");
    }

    public  String getKeyDob(){
        return prefs.getString(KEY_DOB, "");
    }

    public  String getKeyPassport(){
        return prefs.getString(KEY_PASSPORT, "");
    }

    public  String getKeyAddress(){
        return prefs.getString(KEY_ADDRESS, "");
    }

    public  String getKeyCity(){
        return prefs.getString(KEY_CITY, "");
    }

    public  String getKeyState(){
        return prefs.getString(KEY_STATE, "");
    }

    public  String getKeyCountry(){
        return prefs.getString(KEY_COUNTRY, "");
    }

    public  String getKeyCreatedAt(){
        return prefs.getString(KEY_CREATED_AT, "");
    }

    public  String getKeyHelped(){
        return prefs.getString(KEY_HELPED, "");
    }

    public  String getKeyStatus(){
        return prefs.getString(KEY_STATUS, "");
    }

    public  String getKeyLogType(){
        return prefs.getString(KEY_LOG_TYPE, "");
    }

    public  String getKeyAbout(){
        return prefs.getString(KEY_ABOUT, "");
    }

    public  String getKeyPassportTwo(){
        return prefs.getString(KEY_PASSPORT_TWO, "");
    }

    public  String getKeyPassportThree(){
        return prefs.getString(KEY_PASSPORT_THREE, "");
    }

    public  String getKeyPassOne(){
        return prefs.getString(KEY_PASS_ONE, "");
    }

    public  String getKeyPassTwo(){
        return prefs.getString(KEY_PASS_TWO, "");
    }

    public  String getKeyPassThree(){
        return prefs.getString(KEY_PASS_THREE, "");
    }

    public  String getKeyHelpWith(){
        return prefs.getString(KEY_HELP_WITH, "");
    }

    public  String getKeyGender(){
        return prefs.getString(KEY_GENDER, "");
    }

    public  String getKeyCategory(){
        return prefs.getString(KEY_CATEGORY, "");
    }

    public  String getKeyOccupation(){
        return prefs.getString(KEY_OCCUPATION, "");
    }


    public void clearData(){
        editor.clear();
        editor.commit();
    }

}
