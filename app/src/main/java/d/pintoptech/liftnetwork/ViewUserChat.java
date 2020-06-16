package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import d.pintoptech.liftnetwork.prefs.UserInfo;

public class ViewUserChat extends AppCompatActivity {

    private String id, name, email, passport, about, passport2, passport3, location, age, phone, help_with, gender, category, occupation;

    private ImageView AVATAR;
    private TextView NAME, LOCATION, AGE, ABOUT, PHONE, EMAIL, GENDER, CATEGORY, OCCUPATION, HELPWITH;

    private View NEXT, PREV;
    private String activeString = "1";
    private TextView button1, button2, button3;
    private UserInfo userInfo;

    private RelativeLayout FORPHONE, FOREMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_chat);
        getBundle();
        bindData();
        attachData();
        clicks();
    }

    private void getBundle(){
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        passport = intent.getStringExtra("image_1");
        about = intent.getStringExtra("about");
        passport2 = intent.getStringExtra("image_2");
        passport3 = intent.getStringExtra("image_3");
        location = intent.getStringExtra("location");
        age = intent.getStringExtra("age");
        phone = intent.getStringExtra("phone");
        help_with = intent.getStringExtra("help_with");
        gender = intent.getStringExtra("gender");
        category = intent.getStringExtra("category");
        occupation = intent.getStringExtra("occupation");
    }

    private void bindData(){
        AVATAR = findViewById(R.id.avatar);
        NAME = findViewById(R.id.name);
        LOCATION = findViewById(R.id.location);
        AGE = findViewById(R.id.age);
        ABOUT = findViewById(R.id.about);
        NEXT = findViewById(R.id.next);
        PREV = findViewById(R.id.prev);

        PHONE = findViewById(R.id.phone);
        EMAIL = findViewById(R.id.email);


        FOREMAIL = findViewById(R.id.forEmail);
        FORPHONE = findViewById(R.id.forPhone);

        GENDER = findViewById(R.id.gender);
        CATEGORY = findViewById(R.id.category);
        OCCUPATION = findViewById(R.id.occupation);
        HELPWITH = findViewById(R.id.help_with);

        button1 = findViewById(R.id.activeOne);
        button2 = findViewById(R.id.activeTwo);
        button3 = findViewById(R.id.activeThree);

        userInfo = new UserInfo(this);

    }

    private void attachData(){
        Picasso.with(this).load(passport)
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(AVATAR);
        NAME.setText(name);
        LOCATION.setText(location);
        AGE.setText(age);
        ABOUT.setText(about);
        EMAIL.setText(email);
        PHONE.setText(phone);

        GENDER.setText(gender);
        CATEGORY.setText(category);
        OCCUPATION.setText(occupation);
        HELPWITH.setText(help_with);
    }

    private void clicks(){
        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeString.equalsIgnoreCase("1")){

                    Picasso.with(ViewUserChat.this).load(passport2)
                            .placeholder(R.drawable.searches)
                            .error(R.drawable.searches)
                            .into(AVATAR);
                    button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    button2.setBackground(getResources().getDrawable(R.drawable.active_image));
                    button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    activeString = "2";
                }else if(activeString.equalsIgnoreCase("2")){
                    Picasso.with(ViewUserChat.this).load(passport3)
                            .placeholder(R.drawable.searches)
                            .error(R.drawable.searches)
                            .into(AVATAR);
                    button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    button2.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    button3.setBackground(getResources().getDrawable(R.drawable.active_image));
                    activeString = "3";
                }
            }
        });

        PREV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeString.equalsIgnoreCase("3")){
                    Picasso.with(ViewUserChat.this).load(passport2)
                            .placeholder(R.drawable.searches)
                            .error(R.drawable.searches)
                            .into(AVATAR);
                    button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    button2.setBackground(getResources().getDrawable(R.drawable.active_image));
                    button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    activeString = "2";
                }else if(activeString.equalsIgnoreCase("2")){
                    Picasso.with(ViewUserChat.this).load(passport)
                            .placeholder(R.drawable.searches)
                            .error(R.drawable.searches)
                            .into(AVATAR);
                    button1.setBackground(getResources().getDrawable(R.drawable.active_image));
                    button2.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                    activeString = "1";
                }
            }
        });
    }
}
