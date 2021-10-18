package com.tids.clikonservice.Utils.RoomDb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClikonViewModel extends AndroidViewModel {

    private ClikonRepository repository;
    private LiveData<List<ClikonModel>> productlist;
    private LiveData<Integer> productCount;

    //constructor of todoviewModel
    public ClikonViewModel(@NonNull Application application)
    {
        super(application);
        repository=new ClikonRepository(application);
        productlist=repository.getAllNotes();
        productCount=repository.getPending();
    }

    //our activities will use these methods
    public void insert(ClikonModel clikonModel)
    {
        repository.insert(clikonModel);
    }

    public void delete(ClikonModel clikonModel)
    {
        repository.delete(clikonModel);
    }

    public void update(ClikonModel clikonModel)
    {
        repository.update(clikonModel);
    }

    public void deleteAll()
    {
        repository.deleteAllNotes();
    }

    public  LiveData<Integer> getProductCount(){
        return productCount;
    }

    public LiveData<List<ClikonModel>> getProductlist()
    {
        return productlist;
    }

}
