package com.example.new_trivia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.new_trivia.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;;
    private List<Question> questionList;
    private int current_question = 0;
    private int current_score = 0;
    private Score score;
    private prefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        score = new Score(current_score);
        prefs = new prefs(MainActivity.this);

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processfinished(ArrayList<Question> questionArrayList) {
                binding.questionText.setText(questionArrayList.get(current_question).getAnswer());
                binding.questionCounter.setText("Question:"+(current_question+1)+"/"+questionList.size());
                binding.scoreCounter.setText("Score "+score.getScore());
                binding.highestScore.setText("Highest Score: "+prefs.get_highest_score());
            }
        }
        );
        binding.NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_question();
            }
        });
        binding.TrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_answer(true);
                update_question();
            }
        });
        binding.FalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_answer(false);
                update_question();
            }
        });
    }

    private void update_counter(List<Question> questionList) {
        binding.questionCounter.setText("Question: " +(current_question+1)+"/"+questionList.size());
    }
    private void update_score() {
        binding.scoreCounter.setText("Score "+score.getScore());
    }
    private void check_answer(boolean x) {
        boolean answer = questionList.get(current_question).isAnswertrue();
        if (x == answer) {
            fadeAnimation();
            increase_score();
        } else {
            shakeAnimation();
            decrease_score();
        }
    }

    private void decrease_score() {
        if(current_score > 0){
        current_score-=5;}
        else {
            current_score = 0;
        }
        score.setScore(current_score);
    }

    private void increase_score() {
        current_score+=10;
        score.setScore(current_score);
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_anim);
        binding.CardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionText.setTextColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionText.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.CardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionText.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionText.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void update_question(){
        current_question = (current_question + 1) % questionList.size();
        binding.questionText.setText(questionList.get(current_question).getAnswer());
        update_counter(questionList);
        update_score();
        prefs.save_highest_score(score.getScore());
    }

    @Override
    protected void onPause() {
        prefs.save_highest_score(current_score);
        super.onPause();
    }
}
