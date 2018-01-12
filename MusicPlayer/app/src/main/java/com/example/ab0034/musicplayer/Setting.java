package com.example.ab0034.musicplayer;

import android.support.v7.app.AppCompatActivity;


public class Setting extends AppCompatActivity {



//
//        SharedPreferences sharedPreferences, app_preferences;
//        SharedPreferences.Editor editor;
//        Button button;
//        Methods methods;
//
//        int appTheme;
//        int themeColor;
//        int appColor;
//        Constant constant;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_setting);
//
//            app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
//            appColor = app_preferences.getInt("color", 0);
//            appTheme = app_preferences.getInt("theme", 0);
//            themeColor = appColor;
//            constant.color = appColor;
//
//            if (themeColor == 0) {
//                setTheme(Constant.theme);
//            } else if (appTheme == 0) {
//                setTheme(Constant.theme);
//            } else {
//                setTheme(appTheme);
//            }
//
////            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////            toolbar.setTitle("Settings");
////            toolbar.setBackgroundColor(Constant.color);
//
//            methods = new Methods();
//            button = (Button) findViewById(R.id.Btn);
//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//            editor = sharedPreferences.edit();
//            editor.apply();
////
////            colorize();
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ColorChooserDialog dialog = new ColorChooserDialog(Setting.this);
//                    dialog.setTitle("Select");
//                    dialog.setColorListener(new ColorListener() {
//                        @Override
//                        public void OnColorClick(View v, int color) {
////                            colorize();
//                            Constant.color = color;
//
//                            methods.setColorTheme();
//                            editor.putInt("color", color);
//                            editor.putInt("theme", Constant.theme);
//                            editor.commit();
//
//                            Intent intent = new Intent(Setting.this, Song.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    });
//
//                    dialog.show();
//                }
//            });
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    onBackPressed();
//                    break;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//
//
////        }
    }


