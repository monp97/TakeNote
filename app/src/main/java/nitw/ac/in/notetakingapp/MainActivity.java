package nitw.ac.in.notetakingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayoutItem;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;

import java.util.ArrayList;

import static nitw.ac.in.notetakingapp.R.id.parent;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    public void addNote(View view)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater=this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.add, null);

        final EditText editText=view1.findViewById(R.id.edit);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText.setText("");
            }
        });

        dialog.setView(view1).setTitle("Add notes").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText=view1.findViewById(R.id.edit);
                String not=editText.getText().toString();

                 arrayList.add(editText.getText().toString());

                database.execSQL("INSERT INTO users (note) VALUES ('"+not+"') ");
                arrayAdapter.notifyDataSetChanged();

            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=this.openOrCreateDatabase("Users",MODE_PRIVATE,null);

        database.execSQL("CREATE TABLE IF NOT EXISTS users (note VARCHAR)");

        Cursor cursor=database.rawQuery("SELECT * FROM users",null);

        int notepos=cursor.getColumnIndex("note");

        if(cursor.moveToFirst())
        {
            do{

                String note=cursor.getString(notepos);
                arrayList.add(note);

            }
            while(cursor.moveToNext());
        }

        arrayAdapter=new ArrayAdapter<String>(this, R.layout.view_row,R.id.header_text,arrayList);
            final ExpandableLayoutListView listView = (ExpandableLayoutListView) findViewById(R.id.list_item);
            listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                ExpandableLayoutItem mEplItem = (ExpandableLayoutItem) view.findViewById(R.id.row);
                RelativeLayout content = mEplItem.getContentRelativeLayout();
                RelativeLayout header=mEplItem.getHeaderRelativeLayout();

                Button editButton=content.findViewById(R.id.edit);
                Button deleteButton=content.findViewById(R.id.cancel);
                final ImageView imageView=header.findViewById(R.id.star);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imageView.setImageResource(R.drawable.if_star_1930255);

                        String str=arrayList.get(position).toString();
                        int val=1;



                    }
                });
                String editText=arrayList.get(position).toString();

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str=arrayList.get(position).toString();
                        edit(str,position);
                        Log.i("Edit button clicked","edit me!");


                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String text=arrayList.get(position).toString();

                        database.execSQL("DELETE FROM users WHERE note='"+text+"'  ");

                        arrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                        Log.i("Delete button clicked","delete me!");
                    }
                });

            }
        });
    }



    public void edit(final String str, final int pos)
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater=this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.add, null);
        final EditText editText=view1.findViewById(R.id.edit);
        editText.setText(str);

        dialog.setView(view1).setTitle("Edit").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String not=editText.getText().toString();
                database.execSQL("UPDATE users SET note='"+not+"' WHERE note='"+str+"' ");
                arrayList.set(pos,not);
                arrayAdapter.notifyDataSetChanged();

            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}
