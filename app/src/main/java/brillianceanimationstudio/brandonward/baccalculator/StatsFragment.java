package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;
import brillianceanimationstudio.brandonward.baccalculator.service.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_PARAM = "user";
    private String USER_STATE;

    private Button mButton;
    private EditText weightAmt;
    private RadioButton poundsBtn;
    private RadioButton kiloBtn;
    private RadioGroup weightSelect;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    private RadioGroup genderSelect;

    private double weight;
    private boolean weightType; // true for kilograms and false for pounds
    private boolean genderType;// true for male and false for female.

    private userInfo mUserInfo;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userInfo parameter to pass along userInfo.
     * @return A new instance of fragment StatsFragment.
     */
    public static StatsFragment newInstance(userInfo userInfo) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        if (!(userInfo == null)) {
            args.putSerializable(USER_PARAM, userInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return "StatsFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER_STATE = getUserInfo().getPrefsKey();
        if (getArguments() != null) {
            mUserInfo = (userInfo) getArguments().getSerializable(USER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        mButton = (Button) view.findViewById(R.id.SaveStatsButton);
        weightAmt = (EditText) view.findViewById(R.id.weightAmt);
        kiloBtn = (RadioButton) view.findViewById(R.id.kilogramsButton);
        poundsBtn = (RadioButton) view.findViewById(R.id.poundsButton);
        weightSelect = (RadioGroup) view.findViewById(R.id.weightMetric);
        maleBtn = (RadioButton) view.findViewById(R.id.maleButton);
        femaleBtn = (RadioButton) view.findViewById(R.id.femaleButton);
        genderSelect = (RadioGroup) view.findViewById(R.id.genderMetric);
        //Set all base attributes of fragment
        weightAmt.setText(Double.toString(getUserInfo().getWeight()));
        if (getUserInfo().isWeightType()) {
            kiloBtn.setChecked(true);
        } else {
            poundsBtn.setChecked(true);
        }
        if (getUserInfo().isGenderType()) {
            maleBtn.setChecked(true);
        } else {
            femaleBtn.setChecked(true);
        }
        weightAmt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override//Makes the 'Done' button also save stats.
            public boolean onEditorAction(TextView weightAmt, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mButton.performClick();
                    return true;
                }
                return false;
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo().setWeight(Double.parseDouble(weightAmt.getText().toString()));
                if (kiloBtn.isChecked()) {
                    getUserInfo().setWeightType(true);
                } else {
                    getUserInfo().setWeightType(false);
                }
                if (maleBtn.isChecked()) {
                    getUserInfo().setGenderType(true);
                } else {
                    getUserInfo().setGenderType(false);
                }
                onSavePressed(mUserInfo);
            }
        });
        return view;
    }

    public void onSavePressed(userInfo userInfo) {
        if (mListener != null) {
            mListener.saveStats(userInfo);
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

    public userInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void saveStats(userInfo userInfo);
    }
}
