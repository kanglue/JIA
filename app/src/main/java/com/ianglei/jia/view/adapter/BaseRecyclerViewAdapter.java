package com.ianglei.jia.view.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ianglei.jia.utils.ViewAnimHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ianglei on 2018/2/18.
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected Context context;
    private int lastPosition = -1;
    private boolean isOnlyFirst = true;
    private int duration = 300;
    private Interpolator interpolator = new LinearInterpolator();

    protected List<E> list;

    private Map<Integer,onInternalClickListener<E>> internalListenerMap;

    public BaseRecyclerViewAdapter(List<E> list){
        this(list, null);
    }

    public BaseRecyclerViewAdapter(List<E> list, Context context){
        this.list = list;
        this.context = context;
    }

    public void add(E e){
        list.add(0, e);
        notifyItemInserted(0);
    }

    public void update(E e, int fromPosition, int toPosition){
        list.remove(fromPosition);
        list.add(toPosition, e);
        if(fromPosition == toPosition){
            notifyItemChanged(fromPosition);
        }else{
            notifyItemRemoved(fromPosition);
            notifyItemInserted(toPosition);
        }
    }

    public void update(E e, int fromPosition){
        update(e, fromPosition, 0);
    }

    public void update(E e){
        int position = list.indexOf(e);
        update(e, position);
    }

    public void remove(E e){
        int position = list.indexOf(e);
        remove(position);
    }

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void setList(List<E> list){
        list.clear();
        list.addAll(list);
    }

    public List<E> getList(){
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(null != holder){
            addInternalClickListener(holder.itemView, position, list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     *
     * @param itemView  每个子View
     * @param position
     * @param item
     */
    private void addInternalClickListener(final View itemView, final Integer position, final E item){
        if(null != internalListenerMap){
            //遍历View内部的元素
            for(Integer key:internalListenerMap.keySet()){
                //创建子View
                View inView = itemView.findViewById(key);
                final onInternalClickListener<E> listener = internalListenerMap.get(key);
                if(inView != null && listener != null) {
                    inView.setOnClickListener((view) -> {
                        listener.onClickListener(itemView, view, position, item);
                    });
                    inView.setOnLongClickListener((view) -> {
                        listener.onLongClickListener(itemView, view, position, item);
                        return true;
                    });
                }
            }
        }
    }

    /**
     *
     * @param key   元素ID
     * @param onClickListener   点击后事件
     */
    public void setOnInViewClickListener(Integer key, onInternalClickListener<E> onClickListener){
        if(internalListenerMap == null)
            internalListenerMap = new HashMap();
        internalListenerMap.put(key, onClickListener);
    }

    /**
     * 内部元素点击接口
     * @param <T>
     */
    public interface onInternalClickListener<T>{
        void onClickListener(View parentView, View v, Integer posion, T values);

        void onLongClickListener(View parentView, View v, Integer position, T values);
    }

    public static class onInternalClickListenerImpl<T> implements onInternalClickListener<T>{

        @Override
        public void onClickListener(View parentView, View v, Integer position, T values) {
        }

        @Override
        public void onLongClickListener(View parentView, View v, Integer position, T values) {
        }
    }

    public void setDuration(int duration){this.duration = duration;}

    public void setOnlyFirst(boolean onlyFirst){this.isOnlyFirst = onlyFirst;}

    protected void animate(RecyclerView.ViewHolder holder, int position){
        //非首次，同时
        if(!isOnlyFirst || position > lastPosition){
            for(Animator anim:getAnimators(holder.itemView)){
                anim.setDuration(duration).start();
                anim.setInterpolator(interpolator);
            }
            lastPosition = position;
        }else{
            ViewAnimHelper.clear(holder.itemView);
        }
    }

    protected abstract Animator[] getAnimators(View view);
}
