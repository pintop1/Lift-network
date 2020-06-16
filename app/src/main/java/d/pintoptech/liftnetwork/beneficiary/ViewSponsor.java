package d.pintoptech.liftnetwork.beneficiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;

public class ViewSponsor extends AppCompatActivity {

    private ImageView AVATAR;
    private TextView NAME, LOCATION, AGE, ABOUT, HELP_WITH, GENDER, OCCUPATION, CHATWITH;
    private String AVATAR1, AVATAR2, AVATAR3, NAMES, LOCATIONS, AGES, FROM, EMAILS, ID, STATUS, ABOUTS, HELP_WITHS, GENDERS, OCCUPATIONS, PHONES, CATEGORY;
    private ProgressDialog progressDialog;

    private UserInfo userInfo;
    private TextView Menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sponsor);
        bindData();
        getBundle();
        clicks();
    }

    private void bindData(){
        AVATAR = findViewById(R.id.avatar);
        NAME = findViewById(R.id.name);
        LOCATION = findViewById(R.id.location);
        AGE = findViewById(R.id.age);
        ABOUT = findViewById(R.id.about);
        HELP_WITH = findViewById(R.id.help_with);
        GENDER = findViewById(R.id.gender);
        OCCUPATION = findViewById(R.id.occupation);
        CHATWITH = findViewById(R.id.chatWith);


        Menu = findViewById(R.id.menu);
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
        HELP_WITH.setText(HELP_WITHS);
        GENDER.setText(GENDERS);
        OCCUPATION.setText(OCCUPATIONS);
        CHATWITH.setText("Chat with "+NAMES);

        if(STATUS.equalsIgnoreCase("helped")){
            Menu.setVisibility(View.GONE);
        }

        if(FROM.equalsIgnoreCase("accepted")){
            CHATWITH.setVisibility(View.VISIBLE);
        }else {
            CHATWITH.setVisibility(View.GONE);
        }

        //Log.e("TAG", EMAILS);

    }

    private void clicks(){
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FROM.equalsIgnoreCase("accepted")){
                    accepted();
                }else {
                    pending();
                }
            }
        });

        CHATWITH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewSponsor.this, ChatActivity.class);
                intent.putExtra("id", ID);
                intent.putExtra("image_1", AVATAR1);
                intent.putExtra("name", NAMES);
                intent.putExtra("about", ABOUTS);
                intent.putExtra("email", EMAILS);
                intent.putExtra("image_2", AVATAR2);
                intent.putExtra("image_3", AVATAR3);
                intent.putExtra("location", LOCATIONS);
                intent.putExtra("age", AGES);
                intent.putExtra("phone", PHONES);
                intent.putExtra("help_with", HELP_WITHS);
                intent.putExtra("gender", GENDERS);
                intent.putExtra("category", CATEGORY);
                intent.putExtra("occupation", OCCUPATIONS);
                startActivity(intent);
            }
        });
    }

    private void pending(){
        final CharSequence[] options = { "Accept", "Decline","Close" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSponsor.this);
        builder.setTitle("Action!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Accept"))
                {
                    sendRequest("accepted", EMAILS, ID);
                }
                else if (options[item].equals("Decline"))
                {
                    sendRequest("declined", EMAILS, ID);
                }
                else if (options[item].equals("Close")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void accepted(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSponsor.this);
        builder.setMessage("Has this sponsor been of help?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRequest("helped", EMAILS, ID);
            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void sendRequest(final String status, final String sponsor, final String id){
        startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.ACCEPT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(!error){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSponsor.this);
                                builder.setMessage("You have successfully updated a request.");
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        if(FROM.equalsIgnoreCase("accepted")){
                                            Intent intent = new Intent(ViewSponsor.this, RequestActivity.class);
                                            intent.putExtra("type", "accepted");
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(ViewSponsor.this, RequestActivity.class);
                                            intent.putExtra("type", "pending");
                                            startActivity(intent);
                                        }
                                    }
                                });
                                builder.show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSponsor.this);
                        builder.setMessage("Please ensure you have a working internet connection.");
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
                params.put("sponsor", sponsor);
                params.put("id", id);
                params.put("status", status);

                return params;
            }
        };

        VolleySingleton.getInstance(ViewSponsor.this).addToRequestQueue(stringRequest);
    }

    private void startAnimation(){
        progressDialog.setMessage("sending requests....");
        progressDialog.show();
    }

    private void endAnimation(){
        progressDialog.cancel();
    }


}
