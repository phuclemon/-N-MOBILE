package com.example.appnew.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnew.ImageUtil;
import com.example.appnew.R;
import com.example.appnew.admin.UpdateTinTucActivity;
import com.example.appnew.enity.TinTuc;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HienThiTinTucAdminAdapter extends RecyclerView.Adapter<HienThiTinTucAdminAdapter.myViewHolder> {

    Context context;
    List<TinTuc> list;
    ItemCallback itemCallback;
    public HienThiTinTucAdminAdapter(List<TinTuc> list, Context context, ItemCallback itemCallback) {
        this.context = context;
        this.list = list;
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.layout_tintuc_admin, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        TinTuc tinTuc = list.get(position);

        holder.tvTenBaiBao.setText(tinTuc.getTenBaiBao());
        holder.tvDate.setText(tinTuc.getNgayDang());
//        Picasso.get()
//                .load(tinTuc.getAnh())
//                .into(holder.img);

        if (tinTuc.getImagesAnhBia() != null) {
            StringBuilder ima = new StringBuilder();
            for (String s : tinTuc.getImagesAnhBia()) {
                ima.append(s);
            }
            if (ima.toString().isEmpty()) {
                holder.img.setImageResource(R.drawable.ic_placeholder);
            } else {
                holder.img.setImageBitmap(ImageUtil.decode(ima.toString()));
            }
        } else {
            holder.img.setImageResource(R.drawable.ic_placeholder);
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore
                        .getInstance()
                        .collection("TinTuc").whereEqualTo(FieldPath.documentId(), tinTuc.getIDBaiBao())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                WriteBatch a = FirebaseFirestore.getInstance().batch();
                                List<DocumentSnapshot> tinTuc = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : tinTuc){
                                    a.delete(snapshot.getReference());
                                }
                                a.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(view.getContext(), "Xoá tin tức thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        });
            }
        });
         holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(view1.getContext(), UpdateTinTucActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("IDTinTuc", tinTuc.getIDBaiBao());
                intent.putExtras(bundle);
                view1.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(view -> itemCallback.onItemClick("IDBaiBao:" + String.valueOf(tinTuc.getIDBaiBao())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTenBaiBao, tvDate;
        Button btnUpdate, btnDelete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTenBaiBao = itemView.findViewById(R.id.tv_title_item_news_admin);
            tvDate = itemView.findViewById(R.id.tv_date_news_admin);
            btnDelete = itemView.findViewById(R.id.btn_delete_admin);
            btnUpdate = itemView.findViewById(R.id.btn_update_admin);
            img = itemView.findViewById(R.id.iv_news_admin);

        }
    }
}
