package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        args.putSerializable(USER_PARAM, userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserInfo = (userInfo) getArguments().getSerializable(
                    USER_PARAM);
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

        weightAmt.setText(Double.toString(mUserInfo.getWeight()));
        if (mUserInfo.isWeightType()){
            kiloBtn.setChecked(true);
        }
        else {
            poundsBtn.setChecked(true);
        }
        if (mUserInfo.isGenderType()){
            maleBtn.setChecked(true);
        }
        else{
            femaleBtn.setChecked(true);
        }
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
                    mUserInfo.setGenderType(true);
                }
                else{
                    mUserInfo.setGenderType(false);
                }
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
