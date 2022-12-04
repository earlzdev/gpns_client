package com.earl.gpns.ui.rooms

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView

class ChatRoomTouchHelper(
    private val adapter: RoomsRecyclerAdapter,
    private val currentPosition: Int
) : ItemTouchHelper.SimpleCallback(
    UP, DOWN
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
//        adapter.swap(currentPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}

//public class MovieTouchHelper : ItemTouchHelper.SimpleCallback {
//    Aadapter recycleAdapter;
//
//    public MovieTouchHelper(Aadapter recycleAdapter) {
//        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
//        this.recycleAdapter = recycleAdapter;
//    }
//
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        recycleAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        return true;
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        recycleAdapter.remove(viewHolder.getAdapterPosition());
//    }