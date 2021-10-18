package com.tids.clikonservice.Utils.RoomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


//annotation for getting model class ,setting version and exportSchema
@Database(entities = ClikonModel.class, version = 1, exportSchema = false)
public abstract class ClikonDatabase extends RoomDatabase {
    //instanse of given class
    private static ClikonDatabase instanse;

    //difine an abstract method of return type NoteDoa
    public abstract ClikonDao clikonDao();

    //method to create instanse of database named todo_list
    public static synchronized ClikonDatabase getInstance(Context context)
    { if (instanse==null)
    {
        instanse= Room.databaseBuilder(context.getApplicationContext()
                ,ClikonDatabase.class,"clikon_db")
                .fallbackToDestructiveMigration().build();
    }
        return instanse;
    }
}
