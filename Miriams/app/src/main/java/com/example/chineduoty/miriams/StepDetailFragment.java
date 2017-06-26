package com.example.chineduoty.miriams;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chineduoty.miriams.fragment.StepFragment;
import com.example.chineduoty.miriams.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Detail detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";
    private boolean autoPlay = false;
    private int currentWindow;
    private long playbackPosition;
    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private Gson gson = new Gson();
    private boolean isTwoPane;
    private Step step;

    @BindView(R.id.step_number)
    TextView stepCount;
    @BindView(R.id.media_card)
    CardView mediaCardView;
    @BindView(R.id.player_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.step_instruction)
    TextView textInstruction;


    private SimpleExoPlayer player;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailFragment() {
    }

    public StepDetailFragment newInstance(int position, ArrayList<Step> steps) {
        Bundle bundle = new Bundle();
        bundle.putInt(StepFragment.STEP_INDEX,position);
        bundle.putParcelableArrayList(StepFragment.STEP_LIST, steps);
        setArguments(bundle);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Step> steps = getArguments().getParcelableArrayList(StepFragment.STEP_LIST);
        int pos = getArguments().getInt(StepFragment.STEP_INDEX);
        step = steps.get(pos);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        ButterKnife.bind(this,rootView);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable("Step");
            isTwoPane = savedInstanceState.getBoolean("Pane",false);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }

        if (step == null) {
            Log.e(TAG, "step is null");
            getActivity().finish();
            return null;
        }

        stepCount.setText("Step " + (step.getId() + 1));

        isTwoPane =  getResources().getBoolean(R.bool.isTablet);

        initializeMediaSession();

        playStepVideo();

        int dotIndex = step.getDescription().indexOf(".");
        String withoutNumbering = (dotIndex == -1) ? step.getDescription() : step.getDescription().substring(dotIndex + 2);
        textInstruction.setText(withoutNumbering);

        textInstruction.setVisibility(isTwoPane ? View.GONE  : View.VISIBLE);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (player != null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelable("Step", step);
        outState.putBoolean("Pane", isTwoPane);

    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            exoPlayerView.setPlayer(player);

            // Set the ExoPlayer.EventListener to this activity.
            player.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Miriams");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, playbackPosition);
        }
    }

    private void playStepVideo() {
        exoPlayerView.setVisibility(View.VISIBLE);
        exoPlayerView.requestFocus();
        String videoUrl = step.getVideoURL();
        String thumbNailUrl = step.getThumbnailURL();
        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else if (!thumbNailUrl.isEmpty()) {
            initializePlayer(Uri.parse(thumbNailUrl));
        } else {
            exoPlayerView.setVisibility(View.GONE);
            mediaCardView.setVisibility(View.GONE);
        }
    }


    // ExoPlayer Event Listeners

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        //showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            player.seekTo(0);
        }
    }


}
