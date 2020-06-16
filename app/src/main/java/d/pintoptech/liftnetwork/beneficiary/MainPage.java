package d.pintoptech.liftnetwork.beneficiary;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import d.pintoptech.liftnetwork.AuthActivity;
import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.MainActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.RegisterActivity;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.prefs.UserSession;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;
import de.hdodenhof.circleimageview.CircleImageView;
import io.customerly.Customerly;

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView DP;
    private TextView NAME,CITY,EDIT,AVATAR_EDIT,VISITORSC,REQUESTC,ACCEPTEDC;
    private RelativeLayout PENDING,ACCEPTED,SUPPORT,PROFILE,LOGOUT, CHAT;
    private UserInfo userInfo;
    private ProgressDialog progressDialog;
    private Timer timer;
    private GoogleSignInAccount acct;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private UserSession userSession;
    private GoogleApiClient mGoogleApiClient;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page2);

        bindData();
        click();
        login(userInfo.getKeyEmail());
        attachData();

    }

    private void bindData(){
        userInfo = new UserInfo(this);
        DP = findViewById(R.id.dp);
        NAME = findViewById(R.id.name);
        CITY = findViewById(R.id.city);
        EDIT = findViewById(R.id.edit);
        AVATAR_EDIT = findViewById(R.id.avatar_edit);
        VISITORSC = findViewById(R.id.visitorsCount);
        REQUESTC = findViewById(R.id.requestsCount);
        ACCEPTEDC = findViewById(R.id.acceptedCount);
        PENDING = findViewById(R.id.pending);
        ACCEPTED = findViewById(R.id.accepted);
        SUPPORT = findViewById(R.id.support);
        PROFILE = findViewById(R.id.profile);
        LOGOUT = findViewById(R.id.logout);
        CHAT = findViewById(R.id.chat);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        acct = GoogleSignIn.getLastSignedInAccount(this);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        userSession = new UserSession(this);
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());


    }

    private void click(){
        DP.setOnClickListener(this);
        EDIT.setOnClickListener(this);
        AVATAR_EDIT.setOnClickListener(this);
        PENDING.setOnClickListener(this);
        ACCEPTED.setOnClickListener(this);
        SUPPORT.setOnClickListener(this);
        PROFILE.setOnClickListener(this);
        LOGOUT.setOnClickListener(this);
        CHAT.setOnClickListener(this);
    }

    private void attachData(){
        Picasso.with(MainPage.this).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.placeholder_avatar)
                .error(R.drawable.placeholder_avatar)
                .into(DP);
        if(userInfo.getKeyCity().isEmpty() || userInfo.getKeyCountry().isEmpty()){
            CITY.setText("Please edit your address.");
        }else {
            CITY.setText(userInfo.getKeyCity()+", "+userInfo.getKeyCountry());
        }
        NAME.setText(userInfo.getKeyName());
        VISITORSC.setText("0");
        REQUESTC.setText("0");
        ACCEPTEDC.setText("0");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLogs();
                login(userInfo.getKeyEmail());
            }
        }, 0, 10000);   // 1000 Millisecond  = 1 second

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dp:
                selectImage();
                break;
            case R.id.avatar_edit:
                selectImage();
                break;
            case R.id.edit:
                startActivity(new Intent(MainPage.this, ProfileActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MainPage.this, ViewProfile.class));
                break;
            case R.id.pending:
                Intent intent = new Intent(MainPage.this, RequestActivity.class);
                intent.putExtra("type", "pending");
                startActivity(intent);
                break;
            case R.id.accepted:
                Intent intent2 = new Intent(MainPage.this, RequestActivity.class);
                intent2.putExtra("type", "accepted");
                startActivity(intent2);
                break;
            case R.id.support:
                Customerly.openSupport(MainPage.this);
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setMessage(Html.fromHtml("You have initiated a log out request on your account. <br> <b>Are you sure you want to proceed with the request.</b>"));
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.show();
                break;
            case R.id.chat:
                Intent dintent = new Intent(MainPage.this, ChatList.class);
                startActivity(dintent);
                break;
        }
    }

    private void getLogs(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("TAG", response);

                        try {
                            JSONObject resp = new JSONObject(response);
                            String visitors = resp.getString("visitors");
                            String requests = resp.getString("requests");
                            String accepted = resp.getString("accepted");
                            VISITORSC.setText(visitors);
                            REQUESTC.setText(requests);
                            ACCEPTEDC.setText(accepted);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("TAG", error.toString());
                        Toast.makeText(MainPage.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(MainPage.this).addToRequestQueue(stringRequest);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
       AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
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
                DP.setImageBitmap(selectedImage);
                uploadDp(convertImagePassport(DP));
            } else if (requestCode == 2) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    DP.setImageBitmap(bitmap);
                    uploadDp(convertImagePassport(DP));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String convertImagePassport(CircleImageView imageView){
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        return Base64.encodeToString(image, 0);
    }

    private void startAnimation(){
        progressDialog.setMessage("uploading to server.....");
        progressDialog.show();
    }

    private void endAnimation(){
        progressDialog.cancel();
    }

    private void uploadDp(final String image){
        startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CHANGE_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", image);

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(error){
                                String msg = resp.getString("error_msg");
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                                builder.setMessage(Html.fromHtml(msg));
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }else {
                                String image = resp.getString("image");
                                userInfo.setPassport(image);
                                Picasso.with(MainPage.this).load(userInfo.getKeyPassport())
                                        .placeholder(R.drawable.placeholder_avatar)
                                        .error(R.drawable.placeholder_avatar)
                                        .into(DP);
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
                        Toast.makeText(MainPage.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());
                params.put("image", image);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(MainPage.this).addToRequestQueue(stringRequest);
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

                                Picasso.with(MainPage.this).load(userInfo.getKeyPassport())
                                        .placeholder(R.drawable.placeholder_avatar)
                                        .error(R.drawable.placeholder_avatar)
                                        .into(DP);
                                if(userInfo.getKeyCity().isEmpty() || userInfo.getKeyCountry().isEmpty()){
                                    CITY.setText("Please edit your address.");
                                }else {
                                    CITY.setText(userInfo.getKeyCity()+", "+userInfo.getKeyCountry());
                                }
                                NAME.setText(userInfo.getKeyName());

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
                                if(!status.equalsIgnoreCase("completed")){
                                    timer.cancel();
                                    startActivity(new Intent(MainPage.this, ProfileActivity.class));
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
                        Toast.makeText(MainPage.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(MainPage.this).addToRequestQueue(stringRequest);
    }

    private void logout(){
        progressDialog.setMessage("signing out...");
        progressDialog.show();
        if(userInfo.getKeyLogType().equalsIgnoreCase("google")){
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();
            Customerly.logoutUser();
            userInfo.clearData();
            userSession.setLoggedIn(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainPage.this, AuthActivity.class));
                    finish();
                }
            },3000);

        }else if(userInfo.getKeyLogType().equalsIgnoreCase("facebook")){
            LoginManager.getInstance().logOut();
            Customerly.logoutUser();
            userInfo.clearData();
            userSession.setLoggedIn(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainPage.this, AuthActivity.class));
                    finish();
                }
            },3000);
        }
    }


}
