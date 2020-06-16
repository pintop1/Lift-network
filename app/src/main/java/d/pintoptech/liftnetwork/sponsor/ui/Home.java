package d.pintoptech.liftnetwork.sponsor.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

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
import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.beneficiary.ProfileActivity;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.utils.MembersRepo;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private List<MembersRepo> datar;
    private ArrayList<MembersRepo> arrayList;
    private SwipeFlingAdapterView flingContainer;
    private View fragView, popupView;
    private UserInfo userInfo;
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private String activeString = "1";
    private PopupWindow popupWindow;

    private LinearLayout CONTENT, RELOAD;
    private RelativeLayout LOADER, RELOADER, EMPTY;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    private TextView SEARCH;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_home, container, false);
        bindData();
        return fragView;
    }

    private void bindData(){
        flingContainer = fragView.findViewById(R.id.frame);
        arrayList = new ArrayList<>();
        myAppAdapter = new MyAppAdapter(arrayList, getActivity());
        RELOADER = fragView.findViewById(R.id.reloader);
        RELOAD = fragView.findViewById(R.id.reload);
        EMPTY = fragView.findViewById(R.id.empty);
        SEARCH = fragView.findViewById(R.id.search);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        flingContainer.setAdapter(myAppAdapter);
        fragmentManager = getActivity().getSupportFragmentManager();
        SEARCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SearchFragment()).addToBackStack("landing").commit();
            }
        });
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                sendRequest("", arrayList.get(0).getEmail(), "");
                arrayList.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if(arrayList.size() < 1){
                    getData();
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                sendRequest("", arrayList.get(0).getEmail(), "");
                arrayList.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if(arrayList.size() < 1){
                    getData();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();

                /*view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);*/
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(final int itemPosition, Object dataObject) {

                final View view = flingContainer.getSelectedView();
                view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activeString.equalsIgnoreCase("1")){
                            TextView PS2 = view.findViewById(R.id.pass2);
                            ImageView img = view.findViewById(R.id.avatar);
                            TextView button1 = view.findViewById(R.id.activeOne);
                            TextView button2 = view.findViewById(R.id.activeTwo);
                            TextView button3 = view.findViewById(R.id.activeThree);
                            String pass2 = PS2.getText().toString();
                            Picasso.with(getActivity()).load(pass2)
                                    .placeholder(R.drawable.searches)
                                    .error(R.drawable.searches)
                                    .into(img);
                            button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            button2.setBackground(getResources().getDrawable(R.drawable.active_image));
                            button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            activeString = "2";
                        }else if(activeString.equalsIgnoreCase("2")){
                            TextView PS3 = view.findViewById(R.id.pass3);
                            ImageView img = view.findViewById(R.id.avatar);
                            TextView button1 = view.findViewById(R.id.activeOne);
                            TextView button2 = view.findViewById(R.id.activeTwo);
                            TextView button3 = view.findViewById(R.id.activeThree);
                            String pass3 = PS3.getText().toString();
                            Picasso.with(getActivity()).load(pass3)
                                    .placeholder(R.drawable.searches)
                                    .error(R.drawable.searches)
                                    .into(img);
                            button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            button2.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            button3.setBackground(getResources().getDrawable(R.drawable.active_image));
                            activeString = "3";
                        }
                    }
                });

                view.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activeString.equalsIgnoreCase("3")){
                            TextView PS2 = view.findViewById(R.id.pass2);
                            ImageView img = view.findViewById(R.id.avatar);
                            TextView button1 = view.findViewById(R.id.activeOne);
                            TextView button2 = view.findViewById(R.id.activeTwo);
                            TextView button3 = view.findViewById(R.id.activeThree);
                            String pass2 = PS2.getText().toString();
                            Picasso.with(getActivity()).load(pass2)
                                    .placeholder(R.drawable.searches)
                                    .error(R.drawable.searches)
                                    .into(img);
                            button1.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            button2.setBackground(getResources().getDrawable(R.drawable.active_image));
                            button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            activeString = "2";
                        }else if(activeString.equalsIgnoreCase("2")){
                            TextView PS1 = view.findViewById(R.id.pass1);
                            ImageView img = view.findViewById(R.id.avatar);
                            TextView button1 = view.findViewById(R.id.activeOne);
                            TextView button2 = view.findViewById(R.id.activeTwo);
                            TextView button3 = view.findViewById(R.id.activeThree);
                            String pass1 = PS1.getText().toString();
                            Picasso.with(getActivity()).load(pass1)
                                    .placeholder(R.drawable.searches)
                                    .error(R.drawable.searches)
                                    .into(img);
                            button1.setBackground(getResources().getDrawable(R.drawable.active_image));
                            button2.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            button3.setBackground(getResources().getDrawable(R.drawable.inactive_image));
                            activeString = "1";
                        }
                    }
                });

                view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest("", arrayList.get(0).getEmail(), "");
                        arrayList.remove(0);
                        myAppAdapter.notifyDataSetChanged();
                        if(arrayList.size() < 1){
                            getData();
                        }
                    }
                });

                view.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewUser requestFragment = new ViewUser();
                        Bundle bundle = new Bundle();
                        bundle.putString("image_1", arrayList.get(0).getPassport1());
                        bundle.putString("image_2", arrayList.get(0).getPassport2());
                        bundle.putString("image_3", arrayList.get(0).getPassport3());
                        bundle.putString("name", arrayList.get(0).getName());
                        bundle.putString("location", arrayList.get(0).getState()+", "+arrayList.get(0).getCountry());
                        bundle.putString("age", arrayList.get(0).getAge());
                        bundle.putString("about", arrayList.get(0).getAbout());
                        bundle.putString("email", arrayList.get(0).getEmail());
                        bundle.putString("phone", arrayList.get(0).getPhone());
                        bundle.putString("from", "");
                        bundle.putString("help_with", arrayList.get(0).getHelp_with());
                        bundle.putString("gender", arrayList.get(0).getGender());
                        bundle.putString("category", arrayList.get(0).getCategory());
                        bundle.putString("occupation", arrayList.get(0).getOccupation());
                        bundle.putString("from", "home");
                        requestFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
                    }
                });

                view.findViewById(R.id.name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewUser requestFragment = new ViewUser();
                        Bundle bundle = new Bundle();
                        bundle.putString("image_1", arrayList.get(0).getPassport1());
                        bundle.putString("image_2", arrayList.get(0).getPassport2());
                        bundle.putString("image_3", arrayList.get(0).getPassport3());
                        bundle.putString("name", arrayList.get(0).getName());
                        bundle.putString("location", arrayList.get(0).getState()+", "+arrayList.get(0).getCountry());
                        bundle.putString("age", arrayList.get(0).getAge());
                        bundle.putString("about", arrayList.get(0).getAbout());
                        bundle.putString("email", arrayList.get(0).getEmail());
                        bundle.putString("phone", arrayList.get(0).getPhone());
                        bundle.putString("from", "");
                        bundle.putString("help_with", arrayList.get(0).getHelp_with());
                        bundle.putString("gender", arrayList.get(0).getGender());
                        bundle.putString("category", arrayList.get(0).getCategory());
                        bundle.putString("occupation", arrayList.get(0).getOccupation());
                        bundle.putString("from", "home");
                        requestFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
                    }
                });

                //myAppAdapter.notifyDataSetChanged();
            }
        });
        userInfo = new UserInfo(getActivity());
        LOADER = fragView.findViewById(R.id.loader);
        CONTENT = fragView.findViewById(R.id.content);
        getData();
        RELOAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData(){
        startAnimation();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DISCOVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation();
                        datar = new ArrayList<>();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(!error){
                                JSONArray data = resp.getJSONArray("data");
                                for (int i=0; i<data.length(); i++) {
                                    JSONObject user = data.getJSONObject(i);
                                    String sort = user.getString("sort");
                                    String id = user.getString("id");
                                    String email = user.getString("email");
                                    String phone = user.getString("phone");
                                    String name = user.getString("name");
                                    String helped = user.getString("helped");

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
                                    datar.add(new MembersRepo(sort, id, email, phone, name, helped, address, city, state, country, age, views, about, status, passportOne,
                                            passportTwo, passportThree, help_with, gender, category, occupation, helping_with));
                                }
                                arrayList.clear();
                                myAppAdapter.notifyDataSetChanged();
                                arrayList.addAll(datar);
                                myAppAdapter.notifyDataSetChanged();


                            }else {
                                EMPTY.setVisibility(View.VISIBLE);
                            }
                        }catch(JSONException e){
                            EMPTY.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endAnimationfail();
                        //Log.e("TAG", error.getMessage());
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

    public static class ViewHolder {
        public static CardView background;
        public TextView RELOAD, CLOSE, HELP, VIEW, NAME, LOCATION, AGE, ACTIV1, ACTIV2, ACTIV3;
        public ImageView AVATAR;
        public TextView PASS1, PASS2, PASS3;
        public FrameLayout frameLayout;


    }

    public class MyAppAdapter extends BaseAdapter {


        public List<MembersRepo> parkingList;
        public Context context;

        private MyAppAdapter(List<MembersRepo> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.user_list_view, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                //viewHolder.background =  rowView.findViewById(R.id.background);
                viewHolder.AVATAR =  rowView.findViewById(R.id.avatar);
                viewHolder.PASS1 =  rowView.findViewById(R.id.pass1);
                viewHolder.PASS2 =  rowView.findViewById(R.id.pass2);
                viewHolder.PASS3 =  rowView.findViewById(R.id.pass3);
                viewHolder.CLOSE =  rowView.findViewById(R.id.close);
                viewHolder.VIEW =  rowView.findViewById(R.id.view);
                viewHolder.NAME =  rowView.findViewById(R.id.name);
                viewHolder.LOCATION =  rowView.findViewById(R.id.location);
                viewHolder.AGE =  rowView.findViewById(R.id.age);
                viewHolder.ACTIV1 = rowView.findViewById(R.id.activeOne);
                viewHolder.ACTIV2 = rowView.findViewById(R.id.activeTwo);
                viewHolder.ACTIV3 = rowView.findViewById(R.id.activeThree);
                viewHolder.frameLayout = rowView.findViewById(R.id.vd);



                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String avatar1 = parkingList.get(position).getPassport1();
            String avatar2 = parkingList.get(position).getPassport2();
            String avatar3 = parkingList.get(position).getPassport3();
            Picasso.with(getActivity()).load(avatar1)
                    .placeholder(R.drawable.searches)
                    .error(R.drawable.searches)
                    .into(viewHolder.AVATAR);
            viewHolder.PASS1.setText(avatar1);
            viewHolder.PASS2.setText(avatar2);
            viewHolder.PASS3.setText(avatar3);
            viewHolder.NAME.setText(parkingList.get(position).getName());
            viewHolder.LOCATION.setText(parkingList.get(position).getState()+", "+parkingList.get(position).getCountry());
            viewHolder.AGE.setText(parkingList.get(position).getAge());
            viewHolder.ACTIV1.setBackground(getResources().getDrawable(R.drawable.active_image));
            viewHolder.ACTIV2.setBackground(getResources().getDrawable(R.drawable.inactive_image));
            viewHolder.ACTIV2.setBackground(getResources().getDrawable(R.drawable.inactive_image));

            return rowView;
        }
    }

    private void startAnimation(){
        LOADER.setVisibility(View.VISIBLE);
        CONTENT.setClickable(false);
        CONTENT.setFocusableInTouchMode(false);
        CONTENT.setFocusable(false);
        RELOADER.setVisibility(View.GONE);
    }

    private void endAnimation(){
        LOADER.setVisibility(View.GONE);
        CONTENT.setClickable(true);
        CONTENT.setFocusableInTouchMode(true);
        CONTENT.setFocusable(true);
        RELOADER.setVisibility(View.GONE);
    }

    private void endAnimationfail(){
        LOADER.setVisibility(View.GONE);
        CONTENT.setClickable(true);
        CONTENT.setFocusableInTouchMode(true);
        CONTENT.setFocusable(true);
        RELOADER.setVisibility(View.VISIBLE);
    }

    private void sendRequest(final String type, final String beneficiary, final String helping_with){
        //Log.e("TAG SIZE", arrayList.size()+type);
        if(type.equalsIgnoreCase("help")){
            startAnimation2();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(type.equalsIgnoreCase("help")){
                            endAnimation2();
                        }

                        endAnimation();

                        try {
                            JSONObject resp = new JSONObject(response);
                            Boolean error = resp.getBoolean("error");
                            if(error){
                                Toast.makeText(getActivity(), "Please check your internet and try again!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Your request has been sent successfully.", Toast.LENGTH_SHORT).show();
                                if(type.equalsIgnoreCase("help")){
                                    popupWindow.dismiss();
                                    arrayList.remove(0);
                                    myAppAdapter.notifyDataSetChanged();
                                    if(arrayList.size() < 1){
                                        getData();
                                    }

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
                        endAnimationfail();
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

    private void startAnimation2(){
        progressDialog.setMessage("sending request..");
        progressDialog.show();
    }

    private void endAnimation2(){
        progressDialog.hide();
        progressDialog.cancel();
    }

    private void showPopUp(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.search_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        /*final EditText HELPINGWITH = popupView.findViewById(R.id.helping_with);
        TextView CLOSE = popupView.findViewById(R.id.close);
        Button SUBMIT = popupView.findViewById(R.id.proceed);



        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hpw = HELPINGWITH.getText().toString();
                sendRequest("help", beneficiary, hpw);
                popupWindow.dismiss();
            }
        });*/


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        /*CLOSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });*/
    }

}
