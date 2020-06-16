package d.pintoptech.liftnetwork.beneficiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;

public class ViewProfile extends AppCompatActivity {

    private TextView BACK;
    private ImageView IMAGEONE,IMAGETWO,IMAGETHREE;
    private TextView NAME, DOB, PHONE;
    private TextView EMAIL;
    private TextView ABOUT, ADDR, CITY, STATE, COUNTRY, HELPWITH;
    private TextView CATEGORY, GENDER, OCCUPATION;

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
        setContentView(R.layout.activity_view_profile);
        bindData();
        setData();
    }

    private void bindData(){
        BACK = findViewById(R.id.back);

        CATEGORY = findViewById(R.id.category);
        GENDER = findViewById(R.id.gender);
        OCCUPATION = findViewById(R.id.occupation);
        HELPWITH = findViewById(R.id.help_with);

        IMAGEONE = findViewById(R.id.numberOne);
        IMAGETWO = findViewById(R.id.numberTwo);
        IMAGETHREE = findViewById(R.id.numberThree);

        NAME = findViewById(R.id.name);
        DOB = findViewById(R.id.dob);
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

    private void setData(){
        Picasso.with(ViewProfile.this).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(IMAGEONE);
        if(!userInfo.getKeyPassportTwo().isEmpty()){
            Picasso.with(ViewProfile.this).load(userInfo.getKeyPassportTwo())
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(IMAGETWO);
        }
        if(!userInfo.getKeyPassportThree().isEmpty()){
            Picasso.with(ViewProfile.this).load(userInfo.getKeyPassportThree())
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(IMAGETHREE);
        }

        NAME.setText(userInfo.getKeyName());
        if(!userInfo.getKeyDob().isEmpty() && !userInfo.getKeyDob().equalsIgnoreCase("null")) {
            try {
                DOB.setText(userInfo.getKeyDob());
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
        GENDER.setText(userInfo.getKeyGender());
        OCCUPATION.setText(userInfo.getKeyOccupation());
        CATEGORY.setText(userInfo.getKeyCategory());

        HELPWITH.setText(userInfo.getKeyHelpWith());
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewProfile.this, MainPage.class));
        finish();
    }
}
