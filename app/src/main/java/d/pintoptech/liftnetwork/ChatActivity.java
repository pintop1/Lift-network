package d.pintoptech.liftnetwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.MainPageSponsor;
import d.pintoptech.liftnetwork.utils.ChatAdapter;
import d.pintoptech.liftnetwork.utils.ChatsRepo;
import d.pintoptech.liftnetwork.utils.MembersRepo;
import d.pintoptech.liftnetwork.utils.RecyclerTouchListener;
import d.pintoptech.liftnetwork.utils.RecyclerViewAdapterTwo;
import d.pintoptech.liftnetwork.utils.RecyclerviewAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String id, name, email, passport, about, passport2, passport3, location, age, phone, help_with, gender, category, occupation;
    private TextView BACK, NAME;
    private CircleImageView DP;
    private UserInfo userInfo;
    private EditText MESSAGE;
    private ImageView SEND, UPLOAD;
    private Timer timer;

    List<ChatsRepo> data;
    private RecyclerView recyclerview;
    private ArrayList<ChatsRepo> arrayList;
    private ChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getBundle();
        bindData();
        attachData();
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
        userInfo = new UserInfo(this);
        BACK = findViewById(R.id.back);
        NAME = findViewById(R.id.name);
        DP = findViewById(R.id.dp);
        MESSAGE = findViewById(R.id.message);
        SEND = findViewById(R.id.send);
        UPLOAD = findViewById(R.id.photo);

        recyclerview = findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new ChatAdapter(this, arrayList);
        recyclerview = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                if(arrayList.get(position).getSender().equalsIgnoreCase(userInfo.getKeyEmail())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setMessage("Are you sure you want to delete this message!");
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMsg(position);
                        }
                    });
                    builder.show();
                }
            }
        }));

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchReg();
                updateChats();
            }
        }, 0, 10000);   // 1000 Millisecond  = 1 second
    }

    private void attachData(){
        NAME.setText(name);
        Picasso.with(ChatActivity.this).load(passport)
                .placeholder(R.drawable.placeholder_avatar)
                .error(R.drawable.placeholder_avatar)
                .into(DP);
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });
        fetchChats();
        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = MESSAGE.getText().toString();
                sendMessage(msg);
                String currentDate = new SimpleDateFormat("K:mm a", Locale.getDefault()).format(new Date());
                adapter.notifyDataSetChanged();
                arrayList.add(0, new ChatsRepo(arrayList.size()+1+"", msg, email, userInfo.getKeyEmail(), "not read", "", currentDate));
                MESSAGE.setText("");
            }
        });

        DP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfo.getKeyType().equalsIgnoreCase("sponsor")){
                    Intent intent = new Intent(ChatActivity.this, ViewUserChat.class);
                    intent.putExtra("id", id);
                    intent.putExtra("image_1", passport);
                    intent.putExtra("name", name);
                    intent.putExtra("about", about);
                    intent.putExtra("email", email);
                    intent.putExtra("image_2", passport2);
                    intent.putExtra("image_3", passport3);
                    intent.putExtra("location", location);
                    intent.putExtra("age", age);
                    intent.putExtra("phone", phone);
                    intent.putExtra("help_with", help_with);
                    intent.putExtra("gender", gender);
                    intent.putExtra("category", category);
                    intent.putExtra("occupation", occupation);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ChatActivity.this, ViewSponsorChat.class);
                    intent.putExtra("id", id);
                    intent.putExtra("image_1", passport);
                    intent.putExtra("name", name);
                    intent.putExtra("about", about);
                    intent.putExtra("email", email);
                    intent.putExtra("image_2", passport2);
                    intent.putExtra("image_3", passport3);
                    intent.putExtra("location", location);
                    intent.putExtra("age", age);
                    intent.putExtra("phone", phone);
                    intent.putExtra("help_with", help_with);
                    intent.putExtra("gender", gender);
                    intent.putExtra("category", category);
                    intent.putExtra("occupation", occupation);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        finish();
    }

    private void fetchChats() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.ALL_MSG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
                            JSONArray ar = datar.getJSONArray("data");
                            if(error){
                                Toast.makeText(ChatActivity.this, "Please ensure you have a working internet and try again!", Toast.LENGTH_SHORT).show();
                            }else {
                                data = new ArrayList<>();
                                for (int i=0; i<ar.length(); i++) {
                                    JSONObject user = ar.getJSONObject(i);
                                    String id = user.getString("id");
                                    String message = user.getString("message");
                                    String receiver = user.getString("receiver");
                                    String sender = user.getString("sender");
                                    String status = user.getString("status");
                                    String date = user.getString("date");
                                    String time = user.getString("time");

                                    data.add(new ChatsRepo(id, message, receiver, sender, status, date, time));
                                }

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());
                params.put("receiver", email);

                return params;
            }
        };

        VolleySingleton.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);

    }

    private void updateChats() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.UPDATE_CHATS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
                            if(error){
                                //Toast.makeText(ChatActivity.this, "Please ensure you have a working internet and try again!", Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ChatActivity.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);

    }

    private void fetchReg() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.ALL_MSG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
                            JSONArray ar = datar.getJSONArray("data");
                            if(error){
                                //Toast.makeText(ChatActivity.this, "Please ensure you have a working internet and try again!", Toast.LENGTH_SHORT).show();
                            }else {
                                data = new ArrayList<>();
                                for (int i=0; i<ar.length(); i++) {
                                    JSONObject user = ar.getJSONObject(i);
                                    String id = user.getString("id");
                                    String message = user.getString("message");
                                    String receiver = user.getString("receiver");
                                    String sender = user.getString("sender");
                                    String status = user.getString("status");
                                    String date = user.getString("date");
                                    String time = user.getString("time");

                                    data.add(new ChatsRepo(id, message, receiver, sender, status, date, time));
                                }

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(ChatActivity.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());
                params.put("receiver", email);

                return params;
            }
        };

        VolleySingleton.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);

    }

    private void deleteMsg(int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DELETE_MSG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("TAG", response);

                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
                            JSONArray ar = datar.getJSONArray("data");
                            if(error){
                                //Toast.makeText(ChatActivity.this, "Please ensure you have a working internet and try again!", Toast.LENGTH_SHORT).show();
                            }else {
                                arrayList.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ChatActivity.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", arrayList.get(position).getId());
                params.put("email", userInfo.getKeyEmail());
                params.put("receiver", email);

                return params;
            }
        };

        VolleySingleton.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);

    }

    private void sendMessage(final String message) {
        if (!message.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_MESSAGE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject resp = new JSONObject(response);
                                Boolean error = resp.getBoolean("error");
                                if (error) {
                                    //Toast.makeText(ChatActivity.this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.e("TAG", error.getMessage());
                            Toast.makeText(ChatActivity.this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userInfo.getKeyEmail());
                    params.put("receiver", email);
                    params.put("message", message);

                    return params;
                }
            };

            VolleySingleton.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);
        }
    }


}
