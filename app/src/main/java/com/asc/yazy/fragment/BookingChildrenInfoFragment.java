package com.asc.yazy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.AdultsObserverAdapter;
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.core.AdultsCountObserver;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentAdultsInfoBinding;
import com.asc.yazy.interfaces.IFragmentCount;
import com.asc.yazy.utils.AnalyticsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingChildrenInfoFragment extends Fragment implements IFragmentCount, View.OnClickListener {


    private FragmentAdultsInfoBinding binding;
    private final KeyboardVisibilityEventListener keyboardVisibilityEventListener = new KeyboardVisibilityEventListener() {
        @Override
        public void onVisibilityChanged(boolean isOpen) {
            if (isOpen) {
                binding.bookLayout.setVisibility(View.GONE);
            } else {
                binding.bookLayout.setVisibility(View.VISIBLE);
            }
        }
    };
    private AdultsObserverAdapter adapter;
    private boolean isVisibleToUser = false;


    public BookingChildrenInfoFragment() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser)
            enhancedDatePopulation();
        if (adapter != null && isVisibleToUser) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> adapter.showCaseViewChild(), 500);
        } else if (adapter != null && adapter.isCaseViewShowing()) {
            adapter.dismissCaseView();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_adults_info, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.BOOKING_CHILD_INFO);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();

        binding.btnPay.setOnClickListener(this);
        KeyboardVisibilityEvent.setEventListener(Objects.requireNonNull(getActivity()), keyboardVisibilityEventListener);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

        if (v == null)
            return;

        if (v.getId() == R.id.btnPay) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.BOOKING_ADD_CHILD_INFO);
            if (adapter == null)
                return;
            List<AdultsCountObserver> list = adapter.getList();

            if (list == null || list.size() <= 0) {
                BookingControllerFragment.bookingContent.setCurrentItem(2, true);
                return;
            }
            for (AdultsCountObserver adult : list) {

                String[] SeparatedName = adult.getName().split(" ");

                if (SeparatedName.length < 4) {
                    Toast.makeText(BookingChildrenInfoFragment.this.getContext(), getResources().getString(R.string.valid_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (adult.getNationality().trim().isEmpty()) {
                    Toast.makeText(BookingChildrenInfoFragment.this.getContext(), getResources().getString(R.string.missing_data), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (adult.isCivil()) {
                    if (adult.getCivilID().trim().isEmpty()) {
                        Toast.makeText(BookingChildrenInfoFragment.this.getContext(), getResources().getString(R.string.missing_data), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (adult.getNationality().trim().isEmpty()) {
                        Toast.makeText(BookingChildrenInfoFragment.this.getContext(), getResources().getString(R.string.missing_data), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }

            String adultJsonList = generateAdultJasonList(list);
            BookingControllerFragment.paymentModel.setChildList(adultJsonList);
            BookingControllerFragment.paymentModel.setOriginalChild(list);

            BookingControllerFragment.bookingContent.setCurrentItem(4, true);

        }

    }

    private String generateAdultJasonList(List<AdultsCountObserver> list) {
        JsonArray array = new JsonArray();
        for (AdultsCountObserver adult : list) {
            JsonObject object = new JsonObject();
            object.addProperty("name", adult.getName());
            object.addProperty("nationality", adult.getNationality());
            object.addProperty("passport_no", adult.getPassPortNumber());
            object.addProperty("civil_id", adult.getCivilID());
            array.add(object);
        }
        return array.toString();
    }


    @Override
    public void onAdultChangeListener() {

    }

    @Override
    public void onChildChangeListener() {

        //    populateData();
    }

    private List<AdultsCountObserver> getAdultList(List<AdultsCountObserver> filter) {

        List<AdultsCountObserver> data = new ArrayList<>();
        for (ModelTravellerDetails item : BookingTravellersFragment.selectedListChild) {
            AdultsCountObserver order = new AdultsCountObserver();
            if (item.getType() == 2 && item.isSelected() && !contains(item, filter)) {
                order.setName(item.getName());
                order.setNationality(item.getNationality());
                order.setPassPortNumber(item.getPassport_no());
                order.setCivilID(item.getCivil_id());
                data.add(order);
            }
        }

        return data;
    }

    private boolean contains(ModelTravellerDetails item, List<AdultsCountObserver> filter) {
        for (AdultsCountObserver index : filter) {
            if (index.getId() != null && !index.getId().isEmpty() && index.getId().equals(item.getId()))
                return true;
        }
        return false;
    }

    private void enhancedDatePopulation() {

        List<AdultsCountObserver> filter = new ArrayList<>();
        if (adapter != null)
            filter.addAll(adapter.getWriteList());


        filter.addAll(getAdultList(filter));
        AtomicInteger index = new AtomicInteger();
        for (index.set(0); index.get() < BookingControllerFragment.paymentModel.getChild(); index.getAndIncrement())
            filter.add(new AdultsCountObserver());

        adapter = new AdultsObserverAdapter(BookingChildrenInfoFragment.this.getContext(), BookingControllerFragment.offer, filter.subList(0, BookingControllerFragment.paymentModel.getChild()), AdultsObserverAdapter.CHILD_OBSERVER_TYPE);
        binding.rvAdultsInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAdultsInfo.setHasFixedSize(true);
        binding.rvAdultsInfo.setAdapter(adapter);

        if (getActivity() != null) {
            InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert keyboard != null;
            keyboard.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }

        if (isVisibleToUser) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> adapter.showCaseView(), 500);

        }

    }


}
