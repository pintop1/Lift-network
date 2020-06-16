package d.pintoptech.liftnetwork.sponsor.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.beneficiary.ViewSponsor;
import d.pintoptech.liftnetwork.prefs.UserInfo;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUser extends Fragment{

    private View fragView;

    private ImageView AVATAR;
    private TextView NAME, LOCATION, AGE, ABOUT, PHONE, EMAIL, GENDER, CATEGORY, OCCUPATION, HELPWITH, CHATWITH;
    private String AVATAR1, AVATAR2, AVATAR3, NAMES, LOCATIONS, AGES, ABOUTS, EMAILS, PHONES, FROM, DGENDER, DCATEGORY, DOCCUPATION, DHELPWITH;

    private View NEXT, PREV;
    private String activeString = "1";
    private TextView button1, button2, button3;
    private UserInfo userInfo;

    private FragmentManager fragmentManager;

    private RelativeLayout FORPHONE, FOREMAIL;
    PopupWindow popupWindow;


    public ViewUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_view_user, container, false);

        bindData();
        getBundle();
        clicks();

        return fragView;
    }

    private void bindData(){
        AVATAR = fragView.findViewById(R.id.avatar);
        NAME = fragView.findViewById(R.id.name);
        LOCATION = fragView.findViewById(R.id.location);
        AGE = fragView.findViewById(R.id.age);
        ABOUT = fragView.findViewById(R.id.about);
        NEXT = fragView.findViewById(R.id.next);
        PREV = fragView.findViewById(R.id.prev);

        PHONE = fragView.findViewById(R.id.phone);
        EMAIL = fragView.findViewById(R.id.email);

        CHATWITH = fragView.findViewById(R.id.chatWith);

        FOREMAIL = fragView.findViewById(R.id.forEmail);
        FORPHONE = fragView.findViewById(R.id.forPhone);

        GENDER = fragView.findViewById(R.id.gender);
        CATEGORY = fragView.findViewById(R.id.category);
        OCCUPATION = fragView.findViewById(R.id.occupation);
        HELPWITH = fragView.findViewById(R.id.help_with);

        button1 = fragView.findViewById(R.id.activeOne);
        button2 = fragView.findViewById(R.id.activeTwo);
        button3 = fragView.findViewById(R.id.activeThree);

        userInfo = new UserInfo(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();

    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            AVATAR1 = bundle.getString("image_1");
            AVATAR2 = bundle.getString("image_2");
            AVATAR3 = bundle.getString("image_3");
            NAMES = bundle.getString("name");
            LOCATIONS = bundle.getString("location");
            AGES = bundle.getString("age");
            ABOUTS = bundle.getString("about");
            EMAILS = bundle.getString("email");
            PHONES = bundle.getString("phone");
            FROM = bundle.getString("from");

            DGENDER = bundle.getString("gender");
            DCATEGORY = bundle.getString("category");
            DOCCUPATION = bundle.getString("occupation");
            DHELPWITH = bundle.getString("help_with");

            Picasso.with(getActivity()).load(AVATAR1)
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(AVATAR);
            NAME.setText(NAMES);
            LOCATION.setText(LOCATIONS);
            AGE.setText(AGES);
            ABOUT.setText(ABOUTS);
            EMAIL.setText(EMAILS);
            PHONE.setText(PHONES);

            GENDER.setText(DGENDER);
            CATEGORY.setText(DCATEGORY);
            OCCUPATION.setText(DOCCUPATION);
            HELPWITH.setText(DHELPWITH);

            if(FROM.equalsIgnoreCase("home")){
                CHATWITH.setVisibility(View.VISIBLE);
                CHATWITH.setText("Do you want to help "+NAMES+" ?");
                FOREMAIL.setVisibility(View.GONE);
                FORPHONE.setVisibility(View.GONE);
                CHATWITH.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopUp(fragView, EMAILS);
                    }
                });
            }else if(FROM.equalsIgnoreCase("pending")){
                CHATWITH.setVisibility(View.GONE);
                FOREMAIL.setVisibility(View.GONE);
                FORPHONE.setVisibility(View.GONE);
            }else{
                CHATWITH.setVisibility(View.VISIBLE);
                CHATWITH.setText("Chat with "+NAMES);
                FOREMAIL.setVisibility(View.VISIBLE);
                FORPHONE.setVisibility(View.VISIBLE);
                CHATWITH.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("id", "6");
                        intent.putExtra("passport", AVATAR1);
                        intent.putExtra("name", NAMES);
                        intent.putExtra("email", EMAILS);
                        startActivity(intent);
                    }
                });
            }


        }

    }

    private void clicks(){

    }

    private void sendRequest(final String type, final String beneficiary, final String helping_with){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("sending data");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.hide();
                        //Log.e("TAG", response);

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(!error){
                                Toast.makeText(getActivity(), "Your request has been sent successfully!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());
                params.put("beneficiary", beneficiary);
                params.put("helping_with", helping_with);
                params.put("type", type);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showPopUp(View view, final String beneficiary){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_view_2, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        final EditText HELPINGWITH = popupView.findViewById(R.id.helping_with);
        TextView CLOSE = popupView.findViewById(R.id.close);
        Button SUBMIT = popupView.findViewById(R.id.proceed);



        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hpw = HELPINGWITH.getText().toString();
                sendRequest("help", beneficiary, hpw);
                popupWindow.dismiss();
            }
        });


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
    }
}
