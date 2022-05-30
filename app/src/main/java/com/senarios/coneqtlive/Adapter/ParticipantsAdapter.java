package com.senarios.coneqtlive.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.senarios.coneqtlive.Model.DatumBlock;
import com.senarios.coneqtlive.Model.ParticipantsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.senarios.coneqtlive.Model.ParticipentsModel;
import com.senarios.coneqtlive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {
    private List<ParticipentsModel> arrayList = new ArrayList<>();
    private Context context;
    private BlockAndKick blockAndKick;


    public ParticipantsAdapter(List<ParticipentsModel> arrayList, Context context , BlockAndKick blockAndKick) {
        this.arrayList = arrayList;
        this.context = context;
        this.blockAndKick = blockAndKick;
    }

    public interface BlockAndKick {
        void blockKick(String name , Integer Id);

        void kick(Integer Id);
    }

    @NonNull
    @Override
    public ParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.participants_item, parent, false);
        return new ParticipantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SharedPreferences preferences = context.getSharedPreferences("apiToken", MODE_PRIVATE);

        holder.personName.setText(arrayList.get(position).getName().trim());
        if(arrayList.get(position).getImage() !=null && !arrayList.get(position).getImage().isEmpty()) {
            Picasso.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImage())
                .into(holder.itemImageView);
        holder.itemImageView.setVisibility(View.VISIBLE);

        } else {
                String test = arrayList.get(position).getName().trim();
                String s = test.substring(0, 1).toUpperCase();
                holder.firstLetter.setText(s);
                holder.firstLetter.setVisibility(View.VISIBLE);
            }
        holder.kickOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockAndKick.kick(arrayList.get(position).getId());
            }
        });
        holder.blockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockAndKick.blockKick(arrayList.get(position).getName().trim(), arrayList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView personName, idUnBlockText , firstLetter , kickOut , blockOut;
        private ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ///////// Person TextView
            personName = itemView.findViewById(R.id.part_name);
            /////// ImageView Setup
            itemImageView = itemView.findViewById(R.id.participant_image);

            firstLetter = itemView.findViewById(R.id.idFirstLetter);

            kickOut = itemView.findViewById(R.id.kick_out_part);
            blockOut = itemView.findViewById(R.id.block_user);


        }
    }
}
