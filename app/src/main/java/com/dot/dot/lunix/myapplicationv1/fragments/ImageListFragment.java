package com.dot.dot.lunix.myapplicationv1.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dot.dot.lunix.myapplicationv1.R;

import java.io.File;
import java.util.Arrays;

public class ImageListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public ImageListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ImageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageListFragment newInstance(String param1) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            View view=getView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_image_list, container, false);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.f_i_l_scroll_layout);
        setImage(linearLayout, inflater, container);
        return view;
    }

    private void setImage(LinearLayout view, LayoutInflater inflater, ViewGroup container){
        try{
            String file_n=getContext().getExternalFilesDir(null).getAbsolutePath()+"//"+mParam1;
            File list_f[]=new File(file_n).listFiles();
            CFile cFile[]=new CFile[list_f.length];
            for(int i=0;i<cFile.length;i++){
                cFile[i]=new CFile(list_f[i],list_f[i].lastModified());
            }
            Arrays.sort(cFile);
            for(int i=0;i<cFile.length;i++){
                LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.fragment_image_view,view,false);
                BitmapFactory.Options options=new BitmapFactory.Options();
                int inSimpleSize=1;
                inSimpleSize=getExif(file_n+"//"+cFile[i].file.getName());
                options.inSampleSize=inSimpleSize;
                Bitmap bitmap= BitmapFactory.decodeFile(file_n+"//"+cFile[i].file.getName(),options);
                ((ImageView) linearLayout.findViewById(R.id.f_i_v_image)).setImageBitmap(bitmap);
                ((TextView)linearLayout.findViewById(R.id.f_i_v_text)).setText(cFile[i].file.getName());
                ((ImageView)linearLayout.findViewById(R.id.f_i_v_button_delete)).setTag(R.id.button_key, file_n + "//" + cFile[i].file.getName());
                ((ImageView)linearLayout.findViewById(R.id.f_i_v_button_delete)).setTag(R.id.folder_key,mParam1);
                linearLayout.findViewById(R.id.f_i_v_image).setTag(R.id.button_key,file_n+"//"+cFile[i].file.getName());
                view.addView(linearLayout);
            }
        }
        catch(OutOfMemoryError e){

        }
    }

    class CFile implements Comparable{
        public File file;
        public long aLong;
        public CFile(File file,long aLong){
            this.file=file;
            this.aLong=aLong;
        }

        @Override
        public int compareTo(Object another) {
            CFile cFile=(CFile)another;
            if(this.aLong>=cFile.aLong){
                return -1;
            }
            return 1;
        }
    }

    private int getExif(String pPath) {
        int a=0;
        int b=0;
        try{
            ExifInterface exif = new ExifInterface(pPath);
            a=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
            b=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
        }
        catch(Exception e){

        }
        Log.d("LogD", a + " " + b);
        return ((a*b)/600000+1);
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
