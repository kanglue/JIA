package com.ianglei.jia.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ianglei.jia.R;

/**
 * Created by ianglei on 2018/2/19.
 */

public class PhraseItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleText;
    private final TextView translationText;
    private final TextView sampleText;
    private final TextView tagText;

    public PhraseItemViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView)itemView.findViewById(R.id.phrase_title_text);
        translationText = (TextView)itemView.findViewById(R.id.phrase_translation_text);
        sampleText = (TextView)itemView.findViewById(R.id.phrase_sample_text);
        tagText = (TextView)itemView.findViewById(R.id.phrase_tag_edit_text);
    }

    private void setTextView(TextView view, CharSequence text){
        if(view == null)
            return;
        if(TextUtils.isEmpty(text)){
            view.setVisibility(View.GONE);
        }
        else{
            view.setText(text);
        }
    }

    public void setTitleText(CharSequence text){setTextView(titleText, text);}
    public void setTranslationText(CharSequence text){setTextView(translationText, text);}
    public void setSampleText(CharSequence text){setTextView(sampleText,text);}
    public void setTagText(CharSequence text){setTextView(tagText,text);}
}
