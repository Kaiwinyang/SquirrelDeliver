package com.kaiwin.squirreldeliver;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserInformation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtUserName, txtPhotoUri;
    Button button;

    public FragmentUserInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserInformation newInstance(String param1, String param2) {
        FragmentUserInformation fragment = new FragmentUserInformation();
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
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.personal_info);

        View view = inflater.inflate(R.layout.fragment_user_information, container, false);

        txtPhotoUri = view.findViewById(R.id.textInputEditTextPhotoUri);
        txtUserName = view.findViewById(R.id.textInputEditTextName);
        button = view.findViewById(R.id.confirm_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        button.setOnClickListener(v ->
                user.updateProfile(new UserProfileChangeRequest.Builder()
                        .setDisplayName(txtUserName.getText().toString())
                        .setPhotoUri(Uri.parse(txtPhotoUri.getText().toString()))
                        .build())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.change_user_info_sucess)
                                        .setMessage(R.string.confirm_to_change)
                                        .create()
                                        .show();
                                MajorActivity a = ((MajorActivity) getActivity());
                                a.init_mAuthAndGetUserInfo();
                                a.initHeader();
                            }

                        })
        );


        return view;
    }

    @Override
    public void onResume() {

        //((MajorActivity) getActivity()).navigationView.setCheckedItem(R.id.nav_personal_info);
        //Log.v(Tool.TAG, "Fragment onResume");
        super.onResume();
    }
}
