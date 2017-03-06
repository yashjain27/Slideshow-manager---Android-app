package tech.ceece.slideshowmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;

public class AddPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create instance and setContentView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);

        //Display Grid view
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        //Intent
        final Intent returnIntent = new Intent();

        //Listen for a click in the gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Add the position of he photo that's clicked
                //final Intent returnIntent = new Intent();
                returnIntent.putExtra("id", position);

                //Create a dialog
                final Dialog dialog = new Dialog(AddPhoto.this);
                dialog.setContentView(R.layout.add_input);
                dialog.show();

                //NumberPicker object for adding the new photo's position
                final NumberPicker p1 = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                p1.setMinValue(0);
                p1.setMaxValue(Statics.sizeOfList);
                p1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                //Button for the dialog
                Button button = (Button) dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Get the value of the position pointed by Numberpicker
                        returnIntent.putExtra("position",p1.getValue());

                        //Dismiss the dialog
                        dialog.dismiss();

                        //Set the Activity Result and finish
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });

            }
        });
        //End of gridview listener
    }




}
