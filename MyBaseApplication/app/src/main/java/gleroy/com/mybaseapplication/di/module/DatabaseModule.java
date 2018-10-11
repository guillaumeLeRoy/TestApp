package gleroy.com.mybaseapplication.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gleroy.com.mybaseapplication.data.local.dao.MyDatabase;
import gleroy.com.mybaseapplication.data.local.dao.TodoDao;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    MyDatabase providesDatabase(Application application) {
        return Room.databaseBuilder(application, MyDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    TodoDao providePaymentRequestDao(@NonNull MyDatabase database) {
        return database.todoDao();
    }
}
