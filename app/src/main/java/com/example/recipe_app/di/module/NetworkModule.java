package com.example.recipe_app.di.module;


import com.example.recipe_app.data.remote.DataApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Named("kandean_retrofit")
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(DataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public DataApi dataApi(@Named("kandean_retrofit") Retrofit retrofit) {
        return retrofit.create(DataApi.class);
    }
}
