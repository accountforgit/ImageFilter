package com.dot.dot.lunix.myapplicationv1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dot.dot.lunix.myapplicationv1.fragments.FolderList;
import com.dot.dot.lunix.myapplicationv1.fragments.ImageListFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment lFragment;
    private int howFolder=0;
    private Dialog dialog;
    private NavigationView navigationView;
    private boolean inFolder=false;
    private String lastTag="Noready";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageTakerDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView=(NavigationView) findViewById(R.id.nav_view);
        addFolderMenu();
        navigationView.setNavigationItemSelectedListener(this);
        onFolderFragment();
    }

    private void addFolderMenu() {
        Menu menu=navigationView.getMenu();
        File file=getExternalFilesDir(null);
        String list_f[]=file.list();
        if(list_f!=null){
            for (int i=0;i<list_f.length;i++){
                MenuItem menuItem=menu.add(R.id.menu1, i, i, list_f[i]);
                menuItem.setIcon(R.drawable.folder);
            }
        }
    }
    private void showImageTakerDialog(){
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        View dialogView=getLayoutInflater().inflate(R.layout.m_a_image_taker_dialog, null, false);
        dialogView.findViewById(R.id.m_a_image_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showCamera();
            }
        });
        dialogView.findViewById(R.id.m_a_image_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.setNegativeButton(R.string.cancel, null);
        dialog=alertDialog.create();
        dialog.show();
    }

    private void showGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    private void showCamera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                saveBitmap(bitmap);
            }
            catch(Exception e){

            }
        }
        if(requestCode==1 && resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap bitmap=(Bitmap)extras.get("data");
            saveBitmap(bitmap);
        }
        try{
            dialog.cancel();
        }
        catch(Exception e){

        }
        if(resultCode==RESULT_OK){
            if(inFolder){
                getSupportFragmentManager().popBackStack();
                toFolder(lastTag);
            }
        }
    }

    private void saveBitmap(Bitmap bitmap){
        try{
            File file=new File(getExternalFilesDir(null).getAbsolutePath()+"//"+lastTag);
            File name_f=new File(file.getAbsolutePath()+"//"+new Date().getTime()+".jpg");
            OutputStream outputStream=null;
            name_f.createNewFile();
            outputStream=new FileOutputStream(name_f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e){
            Log.d("LogD1",e.getMessage());
        }
    }

    private void onFolderFragment(){
        lastTag="Noready";
        inFolder=false;
        Fragment fragment=new FolderList();
        getSupportFragmentManager().beginTransaction().replace(R.id.c_m_layout,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            inFolder=false;
            lastTag="Noready";
            setTitle(R.string.app_name);
            super.onBackPressed();
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
                finish();
            }
        }
    }

    public void onClickDelete(View view){
        final String string=(String)view.getTag(R.id.button_key);
        Log.d("LogD", string);
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("Вы уверены что хотите удалить папку "+string+" ?");
        alertDialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file=new File(getExternalFilesDir(null).getAbsolutePath(),string);
                if(!string.equals("Noready") && !string.equals("Ready")){
                    if(file.delete()){
                        Toast.makeText(MainActivity.this, "Удалено.", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().popBackStack();
                        onFolderFragment();
                        navigationView.getMenu().removeGroup(R.id.menu1);
                        addFolderMenu();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Не удалось удалить.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Нельзя удалять системные папки.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton(R.string.cancel,null);
        alertDialog.create().show();
    }

    public void onClick(View view){
        String tag=(String)view.getTag(R.id.button_key);
        toFolder(tag);
    }

    private void toFolder(String tag){
        lastTag=tag;
        inFolder=true;
        Fragment fragment= ImageListFragment.newInstance(tag);
        getSupportFragmentManager().beginTransaction().replace(R.id.c_m_layout,fragment).addToBackStack(null).commit();
        setTitle(lastTag);
    }

    public void toFilter(View view){
        String string=(String)view.getTag(R.id.button_key);
        Log.d("LogD",string);
        Intent intent=new Intent(MainActivity.this,ImageFilterActivity.class);
        String pPath="pPath";
        intent.putExtra(pPath, string);
        onBackPressed();
        startActivity(intent);
    }

    public void onImageDelete(View view){
        final String file_n=(String)view.getTag(R.id.button_key);
        Log.d("LogD",file_n);
        final String tag=(String)view.getTag(R.id.folder_key);
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,view);
        popupMenu.getMenu().add(R.string.replace);
        popupMenu.getMenu().add(R.string.share);
        popupMenu.getMenu().add(R.string.delete);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item){
                String string=(String)item.getTitle();
                if(string.equals(getString(R.string.delete))){
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setMessage("Вы точно хотите удалить картинку?");
                    alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file=new File(file_n);
                            file.delete();
                            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStack();
                            toFolder(tag);
                        }
                    });
                    alertDialog.setNegativeButton("Нет",null);
                    alertDialog.create().show();
                }
                if(string.equals(getString(R.string.replace))){
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
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
                            File file = new File(file_n);
                            file.renameTo(new File(getExternalFilesDir(null).getAbsolutePath() + "//" + list_f[howFolder] + "//" + file.getName()));
                            getSupportFragmentManager().popBackStack();
                            toFolder(tag);
                        }
                    });
                    alertDialog.create().show();
                }
                if(string.equals(getString(R.string.share))){
                    Bitmap bitmap=BitmapFactory.decodeFile(file_n);
                    Log.d("LogD",string);
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM,(Uri.fromFile(new File(file_n))));
                    intent.setType("image/*");
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            showCamera();
        } else if (id == R.id.nav_gallery) {
            showGallery();
        } else if (id == R.id.nav_add) {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(R.string.add_folder);
            View view=getLayoutInflater().inflate(R.layout.a_add_folder, null, false);
            final EditText editText=((EditText) view.findViewById(R.id.a_edittext));
            alertDialog.setView(view);
            alertDialog.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name_f=editText.getText().toString();
                    if(!name_f.isEmpty()){
                        File file=new File(getExternalFilesDir(null),name_f);
                        if(file.mkdirs()){
                            navigationView.getMenu().removeGroup(R.id.menu1);
                            Toast.makeText(MainActivity.this, "Создано", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStack();
                            onFolderFragment();
                            addFolderMenu();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Папка существует или название пустое", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            alertDialog.create().show();
        }
        else{
            String string=(String)item.getTitle();
            Log.d("LogD",string);
            toFolder(string);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
