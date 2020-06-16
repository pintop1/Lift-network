package d.pintoptech.liftnetwork.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.ui.ViewUser;

public class RecyclerViewAdapterTwo extends RecyclerView.Adapter<RecyclerViewAdapterTwo.ViewHolder> {

    Context context;
    List<MembersRepo> arrayList;
    FragmentManager fragmentManager;

    public RecyclerViewAdapterTwo(Context context, List<MembersRepo> arrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requests_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MembersRepo repo = arrayList.get(position);
        UserInfo userInfo = new UserInfo(context);

        holder.Name.setText(repo.getName());
        holder.Count.setText(repo.getAge());
        Picasso.with(context).load(repo.getPassport1()).into(holder.DP);
        holder.Category.setText(repo.getCategory());
        holder.Gender.setText(repo.getGender());
        holder.Occupation.setText(repo.getOccupation());
        if(userInfo.getKeyType().equalsIgnoreCase("sponsor")){
            holder.Category.setVisibility(View.VISIBLE);
        }else {
            holder.Category.setVisibility(View.GONE);
        }
        holder.CONTENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUser requestFragment = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("image_1", repo.getPassport1());
                bundle.putString("image_2", repo.getPassport2());
                bundle.putString("image_3", repo.getPassport3());
                bundle.putString("name", repo.getName());
                bundle.putString("location", repo.getState()+", "+repo.getCountry());
                bundle.putString("age", repo.getAge());
                bundle.putString("about", repo.getAbout());
                bundle.putString("email", repo.getEmail());
                bundle.putString("phone", repo.getPhone());
                bundle.putString("help_with", repo.getHelp_with());
                bundle.putString("gender", repo.getGender());
                bundle.putString("category", repo.getCategory());
                bundle.putString("occupation", repo.getOccupation());
                bundle.putString("helping_with", repo.getHelping_with());
                bundle.putString("from", repo.getStatus());
                requestFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, requestFragment).addToBackStack("landing").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView DP;
        public TextView Name, Count, Category, Gender, Occupation;
        public LinearLayout CONTENT;

        public ViewHolder(View itemView) {
            super(itemView);

            DP = itemView.findViewById(R.id.dp);
            Name = itemView.findViewById(R.id.name);
            Count = itemView.findViewById(R.id.counts);
            CONTENT = itemView.findViewById(R.id.content);

            Category = itemView.findViewById(R.id.category);
            Gender = itemView.findViewById(R.id.gender);
            Occupation = itemView.findViewById(R.id.occupation);
        }
    }
}