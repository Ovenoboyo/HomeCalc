package com.sahil.gupte.HomeCalc.CustomViews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.gupte.HomeCalc.Fragments.Dialogs.SpinnerEditDialogFragment;
import com.sahil.gupte.HomeCalc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomRecyclerViewSpinner extends RecyclerView.Adapter<CustomRecyclerViewSpinner.RecyclerViewHolder> implements
        SwipeAndDragHelper.ActionCompletionContract{
    private int count;
    private final Context context1;
    private FragmentManager fragmentManager;
    public final HashMap<Integer, RecyclerView.ViewHolder> holderHashMap = new HashMap<>();

    public static ArrayList<String> spinnerNameList = new ArrayList<>();
    public static ArrayList<String> spinnerNameListFinal = new ArrayList<>();
    private ItemTouchHelper touchHelper;

    public CustomRecyclerViewSpinner(Context context, ArrayList<String> spinnerNameList, FragmentManager fm) {
        context1 = context;
        CustomRecyclerViewSpinner.spinnerNameList.addAll(spinnerNameList);
        CustomRecyclerViewSpinner.spinnerNameList.remove(0);
        count = CustomRecyclerViewSpinner.spinnerNameList.size();
        spinnerNameListFinal.addAll(CustomRecyclerViewSpinner.spinnerNameList);
        fragmentManager = fm;

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        holderHashMap.put(holder.getAdapterPosition(),holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        holderHashMap.remove(holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);

    }
    @Override
    public CustomRecyclerViewSpinner.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_spinner, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewSpinner.RecyclerViewHolder holder, int position) {
        holder.price.setText(spinnerNameList.get(position));
        holder.price.setOnClickListener(view -> holder.price.setOnClickListener(view1 -> {
            SpinnerEditDialogFragment fragment = new SpinnerEditDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", position);
            fragment.setArguments(bundle);
            fragment.show(fragmentManager.beginTransaction(), "dialog1");
        }));
        holder.reorder.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder);
                return false;
            }
            return true;
        });
    }

    public void addCount() {
        spinnerNameList.add("New Item");
        spinnerNameListFinal.add("New Item");
        count++;
        notifyItemInserted(spinnerNameList.size()+1);
    }

    private void removeItem(int pos) {
        spinnerNameList.remove(pos);
        spinnerNameListFinal.remove(spinnerNameList.get(pos));
        count--;
        notifyItemRemoved(pos);

    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        final TextView price;
        final ImageView reorder;

        RecyclerViewHolder(View view) {
            super(view);
            price = view.findViewById(R.id.editText);
            reorder = view.findViewById(R.id.reorder);

        }
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {

        this.touchHelper = touchHelper;
    }

    public void changeItem(int pos, String text) {
        spinnerNameListFinal.set(spinnerNameListFinal.indexOf(CustomRecyclerViewSpinner.spinnerNameList.get(pos)), text);
        CustomRecyclerViewSpinner.spinnerNameList.set(pos, text);
        notifyItemChanged(pos);
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        String targetUser = spinnerNameListFinal.get(oldPosition);
        spinnerNameListFinal.remove(oldPosition);
        spinnerNameListFinal.add(newPosition, targetUser);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
        removeItem(position);
    }
}
