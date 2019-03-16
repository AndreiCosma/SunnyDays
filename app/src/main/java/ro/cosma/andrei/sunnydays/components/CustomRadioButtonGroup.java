package ro.cosma.andrei.sunnydays.components;

import android.view.View;
import android.widget.RadioButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.components.listener.ICustomRadioButtonGroupListener;

/**
 * Created by andre on 26.11.2017.
 */

public class CustomRadioButtonGroup implements View.OnClickListener {
    private byte groupId;
    private List<RadioButton> group;
    private WeakReference<ICustomRadioButtonGroupListener> listener;

    public CustomRadioButtonGroup(ICustomRadioButtonGroupListener listener, byte groupId) {
/*Listenerul nu este necesar si poate fi trecut null
* daca se doreste doar schimbarea starii butoanelor.*/
        this.listener = new WeakReference(listener);
        this.group = new ArrayList<>();
        this.groupId = groupId;
    }

    public CustomRadioButtonGroup(ICustomRadioButtonGroupListener listener, List<RadioButton> group, byte groupId) {
        this.listener = new WeakReference(listener);
        this.group = group;
        this.groupId = groupId;
    }

    @Override
    public void onClick(View v) {
        for (RadioButton rb : group) {
            if (rb.getId() != v.getId()) {
                rb.setChecked(false);
            }
        }
        if (listener.get() != null) {
            listener.get().onRadioButtonPressed(v.getId(), groupId);
        }
    }

    public void addRadioButton(RadioButton rb) {
        rb.setOnClickListener(this);
        this.group.add(rb);
    }

    public void setChecked(int buttonId){
        for (RadioButton rb : group) {
            if (buttonId == rb.getId()) {
                rb.setChecked(true);
            }
        }
    }
}



