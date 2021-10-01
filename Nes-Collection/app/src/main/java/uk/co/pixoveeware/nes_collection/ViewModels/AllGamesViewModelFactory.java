package uk.co.pixoveeware.nes_collection.ViewModels;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AllGamesViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;

    public AllGamesViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AllGamesViewModel.class)) {
            return (T) new AllGamesViewModel(mApplication, mParam);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
