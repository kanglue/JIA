package com.ianglei.jia.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ianglei.jia.R;
import com.ianglei.jia.mo.Phrase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ianglei on 2018/1/14.
 */

public class PhraseAdapter extends BaseRecyclerViewAdapter<Phrase> implements Filterable{

    private List<Phrase> originalList;
    private Context context;

    public PhraseAdapter(List<Phrase> list){
        super(list);
        originalList = new ArrayList<>(list);
    }

    public PhraseAdapter(List<Phrase> list, Context context){
        super(list, context);
        originalList = new ArrayList<>(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.phrase_item_layout, parent,false);
        return new PhraseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        super.onBindViewHolder(viewholder, position);
        PhraseItemViewHolder holder = (PhraseItemViewHolder)viewholder;
        Phrase phrase = list.get(position);
        if(null == phrase)
            return;
        String text = "";
        if(context != null){
            boolean b = TextUtils.equals("", phrase.getPhrase());
            text = b?"":phrase.getPhrase();
        }
        holder.setTitleText(text);
        holder.setTranslationText(phrase.getTranslate());
        holder.setSampleText(phrase.getSample());
        holder.setTagText(phrase.getTags());
    }

    @Override
    protected Animator[] getAnimators(View view) {
        if (view.getMeasuredHeight() <=0){
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1.0f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1.0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1.0f),
        };
    }

    @Override
    public Filter getFilter() {
        return new PhraseFilter(this,originalList);
    }

    private static class PhraseFilter extends Filter{
        private final PhraseAdapter adapter;

        private final List<Phrase> originalList;

        private final List<Phrase> filterList;

        public PhraseFilter(PhraseAdapter adapter, List<Phrase> originalList){
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filterList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterList.clear();
            final FilterResults results = new FilterResults();
            //没有过滤规则就是全部
            if(constraint.length() == 0){
                filterList.addAll(originalList);
            }else{
                for(Phrase phrase:originalList){
                    if(phrase.getPhrase().contains(constraint) || phrase.getTranslate().contains(constraint)){
                        filterList.add(phrase);
                    }
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence text, FilterResults results){
            adapter.list.clear();
            adapter.list.addAll((ArrayList<Phrase>)results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
