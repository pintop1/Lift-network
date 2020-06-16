package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import d.pintoptech.liftnetwork.prefs.UserInfo;

public class ViewSponsorChat extends AppCompatActivity {

    private ImageView AVATAR;
    private TextView NAME, LOCATION, AGE, ABOUT, GENDER, OCCUPATION;
    private String AVATAR1, AVATAR2, AVATAR3, NAMES, LOCATIONS, AGES, FROM, EMAILS, ID, STATUS, ABOUTS, HELP_WITHS, GENDERS, OCCUPATIONS, PHONES, CATEGORY;
    private ProgressDialog progressDialog;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sponsor_chat);
        bindData();
        getBundle();
    }

    private void bindData(){
        AVATAR = findViewById(R.id.avatar);
        NAME = findViewById(R.id.name);
        LOCATION = findViewById(R.id.location);
        AGE = findViewById(R.id.age);
        ABOUT = findViewById(R.id.about);
        GENDER = findViewById(R.id.gender);
        OCCUPATION = findViewById(R.id.occupation);


        userInfo = new UserInfo(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void getBundle(){
        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        AVATAR1 = intent.getStringExtra("image_1");
        AVATAR2 = intent.getStringExtra("image_2");
        AVATAR3 = intent.getStringExtra("image_3");
        CATEGORY = intent.getStringExtra("category");
        PHONES = intent.getStringExtra("phone");
        NAMES = intent.getStringExtra("name");
        LOCATIONS = intent.getStringExtra("location");
        AGES = intent.getStringExtra("age");
        FROM = intent.getStringExtra("from");
        EMAILS = intent.getStringExtra("email");
        STATUS = intent.getStringExtra("status");

        ABOUTS = intent.getStringExtra("about");
        HELP_WITHS = intent.getStringExtra("helping_with");
        GENDERS = intent.getStringExtra("gender");
        OCCUPATIONS = intent.getStringExtra("occupation");

        Picasso.with(this).load(AVATAR1)
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(AVATAR);
        NAME.setText(NAMES);
        LOCATION.setText(LOCATIONS);
        AGE.setText(AGES);

        ABOUT.setText(ABOUTS);
        GENDER.setText(GENDERS);
        OCCUPATION.setText(OCCUPATIONS);

    }
}
