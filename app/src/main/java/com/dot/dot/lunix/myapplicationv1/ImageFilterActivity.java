package com.dot.dot.lunix.myapplicationv1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.dot.dot.lunix.myapplicationv1.filters.BlackWhiteFilter;
import com.dot.dot.lunix.myapplicationv1.filters.BlueFilter;
import com.dot.dot.lunix.myapplicationv1.filters.BlurFilter;
import com.dot.dot.lunix.myapplicationv1.filters.BrightContrastFilter;
import com.dot.dot.lunix.myapplicationv1.filters.CustomFilter;
import com.dot.dot.lunix.myapplicationv1.filters.GausisianBlurFilter;
import com.dot.dot.lunix.myapplicationv1.filters.GrayScaleFilter;
import com.dot.dot.lunix.myapplicationv1.filters.GreenFilter;
import com.dot.dot.lunix.myapplicationv1.filters.HDRFilter;
import com.dot.dot.lunix.myapplicationv1.filters.LightFilter;
import com.dot.dot.lunix.myapplicationv1.filters.LightRadiusFilter;
import com.dot.dot.lunix.myapplicationv1.filters.LomoFilter;
import com.dot.dot.lunix.myapplicationv1.filters.MotionBlurFilter;
import com.dot.dot.lunix.myapplicationv1.filters.NegativeFilter;
import com.dot.dot.lunix.myapplicationv1.filters.NeonFilter;
import com.dot.dot.lunix.myapplicationv1.filters.OilFilter;
import com.dot.dot.lunix.myapplicationv1.filters.OldFilter;
import com.dot.dot.lunix.myapplicationv1.filters.RedFilter;
import com.dot.dot.lunix.myapplicationv1.filters.SharpenFilter;
import com.dot.dot.lunix.myapplicationv1.filters.VignetteFilter;
import com.dot.dot.lunix.myapplicationv1.filters.VignetteFilterBlue;
import com.dot.dot.lunix.myapplicationv1.fragments.FilterGroupFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageFilterActivity extends AppCompatActivity implements FilterGroupFragment.OnFragmentInteractionListener{
    private String pPath;
    private int currentTag;
    private int currentSeek=50;
    private Bitmap gImageBitmap;
    private Bitmap simpleBitmap;
    private ArrayList<ArrayList<CustomFilter>> filterGroup;
    private int currentGroup;
    private HorizontalScrollView horizontalScrollView;
    private BitmapTaker bitmapTaker;
    private boolean inFilter=false;
    private boolean inGroup=false;
    private int howFolder;
    private Dialog dialog;
    private AtomicBoolean busy=new AtomicBoolean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);
        pPath=getIntent().getStringExtra("pPath");
        getSupportActionBar().hide();
        horizontalScrollView=(HorizontalScrollView)findViewById(R.id.a_i_f_scroll);
        int inSampleSize=1;
        inSampleSize=getExif();
        Log.d("LogD2",inSampleSize+"");
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;
        gImageBitmap= BitmapFactory.decodeFile(pPath,options);
        simpleBitmap=Bitmap.createScaledBitmap(gImageBitmap,100,100,true);
        bitmapTaker=new BitmapTaker();
        bitmapTaker.addBitmap(gImageBitmap.copy(gImageBitmap.getConfig(), true));
        ((ImageView)findViewById(R.id.a_i_f_great_view)).setImageBitmap(gImageBitmap);
        ((ImageView)findViewById(R.id.a_i_f_smallview)).setBackground(BitmapDrawable.createFromPath(pPath));
        filterGroup=new ArrayList<ArrayList<CustomFilter>>();
        for(int i=0;i<getResources().getInteger(R.integer.groupCount);i++){
            filterGroup.add(new ArrayList<CustomFilter>());
        }
        SeekBar seekBar=(SeekBar)findViewById(R.id.a_i_f_seekbar);
        seekBar.setOnSeekBarChangeListener(new CustomSeekListener());
        addCustomFilter(0, new BlackWhiteFilter());
        addCustomFilter(0,new NegativeFilter());
        addCustomFilter(0,new GrayScaleFilter());
        addCustomFilter(0,new LightFilter());
        addCustomFilter(1,new RedFilter());
        addCustomFilter(1,new GreenFilter());
        addCustomFilter(1,new BlueFilter());
        addCustomFilter(2,new OldFilter());
        addCustomFilter(2,new BlurFilter());
        addCustomFilter(2,new BrightContrastFilter());
        addCustomFilter(2,new SharpenFilter());
        addCustomFilter(2,new VignetteFilter());
        addCustomFilter(2,new VignetteFilterBlue());
        addCustomFilter(2,new HDRFilter());
        addCustomFilter(2,new LightRadiusFilter());
        addCustomFilter(2,new LomoFilter());
        addCustomFilter(2,new MotionBlurFilter());
        addCustomFilter(2,new NeonFilter());
        addCustomFilter(2,new OilFilter());
        busy.set(false);
    }

    private int getExif() {
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
        return ((a*b)/1000000+1);
    }

    private void addCustomFilter(int group, CustomFilter filter) {
        filterGroup.get(group).add(filter);
    }

    public void onGroupSelected(View view){
        String tag=(String)view.getTag();
        inGroup=true;
        Log.d("LogD", tag + "");
        currentGroup=Integer.parseInt(tag);
        Fragment fragment=FilterGroupFragment.newInstance();
        horizontalScrollView.setVisibility(HorizontalScrollView.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.a_i_f_group,fragment).addToBackStack(null).commit();
    }

    public void gImageClicked(View view){
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }
        else{
            getSupportActionBar().show();
        }
    }

    public void filterClicked(View view){
        inFilter=true;
        currentSeek=50;
        ((LinearLayout)findViewById(R.id.a_i_f_functional)).setVisibility(LinearLayout.VISIBLE);
        ((ImageView)findViewById(R.id.a_i_f_great_view)).setImageBitmap(bitmapTaker.getBitmap());
        ((SeekBar)findViewById(R.id.a_i_f_seekbar)).setProgress(50);
        int tag=Integer.parseInt((String) view.getTag());
        Log.d("LogD", tag + "");
        currentTag=tag;
        if(!busy.get()){
            busy.set(true);
            dialog=getWaitDialog();
            dialog.show();
            new ProccessAsynkTask(currentGroup,currentTag,currentSeek).execute();
        }
    }

    private Dialog getWaitDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ImageFilterActivity.this);
        alertDialog.setView(getLayoutInflater().inflate(R.layout.save_dialog, null, false));
        alertDialog.setCancelable(false);
        return alertDialog.create();
    }

    public void toFilterGroup(View view){
        inFilter=false;
        onBackPressed();
    }

    public void onApply(View view){
        if(!gImageBitmap.equals(bitmapTaker.getBitmap())){
            bitmapTaker.addBitmap(gImageBitmap);
            bitmapTaker.addIndex(currentGroup, currentTag, currentSeek);
            Toast.makeText(ImageFilterActivity.this, "Added", Toast.LENGTH_SHORT).show();
            Log.d("LogD3", currentSeek + "");
            refreshFragment();
            onBackPressed();
        }
        else{
            Toast.makeText(ImageFilterActivity.this, "No Added", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshFragment() {
        simpleBitmap=Bitmap.createScaledBitmap(bitmapTaker.getBitmap(),100,100,true);
        getSupportFragmentManager().popBackStack();
        Fragment fragment=FilterGroupFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.a_i_f_group,fragment).addToBackStack(null).commit();
    }

    public void onDiscard(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ((LinearLayout)findViewById(R.id.a_i_f_functional)).setVisibility(LinearLayout.INVISIBLE);
        currentSeek=50;
        ((ImageView)findViewById(R.id.a_i_f_great_view)).setImageBitmap(bitmapTaker.getBitmap());
        if(inFilter){
            inFilter=false;
        }
        else{
            inGroup=false;
            horizontalScrollView.setVisibility(FrameLayout.VISIBLE);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_previous){
            if(bitmapTaker.getCurrentIndex()>1){
                bitmapTaker.onUndo();
                ((ImageView)findViewById(R.id.a_i_f_great_view)).setImageBitmap(bitmapTaker.getBitmap());
                simpleBitmap=Bitmap.createScaledBitmap(bitmapTaker.getBitmap(),100,100,true);
                if(inGroup){
                    if(inFilter){
                        onBackPressed();
                    }
                    refreshFragment();
                }
            }
        }
        if(item.getItemId()==R.id.menu_save){
            saveImage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        dialog=getWaitDialog();
        dialog.show();
        new CustomAsynkTask().execute();
    }

    @Override
    public void onFragmentInteraction(View view) {
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.f_f_g_filter_layout);
        for(int i=0;i<filterGroup.get(currentGroup).size();i++){
            ImageView imageView=(ImageView)getLayoutInflater().inflate(R.layout.filter_view, linearLayout, false);
            imageView.setImageBitmap(filterGroup.get(currentGroup).get(i).onProgress(simpleBitmap.copy(simpleBitmap.getConfig(),true),50));
            imageView.setTag(i+"");
            linearLayout.addView(imageView);
        }
    }

    class CustomSeekListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress=seekBar.getProgress();
            currentSeek=progress;
            if(!busy.get()){
                busy.set(true);
                dialog=getWaitDialog();
                dialog.show();
                new ProccessAsynkTask(currentGroup,currentTag,currentSeek).execute();
            }
        }
    }

    class BitmapTaker{

        private int currentIndex=0;
        private ArrayList<Bitmap> arrayList=new ArrayList<Bitmap>();
        public ArrayList<Integer[]> indexList=new ArrayList<Integer[]>();
        public BitmapTaker(){
            indexList.add(currentIndex,new Integer[]{0,0,0});
        }

        public void addBitmap(Bitmap bitmap){
            arrayList.add(currentIndex,bitmap);
            currentIndex=currentIndex+1;
        }

        public Bitmap getBitmap(){
            return arrayList.get(currentIndex-1);
        }

        public void onUndo(){
            currentIndex=currentIndex-1;
        }

        public int getCurrentIndex(){
            return currentIndex;
        }

        public void addIndex(int a,int b,int c){
            indexList.add(currentIndex-1,new Integer[]{a,b,c});
        }
    }

    class CustomAsynkTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            gImageBitmap=BitmapFactory.decodeFile(pPath);
            Bitmap.Config config=gImageBitmap.getConfig();
            for(int i=0;i<bitmapTaker.getCurrentIndex()-1;i++){
                Integer filter[]=bitmapTaker.indexList.get(i+1);
                gImageBitmap=filterGroup.get(filter[0]).get(filter[1]).onProgress(gImageBitmap.copy(config,true),filter[2]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.cancel();
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(ImageFilterActivity.this);
            alertDialog.setCancelable(false);
            final String list_f[]=getExternalFilesDir(null).list();
            howFolder=0;
            alertDialog.setSingleChoiceItems(list_f, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    howFolder = which;
                }
            });
            alertDialog.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File file=new File(getExternalFilesDir(null).getAbsolutePath() + "//" + list_f[howFolder] + "//"+new Date().getTime()+".jpg");
                    try{
                        OutputStream outputStream=null;
                        file.createNewFile();
                        outputStream=new FileOutputStream(file);
                        gImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        finish();
                    }
                    catch(Exception e){

                    }
                }
            });
            alertDialog.create().show();
            super.onPostExecute(o);
        }
    }

    class ProccessAsynkTask extends AsyncTask{

        private int lgroup;
        private int ltag;
        private int lseek;

        public ProccessAsynkTask(int lgroup,int ltag,int lseek){
            this.lgroup=lgroup;
            this.ltag=ltag;
            this.lseek=lseek;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            CustomFilter customFilter=filterGroup.get(lgroup).get(ltag);
            gImageBitmap=customFilter.onProgress(bitmapTaker.getBitmap().copy(bitmapTaker.getBitmap().getConfig(), true), lseek);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ((ImageView)findViewById(R.id.a_i_f_great_view)).setImageBitmap(gImageBitmap);
            dialog.cancel();
            busy.set(false);
        }
    }

}
