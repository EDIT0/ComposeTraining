package com.edit.unitconverterapp.di

import android.app.Application
import androidx.room.Room
import com.edit.unitconverterapp.data.ConverterDatabase
import com.edit.unitconverterapp.data.ConverterRepository
import com.edit.unitconverterapp.data.ConverterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConverterDatabase(app: Application): ConverterDatabase {
        return Room.databaseBuilder(
            app,
            ConverterDatabase::class.java,
            "converter_data_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideConverterRepository(db: ConverterDatabase): ConverterRepository {
        return ConverterRepositoryImpl(db.converterDAO)
    }
}