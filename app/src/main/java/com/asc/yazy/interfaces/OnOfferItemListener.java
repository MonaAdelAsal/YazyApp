package com.asc.yazy.interfaces;

import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.cash.room.model.OfferRoomModel;

public interface OnOfferItemListener {

    void OnOfferClicked(ModelOffer offer);

    void OnCashedOfferClicked(OfferRoomModel offer);
}
