package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.databinding.FragmentQuestionAnswerBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.Constants;

public class QuestionAnswerFragment extends Fragment implements View.OnClickListener, IFragment {


    private final String question;
    private final String answer;
    private FragmentQuestionAnswerBinding binding;
    private long mLastClickTime = System.currentTimeMillis();

    public QuestionAnswerFragment(String Question, String Answer) {
        // Required empty public constructor
        this.answer = Answer;
        this.question = Question;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_question_answer, container, false);
        LoadData();
        binding.imgBack.setOnClickListener(this);
        return binding.getRoot();
    }

    private void LoadData() {
        binding.answer.setText(answer);
        binding.question.setText(question);
    }

    @Override
    public void onClick(View v) {
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        if (v.getId() == R.id.imgBack) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }
}