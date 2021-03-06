package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcel;
import org.parceler.Parcels;

public class TweetsDetailsActivity extends AppCompatActivity {
    Tweet tweet;
    TextView tvScreenNameDetails;
    TextView tvUsernameDetails;
    TextView tvTweetContentDetails;
    ImageView ivProfileImageDetails;
    ImageView tweetImageDetails;
    TextView relativeTimestampDetails;
    TextView tvLikeCount;
    EditText etRetweet;
    TextView tvComments;

    public Integer likes;
    boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_details_activity);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // looking up all the TVs, IVs, and Buttons
        tvScreenNameDetails = (TextView) findViewById(R.id.tvScreenNameDetails);
        tvUsernameDetails = (TextView) findViewById(R.id.tvUsernameDetails);
        tvTweetContentDetails = (TextView) findViewById(R.id.tvTweetContentDetails);
        ivProfileImageDetails = (ImageView) findViewById(R.id.ivProfileImageDetails);
        tweetImageDetails = (ImageView) findViewById(R.id.tweetImageDetails);
        relativeTimestampDetails = (TextView) findViewById(R.id.relativeTimestampDetails);
        tvLikeCount = (TextView) findViewById(R.id.tvLikeCount);
        etRetweet = (EditText) findViewById(R.id.etRetweet);
        tvComments = (TextView) findViewById(R.id.tvComments);
        final Button likeButton = (Button) findViewById(R.id.likeButton);
        final Button shareButton = (Button) findViewById(R.id.shareButton);
        final Button retweetButton = (Button) findViewById(R.id.retweetButton);
        final Button replyButton = (Button) findViewById(R.id.replyButton);

        // set all the views to be the corresponding tweet or user content/media
        tvScreenNameDetails.setText(tweet.getUser().getName());
        tvUsernameDetails.setText("@" + tweet.getUser().getScreenName());
        tvTweetContentDetails.setText(tweet.getBody());
        relativeTimestampDetails.setText(tweet.getCreatedAt());
        Glide.with(this).load(tweet.getTweetImage()).into(tweetImageDetails);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImageDetails);
        likes = tweet.getNumberOfLikes();
        tvLikeCount.setText(""+likes);

        // when like button is clicked:
        // 1. update the number of likes
        // 2. change it to the reverse icon (filled vs. outline of heart)
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvLikeCount.setText(""+(tweet.getNumberOfLikes() + 1));
                if(isPlay){
                    v.setBackgroundResource(R.drawable.heartoutlinered);
                    tvLikeCount.setText(""+likes);
                }else{
                    v.setBackgroundResource(R.drawable.heartfilledred);
                }
                // reverse the boolean so that if button is clicked again, it will change the
                // number of likes displayed and the icon of the button
                isPlay = !isPlay;
            }
        });

        // when share button is clicked, create a new intent to share the tweet
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey there, check out this tweet: " + tweet.getBody();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        // when retweet button is clicked, make the views for replying to comment visible
        retweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make etRetweet and replyButton visible
                etRetweet.setVisibility(view.VISIBLE);
                replyButton.setVisibility(view.VISIBLE);
                //make text in etRetweet to be @username
                etRetweet.setHint("Compose your reply here...");
                etRetweet.setText("@" + tweet.getUser().getScreenName());
            }
        });

        // when the comment reply button is clicked, make the comment views invisible and
        // publish the comment on the same page
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make etRetweet and replyButton invisible
                etRetweet.setVisibility(view.INVISIBLE);
                replyButton.setVisibility(view.INVISIBLE);
                String replyContent = etRetweet.getText().toString();
                //set tvComments text to be tweetContent below
                tvComments.setText(replyContent);
                tvComments.setVisibility(view.VISIBLE);
            }
        });
    }
}
