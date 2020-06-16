package d.pintoptech.liftnetwork.utils;

import android.content.Context;
import android.content.Intent;
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
import d.pintoptech.liftnetwork.beneficiary.MainPage;
import d.pintoptech.liftnetwork.beneficiary.RequestActivity;
import d.pintoptech.liftnetwork.beneficiary.ViewSponsor;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.ui.ViewUser;

public class RecyclerViewAdapterThree extends RecyclerView.Adapter<RecyclerViewAdapterThree.ViewHolder> {

    Context context;
    List<MembersRepo> arrayList;

    public RecyclerViewAdapterThree(Context context, List<MembersRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
                Intent intent = new Intent(context, ViewSponsor.class);
                intent.putExtra("id", repo.getId());
                intent.putExtra("image_1", repo.getPassport1());
                intent.putExtra("image_2", repo.getPassport2());
                intent.putExtra("image_3", repo.getPassport3());
                intent.putExtra("name", repo.getName());
                intent.putExtra("location", repo.getState()+", "+repo.getCountry());
                intent.putExtra("age", repo.getAge());
                intent.putExtra("about", repo.getAbout());
                intent.putExtra("email", repo.getEmail());
                intent.putExtra("phone", repo.getPhone());
                intent.putExtra("status", repo.getStatus());
                intent.putExtra("help_with", repo.getHelp_with());
                intent.putExtra("gender", repo.getGender());
                intent.putExtra("category", repo.getCategory());
                intent.putExtra("occupation", repo.getOccupation());
                intent.putExtra("helping_with", repo.getHelping_with());
                if(repo.getStatus().equalsIgnoreCase("pending")){
                    intent.putExtra("from", "pending");
                }else{
                    intent.putExtra("from", "accepted");
                }
                context.startActivity(intent);
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