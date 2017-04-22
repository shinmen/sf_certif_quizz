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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadioQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadioQuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_QUIZZ = "simple_arg_quizz";

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
    public static RadioQuestionFragment newInstance(Quizz radioQuestion) {
        RadioQuestionFragment fragment = new RadioQuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUIZZ, radioQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout loader = (LinearLayout) getActivity().findViewById(R.id.loader_layout);
        if (loader.getVisibility() == View.VISIBLE) {
            //loader.setVisibility(View.GONE);
        }
        if (getArguments() != null) {
            quizz = getArguments().getParcelable(ARG_QUIZZ);
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
        subject.setText(quizz.getTopic());

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
        LinearLayout lessonContainer = (LinearLayout) view.findViewById(R.id.radio_more_container);

        for (String item : quizz.getLessons()) {
            final WebView web = new WebView(getActivity());
            web.getSettings().setJavaScriptEnabled(false);
            web.loadData(item, null, null);
            lessonContainer.addView(web);
        }

        RelativeLayout validBtnLayout = (RelativeLayout) view.findViewById(R.id.radio_valid_btn);
        Button validBtn = (Button) validBtnLayout.findViewById(R.id.valid_btn);
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
        if (v.getId() == R.id.valid_btn) {
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
