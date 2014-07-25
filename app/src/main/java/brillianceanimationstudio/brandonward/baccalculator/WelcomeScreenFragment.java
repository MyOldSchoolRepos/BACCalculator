package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import brillianceanimationstudio.brandonward.baccalculator.domain.userInfo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class WelcomeScreenFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_PARAM = "user";
    private String USER_STATE;

    // TODO: Rename and change types of parameters
    private String userInfoParam;

    private OnFragmentInteractionListener mListener;

    private Button mButton;
    private userInfo wUserInfo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param userInfo receives the latest and greatest instance of userInfo.
     * @return A new instance of fragment WelcomeScreen.
     */
    public static WelcomeScreenFragment newInstance(userInfo userInfo) {
        WelcomeScreenFragment fragment = new WelcomeScreenFragment();
        Bundle args = new Bundle();
        if (!(userInfo==null)){
            args.putSerializable(USER_PARAM, userInfo);
        }
        fragment.setArguments(args);
        return fragment;
    }
    public WelcomeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString(){
        return "WelcomeScreenFragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER_STATE= getUserInfo().getPrefsKey();
        if (getArguments()!= null) {
            wUserInfo = (userInfo) getArguments().getSerializable(USER_PARAM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //  Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        mButton = (Button) view.findViewById(R.id.quickCalculate);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (wUserInfo != null) {
                    if (wUserInfo.getWeight() != 0) {
                        fragmentTransaction.replace(R.id.container, BldAlcCntntCalculation.newInstance(wUserInfo))
                                .commit();
                    } else {
                        fragmentTransaction.replace(R.id.container, StatsFragment.newInstance(wUserInfo))
                                .commit();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "ERROR: Unable to find user stats.", Toast.LENGTH_SHORT).show();

                    fragmentTransaction.replace(R.id.container, StatsFragment.newInstance(wUserInfo))
                            .commit();
                }
//                Toast.makeText(getActivity().getApplicationContext(),"Main Button Works!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)  {
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
    public userInfo getUserInfo() {
        return wUserInfo;
    }


    }
