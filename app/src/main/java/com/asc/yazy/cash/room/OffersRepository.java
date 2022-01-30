package com.asc.yazy.cash.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.dao.OfferRoomDao;
import com.asc.yazy.cash.room.model.OfferRoomModel;

import java.util.List;

public class OffersRepository {

    private OfferRoomDao offerRoomDao;
    private LiveData<List<OfferRoomModel>> allOffers;
    private LiveData<List<OfferRoomModel>> allLikedOffers;


    public OffersRepository(Context application) {
        TravelXRoomDateBase database = TravelXRoomDateBase.getInstance(application);
        offerRoomDao = database.offerRoomDao();
        allOffers = offerRoomDao.getAllOffers();
        allLikedOffers = offerRoomDao.getAllLikedOffers();


    }

    public LiveData<List<OfferRoomModel>> getAllLikedOffers() {
        return allLikedOffers;
    }

    public LiveData<OfferRoomModel> getOfferById(String id){
        return offerRoomDao.getOfferById(id);
    }

    public LiveData<List<OfferRoomModel>> getAllOffers() {
        return allOffers;
    }


    public void insertOrUpdate(OfferRoomModel model) {
        new InsetOfUpdateOfferTask(offerRoomDao, model).execute();
    }


    public void insertOffer(OfferRoomModel model) {
        new InsetOfferTask(offerRoomDao, model).execute();
    }

    public void delete(OfferRoomModel offerRoomModel) {
        new DeleteOfferTask(offerRoomDao, offerRoomModel).execute();
    }

    public void deleteAllOffers() {
        new DeleteAllOffersTask(offerRoomDao).execute();
    }

    private static class InsetOfUpdateOfferTask extends AsyncTask<Void, Void, Void> {
        private OfferRoomDao dao;
        private OfferRoomModel model;

        InsetOfUpdateOfferTask(OfferRoomDao dao, OfferRoomModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            OfferRoomModel offer = dao.getOffer(model.getLocalID());
            if (offer == null)
                dao.insert(model);
            else {
                dao.updateOfferByID(offer.getLocalID(), model.getCurrency());
                System.out.println("OFFERDATEBASE " + "is esxisit with id = " + offer.getLocalID() + " liked = " + offer.isLike());
            }
            return null;
        }
    }

    private static class DeleteOfferTask extends AsyncTask<Void, Void, Void> {
        private OfferRoomDao dao;
        private OfferRoomModel model;

        DeleteOfferTask(OfferRoomDao dao, OfferRoomModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteOffer(model);
            return null;
        }
    }

    private static class InsetOfferTask extends AsyncTask<Void, Void, Void> {
        private OfferRoomDao dao;
        private OfferRoomModel model;

        InsetOfferTask(OfferRoomDao dao, OfferRoomModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.insert(model);
            return null;
        }
    }

    private static class DeleteAllOffersTask extends AsyncTask<Void, Void, Void> {

        private OfferRoomDao dao;

        DeleteAllOffersTask(OfferRoomDao currentLocationDao) {
            this.dao = currentLocationDao;
        }

        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.delete();
            return null;
        }


    }
}
