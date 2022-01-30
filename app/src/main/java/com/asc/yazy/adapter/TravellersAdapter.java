package com.asc.yazy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelTravellerDetails;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.DeleteTravellerDialog;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.RvTravellerBinding;
import com.asc.yazy.fragment.EditFreqTravelerFragment;
import com.asc.yazy.fragment.FrequentTravellersFragment;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravellersAdapter extends RecyclerView.Adapter<TravellersAdapter.ViewHolder> {


    //   private Dialog deleteTraveller;
    private final Activity context;
    private final boolean isLoading;
    private final List<ModelTravellerDetails> offersList;
    private final FrequentTravellersFragment.OnNewItem onNewItem;

    public TravellersAdapter(Activity context, List<ModelTravellerDetails> offersList, FrequentTravellersFragment.OnNewItem onNewItem, boolean isLoading) {
        this.context = context;
        this.offersList = offersList;
        this.isLoading = isLoading;
        this.onNewItem = onNewItem;
    }

    @NonNull
    @Override
    public TravellersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvTravellerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_traveller, parent, false);
        return new TravellersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TravellersAdapter.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION)
            holder.setViewModel(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (offersList == null) return 0;
        return offersList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TravellersAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TravellersAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    private void deleteTraveller(int index) {

        String token = UtilsPreference.getInstance(context).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(context).startActivity(new Intent(context, AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(context));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.deleteTraveller("Bearer " + token, Constants.getLANGUAGE(), offersList.get(index).getId());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {


                planeDialog.Dismiss();

                //     deleteTraveller.dismiss();
                if (response.code() == 401) {
                    Intent intent = new Intent(context, AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    context.startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    offersList.remove(index);
                    notifyItemRemoved(index);
                    if (onNewItem != null)
                        onNewItem.newData();

                } else {
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @SuppressLint("ResourceType")
            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                planeDialog.Dismiss();
                //  GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getResources().getString(R.string.cant_connect)).show();
                context.startActivity(new Intent(context, NoNetActivity.class));


            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RvTravellerBinding binding;

        ViewHolder(RvTravellerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.imgDelete.setOnClickListener(this);
            this.binding.layout.setOnClickListener(this);
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

        void setViewModel(ModelTravellerDetails offerModel) {
            if (binding != null) {

                if (isLoading) {
                    binding.loaderName.startShimmerAnimation();
                } else {
                    binding.loaderName.stopShimmerAnimation();
                    binding.tvName.setBackground(null);
                    binding.setData(offerModel);

                    if (getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION && getAbsoluteAdapterPosition() == offersList.size() - 1)
                        binding.line.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v == null)
                return;
            if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                return;
            if (v.getId() == R.id.imgDelete) {

                DeleteTravellerDialog dialog = new DeleteTravellerDialog(context, new DeleteTravellerDialog.DeleteFT() {
                    @Override
                    public void deleteIndex() {
                        deleteTraveller(getAbsoluteAdapterPosition());
                    }
                });
                dialog.show();
                /*
                deleteTraveller = new Dialog(Objects.requireNonNull(context), android.R.style.Theme_DeviceDefault_Dialog);
                deleteTraveller.setCancelable(true);
                CheckLanguage checkLanguage = new CheckLanguage(context);
                checkLanguage.UpdateLanguage();
                Objects.requireNonNull(deleteTraveller.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                DeleteTravelerBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.delete_traveler, null, false);
                deleteTraveller.setContentView(DialogBinding.getRoot());
                DialogBinding.tvCancel.setOnClickListener(v1 -> deleteTraveller.dismiss());
                DialogBinding.tvConfirm.setOnClickListener(v12 -> deleteTraveller(getAbsoluteAdapterPosition()));
                deleteTraveller.show();
                */
            }
            if (v.getId() == R.id.layout) {
                if (getAbsoluteAdapterPosition() == RecyclerView.NO_POSITION)
                    return;
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new EditFreqTravelerFragment(offersList.get(getAbsoluteAdapterPosition()), onNewItem))
                        .addToBackStack("ChangeLanguageFragment")
                        .commit();

            }
        }
    }
}







