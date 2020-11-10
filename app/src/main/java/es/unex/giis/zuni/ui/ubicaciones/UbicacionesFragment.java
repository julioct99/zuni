package es.unex.giis.zuni.ui.ubicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import es.unex.giis.zuni.R;

public class UbicacionesFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ubicaciones, container, false);
        final TextView textView = root.findViewById(R.id.text_settings);
        textView.setText("This is ubicaciones fragment");

        return root;
    }
}