package fr.link_value.sfcertif.sfcertifquizz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.link_value.sfcertif.sfcertifquizz.R;
import fr.link_value.sfcertif.sfcertifquizz.models.Answer;
import fr.link_value.sfcertif.sfcertifquizz.models.Choice;
import fr.link_value.sfcertif.sfcertifquizz.models.Learn;
import fr.link_value.sfcertif.sfcertifquizz.models.Quizz;
import fr.link_value.sfcertif.sfcertifquizz.utils.converter.QuestionConverter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CheckboxQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckboxQuestionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_QUESTION = "checkbox_arg_question";
    private static final String ARG_MORE = "checkbox_arg_more";
    private static final String ARG_CHOICE = "checkbox_arg_choice";
    private static final String ARG_ANSWER = "checkbox_arg_answer";
    private static final String ARG_SUBJECT = "checkbox_arg_subject";
    private static final String ARG_QUIZZ = "checkbox_arg_quizz";

    private Quizz quizz;

    private TextView question;
    private TextView correctAnswer;
    private TableRow group;
    private ImageView answerStatus;
    private LinearLayout answerContainer;

    public CheckboxQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckboxQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckboxQuestionFragment newInstance(Quizz checkboxQuestion) {
        CheckboxQuestionFragment fragment = new CheckboxQuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUIZZ, checkboxQuestion);
        /*
        args.putString(ARG_QUESTION, checkboxQuestion.getQuestion());
        args.putStringArrayList(ARG_MORE, (ArrayList<String>) checkboxQuestion.getMores());
        args.putStringArrayList(ARG_CHOICE, (ArrayList<String>) checkboxQuestion.getChoices());
        args.putStringArrayList(ARG_ANSWER, (ArrayList<String>) checkboxQuestion.getAnswers());
        args.putString(ARG_SUBJECT, checkboxQuestion.getSubject());
        fragment.setArguments(args);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizz = getArguments().getParcelable(ARG_QUIZZ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_checkbox_quizz, container, false);

        question = (TextView) view.findViewById(R.id.checkbox_question);
        question.setText(quizz.getQuestion());
        TextView subject = (TextView) view.findViewById(R.id.checkbox_subject);
        subject.setText(quizz.getTopic());

        group = (TableRow) view.findViewById(R.id.checkbox_group_question);
        ArrayList<Choice> choices = (ArrayList<Choice>) quizz.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setId(i);
            checkBox.setText(choices.get(i).getText());
            checkBox.setTextSize(20);
            group.setId(i);
            group.addView(checkBox);
        }
        List<Answer> answers = quizz.getAnswers();
        String answer = TextUtils.join(", ", answers);

        correctAnswer = (TextView) view.findViewById(R.id.checkbox_correct_answer);
        correctAnswer.setText(answer);
        answerStatus = (ImageView) view.findViewById(R.id.checkbox_answer_img);
        answerContainer = (LinearLayout) view.findViewById(R.id.checkbox_answer_container);

        TextView more = (TextView) view.findViewById(R.id.checkbox_more);
        StringBuilder moreBuilder = new StringBuilder();
        for (Learn item : quizz.getLessons()) {
            moreBuilder.append(item.getText());
        }
        more.setText(Html.fromHtml(moreBuilder.toString()));

        ImageButton validBtn = (ImageButton) view.findViewById(R.id.checkbox_valid_btn);
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
        if (v.getId() == R.id.checkbox_valid_btn) {
            validateAnswer(v);
        }
    }

    private void validateAnswer(View view) {
        answerContainer.setVisibility(View.VISIBLE);
        ArrayList<Answer> userAnswers = new ArrayList<Answer>();
        for (int i = 0; i< group.getChildCount(); i++) {
            CheckBox checkbox = (CheckBox) group.getChildAt(i);
            if (checkbox.isChecked()) {
                userAnswers.add(new Answer(checkbox.getText().toString()));
            }
        }
        ArrayList<Answer> answers = new ArrayList<Answer>(quizz.getAnswers());
        if (answers.equals(userAnswers)) {
            answerStatus.setImageResource(R.drawable.ic_check_ok);
        } else {
            answerStatus.setImageResource(R.drawable.ic_check_nok);
        }
    }
}
