package com.senarios.coneqtlive.Adapter;

import static android.content.Context.MODE_PRIVATE;

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
import com.senarios.coneqtlive.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.ViewHolder> {
    private List<DatumBlock> arrayList = new ArrayList<>();
    private Context context;
    private UnBlockUser unBlockUser;


    public BlockedAdapter(List<DatumBlock> arrayList, Context context, UnBlockUser unBlockUser) {
        this.arrayList = arrayList;
        this.context = context;
        this.unBlockUser = unBlockUser;
    }
    public interface UnBlockUser {
        void userUnBlock (String id);
    }

    @NonNull
    @Override
    public BlockedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.block_item_list_view, parent, false);
        return new BlockedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.personName.setText(arrayList.get(position).getName());
        if(arrayList.get(position).getImageUrl() !=null && !arrayList.get(position).getImageUrl().isEmpty()) {
            Picasso.with(holder.itemView.getContext())
                    .load(arrayList.get(position).getImageUrl())
                    .into(holder.itemImageView);
            holder.firstLetter.setVisibility(View.GONE);

        } else {
            String test = arrayList.get(position).getName();
            String s = test.substring(0, 1).toUpperCase();
            holder.firstLetter.setText(s);
            holder.firstLetter.setVisibility(View.VISIBLE);
        }

        holder.idUnBlockText.setOnClickListener(v -> {
            unBlockUser.userUnBlock(String.valueOf(arrayList.get(position).getContentViewerId()));
//          unBlockUser.userUnBlock(String id);
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
        private TextView personName , idUnBlockText , firstLetter  ;
        private ImageView itemImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ///////// Person TextView
            personName = itemView.findViewById(R.id.idPersonNameTxt);
            /////// UnBlock TextView
            idUnBlockText = itemView.findViewById(R.id.idUnBlockText);

            /////// ImageView Setup
            itemImageView = itemView.findViewById(R.id.idBlockImage);
            firstLetter = itemView.findViewById(R.id.idFirstLetter);


        }
    }
}