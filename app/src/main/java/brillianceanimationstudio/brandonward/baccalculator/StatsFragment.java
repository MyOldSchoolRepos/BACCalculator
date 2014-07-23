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
 *
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
            fragment.setArguments(args);
        }
        return fragment;
    }
    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER_STATE=mUserInfo.getPrefsKey();
        mUserInfo = (userInfo) getArguments().getSerializable(USER_PARAM);
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
            weightAmt.setText(Double.toString(mUserInfo.getWeight()));
            if (mUserInfo.isWeightType()) {
                kiloBtn.setChecked(true);
            } else {
                poundsBtn.setChecked(true);
            }
            if (mUserInfo.isGenderType()) {
                femaleBtn.setChecked(true);
            } else {
                maleBtn.setChecked(true);
            }
        weightAmt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView weightAmt, int actionId,
                                          KeyEvent event) {
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
                // TODO: Make Save Button DO something.
                mUserInfo.setWeight(Double.parseDouble(weightAmt.getText().toString()));
                if (kiloBtn.isChecked()){
                    mUserInfo.setWeightType(true);
                }
                else{
                    mUserInfo.setWeightType(false);
                }
                if (maleBtn.isChecked()){
                    mUserInfo.setGenderType(false);
                }
                else{
                    mUserInfo.setGenderType(true);
                }
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String user_state = mUserInfo.toPrefsString(mUserInfo);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_STATE, user_state).apply();
                userInfoSIOImpl impl = new userInfoSIOImpl();
                impl.updateUserInfo(mUserInfo);
                Toast.makeText(getActivity().getApplicationContext(), "Stats Saved!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
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
        public void onFragmentInteraction(Uri uri);
    }
}
