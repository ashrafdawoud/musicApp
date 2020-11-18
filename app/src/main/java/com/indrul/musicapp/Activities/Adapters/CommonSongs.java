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

public class CommonSongs extends RecyclerView.Adapter<CommonSongs.ViewHolder> {
    ArrayList<String> track_name=new ArrayList<>();
    ArrayList<String>artist_name=new ArrayList<>();
    ArrayList<String>image=new ArrayList<>();
    ArrayList<String>audio=new ArrayList<>();
    ArrayList<String>audio_downloaded=new ArrayList<>();
    CommonsongsInterface commonsongsInterface;

    public CommonSongs(ArrayList<String> track_name, ArrayList<String> artist_name, ArrayList<String> image, ArrayList<String> audio, ArrayList<String> audio_downloaded ,CommonsongsInterface commonsongsInterface) {
        this.track_name = track_name;
        this.artist_name = artist_name;
        this.image = image;
        this.audio = audio;
        this.audio_downloaded = audio_downloaded;
        this.commonsongsInterface=commonsongsInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.song_row,parent,false);
        ViewHolder vh=new ViewHolder (view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(image.get(position)).into(holder.poster);
        holder.name.setText(track_name.get(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonsongsInterface.Callback(track_name.get(position) ,artist_name.get(position) , image.get(position) ,audio.get(position) ,audio_downloaded.get(position) );
            }
        });
    }

    @Override
    public int getItemCount() {
        return audio.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;
        LinearLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.poster);
            name=itemView.findViewById(R.id.name);
            container=itemView.findViewById(R.id.container);

        }
    }
    public  interface CommonsongsInterface{
        void Callback(String track_name, String artist_name ,String image,String audio, String audio_downloaded);

    }
}
