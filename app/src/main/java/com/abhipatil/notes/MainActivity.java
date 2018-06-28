package com.abhipatil.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
static ArrayList<String> notes = new ArrayList<String>(  );
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu( menu );
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected( item );

        if(item.getItemId() == R.id.addnote){
            Intent intent = new Intent(MainActivity.this,NoteEditorActivity.class);
            startActivity( intent );
            return true;
        }
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ListView listView = (ListView)findViewById( R.id.listview );

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences( "com.abhipatil.notes",MODE_PRIVATE );

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet( "notes",null );

        if(set == null){
            notes.add("example note");
        }
        else{
            notes = new ArrayList( set );
        }




      arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes );
        listView.setAdapter( arrayAdapter );

       listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {// " i " here tells us the row number
                Intent intent = new Intent( MainActivity.this,NoteEditorActivity.class );
                intent.putExtra( "noteId",i );
                startActivity( intent );

           }
       } );

       listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int ItemToDelete = i;
               new AlertDialog.Builder(MainActivity.this)
                       .setIcon( android.R.drawable.ic_dialog_alert )
                       .setTitle("are you sure")
                       .setMessage( "do you want to delete this note" )
                       .setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               notes.remove(ItemToDelete);
                               arrayAdapter.notifyDataSetChanged();

                               //to save permanently the other notes i.e, set is updated when a note is deleted
                               SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences( "com.abhipatil.notes",MODE_PRIVATE );
                               HashSet<String> set = new HashSet<String>( MainActivity.notes );

                               sharedPreferences.edit().putStringSet( "notes",  set  ).apply();
                           }
                       } ).setNegativeButton( "no",null )
                       .show();

               return true;
           }
       } );


    }
}
