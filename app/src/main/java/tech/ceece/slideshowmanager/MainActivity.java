package tech.ceece.slideshowmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {
    //Variables
    int positionOne, positionTwo;
    String photo, choice;

    //SlideShow object
    ArrayList<ImageView> slideshow = new ArrayList<>();

    //UndoRedoStack objects for an Undo Stack and a Redo Stack
    UndoRedoStack undoStack = new UndoRedoStack();
    UndoRedoStack redoStack = new UndoRedoStack();

    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setSlideshow();
    }

    //Add a new Photo ***OPTION***
    public void onAdd(View view){
        Statics.sizeOfList = slideshow.size();
        startActivityForResult(new Intent(this, AddPhoto.class),1);
    }

    //Remove a photo ***OPTION***
    public void onRemove(View view){
        //Check if the slideshow is empty
        if(slideshow.size() == 0){
            Toast.makeText(this,"Empty slideshow. Nothing to remove.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create a dialog otherwise
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_input);
        dialog.show();

        //Create a NumberPicker object
        final NumberPicker p1 = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        p1.setMinValue(0);
        p1.setMaxValue(slideshow.size()-1);
        p1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //Button for the dialog
        Button button = (Button) dialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Receive the position value
                positionOne = p1.getValue();

                //New ActionCommand object of type REMOVE
                ActionCommand remove  = new ActionCommand(ActionType.REMOVE);

                //Set the remove position
                remove.setPositionOne(positionOne);

                //Remove the photo
                remove.perform(slideshow);

                //Remove action to Undo Stack
                undoStack.push(remove);

                //Clear Redo stack
                redoStack.clearStack();

                //Update slideshow
                setSlideshow();

                //Dismiss the dialog
                dialog.dismiss();
            }
        });
    }

    public void onSwap(View view){
        //Check if there is more than one photo
        if(slideshow.size() <= 1){
            Toast.makeText(this,"There's less than one photo. Nothing to swap.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create new dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.doubleinput);
        dialog.show();
        dialog.setTitle("Please select source and destination positions:");

        //Number Picker object
        final NumberPicker p1 = (NumberPicker) dialog.findViewById(R.id.np1);
        final NumberPicker p2 = (NumberPicker) dialog.findViewById(R.id.np2);
        p1.setMinValue(0);
        p1.setMaxValue(slideshow.size()-1);
        p1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        p2.setMinValue(0);
        p2.setMaxValue(slideshow.size()-1);
        p2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //New Button
        Button b1 = (Button) dialog.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set position one
                positionOne = p1.getValue();

                //Set position two
                positionTwo = p2.getValue();

                //New ActionCommand object of type SWAP
                ActionCommand swap = new ActionCommand(ActionType.SWAP);

                //Set the positions to the object
                swap.setPositionOne(positionOne);
                swap.setPositionTwo(positionTwo);

                //Swap the two photos
                swap.perform(slideshow);

                //Swap action to Undo Stack
                undoStack.push(swap);

                //Clear Redo Stack
                redoStack.clearStack();

                //Update slideshow
                setSlideshow();

                //Dismiss the dialog
                dialog.dismiss();
            }
        });

    }

    public void onMove(View view){
        //Check if there is more than one photo
        if(slideshow.size() == 0){
            Toast.makeText(this,"Empty slideshow. Nothing to move.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create new dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.doubleinput);
        dialog.show();
        dialog.setTitle("Please select source and destination positions:");

        //Number Picker object
        final NumberPicker p1 = (NumberPicker) dialog.findViewById(R.id.np1);
        final NumberPicker p2 = (NumberPicker) dialog.findViewById(R.id.np2);
        p1.setMinValue(0);
        p1.setMaxValue(slideshow.size()-1);
        p1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        p2.setMinValue(0);
        p2.setMaxValue(slideshow.size()-1);
        p2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //New Button
        Button b1 = (Button) dialog.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set position one
                positionOne = p1.getValue();

                //Set position two
                positionTwo = p2.getValue();

                //New ActionCommand object of type SWAP
                ActionCommand move = new ActionCommand(ActionType.MOVE);

                //Set the positions to the object
                move.setPositionOne(positionOne);
                move.setPositionTwo(positionTwo);

                //Swap the two photos
                move.perform(slideshow);

                //Swap action to Undo Stack
                undoStack.push(move);

                //Clear Redo Stack
                redoStack.clearStack();

                //Update slideshow
                setSlideshow();

                //Dismiss the dialog
                dialog.dismiss();
            }
        });
    }

    public void redo_btn(View v){

        //Be prepared to catch Empty Stack Exception
        try {
            //Redo the last ActionCommand that was undone.
            ActionCommand redo = redoStack.pop();
            redo.perform(slideshow);

            //Add the redone ActionCommand to UndoStack
            undoStack.push(redo);

            //Update slideshow
            setSlideshow();

        }catch (EmptyStackException ex){
            Toast.makeText(this, "Empty Redo Stack", Toast.LENGTH_SHORT).show();
        }

    }

    public void undo_btn(View v){
        //Be prepared to catch Empty Stack Exception
        try {

            //Get the last ActionCommand on UndoStack
            ActionCommand inverse = undoStack.peek().getInverse();
            inverse.perform(slideshow); //Perform the inverse

            //Pop the stack off the UndoStack and add it to Redo Stack
            redoStack.push(undoStack.pop());

            //Update slideshow
            setSlideshow();
        }catch (EmptyStackException ex){
            Toast.makeText(this, "Empty Undo Stack", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Add Photo
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            //New ActionCommand object of type ADD
            ActionCommand add  = new ActionCommand(ActionType.ADD);

            //Add the photo
            int positionOfGallery = data.getExtras().getInt("id");
            ImageAdapter adapter = new ImageAdapter(this); //Create an adapter object
            ImageView imageView = new ImageView(this); //imageView object
            imageView.setImageResource(adapter.images[positionOfGallery]); //Set the object
            add.setPhoto(imageView);

            //Enter the position of the photo
            positionOne = data.getExtras().getInt("position");
            add.setPositionOne(positionOne);

            //Add the photo
            add.perform(slideshow);

            //Add Action to Undo Stack
            undoStack.push(add);

            //Clear Redo Stack
            redoStack.clearStack();

        }
    }

    public void setSlideshow(){
        //Remove all the Views before adding new photos
        viewFlipper.removeAllViews();

        //Add the photos of slideshow into ViewFlipper
        for(int i = 0; i < slideshow.size(); i++){
            viewFlipper.addView(slideshow.get(i));
        }
        viewFlipper.startFlipping();
        viewFlipper.setFlipInterval(1000);
    }
}
