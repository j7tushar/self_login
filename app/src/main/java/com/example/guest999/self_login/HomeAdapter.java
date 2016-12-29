package com.example.guest999.self_login;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by |<@/\/0 on 12/29/2016.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    public ArrayList<HashMap<String, String>> Joshi;
    Context context;

    public HomeAdapter(Context context, ArrayList<HashMap<String, String>> joshi) {
        this.context = context;
        this.Joshi = joshi;
        Log.e("HomeAdapter: ", Joshi + "");
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_list, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        String name = Joshi.get(position).get(Config.TAG_CITY_NAME);
        String image = Joshi.get(position).get(Config.TAG_CITY_IMAGE);

        holder.textView.setText(name);

        Picasso.with(context)
                .load(image)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return Joshi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_list);
            imageView = (ImageView) itemView.findViewById(R.id.iv_list);
        }
    }
}
