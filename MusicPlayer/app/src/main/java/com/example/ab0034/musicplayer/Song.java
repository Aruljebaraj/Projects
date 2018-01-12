package com.example.ab0034.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Song extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static final int MY_PERMISSION_REQUEST = 1;
    //    private static final long YOUR_PLAYLIST_ID = 1;
//    ContentResolver resolver;
    ListView listView;
    LinearLayout linearLayout, toolbar, playall, control;
    CoordinatorLayout player;
    TextView txtsong, textsongname, textablumname, txtelapsed_time, txtplay_time;
    ImageView img_pause, img_forward, img_backward, img_pause1, img_forward1, img_backward1,
            img_repeat, img_shuffle, img_speaker, img_back, img_menu, img_more;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    long CurrentDuration;
    AudioManager am;
    int CurrentSong = 0, Durationpaused = 0;
    private boolean isShuffle = false;
    private BottomSheetBehavior mBottomSheetBehavior;
    NotificationManager mNotificationManager;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            UpdatePosition();

        }
    };
    boolean isMovingSeekBar = false;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        appColor = app_preferences.getInt("color", 0);
//        appTheme = app_preferences.getInt("theme", 0);
//        themeColor = appColor;
//        constant.color = appColor;
//
//        if (themeColor == 0){
//            setTheme(Constant.theme);
//        }else if (appTheme == 0){
//            setTheme(Constant.theme);
//        }else{
//            setTheme(appTheme);
//        }
        setContentView(R.layout.activity_song);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        linearLayout = (LinearLayout) findViewById(R.id.Control);
        toolbar = (LinearLayout) findViewById(R.id.toolbar1);
        playall = (LinearLayout) findViewById(R.id.playall);
        control = (LinearLayout) findViewById(R.id.Control);
        player = (CoordinatorLayout) findViewById(R.id.player);
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(View.GONE);
        img_pause = (ImageView) findViewById(R.id.img_pause);
        img_pause1 = (ImageView) findViewById(R.id.img_pause1);
        img_backward = (ImageView) findViewById(R.id.img_backward);
        img_forward = (ImageView) findViewById(R.id.img_forward);
        img_backward1 = (ImageView) findViewById(R.id.img_backward1);
        img_forward1 = (ImageView) findViewById(R.id.img_forward1);
        img_repeat = (ImageView) findViewById(R.id.img_repeat);
        img_shuffle = (ImageView) findViewById(R.id.img_shuffle);
        img_speaker = (ImageView) findViewById(R.id.img_speaker);
        img_back = (ImageView) findViewById(R.id.imgback);
        img_menu = (ImageView) findViewById(R.id.img_menu);
        img_more = (ImageView) findViewById(R.id.img_more);
        txtsong = (TextView) findViewById(R.id.txtSong);
        txtelapsed_time = (TextView) findViewById(R.id.txtelapsed_time);
        txtplay_time = (TextView) findViewById(R.id.txtplay_time);
        textsongname = (TextView) findViewById(R.id.txtsongname);
        textablumname = (TextView) findViewById(R.id.txtalbumname);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        if (ContextCompat.checkSelfPermission(Song.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ;
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Song.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(Song.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                Dostuff();
            }
        }


        img_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Repeat = img_repeat.getTag().toString();
                if (Repeat.equals("Repeat")) {
                    Toast.makeText(Song.this, "Repeat", Toast.LENGTH_SHORT).show();
                    img_repeat.setImageResource(R.drawable.ic_repeat1);
                    mediaPlayer.setLooping(true);
                    img_repeat.setTag("NoLooping");

                } else {
                    img_repeat.setImageResource(R.drawable.ic_repeat);
                    mediaPlayer.setLooping(false);
                    img_repeat.setTag("Repeat");
                    Toast.makeText(Song.this, "Nolooping", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pause = img_pause.getTag().toString();
                if (Pause.equals("Pause")) {
                    img_pause.setImageResource(R.drawable.ic_play);
                    img_pause1.setImageResource(R.drawable.ic_play_button);
                    img_pause.setTag("Play");
                    pause();
                } else {
                    img_pause.setImageResource(R.drawable.ic_pause);
                    img_pause1.setImageResource(R.drawable.ic_pauses);
                    img_pause.setTag("Pause");
                    Resume();
                    Notification();
                }
            }
        });
        img_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongDto song = getmusic().get(CurrentSong - 1);
                CurrentSong = CurrentSong - 1;
                txtsong.setText(song.Title);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                play(song.Path);
            }
        });
        img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongDto song = getmusic().get(CurrentSong + 1);
                CurrentSong = CurrentSong + 1;
                txtsong.setText(song.Title);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                play(song.Path);

            }
        });
        img_pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongDto song = getmusic().get(CurrentSong);
                txtsong.setText(song.Title);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                play(song.Path);
            }
        });
        img_pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pause = img_pause1.getTag().toString();

                if (Pause.equals("Pause")) {
                    img_pause.setImageResource(R.drawable.ic_play);
                    img_pause1.setImageResource(R.drawable.ic_play_button);
                    img_pause1.setTag("Play");
                    pause();
                } else {
                    img_pause.setImageResource(R.drawable.ic_pause);
                    img_pause1.setImageResource(R.drawable.ic_pauses);
                    img_pause1.setTag("Pause");
                    Resume();
                    Notification();
                }
            }
        });
        img_backward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongDto song = getmusic().get(CurrentSong - 1);
                CurrentSong = CurrentSong - 1;
                txtsong.setText(song.Title);
                textsongname.setText(song.Title);
                textablumname.setText(song.Artist);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                play(song.Path);

            }
        });
        img_forward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongDto song = getmusic().get(CurrentSong + 1);
                CurrentSong = CurrentSong + 1;
                txtsong.setText(song.Title);
                textsongname.setText(song.Title);
                textablumname.setText(song.Artist);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                play(song.Path);

            }
        });
        img_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();

            }
        });

        img_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mute = img_speaker.getTag().toString();
                if (Mute.equals("Mute")) {
                    Mute();
                    img_speaker.setTag("Unmute");
                    img_speaker.setImageResource(R.drawable.ic_speaker_mute);
                } else {
                    Unmute();
                    img_speaker.setTag("Mute");
                    img_speaker.setImageResource(R.drawable.ic_speaker);
                }
            }
        });

//        img_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(Song.this, img_more);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.Setting:
//                                Intent i = new Intent();
//                                i.setClass(Song.this, Setting.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
//                                return true;
//                        }
//                        return true;
//                    }
//
////                });
//
//                popup.show();
//            }
//        });
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Song.this, Album.class);
                startActivity(intent);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                playall.setVisibility(View.GONE);
                control.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                SongDto song = getmusic().get(CurrentSong);

                textsongname.setText(song.Title);
                textablumname.setText(song.Artist);
            }
        });
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                    playall.setVisibility(View.VISIBLE);

                    control.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
    }

    public RemoteViews Notification() {
        Intent Notification = new Intent(this, MediaNotification.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, Notification, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pIntent1 = PendingIntent.getBroadcast(this, 0, Notification, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.activity_song);
        SongDto song = getmusic().get(CurrentSong);
        android.app.Notification mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_pause)
                .setContentTitle(song.Title)
                .setContentText(song.Artist)
//                .addAction(R.drawable.ic_backward_arrows, "Backward", pIntent)
                .addAction(R.drawable.ic_pause, "pause", pIntent).build();
//                .addAction(R.drawable.ic_forward_button, "Forward", pIntent).build();
//        remoteViews.setOnClickPendingIntent(R.drawable.ic_backward_arrows, pIntent);
        remoteViews.setOnClickPendingIntent(R.id.img_pause, pIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder);
        return remoteViews;
    }

    private void Unmute() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    private void Mute() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }

                }).create().show();
    }
//    public static void addToPlaylist(ContentResolver resolver, int audioId) {
//
//        String[] cols = new String[] {
//                "count(*)"
//        };
//        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", YOUR_PLAYLIST_ID);
//        Cursor cur = resolver.query(uri, cols, null, null, null);
//        cur.moveToFirst();
//        final int base = cur.getInt(0);
//        cur.close();
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
//        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);
//        resolver.insert(uri, values);
//    }

    public void Dostuff() {
//        toolbar.setBackgroundColor(Constant.color);
//        control.setBackgroundColor(Constant.color);

        listView = (ListView) findViewById(R.id.List_song);
        listView.setAdapter(new SongAdapter(this, getmusic()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SongDto song = getmusic().get(i);
                CurrentSong = i;
                txtsong.setText(song.Title);
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }
                } catch (Exception e) {

                }
                int s = (int) song.Duration;
                seekBar.setMax(s);
                UpdatePosition();
                play(song.Path);


            }
        });

    }

    private void play(String uri) {
        try {
            if (!uri.isEmpty()) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(uri));
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mediaPlayer.isPlaying()) {

                            mediaPlayer.stop();
                        } else {
                            mediaPlayer.start();
                            img_pause.setImageResource(R.drawable.ic_pause);
                            img_pause.setTag("Pause");
                            Resume();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void pause() {
        try {
            if (mediaPlayer != null)
                mediaPlayer.pause();
            Durationpaused = mediaPlayer.getCurrentPosition();
        } catch (Exception e) {

        }
    }

    private void Resume() {
        try {
            if (mediaPlayer != null)
                mediaPlayer.seekTo(Durationpaused);
            mediaPlayer.start();
        } catch (Exception e) {

        }
    }

    public ArrayList<SongDto> getmusic() {

        ArrayList<SongDto> arrayList = new ArrayList<>();
        ContentResolver contentresolver = getContentResolver();
        final Uri Songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Cursor songcursor = contentresolver.query(Songuri, null, null, null, null);
        if (songcursor != null && songcursor.moveToFirst()) {
            int SongTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int SongArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            long SongDuration = songcursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int SongPath = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String CurrentTitle = songcursor.getString(SongTitle);
                String CurrentArtist = songcursor.getString(SongArtist);
                CurrentDuration = songcursor.getLong((int) SongDuration);
                String thisdata = songcursor.getString(SongPath);
                SongDto songDto = new SongDto();
                songDto.Artist = CurrentArtist;
                songDto.Title = CurrentTitle;
                songDto.Duration = CurrentDuration;
                songDto.Path = thisdata;
                arrayList.add(songDto);


            } while (songcursor.moveToNext());
        }
        return arrayList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ;
                if (ContextCompat.checkSelfPermission(Song.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    Dostuff();
                } else {
                    Toast.makeText(this, "No Permission Granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    private void UpdatePosition() {
        long i = mediaPlayer.getCurrentPosition();
        mHandler.removeCallbacks(runnable);
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        txtelapsed_time.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
        TextView txtremaing_time = (TextView) findViewById(R.id.txtremaing_time);
        txtremaing_time.setText(milliSecondsToTimer(mediaPlayer.getDuration() - i));
        mHandler.postDelayed(runnable, 1000);


    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
        if (isMovingSeekBar) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isMovingSeekBar = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isMovingSeekBar = false;
    }

    public class SongAdapter extends BaseAdapter {
        ArrayList<SongDto> arrayList;
        private Activity activity;
        private LayoutInflater inflater = null;

        public SongAdapter(Activity a, ArrayList<SongDto> arrayList) {
            this.activity = a;
            this.arrayList = arrayList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return arrayList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.list_row, null);
            TextView title = (TextView) vi.findViewById(R.id.title); // title
            TextView artist = (TextView) vi.findViewById(R.id.artist); // artist name
            TextView duration = (TextView) vi.findViewById(R.id.duration); // duration
            final SongDto song = arrayList.get(position);
            title.setText(song.Title);
            artist.setText(song.Artist);
            duration.setText(milliSecondsToTimer(song.Duration));
            return vi;
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
//        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    class SongDto {
        String Artist;
        String Title;
        String Path;
        long Duration;
    }

    public static class MediaNotification extends BroadcastReceiver {
        @Override

        public void onReceive(Context context, Intent intent) {
            Song song = null;
            Log.d("Here", "I am here");
            Toast.makeText(song, "Pause", Toast.LENGTH_SHORT).show();
        }
    }

}

