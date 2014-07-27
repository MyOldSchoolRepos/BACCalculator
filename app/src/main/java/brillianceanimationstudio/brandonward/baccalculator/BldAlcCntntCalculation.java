package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;
import brillianceanimationstudio.brandonward.baccalculator.engine.calculateBAC;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BldAlcCntntCalculation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BldAlcCntntCalculation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BldAlcCntntCalculation extends Fragment {
    private static final String USER_PARAM = "user";
    private String USER_STATE;

    private userInfo bUserInfo;
    private double drinksCnt;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userInfo receives the current userInfo.
     * @return A new instance of fragment BldAlcCntntCalculation.
     */
    public static BldAlcCntntCalculation newInstance(userInfo userInfo) {
        BldAlcCntntCalculation fragment = new BldAlcCntntCalculation();
        Bundle args = new Bundle();
        if (userInfo != null) {
            args.putSerializable(USER_PARAM, userInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public BldAlcCntntCalculation() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return "BldAlcCntntCalculationFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER_STATE = getUserInfo().getPrefsKey();
        if (getArguments() != null) {
            bUserInfo = (userInfo) getArguments().getSerializable(USER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bld_alc_cntnt_calculation, container, false);
        drinksCnt = bUserInfo.getDrinks();
        TimePicker firstDrink = (TimePicker) view.findViewById(R.id.timePicker);
        EditText drinkCount = (EditText) view.findViewById(R.id.drinkCount);
        Button plusDrink = (Button) view.findViewById(R.id.plusOneDrink);
        Button minusDrink = (Button) view.findViewById(R.id.minusOneDrink);
        final Button goTime = (Button) view.findViewById(R.id.goTimeButton);
        goTime.setVisibility(View.GONE);
        drinkCount.setText(Double.toString(getUserInfo().getDrinks()));
        firstDrink.setCurrentHour(bUserInfo.gettHour());
        firstDrink.setCurrentMinute(bUserInfo.gettMinute());
        plusDrink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drinksCnt = drinksCnt + 1.0;
                bUserInfo.setDrinks(drinksCnt);
                changeDrinkAmtPressed(bUserInfo);
            }
        });
        minusDrink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drinksCnt = drinksCnt - 1.0;
                bUserInfo.setDrinks(drinksCnt);
                changeDrinkAmtPressed(bUserInfo);
            }
        });
        firstDrink.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                bUserInfo.settHour(hourOfDay);
                bUserInfo.settMinute(minute);
                goTime.setVisibility(View.VISIBLE);
            }
        });
        drinkCount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override//Makes the 'Done' button also input drinks.
            public boolean onEditorAction(TextView drinkCount, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager imm;
                        imm = (InputMethodManager) getActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(drinkCount.getApplicationWindowToken(), 0);
                    bUserInfo.setDrinks(Double.parseDouble(drinkCount.getText().toString()));
                    changeDrinkAmtPressed(bUserInfo);
                    return true;
                }
                return false;
            }
        });
        goTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeChanged(bUserInfo);
            }
        });
        TextView BACCalculation = (TextView) view.findViewById(R.id.userBACValue);
        double bacVal = new calculateBAC(bUserInfo).calculateBloodAlcoholContent();
        if (bacVal < 0){
            bacVal = 0.0;
        }
        BACCalculation.setText(String.format("%.6f",bacVal));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void changeDrinkAmtPressed(userInfo userInfo){
        if (mListener != null){
            mListener.changeDrinkCntByBtn(userInfo);
        }
    }

    public void timeChanged(userInfo userInfo){
        if (mListener != null){
            mListener.timeWasChanged(userInfo);
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
        return bUserInfo;
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
        // TODO: Update argument type and name
        public void changeDrinkCntByBtn(userInfo userInfo);
        public void timeWasChanged(userInfo userInfo);
    }

}
