package es.unex.giis.zuni.ui.previsiones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrevisionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PrevisionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is previsiones fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}