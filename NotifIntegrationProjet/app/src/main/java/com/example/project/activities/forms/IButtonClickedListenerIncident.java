package com.example.projet.activities.forms;

import android.view.View;

public interface IButtonClickedListenerIncident {
    void onButtonTempClicked(View button);
    void onButtonRainClicked(View button);
    void onButtonHailClicked(View button);

    void onButtonFogClicked(View button);
    void onButtonCloudClicked(View button);
    void onButtonStormClicked(View button);

    void onButtonWindClicked(View button);
    void onButtonCurrentClicked(View button);
    void onButtonTransparencyClicked(View button);

    void onButtonOtherClicked(View button);
}
