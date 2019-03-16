package ro.cosma.andrei.sunnydays.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import java.lang.ref.WeakReference;
import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.components.listener.IDeleteDialogListener;

/**
 * Created by Cosma Andrei on 17.10.2017.
 */

public class DeleteDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TAG = DeleteDialog.class.getSimpleName();
    private WeakReference<IDeleteDialogListener> listener;
    private String title = "";
    private int id;
    private int positionInAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (Dialog.BUTTON_POSITIVE == which) {
            listener.get().onPositiveDelete(id, positionInAdapter);
        }
    }

    public static DeleteDialog newInstance(IDeleteDialogListener listener, String title, int id, int positionInAdapter) {
        DeleteDialog dialog = new DeleteDialog();
        dialog.listener = new WeakReference<IDeleteDialogListener>(listener);
        dialog.title = title;
        dialog.id = id;
        dialog.positionInAdapter = positionInAdapter;
        return dialog;
    }

}
