package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelPoint;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.RedeemDialog;
import com.asc.yazy.databinding.FragmentRedeemBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class RedeemPointsFragment extends Fragment implements View.OnClickListener, IFragment {


    private FragmentRedeemBinding binding;
    private ModelPoint point;


    public RedeemPointsFragment() {
    }

    public RedeemPointsFragment(ModelPoint point) {
        this.point = point;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_redeem, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.REDEEM_POINTS);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnRedeem.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        bindData();
        return binding.getRoot();
    }

    private void bindData() {
        if (point == null)
            return;
        binding.tvTitle.setText((point.getDiscountPoints() + " " + getResources().getString(R.string.pts)));
        if (point.getDiscountDetailImage() != null && !point.getDiscountDetailImage().trim().isEmpty())
            Glide.with(requireActivity()).asBitmap().load(point.getDiscountDetailImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
        else
            binding.img.setImageResource(R.color.gray);
        binding.tvDetails.setText((getResources().getString(R.string.you_can_exchange) + " " + point.getDiscountPoints() + " " + getResources().getString(R.string.points_with) + " " + point.getDiscount() + " " + getResources().getString(R.string.discount_promo_code_valid_for_using_on_any_upcoming_trip_we_will_send_you_the_promo_code_on_your_email)));
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        if (v.getId() == R.id.imgBack) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        } else if (v.getId() == R.id.btnRedeem) {
            RedeemDialog dialog = new RedeemDialog(requireContext(), point, () -> binding.animation.playAnimation());
            dialog.show();
        }

    }


    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


}
