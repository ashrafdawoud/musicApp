package com.indrul.musicapp.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.indrul.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    ArrayList<String> track_name=new ArrayList<>();
    ArrayList<String>artist_name=new ArrayList<>();
    ArrayList<String>image=new ArrayList<>();
    ArrayList<String>audio=new ArrayList<>();
    ArrayList<String>audio_downloaded=new ArrayList<>();
    SearchedsongsInterface searchedsongsInterface;
    Download download;

    public SearchAdapter(ArrayList<String> track_name, ArrayList<String> artist_name, ArrayList<String> image, ArrayList<String> audio, ArrayList<String> audio_downloaded,SearchedsongsInterface searchedsongsInterface ,Download download) {
        this.track_name = track_name;
        this.artist_name = artist_name;
        this.image = image;
        this.audio = audio;
        this.audio_downloaded = audio_downloaded;
        this.searchedsongsInterface=searchedsongsInterface;
        this.download=download;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.search_row,parent,false);
       ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Picasso.get().load(image.get(position)).into(holder.image);
            holder.singername.setText(artist_name.get(position));
            holder.songname.setText(track_name.get(position));
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchedsongsInterface.Callback2(track_name.get(position) ,artist_name.get(position) , image.get(position) ,audio.get(position) ,audio_downloaded.get(position));
                }
            });
            holder.download2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    download.Callback3(audio.get(position));
                }
            });
            holder.cardcontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchedsongsInterface.Callback2(track_name.get(position) ,artist_name.get(position) , image.get(position) ,audio.get(position) ,audio_downloaded.get(position));
                }
            });
        }catch (IndexOutOfBoundsException e){}



    }

    @Override
    public int getItemCount() {
        return audio.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,download2,play;
        TextView songname,singername;
        CardView cardcontainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            download2=itemView.findViewById(R.id.download);
            play=itemView.findViewById(R.id.play);
            songname=itemView.findViewById(R.id.songename);
            singername=itemView.findViewById(R.id.singername);
            cardcontainer=itemView.findViewById(R.id.cardcontainer);

        }
    }
    public  interface SearchedsongsInterface{
        void Callback2(String track_name, String artist_name ,String image,String audio, String audio_downloaded);

    }
    public  interface Download{
        void Callback3( String audio_downloaded);

    }
}
