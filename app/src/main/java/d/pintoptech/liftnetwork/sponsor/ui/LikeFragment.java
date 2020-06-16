package d.pintoptech.liftnetwork.sponsor.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import d.pintoptech.liftnetwork.utils.RecyclerTouchListener;
import d.pintoptech.liftnetwork.utils.RecyclerviewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends Fragment {

    View fragView;
    private FragmentManager fragmentManager;
    List<MembersRepo> data;
    List<MembersRepo> dataFiltered;
    private RecyclerView recyclerview;
    private ArrayList<MembersRepo> arrayList;
    private RecyclerviewAdapter adapter;
    private UserInfo userInfo;

    private EditText SEARCH;

    ScrollView MAIN;
    LinearLayout RELOAD;
    RelativeLayout LOADER, RELOADER, EMPTY;

    String query;


    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_like, container, false);

        bindData();

        return fragView;
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
        adapter = new RecyclerviewAdapter(getActivity(), arrayList, fragmentManager);
        recyclerview = fragView.findViewById(R.id.recyclerview);
        SEARCH = fragView.findViewById(R.id.search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);


        fetchHelpLists();
        RELOAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchHelpLists();
            }
        });

    }

    private void fetchHelpLists() {
        startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_HELP_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

                        //Log.e("TAG", response);
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

                                getSearch();
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
                params.put("email", userInfo.getKeyEmail());
                params.put("status", "helped");

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

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
