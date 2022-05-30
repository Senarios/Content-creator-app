//package com.senarios.coneqtlive;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.senarios.coneqtlive.Adapter.ParticipantsAdapter;
//import com.senarios.coneqtlive.Model.ParticipantsModel;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//
//public class ShowParticipantsSheet extends BottomSheetDialogFragment {
//    BottomSheetDialog bottomSheetDialog;
//    RecyclerView recyclerView;
//    ImageView crossBtn;
//    ParticipantsAdapter adapter;
//    private ArrayList<ParticipantsModel> arrayList;
//
//    public ShowParticipantsSheet(ParticipantsAdapter adapter, ArrayList<ParticipantsModel> arrayList) {
//        this.adapter = adapter;
//        this.arrayList = arrayList;
//    }
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.show_participants_bs_layout,
//                null);
//        bottomSheetDialog.setContentView(view);
//
//        crossBtn = view.findViewById(R.id.crossBtn);
//        crossBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//
//        recyclerView = view.findViewById(R.id.show_participants_rv);
//
//        recyclerView=view.findViewById(R.id.show_participants_rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<ParticipantsModel> options =
//                new FirebaseRecyclerOptions.Builder<ParticipantsModel>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("streams")
//                                .child("987654").child("participents"), ParticipantsModel.class).build();
//
//        adapter=new ParticipantsAdapter(options);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//        adapter.notifyDataSetChanged();
//        return bottomSheetDialog;
//    }
//
//    private void postrecord() {
//
//    }
//}
