package d.pintoptech.liftnetwork.sponsor.ui;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.prefs.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfile extends Fragment {

    private TextView BACK, DONE;
    private ImageView IMAGEONE;
    private TextView NAME, PHONE;
    private TextView EMAIL;
    private TextView ABOUT, ADDR, CITY, STATE, COUNTRY, OCCUPATION;
    private TextView GENDER;
    private String gender = "";

    private UserInfo userInfo;
    private ProgressDialog progressDialog;

    private View fragView;
    private FragmentManager fragmentManager;
    private String up1 = "";


    public ViewProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        bindData();
        setData();
        return fragView;
    }

    private void bindData(){
        BACK = fragView.findViewById(R.id.back);
        DONE = fragView.findViewById(R.id.done);

        IMAGEONE = fragView.findViewById(R.id.numberOne);

        NAME = fragView.findViewById(R.id.name);
        PHONE = fragView.findViewById(R.id.phone);

        EMAIL = fragView.findViewById(R.id.email);

        ADDR = fragView.findViewById(R.id.addr);
        CITY = fragView.findViewById(R.id.city);
        STATE = fragView.findViewById(R.id.state);
        COUNTRY = fragView.findViewById(R.id.country);

        ABOUT = fragView.findViewById(R.id.about);
        GENDER = fragView.findViewById(R.id.gender);

        OCCUPATION = fragView.findViewById(R.id.occupation);

        userInfo = new UserInfo(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        fragmentManager = getActivity().getSupportFragmentManager();
        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStackImmediate();
            }
        });

    }

    private void setData(){
        Picasso.with(getActivity()).load(userInfo.getKeyPassport())
                .placeholder(R.drawable.searches)
                .error(R.drawable.searches)
                .into(IMAGEONE);

        NAME.setText(userInfo.getKeyName());
        PHONE.setText(userInfo.getKeyPhone());
        EMAIL.setText(userInfo.getKeyEmail());
        ADDR.setText(userInfo.getKeyAddress());
        CITY.setText(userInfo.getKeyCity());
        STATE.setText(userInfo.getKeyState());
        COUNTRY.setText(userInfo.getKeyCountry());
        ABOUT.setText(userInfo.getKeyAbout());

        GENDER.setText(userInfo.getKeyGender());
        OCCUPATION.setText(userInfo.getKeyOccupation());
    }

}
