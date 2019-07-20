package com.example.test;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

public class VideoFragment extends Fragment {
    public static final String PLAY_STATE="play-state";
    SimpleExoPlayer exoPlayer;
    SimpleExoPlayerView playerView;
    public static final String VIDEO_URL_TEXT="video-text";
    DefaultHttpDataSourceFactory dataSourceFactory;
    MediaSource mediaSource;
    ExtractorsFactory extractorsFactory;
    ImageView stepImage;
    boolean playState;
    private final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    String imagePath;
    private long videoPosition;
    public static final String SELECTED_POSITION="selected-position";

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    private String videoUrl;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        exoPlayer= ExoPlayerFactory.newSimpleInstance(context);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity().findViewById(R.id.tablet_linear_layout)==null){
            videoUrl=getArguments().getString("video-url");
            imagePath=getArguments().getString("image-path");

        }

        View view = inflater.inflate(R.layout.fragment_video,container,false);
        playerView=(SimpleExoPlayerView) view.findViewById(R.id.exoplayer_view);
        stepImage =(ImageView)view.findViewById(R.id.step_image);
        if (imagePath!=null && (!imagePath.equals(""))){
            int i=0;
            while( i<okFileExtensions.length){
                if(imagePath.endsWith(okFileExtensions[i])){
                    Picasso.with(getContext()).load(imagePath).into(stepImage);
                    break;

                }
                i++;
                stepImage.setImageResource(R.drawable.photo);


            }
        }
        else{
            stepImage.setImageResource(R.drawable.photo);
        }


        if (videoUrl.equals("") || videoUrl==null)
        {
            playerView.setVisibility(View.GONE);

            return view;
        }


        Uri videoUri=Uri.parse(videoUrl);
        dataSourceFactory= new DefaultHttpDataSourceFactory("exoplayer-video");
        extractorsFactory= new DefaultExtractorsFactory();
        mediaSource= new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);
        playerView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
        if (savedInstanceState!=null){
            videoUrl=savedInstanceState.getString(VIDEO_URL_TEXT);
            videoPosition = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            exoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_STATE));

        }
        else
        {
            exoPlayer.setPlayWhenReady(true);

        }

        exoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
        exoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY){
                    Log.i(VideoFragment.class.getSimpleName(),"Video is playing!");
                    playState=true;

                }


                else {
                    playState=false;
                    Log.i(MediaStore.Video.class.getSimpleName(),"video is paused!");

                }
            }
        });

        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE&& getActivity().findViewById(R.id.tablet_linear_layout)==null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPosition = exoPlayer.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoUrl.equals("")){
            //  exoPlayer.prepare(mediaSource);
            if (videoPosition != C.TIME_UNSET)
                exoPlayer.seekTo(videoPosition);

        }




    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VIDEO_URL_TEXT,videoUrl);
        outState.putLong(SELECTED_POSITION,videoPosition);
        outState.putBoolean(PLAY_STATE,playState);
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayer.stop(false);
    }

}
