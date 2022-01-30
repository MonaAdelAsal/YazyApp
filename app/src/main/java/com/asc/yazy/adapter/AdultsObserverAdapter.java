package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.BookingActivity;
import com.asc.yazy.api.model.ModelDetails;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.AdultsCountObserver;
import com.asc.yazy.core.DatePickerDialog;
import com.asc.yazy.databinding.RvAdultsDataBinding;
import com.asc.yazy.fragment.ScanFragment;
import com.asc.yazy.interfaces.OnIntentReceived;
import com.asc.yazy.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class AdultsObserverAdapter extends RecyclerView.Adapter<AdultsObserverAdapter.ViewHolder> {


    public final static int ADULT_OBSERVER_TYPE = 12;
    public final static int CHILD_OBSERVER_TYPE = 13;
    private final int observerType;
    private ModelDetails offer;
    private Context context;
    private List<AdultsCountObserver> orderList;
    private int offset = 30;
    private boolean animated = true;
    private View view;
    private GuideView caseView;
    private String SelectedNationality = "";
    private final OnIntentReceived listener = (position, data) -> {
        if (data == null) {
            Toast.makeText(context, context.getResources().getString(R.string.retrive), Toast.LENGTH_LONG).show();

        } else {
            String DocumentType = data.getStringExtra("DocumentType");
            String DocumentNumber = data.getStringExtra("DocumentNumber");
            String Name = data.getStringExtra("Name");
            String Nationality = data.getStringExtra("Nationality");
            // String Gender = data.getStringExtra("Gender");
            String ExpiationDate = data.getStringExtra("ExpiationDate");
            // String BirthDate = data.getStringExtra("BirthDate");
            SelectedNationality = Objects.requireNonNull(Nationality).substring(0, 2);
            if (offer.getAccept_civil_id() == 0 && Objects.requireNonNull(DocumentType).equals(Constants.IDENTITY_CARD)) {
                Toast.makeText(context, context.getResources().getString(R.string.accept), Toast.LENGTH_LONG).show();
                return;
            }
            if (Objects.requireNonNull(DocumentType).equals(Constants.IDENTITY_CARD)) {
                orderList.get(position).setName(Name);
                orderList.get(position).setCivil(true);
                orderList.get(position).setCivilID(DocumentNumber);
                orderList.get(position).setPassPortNumber(DocumentNumber);
                orderList.get(position).setNationality(SelectedNationality);
                orderList.get(position).setEdited(true);
            } else {
                orderList.get(position).setName(Name);
                orderList.get(position).setCivil(false);
                orderList.get(position).setCivilID(DocumentNumber);
                orderList.get(position).setPassPortNumber(DocumentNumber);
                orderList.get(position).setPassPortEXDate(ExpiationDate);
                orderList.get(position).setNationality(SelectedNationality);
                orderList.get(position).setEdited(true);
            }
            notifyItemChanged(position);
        }

    };


    public AdultsObserverAdapter(Context context, ModelDetails offer, List<AdultsCountObserver> orderList, int observerType) {
        this.context = context;
        this.offer = offer;
        this.orderList = orderList;
        this.observerType = observerType;
    }

    @Override
    public int getItemCount() {
        if (orderList == null) return 0;
        return orderList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAdultsDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_adults_data, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        assert orderList != null;
        if (position < orderList.size() && position != RecyclerView.NO_POSITION) {
            holder.setViewModel(orderList.get(position), position);
        }
        if (position == 0) {
            view = holder.binding.getRoot();
        }

    }

    public List<AdultsCountObserver> getList() {
        return orderList;
    }

    public List<AdultsCountObserver> getWriteList() {
        List<AdultsCountObserver> newList = new ArrayList<>();
        for (AdultsCountObserver item : orderList) {
            Log.d("ADULTS", "getWriteList: name " + item.getName());
            Log.d("ADULTS", "getWriteList: nati " + item.getNationality());
            Log.d("ADULTS", "getWriteList: pass " + item.getPassPortNumber());
            Log.d("ADULTS", "getWriteList: civi " + item.getCivilID());
            Log.d("ADULTS", "getWriteList: edit " + item.isEdited());
            if (item.isEdited())
                newList.add(item);
        }
        return newList;
    }

    public void showCaseView() {

        if (context == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(context).getPreference(Constants.IS_BOOK_COUNT_ADULTS_FIRST_RUN, true);


        if (!firstRun)
            return;

        caseView = new GuideView.Builder(context)
                .setTitle(Objects.requireNonNull(context).getResources().getString(R.string.intro_book))
                .setContentText(Objects.requireNonNull(context).getResources().getString(R.string.book_info_adult))
                .setTargetView(view)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();


        caseView.show();

        UtilsPreference.getInstance(context).savePreference(Constants.IS_BOOK_COUNT_ADULTS_FIRST_RUN, false);

    }

    public void showCaseViewChild() {

        if (context == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(context).getPreference(Constants.IS_BOOK_COUNT_CHILD_FIRST_RUN, true);

        if (!firstRun)
            return;

        GuideView caseView = new GuideView.Builder(context)
                .setTitle(Objects.requireNonNull(context).getResources().getString(R.string.intro_book))
                .setContentText(Objects.requireNonNull(context).getResources().getString(R.string.book_info_child))
                .setTargetView(view)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();


        caseView.show();

        UtilsPreference.getInstance(context).savePreference(Constants.IS_BOOK_COUNT_CHILD_FIRST_RUN, false);

    }

    public boolean isCaseViewShowing() {
        return caseView != null && caseView.isShowing();
    }

    public void dismissCaseView() {
        if (caseView != null)
            caseView.dismiss();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RvAdultsDataBinding binding;


        private ViewHolder(RvAdultsDataBinding rvCustomItemBinding) {
            super(rvCustomItemBinding.getRoot());
            this.binding = rvCustomItemBinding;

            binding.ivScan.setOnClickListener(this);

            binding.etName.addTextChangedListener(nameWatcher());
            // binding.etNationality.addTextChangedListener(nationalityWatcher());
            binding.etCivilID.addTextChangedListener(civilIDWatcher());
            binding.etPassPortNumber.addTextChangedListener(passPortWatcher());
            binding.etExpiryDate.addTextChangedListener(ExpiryDateWatcher());
            binding.etBirthDate.addTextChangedListener(ExpiryDateWatcher());

            binding.cpNationality.setOnCountryChangeListener(() -> {
                SelectedNationality = binding.cpNationality.getSelectedCountryNameCode();
                orderList.get(getAbsoluteAdapterPosition()).setNationality(SelectedNationality);
            });
            binding.etName.clearFocus();
            // binding.etNationality.clearFocus();
            binding.etCivilID.clearFocus();
            binding.etPassPortNumber.clearFocus();
            binding.etExpiryDate.clearFocus();
            binding.etBirthDate.clearFocus();
            binding.cpNationality.setExcludedCountries("IL");
            binding.ex.setOnClickListener(this);
            binding.BD.setOnClickListener(this);

        }


        void setViewModel(AdultsCountObserver customOrderObserver, int position) {
            if (binding != null) {


                if (animated) {
                    Animation title = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall);
                    title.setDuration(300);
                    title.setStartOffset(offset);
                    binding.tvTitle.setAnimation(title);

                    Animation name = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall);
                    name.setDuration(300);
                    name.setStartOffset(offset + 30);
                    binding.layoutName.setAnimation(name);

                    Animation nationality = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall);
                    nationality.setDuration(300);
                    nationality.setStartOffset(offset + 60);
                    binding.layoutNationality.setAnimation(nationality);


                    Animation passport = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall);
                    passport.setDuration(300);
                    passport.setStartOffset(offset + 90);
                    binding.layoutPassPort.setAnimation(passport);

                    offset = offset + 30;
                }
                if (position == orderList.size() - 1) {
                    animated = false;
                }
                binding.etName.setText(String.valueOf(customOrderObserver.getName()));

                binding.cpNationality.setCountryForNameCode(String.valueOf(customOrderObserver.getNationality()));
                SelectedNationality = String.valueOf(customOrderObserver.getNationality());
                if (SelectedNationality.length() == 0) {
                    SelectedNationality = "KW";
                    customOrderObserver.setNationality(SelectedNationality);
                }
                binding.etCivilID.setText(!("" + customOrderObserver.getCivilID()).equals("null") ? String.valueOf(customOrderObserver.getCivilID()) : "");
                binding.etPassPortNumber.setText(!("" + customOrderObserver.getPassPortNumber()).equals("null") ? String.valueOf(customOrderObserver.getPassPortNumber()) : "");
                binding.etExpiryDate.setText(!("" + customOrderObserver.getPassPortEXDate()).equals("null") ? String.valueOf(customOrderObserver.getPassPortEXDate()) : "");
                binding.etBirthDate.setText(!("" + customOrderObserver.getBirthDate()).equals("null") ? String.valueOf(customOrderObserver.getBirthDate()) : "");

                if (observerType == ADULT_OBSERVER_TYPE) {
                    binding.BDLayout.setVisibility(View.GONE);
                    binding.tvTitle.setText((context.getResources().getString(R.string.adult) + " " + (position + 1)));
                    binding.tvAge.setVisibility(View.INVISIBLE);
                } else if (observerType == CHILD_OBSERVER_TYPE) {
                    binding.BDLayout.setVisibility(View.VISIBLE);
                    binding.tvTitle.setText((context.getResources().getString(R.string.child) + " " + (position + 1)));
                    binding.tvAge.setVisibility(View.VISIBLE);
                }

                if (offer.getAccept_civil_id() == 1) {
                    binding.layoutChecker.setVisibility(View.VISIBLE);
                    binding.checkCivilID.setOnClickListener(this);
                    binding.checkPassPort.setOnClickListener(this);

                } else {
                    binding.layoutChecker.setVisibility(View.GONE);

                }
                if (customOrderObserver.isCivil()) {
                    binding.checkCivilID.setChecked(true);
                    binding.checkPassPort.setChecked(false);
                    binding.llExpiryDate.setVisibility(View.GONE);
                    binding.layoutPassPort.setVisibility(View.GONE);
                    binding.layoutCivilID.setVisibility(View.VISIBLE);
                } else {
                    binding.checkCivilID.setChecked(false);
                    binding.checkPassPort.setChecked(true);
                    binding.llExpiryDate.setVisibility(View.VISIBLE);
                    binding.layoutPassPort.setVisibility(View.VISIBLE);
                    binding.layoutCivilID.setVisibility(View.GONE);
                }

                //  binding.layoutPassPort.setVisibility(View.VISIBLE);
                //  binding.layoutCivilID.setVisibility(View.GONE);

                binding.etName.clearFocus();
                //binding.etNationality.clearFocus();
                binding.etCivilID.clearFocus();
                binding.etPassPortNumber.clearFocus();
                binding.etExpiryDate.clearFocus();
                binding.etBirthDate.clearFocus();

                if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && getAbsoluteAdapterPosition() == orderList.size() - 1) {
                    binding.etCivilID.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    binding.etPassPortNumber.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    binding.etExpiryDate.setImeOptions(EditorInfo.IME_ACTION_DONE);

                } else {
                    binding.etExpiryDate.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    binding.etCivilID.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    binding.etPassPortNumber.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                }
/*
                if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && getAbsoluteAdapterPosition() == 0) {

                    binding.etName.requestFocus();
                    binding.etNationality.requestFocus();
                    binding.etCivilID.requestFocus();
                    binding.etPassPortNumber.requestFocus();
                }
*/
            }
        }

        TextWatcher passPortWatcher() {
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null && !editable.toString().isEmpty() && !editable.toString().toLowerCase().equals("null") && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && orderList.get(getAbsoluteAdapterPosition()).getPassPortNumber() != null) {
                        if (!orderList.get(getAbsoluteAdapterPosition()).getPassPortNumber().trim().toLowerCase().equals(editable.toString().trim().toLowerCase()))
                            orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        orderList.get(getAbsoluteAdapterPosition()).setPassPortNumber(editable.toString());
                    }
                }
            };
        }

        TextWatcher ExpiryDateWatcher() {
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null && !editable.toString().isEmpty() && !editable.toString().toLowerCase().equals("null") && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && orderList.get(getAbsoluteAdapterPosition()).getPassPortNumber() != null) {
                        if (!orderList.get(getAbsoluteAdapterPosition()).getPassPortEXDate().trim().toLowerCase().equals(editable.toString().trim().toLowerCase()))
                            orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        orderList.get(getAbsoluteAdapterPosition()).setPassPortEXDate(editable.toString());
                    }
                }
            };
        }

        TextWatcher civilIDWatcher() {
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null && !editable.toString().isEmpty() && !editable.toString().toLowerCase().equals("null") && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && orderList.get(getAbsoluteAdapterPosition()).getCivilID() != null) {
                        if (!orderList.get(getAbsoluteAdapterPosition()).getCivilID().trim().toLowerCase().equals(editable.toString().trim().toLowerCase()))
                            orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        orderList.get(getAbsoluteAdapterPosition()).setCivilID(editable.toString());
                    }
                }
            };
        }

        TextWatcher nameWatcher() {
            return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable != null && !editable.toString().isEmpty() && !editable.toString().toLowerCase().equals("null") && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && orderList.get(getAbsoluteAdapterPosition()).getName() != null) {
                        if (!orderList.get(getAbsoluteAdapterPosition()).getName().trim().toLowerCase().equals(editable.toString().trim().toLowerCase()))
                            orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        orderList.get(getAbsoluteAdapterPosition()).setName(editable.toString());
                    }
                }
            };
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {

            if (v == null)
                return;
            int index = getAbsoluteAdapterPosition();
            if (index == RecyclerView.NO_POSITION)
                return;

            switch (v.getId()) {
                case R.id.checkCivilID:
                    if (!orderList.get(index).isCivil()) {
                        binding.layoutPassPort.setVisibility(View.GONE);
                        binding.llExpiryDate.setVisibility(View.GONE);
                        binding.layoutCivilID.setVisibility(View.VISIBLE);
                        orderList.get(getAbsoluteAdapterPosition()).setCivil(true);
                        binding.checkPassPort.setChecked(false);
                        binding.checkCivilID.setChecked(true);
                    } else
                        binding.checkCivilID.setChecked(true);
                    break;
                case R.id.ivScan:
                    ((BookingActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.mainContent, new ScanFragment(getAbsoluteAdapterPosition(), listener))
                            .addToBackStack(null)
                            .commit();
                case R.id.checkPassPort:
                    if (orderList.get(index).isCivil()) {
                        binding.layoutPassPort.setVisibility(View.VISIBLE);
                        binding.llExpiryDate.setVisibility(View.VISIBLE);
                        binding.layoutCivilID.setVisibility(View.GONE);
                        orderList.get(getAbsoluteAdapterPosition()).setCivil(false);
                        binding.checkPassPort.setChecked(true);
                        binding.checkCivilID.setChecked(false);
                    } else
                        binding.checkPassPort.setChecked(true);

                    break;
                case R.id.ex:
                    new DatePickerDialog(Objects.requireNonNull(context), (selectedDate, formattedDate) -> {
                        if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                            return;
                        orderList.get(getAbsoluteAdapterPosition()).setPassPortEXDate(formattedDate);
                        orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        notifyItemChanged(getAbsoluteAdapterPosition());
                    });
                    break;
                case R.id.BD:
                    new DatePickerDialog(Objects.requireNonNull(context), (selectedDate, formattedDate) -> {
                        if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                            return;
                        orderList.get(getAbsoluteAdapterPosition()).setBirthDate(formattedDate);
                        orderList.get(getAbsoluteAdapterPosition()).setEdited(true);
                        notifyItemChanged(getAbsoluteAdapterPosition());
                    });
                    break;

            }
        }
    }

}
