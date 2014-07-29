package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_NOTIFICATIONS = "notifications";
    private static final String ARG_PARAM_ONGOING = "ongoing";


    private boolean showNotifications;
    private boolean ongoingNotifications;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param showNotifications boolean to determine if notifications are shown.
     * @return A new instance of fragment BlankFragment.
     */
    public static SettingsFragment newInstance(boolean showNotifications, boolean ongoingNotifications) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_NOTIFICATIONS, showNotifications);
        args.putBoolean(ARG_PARAM_ONGOING, ongoingNotifications);
        fragment.setArguments(args);
        return fragment;
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showNotifications = getArguments().getBoolean(ARG_PARAM_NOTIFICATIONS);
            ongoingNotifications = getArguments().getBoolean(ARG_PARAM_ONGOING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final RadioButton enableNotify = (RadioButton) view.findViewById(R.id.notifyEnable);
        final RadioButton disableNotify = (RadioButton) view.findViewById(R.id.notifyDisable);
        final RadioButton enableOngoing = (RadioButton) view.findViewById(R.id.ongoingEnable);
        final RadioButton disableOngoing = (RadioButton) view.findViewById(R.id.ongoingDisable);
        final RadioGroup ongoingGroup = (RadioGroup) view.findViewById(R.id.ongoingSetting);
        if (showNotifications){
            enableNotify.setChecked(true);
        }
        else{
            disableNotify.setChecked(true);
            enableOngoing.setEnabled(false);
            disableOngoing.setEnabled(false);
        }
        enableNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationsSettingsChange(enableNotify, disableNotify, ongoingGroup);
            }
        });
        disableNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationsSettingsChange(enableNotify, disableNotify, ongoingGroup);
            }
        });
        return view;
    }

    public void onNotificationsSettingsChange(RadioButton enable, RadioButton disable, RadioGroup ongoing){
        if (disable.isChecked()){
            showNotifications = false;
            for (int i= 0; i < ongoing.getChildCount(); i++){
                ongoing.getChildAt(i).setEnabled(false);
            }
        }
        else if (enable.isChecked()){
            showNotifications = true;
            for (int i= 0; i < ongoing.getChildCount(); i++) {
                ongoing.getChildAt(i).setEnabled(true);
            }
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Show Notifications Default: TRUE", Toast.LENGTH_LONG).show();
            showNotifications = true;
            for (int i= 0; i < ongoing.getChildCount(); i++) {
                ongoing.getChildAt(i).setEnabled(true);
            }
        }
        onNotificationChanged(showNotifications);
    }

    public void onOngoingSettingsChange(){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onNotificationChanged(boolean showNotifications) {
        if (mListener != null) {
            mListener.toShowNotifications(showNotifications);
        }
    }

    public void onOngoingChanged(boolean ongoingNotifications){
        if (mListener != null){
            mListener.toShowOngoing(ongoingNotifications);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void toShowNotifications(boolean showNotifications);
        public void toShowOngoing(boolean showOngoing);
    }

}
