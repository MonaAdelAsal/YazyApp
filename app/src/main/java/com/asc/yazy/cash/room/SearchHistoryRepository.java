package com.asc.yazy.cash.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.dao.SearchHistoryDao;
import com.asc.yazy.cash.room.model.SearchHistoryModel;

import java.util.List;

public class SearchHistoryRepository {

    private SearchHistoryDao searchHistoryDao;
    private LiveData<List<SearchHistoryModel>> all;


    public SearchHistoryRepository(Context application) {
        TravelXRoomDateBase database = TravelXRoomDateBase.getInstance(application);
        searchHistoryDao = database.searchHistoryDao();
        all = searchHistoryDao.getAll();

    }


    public LiveData<List<SearchHistoryModel>> getAll() {
        return all;
    }


    public void insert(SearchHistoryModel model) {
        new InsetTask(searchHistoryDao, model).execute();
    }

    public void delete(SearchHistoryModel notificationRoomModel) {
        new DeleteTask(searchHistoryDao, notificationRoomModel).execute();
    }

    public void update(SearchHistoryModel notificationRoomModel) {
        new UpdateTask(searchHistoryDao, notificationRoomModel).execute();
    }

    public void deleteAll() {
        new DeleteAllTask(searchHistoryDao).execute();
    }


    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private SearchHistoryDao dao;


        DeleteAllTask(SearchHistoryDao dao) {
            this.dao = dao;

        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteAll();
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Void, Void, Void> {
        private SearchHistoryDao dao;
        private SearchHistoryModel model;

        DeleteTask(SearchHistoryDao dao, SearchHistoryModel model) {
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
        private SearchHistoryDao dao;
        private SearchHistoryModel model;

        InsetTask(SearchHistoryDao dao, SearchHistoryModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            if (model.getDestination().length() > 0) {
                dao.insert(model);
            }

            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Void, Void, Void> {

        private SearchHistoryDao dao;
        private SearchHistoryModel notificationRoomModel;

        UpdateTask(SearchHistoryDao currentLocationDao, SearchHistoryModel notificationRoomModel) {
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
