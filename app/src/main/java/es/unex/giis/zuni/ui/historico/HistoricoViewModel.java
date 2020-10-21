package es.unex.giis.zuni.ui.historico;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoricoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HistoricoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is historico fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}