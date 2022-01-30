package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelQuestions;
import com.asc.yazy.databinding.QuestionCellBinding;
import com.asc.yazy.interfaces.OnQuestionClickListener;

import java.util.List;

public class FAQsQuestionsAdapter extends RecyclerView.Adapter<FAQsQuestionsAdapter.ViewHolder> {

    private final List<ModelQuestions> list;
    OnQuestionClickListener listener;

    public FAQsQuestionsAdapter(List<ModelQuestions> list, OnQuestionClickListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public FAQsQuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.question_cell, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQsQuestionsAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)

            holder.setViewModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FAQsQuestionsAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FAQsQuestionsAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private QuestionCellBinding binding;

        ViewHolder(QuestionCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        void unbind() {
            if (binding != null) {
                binding.unbind();
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void setViewModel(ModelQuestions Question) {
            if (binding != null) {
                binding.question.setText(Question.getQuestion());
                binding.linearQuestion.setOnClickListener(v -> listener.OnQuestionClickListener(Question));
            }
        }
    }
}