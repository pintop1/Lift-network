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

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.beneficiary.ViewSponsor;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.ui.ViewUser;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    List<MembersChatRepo> arrayList;

    public ChatListAdapter(Context context, List<MembersChatRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_lists,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MembersChatRepo repo = arrayList.get(position);
        UserInfo userInfo = new UserInfo(context);

        holder.Name.setText(repo.getName());
        holder.Count.setText(repo.getUnread());
        if(repo.getUnread().equalsIgnoreCase("0")){
            holder.Count.setVisibility(View.GONE);
        }else {
            holder.Count.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(repo.getPassport1()).into(holder.DP);
        holder.MSG.setText(repo.getLast_msg());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView DP;
        public TextView Name, Count, MSG;

        public LinearLayout CONTENT;

        public ViewHolder(View itemView) {
            super(itemView);

            DP = itemView.findViewById(R.id.dp);
            Name = itemView.findViewById(R.id.name);
            Count = itemView.findViewById(R.id.counts);
            MSG = itemView.findViewById(R.id.msg);

            CONTENT = itemView.findViewById(R.id.content);
        }
    }
}