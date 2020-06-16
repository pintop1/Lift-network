package d.pintoptech.liftnetwork.sponsor.ui;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.beneficiary.ProfileActivity;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private TextView BACK, DONE;
    private ImageView IMAGEONE;
    private EditText NAME, PHONE;
    private TextView EMAIL, GENDERTEXT;
    private EditText ABOUT, ADDR, CITY, STATE, COUNTRY, OCCUPATION;
    private Spinner  GENDER;
    private String gender = "";

    private UserInfo userInfo;
    private ProgressDialog progressDialog;

    private View fragView;
    private FragmentManager fragmentManager;
    private String up1 = "";

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        bindData();
        setData();
        setClicks();
        return fragView;
    }

    private void bindData(){
        BACK = fragView.findViewById(R.id.back);
        DONE = fragView.findViewById(R.id.done);

        IMAGEONE = fragView.findViewById(R.id.numberOne);

        NAME = fragView.findViewById(R.id.name);
        PHONE = fragView.findViewById(R.id.phone);

        EMAIL = fragView.findViewById(R.id.email);

        ADDR = fragView.findViewById(R.id.addr);
        CITY = fragView.findViewById(R.id.city);
        STATE = fragView.findViewById(R.id.state);
        COUNTRY = fragView.findViewById(R.id.country);

        ABOUT = fragView.findViewById(R.id.about);
        GENDERTEXT = fragView.findViewById(R.id.genderText);
        GENDER = fragView.findViewById(R.id.gender);

        OCCUPATION = fragView.findViewById(R.id.occupation);

        userInfo = new UserInfo(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();

    }

    private void setClicks(){
        BACK.setOnClickListener(this);
        DONE.setOnClickListener(this);
        IMAGEONE.setOnClickListener(this);
    }

    private void setData(){
        Picasso.with(getActivity()).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(IMAGEONE);

        NAME.setText(userInfo.getKeyName());
        PHONE.setText(userInfo.getKeyPhone());
        EMAIL.setText(userInfo.getKeyEmail());
        ADDR.setText(userInfo.getKeyAddress());
        CITY.setText(userInfo.getKeyCity());
        STATE.setText(userInfo.getKeyState());
        COUNTRY.setText(userInfo.getKeyCountry());
        ABOUT.setText(userInfo.getKeyAbout());

        GENDERTEXT.setText(userInfo.getKeyGender());
        OCCUPATION.setText(userInfo.getKeyOccupation());

        if(!userInfo.getKeyGender().isEmpty() && !userInfo.getKeyGender().equalsIgnoreCase("null")){
            GENDERTEXT.setVisibility(View.VISIBLE);
            GENDER.setVisibility(View.GONE);
        }else {
            GENDERTEXT.setVisibility(View.GONE);
            GENDER.setVisibility(View.VISIBLE);
        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                IMAGEONE.setImageBitmap(selectedImage);
                up1 = "upload";
            } else if (requestCode == 2) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    IMAGEONE.setImageBitmap(bitmap);
                    up1 = "upload";
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
                fragmentManager.popBackStackImmediate();
                break;
            case R.id.numberOne:
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
        final String address = ADDR.getText().toString();
        final String city = CITY.getText().toString();
        final String state = STATE.getText().toString();
        final String country = COUNTRY.getText().toString();
        final String phone = PHONE.getText().toString();
        final String about = ABOUT.getText().toString();
        if(!userInfo.getKeyGender().isEmpty() && !userInfo.getKeyGender().equalsIgnoreCase("")){
            gender = userInfo.getKeyGender();
        }else {
            gender = GENDER.getSelectedItem().toString();
        }

        final String occupation = OCCUPATION.getText().toString();

        if(name.isEmpty()){
            NAME.setError("Please enter your name");
            NAME.requestFocus();
        }else {
            startAnimation();
            updateContent(name, "", about, address, city, state, country, phone, "", gender, "", occupation);
            if(up1.equalsIgnoreCase("upload")){
                updatePhoto(userInfo.getKeyPassOne(), convertImagePassport(IMAGEONE));
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                if(up1.isEmpty()){
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private void login(final String email){
        startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation();

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
                                /*// passport 2
                                JSONObject passport2 = passports.getJSONObject(1);
                                userInfo.setKeyPassTwo(passport2.getString("id"));
                                userInfo.setKeyPassportTwo("https://liftnigeria.ng/mobile_app/Profile/"+passport2.getString("image"));
                                // passport 3
                                JSONObject passport3 = passports.getJSONObject(0);
                                userInfo.setKeyPassThree(passport3.getString("id"));
                                userInfo.setKeyPassportThree("https://liftnigeria.ng/mobile_app/Profile/"+passport3.getString("image"));*/




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
                        endAnimation();
                        //Log.e("TAG", error.getMessage());
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
