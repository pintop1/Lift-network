package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.prefs.UserSession;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;

public class RegisterActivity extends AppCompatActivity {

    private UserInfo userInfo;
    private Button BENEFICIARY,SPONSOR,CONTINUE;
    private UserSession userSession;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        BENEFICIARY = findViewById(R.id.a_beneficiary);
        SPONSOR = findViewById(R.id.a_sponsor);
        CONTINUE = findViewById(R.id.proceed);

        SPONSOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setType("sponsor");
                SPONSOR.setBackground(getResources().getDrawable(R.drawable.selected_btn));
                BENEFICIARY.setBackground(getResources().getDrawable(R.drawable.unselected_btn));
            }
        });

        BENEFICIARY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.setType("beneficiary");
                BENEFICIARY.setBackground(getResources().getDrawable(R.drawable.selected_btn));
                SPONSOR.setBackground(getResources().getDrawable(R.drawable.unselected_btn));
            }
        });

        CONTINUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedNw();
            }
        });
    }

    private void proceedNw(){
        if(userInfo.getKeyType().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please select your account type", Toast.LENGTH_SHORT).show();
        }else{
            final String fcm_id = FirebaseInstanceId.getInstance().getToken();
            progressDialog.setMessage("Creating your account...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("TAG", response);

                            progressDialog.cancel();

                            try {
                                JSONObject resp = new JSONObject(response);
                                Boolean error = resp.getBoolean("error");
                                if(error){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Unknown error occurred while creating your account!");
                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }else {
                                    userSession.setLoggedIn(true);
                                    if(userInfo.getKeyType().equalsIgnoreCase("sponsor")){
                                        startActivity(new Intent(RegisterActivity.this, MainPageSponsor.class));
                                        finish();
                                    }else {
                                        startActivity(new Intent(RegisterActivity.this, MainPage.class));
                                        finish();
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
                            //Log.e("TAG", error.getMessage());
                            progressDialog.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Please ensure you have a working internet!");
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
                    params.put("name", userInfo.getKeyName());
                    params.put("type", userInfo.getKeyType());
                    params.put("passport", userInfo.getKeyPassport());
                    params.put("fcm_id", fcm_id);

                    return params;
                }
            };

            VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
        }
    }
}
