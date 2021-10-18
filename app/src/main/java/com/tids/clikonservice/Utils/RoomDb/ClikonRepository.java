package com.tids.clikonservice.Utils.RoomDb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ClikonRepository {

    private ClikonDao clikonDao;
    private LiveData<List<ClikonModel>> productlist;
    private LiveData<Integer> productCount;

    public ClikonRepository(Application application)
    {
        ClikonDatabase database = ClikonDatabase.getInstance(application);
        clikonDao = database.clikonDao();
        productlist = clikonDao.getAllNotes();
        productCount=clikonDao.totalProductCount();
    }

    //our viewmodel will access these mathods
    public void insert(ClikonModel todo) {
        new InsertTodoAsyncTask(clikonDao).execute(todo);
    }

    public void update(ClikonModel todo) {
        new UpdateTodoAsyncTask(clikonDao).execute(todo);
    }

    public void delete(ClikonModel todo) {
        new DeleteTodoAsyncTask(clikonDao).execute(todo);
    }

    public void deleteAllNotes() {
        new DeleteAllTodoAsyncTask(clikonDao).execute();
    }

    public LiveData<Integer> getPending(){
        return productCount;
    }

    public LiveData<List<ClikonModel>> getAllNotes() {
        return productlist;
    }

    //all asyncronous tasks to run in background thread
    private static class InsertTodoAsyncTask extends AsyncTask<ClikonModel, Void, Void> {
        private ClikonDao todoDao;
        private InsertTodoAsyncTask(ClikonDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(ClikonModel... todoModels) {
            todoDao.insertData(todoModels[0]);
            return null;
        }
    }
    private static class DeleteTodoAsyncTask extends AsyncTask<ClikonModel, Void, Void> {
        private ClikonDao todoDao;
        private DeleteTodoAsyncTask(ClikonDao todoDao) {
            this.todoDao=todoDao;
        }

        @Override
        protected Void doInBackground(ClikonModel... todoModels) {
            todoDao.deleteData(todoModels[0]);
            return null;
        }
    }
    private static class UpdateTodoAsyncTask extends AsyncTask<ClikonModel, Void, Void> {
        private ClikonDao todoDao;
        private UpdateTodoAsyncTask(ClikonDao noteDao) {
            this.todoDao = noteDao;
        }

        @Override
        protected Void doInBackground(ClikonModel... todoModels) {
            todoDao.updateData(todoModels[0]);
            return null;
        }
    }
    private static class DeleteAllTodoAsyncTask extends AsyncTask<Void, Void, Void> {
        private ClikonDao todoDao;
        private DeleteAllTodoAsyncTask(ClikonDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.deleteAllData();
            return null;
        }
    }

}
