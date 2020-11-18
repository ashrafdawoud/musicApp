package com.indrul.musicapp.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indrul.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommonAlbums extends RecyclerView.Adapter<CommonAlbums.ViewHolder> {
    ArrayList<String> image=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> singerName=new ArrayList<>();
    ArrayList<String> Tracks=new ArrayList<>();
    Showalbum showalbum;

    public CommonAlbums(ArrayList<String> image, ArrayList<String> name, ArrayList<String> singerName, ArrayList<String> tracks,Showalbum showalbum) {
        this.image = image;
        this.name = name;
        this.singerName = singerName;
        this.Tracks = tracks;
        this.showalbum=showalbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.album_row,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(image.get(position)).into(holder.imageView);
        holder.textView.setText(name.get(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showalbum.callbackShowAlbum(Tracks.get(position),name.get(position),image.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.trackname);
            container=itemView.findViewById(R.id.container);

        }
    }
    public interface Showalbum{
        void callbackShowAlbum(String trcks,String name,String image);
    }
}
