package d.pintoptech.liftnetwork.sponsor.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import d.pintoptech.liftnetwork.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private View fragView;
    private TextView TITLE, BACK, FILTER, SEARCHB;
    private LinearLayout SEARCHVIEW, FILTERVIEW;
    private EditText SEARCH;
    private Spinner CATEGORY, GENDER, OCCUPATION;
    private String dActive = "search";
    private Button PROCEED;
    private FragmentManager fragmentManager;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_search, container, false);
        bindData();
        return fragView;
    }

    private void bindData(){
        TITLE = fragView.findViewById(R.id.title);
        BACK = fragView.findViewById(R.id.back);
        FILTER = fragView.findViewById(R.id.filter);
        SEARCHVIEW = fragView.findViewById(R.id.searchView);
        FILTERVIEW = fragView.findViewById(R.id.filter_view);
        SEARCH = fragView.findViewById(R.id.search);
        CATEGORY = fragView.findViewById(R.id.filter_category);
        GENDER = fragView.findViewById(R.id.filter_gender);
        OCCUPATION = fragView.findViewById(R.id.filter_occupation);
        PROCEED = fragView.findViewById(R.id.proceed);
        SEARCHB = fragView.findViewById(R.id.searchb);
        fragmentManager = getActivity().getSupportFragmentManager();

        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStackImmediate();
            }
        });

        FILTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dActive = "filter";
                TITLE.setText("Filter Beneficiaries");
                SEARCHVIEW.setVisibility(View.GONE);
                FILTERVIEW.setVisibility(View.VISIBLE);
            }
        });

        SEARCHB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dActive = "search";
                TITLE.setText("Search Beneficiaries");
                FILTERVIEW.setVisibility(View.GONE);
                SEARCHVIEW.setVisibility(View.VISIBLE);
            }
        });

        PROCEED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dActive.equalsIgnoreCase("search")){
                    String search = SEARCH.getText().toString();
                    if(search.isEmpty()){
                        SEARCH.setError("Please enter your search query");
                        SEARCH.requestFocus();
                    }else {
                        SearchResults requestFragment = new SearchResults();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "search");
                        bundle.putString("query", search);
                        requestFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
                    }
                }else {
                    String category = CATEGORY.getSelectedItem().toString();
                    String gender = GENDER.getSelectedItem().toString();
                    String occupation = OCCUPATION.getSelectedItem().toString();
                    SearchResults requestFragment = new SearchResults();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "filter");
                    bundle.putString("category", category);
                    bundle.putString("gender", gender);
                    bundle.putString("occupation", occupation);
                    requestFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
                }
            }
        });
    }

}
