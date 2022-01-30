package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.FAQsCategoriesAdapter;
import com.asc.yazy.adapter.FAQsQuestionsAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelFAQs;
import com.asc.yazy.api.model.ModelFAQsApi;
import com.asc.yazy.api.model.ModelQuestions;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentFaqsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnCategoryClickListener;
import com.asc.yazy.interfaces.OnQuestionClickListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQsFragment extends Fragment implements View.OnClickListener, IFragment {


    private final OnQuestionClickListener listenerQuestion = Questions -> {
        AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_Question_Answer);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                .add(R.id.fullContent, new QuestionAnswerFragment(Questions.getQuestion(), Questions.getAnswer()))
                .addToBackStack("QuestionAnswerFragment")
                .commit();
    };
    private FragmentFaqsBinding binding;
    private FAQsQuestionsAdapter adapterQuestions;
    private final OnCategoryClickListener listener = new OnCategoryClickListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void OnCategoryClickListener(ModelFAQs FAQ) {
            adapterQuestions = new FAQsQuestionsAdapter(FAQ.getQuestions(), listenerQuestion);
            adapterQuestions.notifyDataSetChanged();
            binding.Questions.setAdapter(adapterQuestions);
        }
    };

    public FAQsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.FAQs);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_faqs, container, false);
        binding.imgBack.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFAQs();
    }

    private void getFAQs() {
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelFAQsApi> call = mApiService.GetFAQs(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelFAQsApi>() {
            @Override
            public void onResponse(@NonNull Call<ModelFAQsApi> call, @NonNull Response<ModelFAQsApi> response) {
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(FAQsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (response.body() != null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    List<ModelFAQs> FAQsCategories = response.body().getData();
                    FAQsCategoriesAdapter adapter = new FAQsCategoriesAdapter(FAQsFragment.this.getContext(), FAQsCategories, listener);
                    binding.Categories.setLayoutManager(layoutManager);
                    binding.Categories.setAdapter(adapter);
                    List<ModelQuestions> Questions = response.body().getData().get(0).getQuestions();
                    adapterQuestions = new FAQsQuestionsAdapter(Questions, listenerQuestion);
                    binding.Questions.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.Questions.setAdapter(adapterQuestions);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelFAQsApi> call, @NonNull Throwable t) {
                if (FAQsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.PENDING_TRIPS);
                // GlobalInfoDialog.getInstance(FAQsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(FAQsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    @Override
    public void onClick(View v) {
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