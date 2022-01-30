package com.asc.yazy.cash.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.dao.FullSearchHistoryDao;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;

import java.util.List;

public class FullSearchHistoryRepository {

    private FullSearchHistoryDao searchHistoryDao;
    private LiveData<List<FullSearchHistoryModel>> all;


    public FullSearchHistoryRepository(Context application) {
        TravelXRoomDateBase database = TravelXRoomDateBase.getInstance(application);
        searchHistoryDao = database.FullSearchHistoryDao();
        all = searchHistoryDao.getAll();

    }


    public LiveData<List<FullSearchHistoryModel>> getAll() {
        return all;
    }


    public void insert(FullSearchHistoryModel model) {
        new InsetTask(searchHistoryDao, model).execute();
    }

    public void delete(FullSearchHistoryModel notificationRoomModel) {
        new DeleteTask(searchHistoryDao, notificationRoomModel).execute();
    }

    public void update(FullSearchHistoryModel notificationRoomModel) {
        new UpdateTask(searchHistoryDao, notificationRoomModel).execute();
    }

    public void deleteAll() {
        new DeleteAllTask(searchHistoryDao).execute();
    }


    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private FullSearchHistoryDao dao;


        DeleteAllTask(FullSearchHistoryDao dao) {
            this.dao = dao;

        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteAll();
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Void, Void, Void> {
        private FullSearchHistoryDao dao;
        private FullSearchHistoryModel model;

        DeleteTask(FullSearchHistoryDao dao, FullSearchHistoryModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.delete(model);
            return null;
        }
    }

    private static class InsetTask extends AsyncTask<Void, Void, Void> {
        private FullSearchHistoryDao dao;
        private FullSearchHistoryModel model;

        InsetTask(FullSearchHistoryDao dao, FullSearchHistoryModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            if (model != null && model.getDestination().length() > 0) {
                dao.insert(model);
            }

            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Void, Void, Void> {

        private FullSearchHistoryDao dao;
        private FullSearchHistoryModel notificationRoomModel;

        UpdateTask(FullSearchHistoryDao currentLocationDao, FullSearchHistoryModel notificationRoomModel) {
            this.dao = currentLocationDao;
            this.notificationRoomModel = notificationRoomModel;
        }

        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.update(notificationRoomModel);
            return null;
        }


    }
}
