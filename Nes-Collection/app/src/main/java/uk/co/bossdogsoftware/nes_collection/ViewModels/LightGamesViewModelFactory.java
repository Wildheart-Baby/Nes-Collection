package uk.co.bossdogsoftware.nes_collection.ViewModels;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LightGamesViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public LightGamesViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create( Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LightGamesViewModel.class)) {
            return (T) new LightGamesViewModel(mApplication, mParam);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
