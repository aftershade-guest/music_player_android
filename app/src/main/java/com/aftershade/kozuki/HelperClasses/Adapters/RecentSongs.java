package com.aftershade.kozuki.HelperClasses.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aftershade.kozuki.HelperClasses.MusicFiles;
import com.aftershade.kozuki.MainScreen.MainActivity;
import com.aftershade.kozuki.Play.PlayPauseActivity;
import com.aftershade.kozuki.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class RecentSongs extends RecyclerView.Adapter<RecentSongs.RecentViewHolder> {

    private final Context mContext;
    public static ArrayList<MusicFiles> recentFiles;

    public RecentSongs(Context mContext, ArrayList<MusicFiles> recentFiles) {
        this.mContext = mContext;
        RecentSongs.recentFiles = recentFiles;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recent_card, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {

        holder.songName.setText(recentFiles.get(position).getTitle());
        byte[] image = getAlbumArt(recentFiles.get(position).getPath());

        if (image != null) {
            Glide.with(mContext).asBitmap().load(image).into(holder.songArt);
        } else {
            Glide.with(mContext).load(R.drawable.lens_flare).into(holder.songArt);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayPauseActivity.class);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return recentFiles.size();
    }

    class RecentViewHolder extends RecyclerView.ViewHolder {

        TextView songName;
        ImageView songArt;

        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.recent_song_name_);
            songArt = itemView.findViewById(R.id.recent_song_art);

        }
    }

    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();

        return art;
    }


    void updateList(ArrayList<MusicFiles> musicFilesArray) {
        recentFiles = new ArrayList<>();
        recentFiles.addAll(musicFilesArray);
        notifyDataSetChanged();
    }
}
