package com.tids.clikonservice.Utils.RoomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


//data access object(Dao) to apply all queries on data base
@Dao
public interface ClikonDao {

    //annotation for inserting
    @Insert
    void insertData(ClikonModel clikonModel);

    //annotation for deleting
    @Delete
    void deleteData(ClikonModel clikonModel);

    //annotation for deleting
    @Update
    void updateData(ClikonModel clikonModel);

    //delete all list in main activity
    @Query("DELETE FROM products")
    void deleteAllData();

    //query to get count of products
    @Query("SELECT COUNT(1) FROM products")
    LiveData<Integer> totalProductCount();

    //query get list for Compelete Tasks activity
//    @Query("SELECT * FROM tasks WHERE status_check=1 ORDER BY todo_createdAt DESC")
//    LiveData<List<TodoModel>> comletedTasks();

    //query to get list for Main Activity
    @Query("SELECT * FROM products")
    LiveData<List<ClikonModel>> getAllNotes();
}
