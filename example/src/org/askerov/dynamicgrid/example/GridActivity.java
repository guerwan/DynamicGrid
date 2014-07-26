package org.askerov.dynamicgrid.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.Arrays;

public class GridActivity extends Activity {

    private static final String TAG = GridActivity.class.getName();

    private DynamicGridView gridView;
    private CheeseDynamicAdapter mAdapter;
    private Button mDeleteBtn;
    private Button mEditBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);

        mAdapter = new CheeseDynamicAdapter(this,
                new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)),
                getResources().getInteger(R.integer.column_count));
        gridView.setAdapter(mAdapter);
//        add callback to stop edit mode if needed
//        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
//        {
//            @Override
//            public void onActionDrop()
//            {
//                gridView.stopEditMode();
//            }
//        });

        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mDeleteBtn = (Button) findViewById(R.id.delete);
        mEditBtn = (Button) findViewById(R.id.edit);

        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                mDeleteBtn.setPressed(false);
                mEditBtn.setPressed(false);
            }

            @Override
            public void onRightButtonHovering(int itemPosition) {
                mDeleteBtn.setPressed(true);
                mEditBtn.setPressed(false);
            }

            @Override
            public void onLeftButtonHovering(int itemPosition) {
                mDeleteBtn.setPressed(false);
                mEditBtn.setPressed(true);
            }

            @Override
            public void onLeftButtonDrop(int itemPosition) {
                mDeleteBtn.setPressed(false);
                mEditBtn.setPressed(false);
                Toast.makeText(GridActivity.this, "Edit", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRightButtonDrop(int itemPosition) {
                mDeleteBtn.setPressed(false);
                mEditBtn.setPressed(false);
                Toast.makeText(GridActivity.this, "Delete", Toast.LENGTH_LONG).show();
                mAdapter.remove(mAdapter.getItem(itemPosition));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }
}
