package com.aftershade.kozuki.HelperClasses.Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aftershade.kozuki.HelperClasses.MusicFiles;
import com.aftershade.kozuki.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Comparator;

public class MostPlayed extends RecyclerView.Adapter<MostPlayed.MostPlayedViewHolder> {

    Context context;
    ArrayList<MusicFiles> mostPlayedList;

    public MostPlayed(Context context, ArrayList<MusicFiles> mostPlayedList) {
        this.context = context;
        this.mostPlayedList = mostPlayedList;
    }

    @NonNull
    @Override
    public MostPlayedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.most_played_card, parent, false);
        return new MostPlayedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPlayedViewHolder holder, int position) {

        holder.artist.setText(mostPlayedList.get(position).getArtist());
        holder.songName.setText(mostPlayedList.get(position).getTitle());

        byte[] image = getAlbumArt(mostPlayedList.get(position).getPath());

        if (image != null){
            Glide.with(context).asBitmap().load(image).into(holder.imageView);
        } else {
            Glide.with(context).load(R.drawable.lens_flare).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, mostPlayedList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return mostPlayedList.size();
    }

    class MostPlayedViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView artist, songName;

        public MostPlayedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_most_played);
            artist = itemView.findViewById(R.id.most_played_artist);
            songName = itemView.findViewById(R.id.most_played_title);

        }
    }

    private byte[] getAlbumArt(String uri) {
        /*MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();*/

        return null;
    }
}
