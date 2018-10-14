package com.kaiwin.squirreldeliver;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCreateDeliverTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCreateDeliverTask extends Fragment {
    public final static String TAG = "FragmentCreateDeliverTask";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button buttonComfirm;
    RadioGroup radioGroup;
    TextInputEditText textInputEditTextFromName, textInputEditTextFromPhone;
    TextInputEditText textInputEditTextToName, textInputEditTextToPhone;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCreateDeliverTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCreateDeliverTask.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCreateDeliverTask newInstance(String param1, String param2) {
        FragmentCreateDeliverTask fragment = new FragmentCreateDeliverTask();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.Create_Deliver_Task_Fragment_Title);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_deliver_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        textInputEditTextFromName = activity.findViewById(R.id.textInputEditTextFromName);
        textInputEditTextFromPhone = activity.findViewById(R.id.textInputEditTextFromPhone);
        textInputEditTextToName = activity.findViewById(R.id.textInputEditTextToName);
        textInputEditTextToPhone = activity.findViewById(R.id.textInputEditTextToPhone);

        radioGroup = activity.findViewById(R.id.radioGroupInFragment);

        buttonComfirm = activity.findViewById(R.id.confirm_button);
        buttonComfirm.setOnClickListener(
                view -> {
                    RadioButton radioBtnSelected = activity.findViewById(radioGroup.getCheckedRadioButtonId());
                    String str;

                    if (radioBtnSelected == null)
                        str = getString(R.string.please_select_at_least_one_button_in_radio_button_group);
                    else
                        str = textInputEditTextFromPhone.getText().toString() + "\n"
                                + textInputEditTextFromName.getText().toString() + "\n"
                                + textInputEditTextToPhone.getText().toString() + "\n"
                                + textInputEditTextToName.getText().toString() + "\n"
                                + radioBtnSelected.getText().toString();

                    new AlertDialog.Builder(activity).setMessage(str).setCancelable(false)
                            .setPositiveButton(R.string.confirm_order, null)
                            .create().show();
                }
        );
    }
}
