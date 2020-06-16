package d.pintoptech.liftnetwork.beneficiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import d.pintoptech.liftnetwork.AuthActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.RegisterActivity;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView BACK, DONE;
    private ImageView IMAGEONE,IMAGETWO,IMAGETHREE;
    private EditText NAME, DATE, MONTH, YEAR, PHONE;
    private TextView EMAIL, GENDERTEXT;
    private EditText ABOUT, ADDR, CITY, STATE, COUNTRY, HELPWITH;
    private Spinner CATEGORY, GENDER, OCCUPATION;

    private UserInfo userInfo;
    private String selected = "";
    private String up1 = "";
    private String up2 = "";
    private String up3 = "";
    private ProgressDialog progressDialog;
    private String gender = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindData();
        setData();
        setClicks();
    }

    private void bindData(){
        BACK = findViewById(R.id.back);
        DONE = findViewById(R.id.done);

        GENDERTEXT = findViewById(R.id.genderText);
        CATEGORY = findViewById(R.id.category);
        GENDER = findViewById(R.id.gender);
        OCCUPATION = findViewById(R.id.occupation);
        HELPWITH = findViewById(R.id.help_with);

        IMAGEONE = findViewById(R.id.numberOne);
        IMAGETWO = findViewById(R.id.numberTwo);
        IMAGETHREE = findViewById(R.id.numberThree);

        NAME = findViewById(R.id.name);
        DATE = findViewById(R.id.date);
        MONTH = findViewById(R.id.month);
        YEAR = findViewById(R.id.year);
        PHONE = findViewById(R.id.phone);

        EMAIL = findViewById(R.id.email);

        ABOUT = findViewById(R.id.about);
        ADDR = findViewById(R.id.addr);
        CITY = findViewById(R.id.city);
        STATE = findViewById(R.id.state);
        COUNTRY = findViewById(R.id.country);

        userInfo = new UserInfo(this);
        progressDialog = new ProgressDialog(this);


    }

    private void setClicks(){
        BACK.setOnClickListener(this);
        DONE.setOnClickListener(this);
        IMAGEONE.setOnClickListener(this);
        IMAGETWO.setOnClickListener(this);
        IMAGETHREE.setOnClickListener(this);
    }

    private void setData(){
        Picasso.with(ProfileActivity.this).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(IMAGEONE);
        if(!userInfo.getKeyPassportTwo().isEmpty()){
            Picasso.with(ProfileActivity.this).load(userInfo.getKeyPassportTwo())
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(IMAGETWO);
        }
        if(!userInfo.getKeyPassportThree().isEmpty()){
            Picasso.with(ProfileActivity.this).load(userInfo.getKeyPassportThree())
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(IMAGETHREE);
        }

        NAME.setText(userInfo.getKeyName());
        if(!userInfo.getKeyDob().isEmpty() && !userInfo.getKeyDob().equalsIgnoreCase("null")) {
            try {
                DATE.setText(userInfo.getKeyDob().split("/")[0]);
                MONTH.setText(userInfo.getKeyDob().split("/")[1]);
                YEAR.setText(userInfo.getKeyDob().split("/")[2]);
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        PHONE.setText(userInfo.getKeyPhone());
        EMAIL.setText(userInfo.getKeyEmail());
        ABOUT.setText(userInfo.getKeyAbout());
        ADDR.setText(userInfo.getKeyAddress());
        CITY.setText(userInfo.getKeyCity());
        STATE.setText(userInfo.getKeyState());
        COUNTRY.setText(userInfo.getKeyCountry());
        GENDERTEXT.setText(userInfo.getKeyGender());

        if(!userInfo.getKeyGender().isEmpty() && !userInfo.getKeyGender().equalsIgnoreCase("null")){
            GENDERTEXT.setVisibility(View.VISIBLE);
            GENDER.setVisibility(View.GONE);
        }else {
            GENDERTEXT.setVisibility(View.GONE);
            GENDER.setVisibility(View.VISIBLE);
        }

        if(!userInfo.getKeyOccupation().isEmpty() && !userInfo.getKeyOccupation().equalsIgnoreCase("null")){
            String[] occuArray = getResources().getStringArray(R.array.occupations);
            int occuPos = new ArrayList<String>(Arrays.asList(occuArray)).indexOf(userInfo.getKeyOccupation());
            OCCUPATION.setSelection(occuPos);
        }

        HELPWITH.setText(userInfo.getKeyHelpWith());

        if(!userInfo.getKeyCategory().isEmpty() && !userInfo.getKeyCategory().equalsIgnoreCase("null")){
            String[] catArr = getResources().getStringArray(R.array.categories);
            int catPos = new ArrayList<String>(Arrays.asList(catArr)).indexOf(userInfo.getKeyCategory());
            CATEGORY.setSelection(catPos);
        }

    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                if(selected.equalsIgnoreCase("1")) {
                    IMAGEONE.setImageBitmap(selectedImage);
                    up1 = "upload";
                }
                else if(selected.equalsIgnoreCase("2")) {
                    IMAGETWO.setImageBitmap(selectedImage);
                    up2 = "upload";
                }
                if(selected.equalsIgnoreCase("3")) {
                    IMAGETHREE.setImageBitmap(selectedImage);
                    up3 = "upload";
                }
            } else if (requestCode == 2) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    if(selected.equalsIgnoreCase("1")) {
                        IMAGEONE.setImageBitmap(bitmap);
                        up1 = "upload";
                    }
                    else if(selected.equalsIgnoreCase("2")) {
                        IMAGETWO.setImageBitmap(bitmap);
                        up2 = "upload";
                    }
                    if(selected.equalsIgnoreCase("3")) {
                        IMAGETHREE.setImageBitmap(bitmap);
                        up3 = "upload";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(ProfileActivity.this, MainPage.class));
                finish();
                break;
            case R.id.numberOne:
                selected = "1";
                selectImage();
                break;
            case R.id.numberTwo:
                selected = "2";
                selectImage();
                break;
            case R.id.numberThree:
                selected = "3";
                selectImage();
                break;
            case R.id.done:
                updateProfile();
                break;
        }
    }

    private String convertImagePassport(ImageView imageView){
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        return Base64.encodeToString(image, 0);
    }

    private void startAnimation(){
        progressDialog.setMessage("updating profile.....");
        progressDialog.show();
    }

    private void endAnimation(){
        progressDialog.cancel();
    }

    private void updateProfile(){
        final String name = NAME.getText().toString();
        final String dob = DATE.getText().toString()+"/"+MONTH.getText().toString()+"/"+YEAR.getText().toString();
        final String about = ABOUT.getText().toString();
        final String address = ADDR.getText().toString();
        final String city = CITY.getText().toString();
        final String state = STATE.getText().toString();
        final String country = COUNTRY.getText().toString();
        final String phone = PHONE.getText().toString();

        final String helpWith = HELPWITH.getText().toString();
        if(!userInfo.getKeyGender().isEmpty() && !userInfo.getKeyGender().equalsIgnoreCase("")){
            gender = userInfo.getKeyGender();
        }else {
            gender = GENDER.getSelectedItem().toString();
        }

        final String category = CATEGORY.getSelectedItem().toString();
        final String occupation = OCCUPATION.getSelectedItem().toString();


        if(name.isEmpty()){
            NAME.setError("Please enter your name");
            NAME.requestFocus();
        }else {
            startAnimation();
            updateContent(name, dob, about, address, city, state, country, phone, helpWith, gender, category, occupation);
            if(up1.equalsIgnoreCase("upload")){
                updatePhoto(userInfo.getKeyPassOne(), convertImagePassport(IMAGEONE));
                up1 = "";
            }
            if(up2.equalsIgnoreCase("upload")){
                updatePhoto(userInfo.getKeyPassTwo(), convertImagePassport(IMAGETWO));
                up2 = "";
            }
            if(up3.equalsIgnoreCase("upload")){
                updatePhoto(userInfo.getKeyPassThree(), convertImagePassport(IMAGETHREE));
                up3 = "";
            }


        }


    }

    private void updatePhoto(final String imageId, final String image){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.UPLOAD_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("TAG", response);

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(error){
                                //Log.e("TAG", response);
                            }else {
                                login(userInfo.getKeyEmail());
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.toString());
                        endAnimation();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Unknown server error!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", image);
                params.put("imageId", imageId);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

    }

    private void updateContent(final String name, final String dob, final  String about, final String address, final String city, final String state,
                               final String country, final String phone, final String helpWith, final String gender, final String category, final String occupation){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("TAG", response);

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(error){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                builder.setMessage("Error occurred while sending your data to the server.");
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }else {
                                //Log.e("TAG", response);
                                if(up1.isEmpty() || up2.isEmpty() || up3.isEmpty()){
                                    login(userInfo.getKeyEmail());
                                }
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.toString());
                        endAnimation();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Unknown server error!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());
                params.put("name", name);
                params.put("dob", dob);
                params.put("about", about);
                params.put("address", address);
                params.put("city", city);
                params.put("state", state);
                params.put("country", country);
                params.put("phone", phone);
                params.put("help_with", helpWith);
                params.put("gender", gender);
                params.put("category", category);
                params.put("occupation", occupation);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
    }
    private void login(final String email){
        //startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //endAnimation();

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

                                String help_with = resp.getString("help_with");
                                String gender = resp.getString("gender");
                                String category = resp.getString("category");
                                String occupation = resp.getString("occupation");


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
                                userInfo.setKeyHelpWith(help_with);
                                userInfo.setKeyGender(gender);
                                userInfo.setKeyCategory(category);
                                userInfo.setKeyOccupation(occupation);


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

                                setData();

                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //endAnimation();
                        //Log.e("TAG", error.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Unknown server error!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
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

        VolleySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainPage.class));
        finish();
    }
}
