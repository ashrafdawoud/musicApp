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

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.ViewHolder> {
    ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    DownloadedsongsInterface downloadedsongsInterface;

    public DownloadsAdapter(ArrayList<HashMap<String, String>> songsList,DownloadedsongsInterface downloadedsongsInterface) {
        this.songsList = songsList;
        this.downloadedsongsInterface=downloadedsongsInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from (parent.getContext ()).inflate (R.layout.download_row,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.songname.setText(songsList.get(position).get("songTitle"));
        holder.cardcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadedsongsInterface.Callback5(songsList.get(position).get("songTitle"),songsList.get(position).get("songPath"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songname;
        CardView cardcontainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songname=itemView.findViewById(R.id.songename);
            cardcontainer=itemView.findViewById(R.id.cardcontainer);

        }
    }
    public  interface DownloadedsongsInterface{
        void Callback5(String track_name,String audio);

    }
}
