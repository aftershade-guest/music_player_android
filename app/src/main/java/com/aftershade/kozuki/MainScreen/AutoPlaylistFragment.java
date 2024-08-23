package com.aftershade.kozuki.MainScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aftershade.kozuki.HelperClasses.Adapters.MostPlayed;
import com.aftershade.kozuki.HelperClasses.Adapters.RecentSongs;
import com.aftershade.kozuki.HelperClasses.MusicFiles;
import com.aftershade.kozuki.R;

import java.util.Comparator;

import static com.aftershade.kozuki.MainScreen.MainActivity.musicFiles;

public class AutoPlaylistFragment extends Fragment {

    RecyclerView recentRecyclerView, mostPlayedRecyclerView;
    static RecentSongs recentSongsAdap;
    static MostPlayed mostPlayedAdap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_playlist, container, false);

        recentRecyclerView = view.findViewById(R.id.recnt_recycler);
        mostPlayedRecyclerView = view.findViewById(R.id.most_played_recycler);

        recentRecyclerView.setHasFixedSize(true);
        recentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        mostPlayedRecyclerView.setHasFixedSize(true);
        mostPlayedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        if (musicFiles.size() > 1){
            recentSongsAdap = new RecentSongs(getContext(), musicFiles);


            recentRecyclerView.setAdapter(recentSongsAdap);

            mostPlayedAdap = new MostPlayed(getContext(), musicFiles);
            mostPlayedRecyclerView.setAdapter(mostPlayedAdap);
        }



        return view;
    }
}