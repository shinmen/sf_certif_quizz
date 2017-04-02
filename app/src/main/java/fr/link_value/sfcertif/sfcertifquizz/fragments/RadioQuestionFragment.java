package fr.link_value.sfcertif.sfcertifquizz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentResponseListener} interface
 * to handle interaction events.
 * Use the {@link RadioQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioQuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_QUESTION = "radio_arg_question";
    private static final String ARG_MORE = "radio_arg_more";
    private static final String ARG_CHOICE = "radio_arg_choice";
    private static final String ARG_ANSWER = "radio_arg_answer";
    private static final String ARG_SUBJECT = "radio_arg_subject";

    private Quizz quizz;

    private TextView question;
    private TextView correctAnswer;
    private RadioGroup group;
    private ImageView answerStatus;
    private LinearLayout answerContainer;

    public RadioQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RadioQuestionFragment.
     */
    public static RadioQuestionFragment newInstance(QuestionConverter radioQuestionConverter) {
        RadioQuestionFragment fragment = new RadioQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION, radioQuestionConverter.getQuestion());
        args.putStringArrayList(ARG_MORE, (ArrayList<String>) radioQuestionConverter.getMores());
        args.putStringArrayList(ARG_CHOICE, (ArrayList<String>) radioQuestionConverter.getChoice());
        args.putStringArrayList(ARG_ANSWER, (ArrayList<String>) radioQuestionConverter.getAnswer());
        args.putString(ARG_SUBJECT, radioQuestionConverter.getSubject());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizz = new Quizz(
                    getArguments().getString(ARG_QUESTION),
                    getArguments().getStringArrayList(ARG_MORE),
                    getArguments().getStringArrayList(ARG_CHOICE),
                    getArguments().getStringArrayList(ARG_ANSWER),
                    getArguments().getString(ARG_SUBJECT)
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_radio_quizz, container, false);

        question = (TextView) view.findViewById(R.id.radio_question);
        question.setText(quizz.getQuestion());
        TextView subject = (TextView) view.findViewById(R.id.radio_subject);
        subject.setText(quizz.getSubject());

        group = (RadioGroup) view.findViewById(R.id.radio_group_question);
        List<String> choices = quizz.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);
            RadioButton rb = new RadioButton(getActivity());
            rb.setTextSize(20);
            rb.setText(choice);
            group.addView(rb, i);
        }

        List<String> answers = quizz.getAnswers();
        String answer = TextUtils.join(", ", answers);

        correctAnswer = (TextView) view.findViewById(R.id.radio_correct_answer);
        correctAnswer.setText(answer);
        answerStatus = (ImageView) view.findViewById(R.id.radio_answer_img);
        answerContainer = (LinearLayout) view.findViewById(R.id.radio_answer_container);
        LinearLayout moreContainer = (LinearLayout) view.findViewById(R.id.radio_more_container);


        //TextView more = (TextView) view.findViewById(R.id.radio_more);
        //StringBuilder moreBuilder = new StringBuilder();
        for (String item : quizz.getMores()) {
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(false);
            web.loadData(item, null, null);
            moreContainer.addView(web);
        }

        ImageButton validBtn = (ImageButton) view.findViewById(R.id.radio_valid_btn);
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
        if (v.getId() == R.id.radio_valid_btn) {
            validateAnswer(v);
        }
    }

    private void validateAnswer(View view) {
        answerContainer.setVisibility(View.VISIBLE);
        RadioButton radio = (RadioButton) group.findViewById(group.getCheckedRadioButtonId());
        if (radio != null && radio.getText().toString().equals(correctAnswer.getText().toString())) {
            answerStatus.setImageResource(R.drawable.ic_check_ok);
        } else {
            answerStatus.setImageResource(R.drawable.ic_check_nok);
        }
    }
}
