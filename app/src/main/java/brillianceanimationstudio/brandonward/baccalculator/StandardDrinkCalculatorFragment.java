package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import brillianceanimationstudio.brandonward.baccalculator.domain.userInfo;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StandardDrinkCalculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StandardDrinkCalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class StandardDrinkCalculatorFragment extends Fragment {

    private static final String ARG_USER_INFO = "SDUserInfo";

    private userInfo sdUserInfo;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userInfo the instance of the user passed to this calculator
     * @return A new instance of fragment StandardDrinkCalculatorFragment.
     */
    public static StandardDrinkCalculatorFragment newInstance(userInfo userInfo) {
        StandardDrinkCalculatorFragment fragment = new StandardDrinkCalculatorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_INFO, userInfo);
        fragment.setArguments(args);
        return fragment;
    }
    public StandardDrinkCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sdUserInfo =(userInfo) getArguments().getSerializable(ARG_USER_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standard_drink_calculator, container, false);
        Button instructions = (Button) view.findViewById(R.id.infoBtn);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showHelp(); TODO: Re-hook UI once DialogueFragment is working.
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(userInfo userInfo) {
        if (mListener != null) {
            mListener.onCalculateStandardDrink(userInfo);
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

    /*public void showHelp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
        builder.setMessage("Look at this dialog!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: Crashes app...  I should change this to a dialogue fragment, it is bookmarked.
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

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
        public void onCalculateStandardDrink(userInfo userInfo);
    }

}
