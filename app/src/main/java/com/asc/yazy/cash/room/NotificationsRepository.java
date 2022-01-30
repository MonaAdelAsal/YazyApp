package com.asc.yazy.cash.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.dao.NotificationRoomDao;
import com.asc.yazy.cash.room.model.NotificationRoomModel;

import java.util.List;

public class NotificationsRepository {

    private static final String TAG = "NotificationsRepository";
    private final NotificationRoomDao noticationRoomDao;
    private final LiveData<List<NotificationRoomModel>> allNotifications;
    private final LiveData<Integer> allNotOpenedNotifications;


    public NotificationsRepository(Context application) {
        TravelXRoomDateBase database = TravelXRoomDateBase.getInstance(application);
        noticationRoomDao = database.NotificationRoomDao();
        allNotifications = noticationRoomDao.getAllNotifications();
        allNotOpenedNotifications = noticationRoomDao.getAllNotOpenNotifications();
    }


    public LiveData<List<NotificationRoomModel>> getAllNotifications() {
        return allNotifications;
    }

    public LiveData<Integer> getAllNotOpenedNotifications() {
        return allNotOpenedNotifications;
    }

    public void insertNotification(NotificationRoomModel model) {
        new InsetNotificationTask(noticationRoomDao, model).execute();
    }

    public void deleteNotification(NotificationRoomModel notificationRoomModel) {
        new DeleteNotificationTask(noticationRoomDao, notificationRoomModel).execute();
    }

    public void updateNotification(NotificationRoomModel notificationRoomModel) {
        new UpdateNotificationTask(noticationRoomDao, notificationRoomModel).execute();
    }

    public void deleteAllNotification() {
        new DeleteAllNotificationTask(noticationRoomDao).execute();
    }

    public void deleteAllNotificationByType(int type) {
        new DeleteNotificationByTypeTask(noticationRoomDao, type).execute();
    }

    public void updateNotificationsToRead() {
        new UpdateNotificationsStatusToReadTask(noticationRoomDao).execute();
    }

    private static class UpdateNotificationsStatusToReadTask extends AsyncTask<Void, Void, Void> {
        private final NotificationRoomDao dao;


        UpdateNotificationsStatusToReadTask(NotificationRoomDao dao) {
            this.dao = dao;

        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.markAllRead();
            return null;
        }
    }


    private static class DeleteAllNotificationTask extends AsyncTask<Void, Void, Void> {
        private final NotificationRoomDao dao;


        DeleteAllNotificationTask(NotificationRoomDao dao) {
            this.dao = dao;

        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteAllNotification();
            return null;
        }
    }

    private static class DeleteNotificationByTypeTask extends AsyncTask<Void, Void, Void> {
        private final NotificationRoomDao dao;
        private final int type;

        DeleteNotificationByTypeTask(NotificationRoomDao dao, int type) {
            this.dao = dao;
            this.type = type;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteAllNotificationByType(type);
            return null;
        }
    }

    private static class DeleteNotificationTask extends AsyncTask<Void, Void, Void> {
        private final NotificationRoomDao dao;
        private final NotificationRoomModel model;

        DeleteNotificationTask(NotificationRoomDao dao, NotificationRoomModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            dao.deleteNotification(model);
            return null;
        }
    }

    private static class InsetNotificationTask extends AsyncTask<Void, Void, Void> {
        private final NotificationRoomDao dao;
        private final NotificationRoomModel model;

        InsetNotificationTask(NotificationRoomDao dao, NotificationRoomModel model) {
            this.dao = dao;
            this.model = model;
        }


        @Override
        protected Void doInBackground(Void... PromoCodeModels) {
            NotificationRoomModel notification = dao.getNotification(model.getNotificationID());
            if (notification == null) {
                Log.d(TAG, "doInBackground: first time " + model.getNotificationID());
                dao.insert(model);
                return null;
            } else if (notification.getNotificationID() == model.getNotificationID() && !notification.getUpdated_at().equals(model.getUpdated_at())) {
                Log.d(TAG, "doInBackground: update time " + model.getNotificationID());
                dao.deleteNotification(notification);
                dao.insert(model);
                return null;
            } else {
                Log.d(TAG, "doInBackground: no insertion");
                return null;
            }
        }
    }

    private static class UpdateNotificationTask extends AsyncTask<Void, Void, Void> {

        private final NotificationRoomDao dao;
        private final NotificationRoomModel notificationRoomModel;

        UpdateNotificationTask(NotificationRoomDao currentLocationDao, NotificationRoomModel notificationRoomModel) {
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
