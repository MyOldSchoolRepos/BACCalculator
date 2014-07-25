package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BldAlcCntntCalculation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BldAlcCntntCalculation#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BldAlcCntntCalculation extends Fragment {
    private static final String USER_PARAM = "user";
    private String USER_STATE;

    private userInfo bUserInfo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userInfo receives the current userInfo.
     * @return A new instance of fragment BldAlcCntntCalculation.
     */
    // TODO: Rename and change types and number of parameters
    public static BldAlcCntntCalculation newInstance(userInfo userInfo) {
        BldAlcCntntCalculation fragment = new BldAlcCntntCalculation();
        Bundle args = new Bundle();
        if (userInfo != null){
            args.putSerializable(USER_PARAM, userInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }
    public BldAlcCntntCalculation() {
        // Required empty public constructor
    }
    @Override
    public String toString(){
        return "BldAlcCntntCalculationFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER_STATE= getUserInfo().getPrefsKey();
        if (getArguments() != null) {
            bUserInfo = (userInfo) getArguments().getSerializable(USER_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bld_alc_cntnt_calculation, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
