package fr.link_value.sfcertif.sfcertifquizz.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.Converter.QuestionConverter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentResponseListener} interface
 * to handle interaction events.
 * Use the {@link SimpleQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleQuestionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_QUESTION = "simple_arg_question";
    private static final String ARG_MORE = "simple_arg_more";
    private static final String ARG_ANSWER = "simple_arg_answer";

    private Quizz quizz;

    private TextView question;
    private TextView correctAnswer;
    private EditText answerProposition;
    private ImageView answerStatus;
    private LinearLayout answerContainer;

    public SimpleQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleQuestionFragment newInstance(QuestionConverter simpleQuestion) {
        SimpleQuestionFragment fragment = new SimpleQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, simpleQuestion.getQuestion());
        args.putString(ARG_MORE, simpleQuestion.getMore());
        args.putStringArrayList(ARG_ANSWER, (ArrayList<String>) simpleQuestion.getAnswer());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizz = new Quizz(
                    getArguments().getString(ARG_QUESTION),
                    getArguments().getString(ARG_MORE),
                    getArguments().getStringArrayList(ARG_ANSWER)
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_simple_quizz, container, false);

        question = (TextView) view.findViewById(R.id.simple_question);
        question.setText(quizz.getQuestion());

        answerProposition = (EditText) view.findViewById(R.id.simple_question_edit);
        answerStatus = (ImageView) view.findViewById(R.id.simple_answer_img);
        answerContainer = (LinearLayout) view.findViewById(R.id.simple_answer_container);

        TextView more = (TextView) view.findViewById(R.id.simple_more);
        more.setText(quizz.getMore());

        List<String> answers = quizz.getAnswers();
        String answer = TextUtils.join(", ", answers);
        correctAnswer = (TextView) view.findViewById(R.id.simple_correct_answer);
        correctAnswer.setText(answer);

        Button validBtn = (Button) view.findViewById(R.id.simple_valid_btn);
        validBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        answerContainer.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.simple_valid_btn) {
            validateAnswer(v);
        }
    }

    private void validateAnswer(View view) {
        answerContainer.setVisibility(View.VISIBLE);
        if (answerProposition.getText().toString().equals(correctAnswer.getText().toString())) {
            answerStatus.setImageResource(android.R.drawable.btn_plus);
        } else {
            answerStatus.setImageResource(android.R.drawable.btn_minus);
        }
    }

}
