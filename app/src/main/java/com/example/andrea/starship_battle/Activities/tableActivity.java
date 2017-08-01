package com.example.andrea.starship_battle.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.andrea.starship_battle.R;

/*public class tableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        TableRow rowLabels = (TableRow) findViewById(R.id.rowLabels0);
        TableRow row1 = (TableRow) findViewById(R.id.row1);
        TableRow row2 = (TableRow) findViewById(R.id.row2);
        TableRow row3 = (TableRow) findViewById(R.id.row3);
        TableRow row4 = (TableRow) findViewById(R.id.row4);
        TableRow row5 = (TableRow) findViewById(R.id.row5);
        TableRow row6 = (TableRow) findViewById(R.id.row6);
        TableRow row7 = (TableRow) findViewById(R.id.row7);
        TableRow row8 = (TableRow) findViewById(R.id.row8);
        LinearLayout shipLayout = (LinearLayout) findViewById(R.id.ship_deposit);

        // Creates a new drag event listener
        myDragEventListener mDragListen = new myDragEventListener();

        // Declares the imageview and creates the bitmaps by them
        ImageView tie = (ImageView) findViewById(R.id.id_tie);
        Bitmap bmap_tie = BitmapFactory.decodeResource(getResources(), R.drawable.tie_sx);
        ImageView star_destroyer = (ImageView) findViewById(R.id.id_star_dest);
        Bitmap bmap_star_destroyer = BitmapFactory.decodeResource(getResources(), R.drawable.star_destroyer_sx);
        ImageView death_star = (ImageView) findViewById(R.id.id_death_star);
        Bitmap bmap_death_star = BitmapFactory.decodeResource(getResources(), R.drawable.death_star_sx);

        // Sets the drag event listener for the View
        tie.setOnDragListener(mDragListen);
        death_star.setOnDragListener(mDragListen);
        star_destroyer.setOnDragListener(mDragListen);

        // Sets the activity title
        TextView place_ships = (TextView) findViewById(R.id.id_place_ships);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Starjedi.ttf");
        place_ships.setTypeface(custom_font);

        // Sets the dimension of the field square and the ships
        int dim_field_square = 9;
        int dim_ship = 4;

        resize(rowLabels, dim_field_square);
        resize(row1, dim_field_square);
        resize(row2, dim_field_square);
        resize(row3, dim_field_square);
        resize(row4, dim_field_square);
        resize(row5, dim_field_square);
        resize(row6, dim_field_square);
        resize(row7, dim_field_square);
        resize(row8, dim_field_square);
        resize(shipLayout, dim_ship);

        // Registers the ships as a draggable item
        registerDandD(tie, bmap_tie);
        registerDandD(star_destroyer, bmap_star_destroyer);
        registerDandD(death_star, bmap_death_star);

        tie.setOnDragListener(new myDragEventListener());
        place_ships.setOnDragListener(new myDragEventListener());
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {

            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth()/2;

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight()/2;

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the Canvas.
            shadow.setBounds(0,0,width,height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width,height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width/2,height/2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

    protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            int action = event.getAction();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        Toast.makeText(getApplicationContext(), "Dragged data is ", Toast.LENGTH_LONG);

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        v.setDrawingCacheBackgroundColor(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Applies a green tint to the View. Return true; the return value is ignored.

                    v.setDrawingCacheBackgroundColor(Color.GREEN);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    v.setDrawingCacheBackgroundColor(Color.BLUE);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    CharSequence dragData = item.getText();

                    // Displays a message containing the dragged data.
                    Toast.makeText(getApplicationContext(), "Dragged data is " + dragData, Toast.LENGTH_LONG);

                    // Turns off any color tints
                    //v.clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    //v.clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Toast.makeText(getApplicationContext(), "The drop was handled.", Toast.LENGTH_LONG);

                    } else {
                        Toast.makeText(getApplicationContext(), "The drop didn't work.", Toast.LENGTH_LONG);

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    };

    public void resize(View view, int dim) {
        for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
            View nextChild = ((ViewGroup) view).getChildAt(index);
            ViewGroup.LayoutParams lp = nextChild.getLayoutParams();
            //per avere dim dello schermo
            Display d = getWindowManager().getDefaultDisplay();
            Point outsize = new Point();
            d.getSize(outsize);
            lp.width = outsize.y / dim;
            lp.height = outsize.y / dim;
            nextChild.setLayoutParams(lp);
        }
    }

    public void registerDandD(final ImageView view, Bitmap bmap) {

        // Create a string for the ImageView label
        final String IMAGEVIEW_TAG = "icon bitmap";

        // Creates a new ImageView
        //ImageView imageView = new ImageView(this);

        //view.buildDrawingCache();
        //Bitmap bmap = BitmapFactory.decodeResource(getResources(), R.drawable.tie_sx);

        // Sets the bitmap for the ImageView from an icon bit map (defined elsewhere)

        view.setImageBitmap(bmap);

        // Sets the tag
        view.setTag(IMAGEVIEW_TAG);

        // Sets a long click listener for the ImageView using an anonymous listener object that
        // implements the OnLongClickListener interface
        view.setOnLongClickListener(new View.OnLongClickListener() {

            // Defines the one method for the interface, which is called when the View is long-clicked
            public boolean onLongClick(View v) {

                // Create a new ClipData.
                // This is done in two steps to provide clarity. The convenience method
                // ClipData.newPlainText() can create a plain text ClipData in one step.

                // Create a new ClipData.Item from the ImageView object's tag
                ClipData.Item item = new ClipData.Item((String)v.getTag());

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                ClipData dragData = new ClipData((CharSequence)v.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(view);

                // Starts the drag

                v.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                return true;

            }
        });
    }
}*/