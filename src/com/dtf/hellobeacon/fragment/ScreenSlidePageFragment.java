package com.dtf.hellobeacon.fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


public class ScreenSlidePageFragment extends Fragment{

	//argument key for the page number of the fragment
	public static final String ARG_PAGE = "page";
	
	//the fragment's page number
	private int mPageNumber;
	
	
	public ScreenSlidePageFragment() {
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set the current page number
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
	
	public static ScreenSlidePageFragment create(int pageNumber) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		
		//pass arguments to the fragment
		Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
		
        return fragment;
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//inflate the layout containing the content of the fragment
		//get the resource ID of the corresponding layout for the page
		
		String layoutID = "fragment_page" + (mPageNumber + 1);
		Log.i("EEE", layoutID);
		int resID = getResources().getIdentifier(layoutID, "layout", "com.example.hellobeacon");
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(resID, container, false);
		
		return rootView;
	}
	
	
	public int getPageNumber(){
		return mPageNumber;
	}
	
}
