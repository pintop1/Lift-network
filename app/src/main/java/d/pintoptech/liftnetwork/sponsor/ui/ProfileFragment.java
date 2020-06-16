package d.pintoptech.liftnetwork.sponsor.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import d.pintoptech.liftnetwork.AuthActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.beneficiary.ProfileActivity;
import d.pintoptech.liftnetwork.beneficiary.RequestActivity;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.prefs.UserSession;
import de.hdodenhof.circleimageview.CircleImageView;
import io.customerly.Customerly;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private CircleImageView DP;
    private TextView NAME,CITY,EDIT,AVATAR_EDIT,HELPED,REQUESTC,ACCEPTEDC;
    private RelativeLayout PENDING,ACCEPTED,SUPPORT,PROFILE,LOGOUT/*, SETTINGS*/;
    private UserInfo userInfo;
    private ProgressDialog progressDialog;
    private Timer timer;
    private GoogleSignInAccount acct;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private UserSession userSession;

    private CallbackManager callbackManager;

    private View fragView;

    private FragmentManager fragmentManager;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_profile, container, false);
        bindData();
        click();
        login(userInfo.getKeyEmail());
        attachData();
        return fragView;

    }

    private void bindData(){
        userInfo = new UserInfo(getActivity());
        DP = fragView.findViewById(R.id.dp);
        NAME = fragView.findViewById(R.id.name);
        CITY = fragView.findViewById(R.id.city);
        EDIT = fragView.findViewById(R.id.edit);
        AVATAR_EDIT = fragView.findViewById(R.id.avatar_edit);
        HELPED = fragView.findViewById(R.id.visitorsCount);
        REQUESTC = fragView.findViewById(R.id.requestsCount);
        ACCEPTEDC = fragView.findViewById(R.id.acceptedCount);
        PENDING = fragView.findViewById(R.id.pending);
        ACCEPTED = fragView.findViewById(R.id.accepted);
        SUPPORT = fragView.findViewById(R.id.support);
        //SETTINGS = fragView.findViewById(R.id.settings);
        PROFILE = fragView.findViewById(R.id.profile);
        LOGOUT = fragView.findViewById(R.id.logout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        userSession = new UserSession(getActivity());
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        fragmentManager = getActivity().getSupportFragmentManager();
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
        //SETTINGS.setOnClickListener(this);
    }

    private void attachData(){
        Picasso.with(getActivity()).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.placeholder_avatar)
                .error(R.drawable.placeholder_avatar)
                .into(DP);
        if(userInfo.getKeyCity().isEmpty() || userInfo.getKeyCountry().isEmpty()){
            CITY.setText("Please edit your address.");
        }else {
            CITY.setText(userInfo.getKeyCity()+", "+userInfo.getKeyCountry());
        }
        NAME.setText(userInfo.getKeyName());
        HELPED.setText("0");
        REQUESTC.setText("0");
        ACCEPTEDC.setText("0");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLogs();
            }
        }, 0, 10000);   // 1000 Millisecond  = 1 second

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dp:
                selectImage();
                break;
            case R.id.avatar_edit:
                selectImage();
                break;
            case R.id.edit:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new EditProfileFragment()).addToBackStack("landing").commit();
                break;
            case R.id.profile:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewProfile()).addToBackStack("landing").commit();
                break;
            /*case R.id.settings:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).addToBackStack("landing").commit();
                break;*/
            case R.id.pending:
                RequestFragment requestFragment = new RequestFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "pending");
                requestFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
                break;
            case R.id.accepted:
                RequestFragment requestFragmentA = new RequestFragment();
                Bundle bundleA = new Bundle();
                bundleA.putString("type", "accepted");
                requestFragmentA.setArguments(bundleA);
                fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragmentA).addToBackStack("landing").commit();
                break;
            case R.id.support:
                Customerly.openSupport(getActivity());
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        }
    }

    private void getLogs(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SPONSOR_LOGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("TAG", response);

                        try {
                            JSONObject resp = new JSONObject(response);
                            String helps = resp.getString("helps");
                            String requests = resp.getString("requests");
                            String accepted = resp.getString("accepted");
                            HELPED.setText(helps);
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
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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
                DP.setImageBitmap(selectedImage);
                uploadDp(convertImagePassport(DP));
            } else if (requestCode == 2) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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
                        //Log.e("TAG", image);

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(error){
                                String msg = resp.getString("error_msg");
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Picasso.with(getActivity()).load(userInfo.getKeyPassport())
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
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

                                Picasso.with(getActivity()).load(userInfo.getKeyPassport())
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

    private void logout(){
        progressDialog.setMessage("signing out...");
        progressDialog.show();
        if(userInfo.getKeyLogType().equalsIgnoreCase("google")){
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Customerly.logoutUser();
                            userInfo.clearData();
                            userSession.setLoggedIn(false);
                            startActivity(new Intent(getActivity(), AuthActivity.class));
                            getActivity().finish();
                        }
                    });

        }else if(userInfo.getKeyLogType().equalsIgnoreCase("facebook")){
            LoginManager.getInstance().logOut();
            Customerly.logoutUser();
            userInfo.clearData();
            userSession.setLoggedIn(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getActivity(), AuthActivity.class));
                    getActivity().finish();
                }
            },3000);
        }
    }
}
