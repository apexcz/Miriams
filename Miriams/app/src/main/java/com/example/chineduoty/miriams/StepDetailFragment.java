package com.example.chineduoty.miriams;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chineduoty.miriams.dummy.DummyContent;
import com.example.chineduoty.miriams.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Detail detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String RECIPE_NAME = "recipe_name";

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private static int stepPosition;
    private static List<Step> lstStep = new ArrayList<Step>();
    private static Step step;
    private Gson gson = new Gson();

    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer player;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(int position,List<Step> steps){
      StepDetailFragment fragment = new StepDetailFragment();
        stepPosition = position;
        lstStep = steps;
        step = lstStep.get(stepPosition);
      return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle("Step "+stepPosition);
//            }

        //Toolbar toolbar = (Toolbar) activity.findViewById(R.id.detail_toolbar);
        //toolbar.setTitle("Step "+stepPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (step == null) {
            Log.e(TAG,"step is null");
            getActivity().finish();
            return null;
        }

        TextView stepCount = (TextView)rootView.findViewById(R.id.step_number);
        TextView textInstruction = (TextView) rootView.findViewById(R.id.step_instruction);
        exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        FloatingActionButton btnPrevious = (FloatingActionButton)rootView.findViewById(R.id.previous_btn);
        FloatingActionButton btnNext = (FloatingActionButton)rootView.findViewById(R.id.next_btn);


        stepCount.setText("Step " + (step.getId() + 1));
        //Handler mainHandler = new Handler();
       // BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector();

        player =
                ExoPlayerFactory.newSimpleInstance(getActivity(),trackSelector,new DefaultLoadControl());

        exoPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "yourApplicationName"), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.

        Uri videoUri = Uri.parse(step.getVideoURL());

        MediaSource videoSource = new ExtractorMediaSource(videoUri,
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        player.prepare(videoSource);

        int dotIndex = step.getDescription().indexOf(".");
        String withoutNumbering = (dotIndex == -1) ? step.getDescription() : step.getDescription().substring(dotIndex + 2);
        textInstruction.setText(withoutNumbering);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StepDetailActivity)getActivity()).onPreviousClicked();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StepDetailActivity)getActivity()).onNextClicked();
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && player != null) {
            player.release();
            player = null;
        }
    }
}
