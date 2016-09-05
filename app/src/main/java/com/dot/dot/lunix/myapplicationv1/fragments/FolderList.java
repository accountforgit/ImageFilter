package com.dot.dot.lunix.myapplicationv1.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dot.dot.lunix.myapplicationv1.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FolderList#} factory method to
 * create an instance of this fragment.
 */
public class FolderList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public FolderList() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_folder_list, container, false);
        LinearLayout view1=(LinearLayout)view.findViewById(R.id.f_f_l_layout);
        String state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            setFolderList(view1,inflater,container);
        }
        return view;
    }

    private void setFolderList(LinearLayout view,LayoutInflater inflater,ViewGroup container) {
        File file=new File(getContext().getExternalFilesDir(null),"Noready");
        File file1=new File(getContext().getExternalFilesDir(null),"Ready");
        file.mkdirs();
        file1.mkdirs();
        String list_f[]=getContext().getExternalFilesDir(null).list();
        LinearLayout layout_h=(LinearLayout)inflater.inflate(R.layout.folder_view_layout, view, false);
        for(int i=0;i<list_f.length;i++){
            Log.d("LogD", list_f[i]);
            LinearLayout view_f=(LinearLayout)inflater.inflate(R.layout.folder_view, layout_h, false);
            ((ImageView)view_f.findViewById(R.id.f_v_button_delete)).setTag(R.id.button_key,list_f[i]);
            ((TextView)view_f.findViewById(R.id.f_v_text_name)).setText(list_f[i]);
            ((ImageView)view_f.findViewById(R.id.f_v_imageview_folder)).setTag(R.id.button_key, list_f[i]);
            layout_h.addView(view_f);
            if(i%2==1){
                view.addView(layout_h);
                layout_h=(LinearLayout)inflater.inflate(R.layout.folder_view_layout, view, false);
            }
        }
        if(list_f.length%2==1){
            view.addView(layout_h);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
