package d.pintoptech.liftnetwork.sponsor.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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

import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.Utils;
import d.pintoptech.liftnetwork.VolleySingleton;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.utils.MembersRepo;
import d.pintoptech.liftnetwork.utils.RecyclerViewAdapterSearch;
import d.pintoptech.liftnetwork.utils.RecyclerViewAdapterTwo;
import d.pintoptech.liftnetwork.utils.RecyclerviewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResults extends Fragment {

    private String type = "";
    private String query, category, gender, occupation;
    private View fragView;

    private FragmentManager fragmentManager;
    List<MembersRepo> data;
    private RecyclerView recyclerview;
    private ArrayList<MembersRepo> arrayList;
    private RecyclerViewAdapterSearch adapter;
    private UserInfo userInfo;

    private TextView SEARCH;

    ScrollView MAIN;
    LinearLayout RELOAD;
    RelativeLayout LOADER, RELOADER, EMPTY;



    public SearchResults() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_search_results, container, false);
        getBundle();
        bindData();
        return  fragView;
    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            if(type.equalsIgnoreCase("search")){
                query = bundle.getString("query");
            }else if(type.equalsIgnoreCase("filter")){
                category = bundle.getString("category");
                gender = bundle.getString("gender");
                occupation = bundle.getString("occupation");
            }
        }

    }

    private void bindData(){
        fragmentManager = getActivity().getSupportFragmentManager();
        userInfo = new UserInfo(getActivity());
        MAIN = fragView.findViewById(R.id.main);
        RELOAD = fragView.findViewById(R.id.reload);
        LOADER = fragView.findViewById(R.id.loader);
        RELOADER = fragView.findViewById(R.id.reloader);
        EMPTY = fragView.findViewById(R.id.empty);
        recyclerview = fragView.findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapterSearch(getActivity(), arrayList, fragmentManager);
        recyclerview = fragView.findViewById(R.id.recyclerview);
        SEARCH = fragView.findViewById(R.id.search);
        SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SearchFragment()).addToBackStack("landing").commit();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        recyclerview.setAdapter(adapter);
        if(type.equalsIgnoreCase("search")){
            search();
        }else {
            filter();
        }
    }

    private void search() {
        startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

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
                                    String age = user.getString("age");
                                    String views = user.getString("views");
                                    String about = user.getString("about");
                                    String status = user.getString("status");

                                    String help_with = user.getString("help_with");
                                    String gender = user.getString("gender");
                                    String category = user.getString("category");
                                    String occupation = user.getString("occupation");
                                    String helping_with = user.getString("helping_with");

                                    JSONArray passports = user.getJSONArray("passport");
                                    JSONObject passport1 = passports.getJSONObject(2);
                                    String passportOne = "https://liftnigeria.ng/mobile_app/Profile/"+passport1.getString("image");
                                    JSONObject passport2 = passports.getJSONObject(1);
                                    String passportTwo = "https://liftnigeria.ng/mobile_app/Profile/"+passport2.getString("image");
                                    JSONObject passport3 = passports.getJSONObject(0);
                                    String passportThree = "https://liftnigeria.ng/mobile_app/Profile/"+passport3.getString("image");
                                    data.add(new MembersRepo(sort, id, email, phone, name, helped, address, city, state, country, age, views, about, status, passportOne,
                                            passportTwo, passportThree, help_with, gender, category, occupation, helping_with));
                                }

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();

                                endAnimation();

                                MAIN.setVisibility(View.VISIBLE);

                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            endAnimationEmpty();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endAnimationFail();
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search", query);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void filter() {
        startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

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
                                    String age = user.getString("age");
                                    String views = user.getString("views");
                                    String about = user.getString("about");
                                    String status = user.getString("status");

                                    String help_with = user.getString("help_with");
                                    String gender = user.getString("gender");
                                    String category = user.getString("category");
                                    String occupation = user.getString("occupation");
                                    String helping_with = user.getString("helping_with");

                                    JSONArray passports = user.getJSONArray("passport");
                                    JSONObject passport1 = passports.getJSONObject(2);
                                    String passportOne = "https://liftnigeria.ng/mobile_app/Profile/"+passport1.getString("image");
                                    JSONObject passport2 = passports.getJSONObject(1);
                                    String passportTwo = "https://liftnigeria.ng/mobile_app/Profile/"+passport2.getString("image");
                                    JSONObject passport3 = passports.getJSONObject(0);
                                    String passportThree = "https://liftnigeria.ng/mobile_app/Profile/"+passport3.getString("image");
                                    data.add(new MembersRepo(sort, id, email, phone, name, helped, address, city, state, country, age, views, about, status, passportOne,
                                            passportTwo, passportThree, help_with, gender, category, occupation, helping_with));
                                }

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();

                                endAnimation();

                                MAIN.setVisibility(View.VISIBLE);

                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                            endAnimationEmpty();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endAnimationFail();
                        Toast.makeText(getActivity(), "Please ensure you have a working internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category", category);
                params.put("gender", gender);
                params.put("occupation", occupation);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

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
        MAIN.setVisibility(View.VISIBLE);
        RELOADER.setVisibility(View.GONE);
        EMPTY.setVisibility(View.VISIBLE);
        LOADER.setVisibility(View.GONE);
    }

    private void endAnimationFail(){
        MAIN.setVisibility(View.VISIBLE);
        RELOADER.setVisibility(View.VISIBLE);
        EMPTY.setVisibility(View.GONE);
        LOADER.setVisibility(View.GONE);
    }

}
