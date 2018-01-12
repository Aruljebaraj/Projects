package com.example.ab0034.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Album extends Song{
    SongDto song;
    TextView album;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        album =(TextView)findViewById(R.id.album_name);
        img_back=(ImageView)findViewById(R.id.imgback);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Album.this,Song.class);
                startActivity(intent);
            }
        });
        song= getmusic().get(CurrentSong);
        album.setText(song.Artist);

    }
}
