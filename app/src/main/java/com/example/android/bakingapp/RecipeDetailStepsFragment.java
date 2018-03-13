package com.example.android.bakingapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by namit on 3/12/2018.
 */

public class RecipeDetailStepsFragment extends android.support.v4.app.Fragment{
    String recipeDetails;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoUrl;
    private Long playPosition;
    private ImageView noVideoImage;
    public RecipeDetailStepsFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_activity_recipe_detail_steps, container, false);
        TextView recipeStepTv = (TextView)rootView.findViewById(R.id.recipeStepId);
        noVideoImage = (ImageView)rootView.findViewById(R.id.noVideoImage);
        if(getArguments() != null){
            recipeDetails = getArguments().getString("recipeStepDetails");
            final String[] recipeSteps = recipeDetails.split(">");
            recipeStepTv.setText(recipeSteps[1]);
            videoUrl = recipeSteps[2];
        }
        exoPlayerView = (SimpleExoPlayerView)rootView.findViewById(R.id.stepVideoExoPlayer);


            if (videoUrl.equals(" ")) {
                Log.d("inside if",""+videoUrl);
                exoPlayerView.setVisibility(View.INVISIBLE);
                noVideoImage.setImageResource(R.drawable.no_video_image);
                noVideoImage.setVisibility(View.VISIBLE);
            } else {
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    LoadControl loadControl = new DefaultLoadControl();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                Uri videoUri = Uri.parse(videoUrl);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayerView.setVisibility(View.VISIBLE);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
                noVideoImage.setVisibility(View.INVISIBLE);
            }catch(Exception e){
                Log.e("Exolplayere", "" + e);
            }
        }
        return rootView;

    }

    @Override
    public void onPause() {
        super.onPause();
       // Log.i(TAG,"onPause");

            if (exoPlayer != null ) {
                exoPlayer.setPlayWhenReady(false);
                playPosition = exoPlayer.getCurrentPosition();
                exoPlayer.stop();
                exoPlayer.release();
                exoPlayer = null;
            }

    }
}
