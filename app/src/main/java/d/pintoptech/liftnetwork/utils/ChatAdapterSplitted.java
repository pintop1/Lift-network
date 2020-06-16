package d.pintoptech.liftnetwork.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import d.pintoptech.liftnetwork.ChatActivity;
import d.pintoptech.liftnetwork.R;
import d.pintoptech.liftnetwork.beneficiary.ViewSponsor;
import d.pintoptech.liftnetwork.prefs.UserInfo;
import d.pintoptech.liftnetwork.sponsor.ui.ViewUser;

public class ChatAdapterSplitted extends RecyclerView.Adapter<ChatAdapterSplitted.ViewHolder> {

    Context context;
    List<ChatsRepo> arrayList;

    public ChatAdapterSplitted(Context context, List<ChatsRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_lists,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ChatsRepo repo = arrayList.get(position);
        UserInfo userInfo = new UserInfo(context);
        if(!repo.getDate().isEmpty()){
            holder.DATE.setText(repo.getDate());
        }

        if(userInfo.getKeyEmail().equalsIgnoreCase(repo.getSender())){
            holder.FOROTHER.setVisibility(View.GONE);
            holder.FORME.setVisibility(View.VISIBLE);
            holder.MSGME.setText(repo.getMessage());
            holder.TIMEME.setText(repo.getTime());
        }else {
            holder.FORME.setVisibility(View.GONE);
            holder.FOROTHER.setVisibility(View.VISIBLE);
            holder.MSGOTHER.setText(repo.getMessage());
            holder.TIMEOTHER.setText(repo.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView MSGME, MSGOTHER, TIMEME, TIMEOTHER, DATE;

        public LinearLayout FORME, FOROTHER;

        public ViewHolder(View itemView) {
            super(itemView);

            MSGME = itemView.findViewById(R.id.msgMe);
            MSGOTHER = itemView.findViewById(R.id.msgOther);
            TIMEME = itemView.findViewById(R.id.timeMe);
            TIMEOTHER = itemView.findViewById(R.id.timeOther);
            DATE = itemView.findViewById(R.id.date);

            FORME = itemView.findViewById(R.id.forMe);
            FOROTHER = itemView.findViewById(R.id.forOther);
        }
    }
}