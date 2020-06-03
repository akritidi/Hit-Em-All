package com.example.myapplication;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

/** Αυτή η κλάσση αντιπροσωπεύει έναν δίαλογο εξόδου με τον χρήστη.*/

public class ExitDialog extends AppCompatDialogFragment {
    /**Σε αυτή την μέθοδο κατασκευάζεται ο δίαλογος που δίνει την επολογή για έξοδο
     * από την εφαρμογή.Καλείται από την πρώτη και βασική δραστηριότητα με το κουμπι
     * έξοδος.Δημηουτργεί δύο dialogButtons ναι και όχι,δρά ανάλογα.
     * @param savedInstanceState Bundle.
     *Επιστρέφει
     * @return builder αντικείμενο διλόγου.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.exit_dialog);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
}
