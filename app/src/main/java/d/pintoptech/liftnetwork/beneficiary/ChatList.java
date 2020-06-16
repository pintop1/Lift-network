package d.pintoptech.liftnetwork.beneficiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.utils.ChatListAdapter;
import d.pintoptech.liftnetwork.utils.MembersChatRepo;
import d.pintoptech.liftnetwork.utils.RecyclerTouchListener;

public class ChatList extends AppCompatActivity {

    List<MembersChatRepo> data;
    List<MembersChatRepo> dataFiltered;
    private RecyclerView recyclerview;
    private ArrayList<MembersChatRepo> arrayList;
    private ChatListAdapter adapter;
    private UserInfo userInfo;

    private EditText SEARCH;

    private RelativeLayout MAIN;
    private LinearLayout RELOAD;
    private RelativeLayout LOADER, RELOADER, EMPTY;

    private Timer timer;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        bindData();
        fetchChatList();
    }

    private void bindData(){
        userInfo = new UserInfo(this);
        MAIN = findViewById(R.id.main);
        RELOAD = findViewById(R.id.reload);
        LOADER = findViewById(R.id.loader);
        RELOADER = findViewById(R.id.reloader);
        EMPTY = findViewById(R.id.empty);
        recyclerview = findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new ChatListAdapter(this, arrayList);
        recyclerview = findViewById(R.id.recyclerview);
        SEARCH = findViewById(R.id.search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ChatList.this, ChatActivity.class);
                intent.putExtra("id", arrayList.get(position).getId());
                intent.putExtra("image_1", arrayList.get(position).getPassport1());
                intent.putExtra("name", arrayList.get(position).getName());
                intent.putExtra("about", arrayList.get(position).getAbout());
                intent.putExtra("email", arrayList.get(position).getEmail());
                intent.putExtra("image_2", arrayList.get(position).getPassport2());
                intent.putExtra("image_3", arrayList.get(position).getPassport3());
                intent.putExtra("location", arrayList.get(position).getState()+", "+arrayList.get(position).getCountry());
                intent.putExtra("age", arrayList.get(position).getAge());
                intent.putExtra("phone", arrayList.get(position).getPhone());
                intent.putExtra("help_with", arrayList.get(position).getHelp_with());
                intent.putExtra("gender", arrayList.get(position).getGender());
                intent.putExtra("category", arrayList.get(position).getCategory());
                intent.putExtra("occupation", arrayList.get(position).getOccupation());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                LinearLayout CONTENT = view.findViewById(R.id.content);
                LinearLayout TEXTCONTAIN = view.findViewById(R.id.textContain);
                CONTENT.setBackgroundColor(getResources().getColor(R.color.colorOff));
                TEXTCONTAIN.setBackgroundColor(getResources().getColor(R.color.colorOff));
                final CharSequence[] options = { "Clear Chat", "Close" };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatList.this);
                builder.setTitle("Action!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Clear Chat"))
                        {
                            deleteChats(position, arrayList.get(position).getEmail());
                            CONTENT.setBackgroundColor(getResources().getColor(R.color.colorPlain));
                            TEXTCONTAIN.setBackground(getResources().getDrawable(R.drawable.border_bottom_view));
                            dialog.dismiss();
                        }
                        else if (options[item].equals("Close"))
                        {
                            CONTENT.setBackgroundColor(getResources().getColor(R.color.colorPlain));
                            TEXTCONTAIN.setBackground(getResources().getDrawable(R.drawable.border_bottom_view));
                            dialog.dismiss();
                        }
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        CONTENT.setBackgroundColor(getResources().getColor(R.color.colorPlain));
                        TEXTCONTAIN.setBackground(getResources().getDrawable(R.drawable.border_bottom_view));
                    }
                });
                builder.show();
            }
        }));
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchChatList();
            }
        }, 0, 10000);   // 1000 Millisecond  = 1 second

    }

    private void deleteChats(int position, String receiver) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DELETE_CHATS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
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
                params.put("email", userInfo.getKeyEmail());
                params.put("receiver", receiver);

                return params;
            }
        };

        VolleySingleton.getInstance(ChatList.this).addToRequestQueue(stringRequest);

    }

    private void fetchChatList() {
        //startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CHAT_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //endAnimation();

                        try {
                            JSONObject datar = new JSONObject(response);
                            Boolean error = datar.getBoolean("error");
                            JSONArray ar = datar.getJSONArray("data");
                            if(error){
                                endAnimationEmpty();
                            }else {
                                data = new ArrayList<>();
                                for (int i=0; i<ar.length(); i++) {
                                    JSONObject user = ar.getJSONObject(i);
                                    String id = user.getString("id");
                                    String email = user.getString("email");
                                    String phone = user.getString("phone");
                                    String name = user.getString("name");
                                    String helped = user.getString("helped");
                                    String sort = user.getString("sort");

                                    String address = user.getString("address");
                                    String city = user.getString("city");
                                    String state = user.getString("state");
                                    String country = user.getString("country");
                                    String unread = user.getString("unread");
                                    String views = user.getString("views");
                                    String about = user.getString("about");

                                    String help_with = user.getString("help_with");
                                    String gender = user.getString("gender");
                                    String category = user.getString("category");
                                    String occupation = user.getString("occupation");
                                    String age = user.getString("age");


                                    JSONObject msgs = user.getJSONObject("msg");
                                    String last_msg = msgs.getString("last_msg");

                                    JSONArray passports = user.getJSONArray("passport");
                                    JSONObject passport1 = passports.getJSONObject(2);
                                    String passportOne = "https://liftnigeria.ng/mobile_app/Profile/"+passport1.getString("image");
                                    JSONObject passport2 = passports.getJSONObject(1);
                                    String passportTwo = "https://liftnigeria.ng/mobile_app/Profile/"+passport2.getString("image");
                                    JSONObject passport3 = passports.getJSONObject(0);
                                    String passportThree = "https://liftnigeria.ng/mobile_app/Profile/"+passport3.getString("image");
                                    data.add(new MembersChatRepo(sort, id, email, phone, name, helped, address, city, state, country, unread, views, about, passportOne,
                                            passportTwo, passportThree, help_with, gender, category, occupation, last_msg, age));
                                }

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();

                                //endAnimation();

                                MAIN.setVisibility(View.VISIBLE);

                                getSearch();
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            //endAnimationEmpty();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //endAnimationFail();
                        //Toast.makeText(ChatList.this, "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(ChatList.this).addToRequestQueue(stringRequest);

    }

    public void getSearch(){
        SEARCH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString().toUpperCase();

                dataFiltered = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {

                    final String text = data.get(i).getName().toUpperCase();
                    final String text2 = data.get(i).getCategory().toLowerCase();
                    final String text3 = data.get(i).getGender().toLowerCase();
                    final String text4 = data.get(i).getOccupation().toUpperCase();
                    if (text.contains(query) || text2.contains(query) || text3.contains(query) || text4.contains(query)) {

                        dataFiltered.add(data.get(i));
                    }
                }
                arrayList.clear();
                arrayList.addAll(dataFiltered);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void startAnimation(){
        MAIN.setVisibility(View.GONE);
        RELOADER.setVisibility(View.GONE);
        EMPTY.setVisibility(View.GONE);
        LOADER.setVisibility(View.VISIBLE);
    }

    private void endAnimation(){
        MAIN.setVisibility(View.VISIBLE);
        RELOADER.setVisibility(View.GONE);
        EMPTY.setVisibility(View.GONE);
        LOADER.setVisibility(View.GONE);
    }

    private void endAnimationEmpty(){
        MAIN.setVisibility(View.GONE);
        RELOADER.setVisibility(View.GONE);
        EMPTY.setVisibility(View.VISIBLE);
        LOADER.setVisibility(View.GONE);
    }

    private void endAnimationFail(){
        MAIN.setVisibility(View.GONE);
        RELOADER.setVisibility(View.VISIBLE);
        EMPTY.setVisibility(View.GONE);
        LOADER.setVisibility(View.GONE);
    }
}
