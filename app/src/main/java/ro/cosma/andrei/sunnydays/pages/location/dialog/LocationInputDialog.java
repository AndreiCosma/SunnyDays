package ro.cosma.andrei.sunnydays.pages.location.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.pages.location.dialog.listener.ILocationInputDialogListener;

/*Created by Cosma Andrei
* 9/27/2017*/
public class LocationInputDialog extends AppCompatDialogFragment implements DialogInterface.OnClickListener {
    public static final String TAG = LocationInputDialog.class.getSimpleName();
    private TextInputEditText locationInputET;
    private WeakReference<ILocationInputDialogListener> listener;

    public static LocationInputDialog newInstance(ILocationInputDialogListener listener) {
        LocationInputDialog dialog = new LocationInputDialog();
        dialog.listener = new WeakReference<ILocationInputDialogListener>(listener);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

		/*initializare componente layout*/
        View baseView = inflater.inflate(R.layout.dialog_location_input, null);
        locationInputET = (TextInputEditText) baseView.findViewById(R.id.weather_location);

        builder.setTitle(getString(R.string.add_location));
        builder.setView(baseView);
        builder.setPositiveButton(getString(R.string.confirm_button), this);
        builder.setNegativeButton(getString(R.string.cancel), this);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (Dialog.BUTTON_POSITIVE == i) {
            listener.get().onPositiveAction(locationInputET.getText().toString());
        }
    }
}
