package com.techexchange.mobileapps.lab2;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    static final String KEY_SCORE = "Score";
    private final String TAG = this.getClass().getSimpleName();

    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList;
    private int currentQuestionIndex;
    private int currentScore;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        questionList = initQuestionList();
        questionText = rootView.findViewById(R.id.question_text);
        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);
        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);
        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(v -> onNextButtonPressed());
        correctText = rootView.findViewById(R.id.correct_incorrect_text);
        currentQuestionIndex = 0;

        if (savedInstanceState != null){
            currentQuestionIndex = savedInstanceState.getInt("CurrentIndex");
        }
        updateView();
        // Inflate the layout for this fragment
        return rootView;
    }


    private void onAnswerButtonPressed (View v) {
        Button selectedButton = (Button) v;
        Question ques = questionList.get(currentQuestionIndex);
        if (ques.getCorrectAnswer().contentEquals(selectedButton.getText())) {
            correctText.setText("Right");
            currentScore++;
        } else {
            correctText.setText("Wrong");
        }
        nextButton.setEnabled(true);
    }
    private void onNextButtonPressed () {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            updateView();
        }else{
            Intent scoreIntent = new Intent(getActivity(), ScoreActivity.class);
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);
        }
    }

    private List<Question> initQuestionList(){
        Resources res = getResources();
        String[] questions = res.getStringArray(R.array.questions);
        String[] correctAnswers = res.getStringArray(R.array.correct_answers);
        String[] wrongAnswers = res.getStringArray(R.array.incorrect_answers);

        //Make sure that all arrays are the same length
        Preconditions.checkState(questions.length == correctAnswers.length);
        Preconditions.checkState(questions.length == wrongAnswers.length);

        List<Question> qList = new ArrayList<>();

        for(int i = 0; i< questions.length;i++) {
            qList.add(new Question(questions[i], correctAnswers[i], wrongAnswers[i]));
        }
        return qList;
    }



    private void updateView() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());
        if (Math.random() < 0.5) {
            leftButton.setText(currentQuestion.getCorrectAnswer());
            rightButton.setText(currentQuestion.getWrongAnswer());
        }else{
            rightButton.setText(currentQuestion.getCorrectAnswer());
            leftButton.setText(currentQuestion.getWrongAnswer());
        }
        nextButton.setEnabled(false);
        correctText.setText(R.string.initial_correct_incorrect);
    }



    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
        }
        boolean restart = data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ, true);
        if(restart){
            currentQuestionIndex = 0;
            currentScore = 0;
            updateView();
        }else{
            getActivity().finish();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() was called");
        updateView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() was called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() was called");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() was called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() was called");
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", currentQuestionIndex);
    }



}
