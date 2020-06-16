package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.prefs.UserSession;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView LOGO;

    private RelativeLayout FACEBOOK, GOOGLE, LOADER;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1001;

    private UserInfo userInfo;
    private UserSession userSession;

    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);

        googleBining();
        facebookBinding();

        changeTextColor();

        LOADER = findViewById(R.id.loader);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "d.pintoptech.liftnetwork",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }*/

    }

    private void googleBining(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GOOGLE = findViewById(R.id.loginGoogle);
        GOOGLE.setOnClickListener(this);
    }

    private void facebookBinding(){
        FACEBOOK = findViewById(R.id.loginFb);
        FACEBOOK.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fbBtn);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();

                handleSignInResultTwo(accessToken);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("TAG", exception.getMessage());
            }
        });
    }

    private void changeTextColor(){
        LOGO = findViewById(R.id.logo);
        LOGO.setText(getResources().getString(R.string.app_name));

        TextPaint paint = LOGO.getPaint();
        float width = paint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, LOGO.getTextSize(),
                new int[]{
                        Color.parseColor("#FF1744"),
                        Color.parseColor("#B71C1C"),/*,
                        Color.parseColor("#D500F9"),
                        Color.parseColor("#4A148C"),*/
                        Color.parseColor("#00E676"),
                }, null, Shader.TileMode.CLAMP);
        LOGO.getPaint().setShader(textShader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginGoogle:
                signInGoogle();
                break;
            case R.id.loginFb:
                signInFacebook();
                break;
            // ...
        }

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInFacebook(){
        loginButton.performClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            checkEmailForGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.e("TAG", "signInResult:failed code=" + e.getMessage());
            //updateUI(null);
            AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
            builder.setMessage(Html.fromHtml(e.getMessage()));
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }
    }

    private void handleSignInResultTwo(AccessToken accessToken){
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    checkEmailForFacebook(name,email,image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void checkEmailForFacebook(final String name, final String email, final String passport){
        startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CHECK_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean email_exists = resp.getBoolean("email_exists");
                            if(email_exists){
                                login(email);
                            }else {
                                userInfo.setName(name);
                                userInfo.setPassport(passport);
                                userInfo.setEmail(email);
                                userInfo.setKeyLogType("facebook");
                                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
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
                        endAnimation();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
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

                return params;
            }
        };

        VolleySingleton.getInstance(AuthActivity.this).addToRequestQueue(stringRequest);
    }

    private void checkEmailForGoogle(final GoogleSignInAccount account){
        startAnimation();
        final String email = account.getEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CHECK_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean email_exists = resp.getBoolean("email_exists");
                            if(email_exists){
                                login(email);
                            }else {
                                String name = account.getGivenName()+" "+account.getFamilyName();
                                try {
                                    String passport = account.getPhotoUrl().toString();
                                    userInfo.setPassport(passport);
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }

                                userInfo.setName(name);
                                userInfo.setEmail(email);
                                userInfo.setKeyLogType("google");
                                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
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
                        endAnimation();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                        builder.setMessage("Please check your internet connection and try again!");
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

                return params;
            }
        };

        VolleySingleton.getInstance(AuthActivity.this).addToRequestQueue(stringRequest);
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
                            if(error){
                                startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
                            }else {
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
                                userInfo.setPassport(passport1.getString("image"));
                                // passport 2
                                JSONObject passport2 = passports.getJSONObject(1);
                                userInfo.setKeyPassTwo(passport2.getString("id"));
                                userInfo.setKeyPassportTwo(passport2.getString("image"));
                                // passport 3
                                JSONObject passport3 = passports.getJSONObject(0);
                                userInfo.setKeyPassThree(passport3.getString("id"));
                                userInfo.setKeyPassportThree(passport3.getString("image"));

                                userSession.setLoggedIn(true);



                                if(type.equalsIgnoreCase("sponsor")){
                                    startActivity(new Intent(AuthActivity.this, MainPageSponsor.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(AuthActivity.this, MainPage.class));
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
                        endAnimation();
                        //Log.e("TAG", error.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                        builder.setMessage("Please ensure you have a working internet connection!");
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

        VolleySingleton.getInstance(AuthActivity.this).addToRequestQueue(stringRequest);
    }



    private void startAnimation(){
        LOADER.setVisibility(View.VISIBLE);
        FACEBOOK.setClickable(false);
        GOOGLE.setClickable(false);
    }

    private void endAnimation(){
        LOADER.setVisibility(View.GONE);
        FACEBOOK.setClickable(true);
        GOOGLE.setClickable(true);
    }
}
