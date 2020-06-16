package d.pintoptech.liftnetwork.sponsor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.ChatFragment;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.ui.Home;
import d.pintoptech.liftnetwork.sponsor.ui.LikeFragment;
import d.pintoptech.liftnetwork.sponsor.ui.ProfileFragment;
import d.pintoptech.liftnetwork.sponsor.ui.ViewUser;
import io.customerly.Customerly;

public class MainPageSponsor extends AppCompatActivity {

    private UserInfo userInfo;
    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;
    private String did = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new Home()).addToBackStack("landing").commit();
                    return true;
                case R.id.navigation_likes:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new LikeFragment()).addToBackStack("landing").commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).addToBackStack("landing").commit();
                    return true;
                case R.id.navigation_chat:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ChatFragment()).addToBackStack("landing").commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_sponsor2);

        userInfo = new UserInfo(this);
        login(userInfo.getKeyEmail());

        Intent intent = getIntent();
        did = intent.getStringExtra("id");


        fragmentManager = getSupportFragmentManager();

        try {
            if(!did.isEmpty()){
                String AVATAR1 = intent.getStringExtra("image_1");
                String AVATAR2 = intent.getStringExtra("image_2");
                String AVATAR3 = intent.getStringExtra("image_3");
                String CATEGORY = intent.getStringExtra("category");
                String PHONES = intent.getStringExtra("phone");
                String NAMES = intent.getStringExtra("name");
                String LOCATIONS = intent.getStringExtra("location");
                String AGES = intent.getStringExtra("age");
                String FROM = intent.getStringExtra("from");
                String EMAILS = intent.getStringExtra("email");
                String STATUS = intent.getStringExtra("status");
                String HELP_WITHS = intent.getStringExtra("help_with");

                String ABOUTS = intent.getStringExtra("about");
                String HELPING_WITHS = intent.getStringExtra("helping_with");
                String GENDERS = intent.getStringExtra("gender");
                String OCCUPATIONS = intent.getStringExtra("occupation");
                ViewUser requestFragment = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("image_1", AVATAR1);
                bundle.putString("image_2", AVATAR2);
                bundle.putString("image_3", AVATAR3);
                bundle.putString("name", NAMES);
                bundle.putString("location", LOCATIONS);
                bundle.putString("age", AGES);
                bundle.putString("about", ABOUTS);
                bundle.putString("email", EMAILS);
                bundle.putString("phone", PHONES);
                bundle.putString("help_with", HELP_WITHS);
                bundle.putString("gender", GENDERS);
                bundle.putString("category", CATEGORY);
                bundle.putString("occupation", OCCUPATIONS);
                bundle.putString("helping_with", HELPING_WITHS);
                bundle.putString("from", FROM);
                requestFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
            }else {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Home(), "main").addToBackStack("landing").commit();
            }
        }catch (NullPointerException e){
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Home(), "main").addToBackStack("landing").commit();
        }

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Customerly.registerUser(
                userInfo.getKeyEmail(),
                //"12345",                //OPTIONALLY you can pass the user ID or null
                userInfo.getKeyName()               //OPTIONALLY you can pass the user name or null
                //attributesMap,          //OPTIONALLY you can pass some custom attributes or null (See the *Attributes* section below for the map building)
                //companyMap,             //OPTIONALLY you can pass the user company informations or null (See the *Companies* section below for the map building)
        );

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
    }
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void login(final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(!error){
                                String name = resp.getString("name");
                                String type = resp.getString("type");
                                String dob = resp.getString("dob");
                                String phone = resp.getString("phone");

                                String address = resp.getString("address");
                                String city = resp.getString("city");
                                String state = resp.getString("state");
                                String country = resp.getString("country");
                                String about = resp.getString("about");
                                String status = resp.getString("status");

                                userInfo.setEmail(email);
                                userInfo.setName(name);
                                userInfo.setDob(dob);
                                userInfo.setPhone(phone);
                                userInfo.setType(type);
                                userInfo.setKeyAddress(address);
                                userInfo.setKeyCity(city);
                                userInfo.setKeyState(state);
                                userInfo.setKeyCountry(country);
                                userInfo.setKeyAbout(about);
                                userInfo.setKeyStatus(status);


                                JSONArray passports = resp.getJSONArray("passports");
                                // passport 1
                                JSONObject passport1 = passports.getJSONObject(2);
                                userInfo.setKeyPassOne(passport1.getString("id"));
                                userInfo.setPassport("https://liftnigeria.ng/mobile_app/Profile/"+passport1.getString("image"));
                                // passport 2
                                JSONObject passport2 = passports.getJSONObject(1);
                                userInfo.setKeyPassTwo(passport2.getString("id"));
                                userInfo.setKeyPassportTwo("https://liftnigeria.ng/mobile_app/Profile/"+passport2.getString("image"));
                                // passport 3
                                JSONObject passport3 = passports.getJSONObject(0);
                                userInfo.setKeyPassThree(passport3.getString("id"));
                                userInfo.setKeyPassportThree("https://liftnigeria.ng/mobile_app/Profile/"+passport3.getString("image"));

                                Customerly.registerUser(
                                        userInfo.getKeyEmail(),
                                        //"12345",                //OPTIONALLY you can pass the user ID or null
                                        userInfo.getKeyName());//,               //OPTIONALLY you can pass the user name or null
                                //attributesMap,          //OPTIONALLY you can pass some custom attributes or null (See the *Attributes* section below for the map building)
                                //companyMap,             //OPTIONALLY you can pass the user company informations or null (See the *Companies* section below for the map building)
                                        /*new Callback() {        //OPTIONALLY you can pass a callback to be notified of the success of the task or null
                                            @Override
                                            public Unit invoke() {
                                                //Called if the task completes successfully
                                                return null;
                                            }
                                        },
                                        new Callback() {        //OPTIONALLY you can pass a callback to be notified of the failure of the task or null
                                            @Override
                                            public Unit invoke() {
                                                //Called if the task fails
                                                return null;
                                            }
                                        });*/


                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.getMessage());
                        Toast.makeText(MainPageSponsor.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("fcm", FirebaseInstanceId.getInstance().getToken());

                return params;
            }
        };

        VolleySingleton.getInstance(MainPageSponsor.this).addToRequestQueue(stringRequest);
    }

}
