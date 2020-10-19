package es.unex.giis.zuni.ui.previsiones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import es.unex.giis.zuni.R;
// https://api.openweathermap.org/data/2.5/weather?lat=38.59758&lon=-5.43701&appid=55ab2d28aad932680b93bf96e8e44f6e

/*
{
  "coord": {
    "lon": -5.44,
    "lat": 38.6
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01n"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 288.15,
    "feels_like": 284.24,
    "temp_min": 288.15,
    "temp_max": 288.15,
    "pressure": 1017,
    "humidity": 35
  },
  "visibility": 10000,
  "wind": {
    "speed": 2.68,
    "deg": 317,
    "gust": 4.47
  },
  "clouds": {
    "all": 0
  },
  "dt": 1602699728,
  "sys": {
    "type": 3,
    "id": 2011374,
    "country": "ES",
    "sunrise": 1602657000,
    "sunset": 1602697501
  },
  "timezone": 7200,
  "id": 2513618,
  "name": "Monterrubio de la Serena",
  "cod": 200
}

https://api.openweathermap.org/data/2.5/onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e



 */
public class PrevisionesFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_previsiones, container, false);


        /*
        Lista de posibles condiciones:
        - Thunderstorm
        - Drizzle
        - Rain
        - Snow
        - Clear
        - Clouds
         */


        return root;
    }
}