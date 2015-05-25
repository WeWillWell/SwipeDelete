package com.example.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.test.swipeablelistview.ActionableToastBar;
import com.example.test.swipeablelistview.SwipeableListView;

public class MainActivity extends Activity{
	private SwipeableListView mSwipeableListView;
	private ArrayList<String> mArrayList = new ArrayList<String>();
	private MyAdapter mAdapter;
	
    private ActionableToastBar mUndoBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		getDATA();
		
		mSwipeableListView=(SwipeableListView) findViewById(R.id.alarms_list);
		mUndoBar = (ActionableToastBar) findViewById(R.id.undo_bar);
		mAdapter=new MyAdapter(this);
		mSwipeableListView.setAdapter(mAdapter);
		mSwipeableListView.setVerticalScrollBarEnabled(true);
		mSwipeableListView.enableSwipe(true);//flag.
		
		mSwipeableListView.setOnItemSwipeListener(new SwipeableListView.OnItemSwipeListener() {
            @Override
            public void onSwipe(View view) {
            	final ItemHolder itemHolder =(ItemHolder) view.getTag();
            	final String teString=itemHolder.label.getText().toString();
            	
            	mArrayList.remove(itemHolder.itemId);//delete the item.
            	mAdapter.notifyDataSetChanged();
            	
            	//show toast.
            	mUndoBar.show(new ActionableToastBar.ActionClickedListener() {
                    @Override
                    public void onActionClicked() {
                        ////////////undo action.
                    	mArrayList.add(itemHolder.itemId, teString);
                    	mAdapter.notifyDataSetChanged();
                    }
                }, 0, getResources().getString(R.string.alarm_deleted), true, R.string.alarm_undo, true);
            }
        });
		
		mSwipeableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                hideUndoBar(true, event);
                return false;
            }
        });
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (mUndoBar != null) {
            hideUndoBar(false, null);
        }
	}
	
	private void getDATA(){
		int i=0;
		while (i<10) {
			mArrayList.add("TEST ITEM "+i);
			i++;
		}
	}
	
	private void hideUndoBar(boolean animate, MotionEvent event) {
        if (mUndoBar != null) {
            if (event != null && mUndoBar.isEventInToastBar(event)) {
                return;
            }
            mUndoBar.hide(animate);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class MyAdapter extends BaseAdapter{
        private LayoutInflater mInflater; 
          
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context); 
        } 
        
        @Override
        public int getCount() { 
            return mArrayList.size(); 
        } 
  
        @Override
        public Object getItem(int arg0) { 
            return arg0; 
        } 
  
        @Override
        public long getItemId(int arg0) {
            return arg0; 
        } 
  
        @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemHolder holder = new ItemHolder();

			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.onoff = (Switch) convertView.findViewById(R.id.onoff);
			convertView.setTag(holder);
			holder.label.setText(mArrayList.get(position));
			holder.itemId = position;

			return convertView;
		}
    }
	
	public class ItemHolder {
		int itemId;
        Switch onoff;
        TextView label;
    }
}
