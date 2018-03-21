package com.example.android.bakingapp;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.databinding.FragmentActivityRecipeDetailStepsBinding;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by sweety on 3/12/2018.
 */

public class RecipeDetailStepsFragment extends android.support.v4.app.Fragment {
    String recipeDetails;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoUrl;
    String thumbnailUrl;
    private Long playPosition;
    private int adapterPosition;
    private final String KEY_PLAYER_POSITION = "player_position";
    int recipeStepsArraySize;
    String[] recipeStepsArray;
    FragmentActivityRecipeDetailStepsBinding binding;

    public RecipeDetailStepsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            playPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_recipe_detail_steps, container, false);
        final View rootView = binding.getRoot();
        if (RecipeDetailActivity.mTwoPane == true && RecipeDetailActivity.flag == true) {
            String recipeDescriptionForTabvie = getArguments().getString("recipeDescription");
            binding.recipeStepId.setText(recipeDescriptionForTabvie);
            binding.recipeStepId.setVisibility(View.VISIBLE);
            binding.nextStep.setVisibility(View.GONE);
            binding.previousStep.setVisibility(View.GONE);
            String videoUrlForTabView = getArguments().getString("recipeVideoUrl");
            String thumbnailurlfortabview = getArguments().getString("thumbnailurl");
            videoUrl = videoUrlForTabView;
            thumbnailUrl = thumbnailurlfortabview;

        } else {
            recipeDetails = getArguments().getString("recipeStepDetails");
            recipeStepsArray = getArguments().getStringArray("recipestepsarray");
            recipeStepsArraySize = recipeStepsArray.length;
            final String[] recipeSteps = recipeDetails.split(">");
            binding.recipeStepId.setText(recipeSteps[1]);
            binding.recipeStepId.setVisibility(View.VISIBLE);
            videoUrl = recipeSteps[2];
            thumbnailUrl = recipeSteps[3];
            Log.d("thumbnailurl",""+thumbnailUrl);
            adapterPosition = getArguments().getInt("adapterposition");

            if (RecipeDetailActivity.flag == false) {
                binding.nextStep.setVisibility(View.GONE);
                binding.previousStep.setVisibility(View.GONE);
            }
        }

        if (videoUrl.equals(" ")) {
            if (thumbnailUrl.equals(" ")) {
                binding.noVideoImage.setImageResource(R.drawable.no_video_image);
            }else{
                Picasso.with(getContext()).load(thumbnailUrl).into(binding.noVideoImage);
            }
            binding.stepVideoExoPlayer.setVisibility(View.INVISIBLE);
            binding.noVideoImage.setImageResource(R.drawable.no_video_image);
            binding.noVideoImage.setVisibility(View.VISIBLE);

        } else {
            setUpViews(videoUrl);
        }
        binding.nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapterPosition == -1) {
                    adapterPosition = adapterPosition + 2;
                } else {
                    adapterPosition = adapterPosition + 1;
                }
                if (adapterPosition == recipeStepsArraySize - 1) {
                    binding.nextStep.setVisibility(View.GONE);
                    binding.previousStep.setVisibility(View.VISIBLE);
                } else {
                    if (exoPlayer != null)
                        exoPlayer.stop();
                    binding.recipeStepId.setVisibility(View.VISIBLE);
                    String[] recipeStepsArr = recipeStepsArray[adapterPosition].split(">");
                    binding.recipeStepId.setText(recipeStepsArr[1].replace(",", " "));
                    videoUrl = recipeStepsArr[2];
                    if (videoUrl.equals(" ")) {
                        binding.stepVideoExoPlayer.setVisibility(View.INVISIBLE);
                        binding.noVideoImage.setImageResource(R.drawable.no_video_image);
                        binding.noVideoImage.setVisibility(View.VISIBLE);

                    } else {
                        setUpViews(videoUrl);
                        binding.noVideoImage.setVisibility(View.INVISIBLE);
                    }
                    binding.nextStep.setVisibility(View.VISIBLE);
                    binding.previousStep.setVisibility(View.VISIBLE);
                }

            }
        });
        binding.previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapterPosition == recipeStepsArraySize - 1) {
                    adapterPosition = adapterPosition - 2;
                } else {
                    adapterPosition = adapterPosition - 1;
                }
                if (adapterPosition == -1) {
                    binding.previousStep.setVisibility(View.GONE);
                    binding.nextStep.setVisibility(View.VISIBLE);
                } else {
                    if (exoPlayer != null)
                        exoPlayer.stop();
                    binding.recipeStepId.setVisibility(View.VISIBLE);
                    String[] recipeStepsArr = recipeStepsArray[adapterPosition].split(">");
                    binding.recipeStepId.setText(recipeStepsArr[1]);
                    videoUrl = recipeStepsArr[2];
                    if (videoUrl.equals(" ")) {
                        binding.stepVideoExoPlayer.setVisibility(View.INVISIBLE);
                        binding.noVideoImage.setImageResource(R.drawable.no_video_image);
                        binding.noVideoImage.setVisibility(View.VISIBLE);

                    } else {
                        setUpViews(videoUrl);
                        binding.noVideoImage.setVisibility(View.INVISIBLE);
                    }
                    binding.previousStep.setVisibility(View.VISIBLE);
                    binding.nextStep.setVisibility(View.VISIBLE);
                }

            }
        });
        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (playPosition != null) {
            outState.putLong(KEY_PLAYER_POSITION, playPosition);
        }
    }

    public void setUpViews(String videoUrl) {
        if (videoUrl.equals(" ")) {
            Log.d("inside if", "" + videoUrl);
            binding.stepVideoExoPlayer.setVisibility(View.INVISIBLE);
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
                binding.stepVideoExoPlayer.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                binding.stepVideoExoPlayer.setVisibility(View.VISIBLE);
                binding.noVideoImage.setVisibility(View.INVISIBLE);
                if (playPosition != null)
                    exoPlayer.seekTo(playPosition);
                else
                    exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(true);

            } catch (Exception e) {
                Log.e("Exolplayere", "" + e);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            playPosition = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
