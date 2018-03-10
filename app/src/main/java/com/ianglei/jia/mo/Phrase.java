package com.ianglei.jia.mo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ianglei on 2018/1/6.
 */

@Entity(indexes = {
        @Index(value = "phrase, createTime DESC", unique = true)
})
public class Phrase {
    @Id
    private String phrase;

    private String translate;

    private String sample;

    private String tags;

    private long createTime;

    private boolean isLearned;

    private int learnedCount;



@Generated(hash = 1136898449)
public Phrase() {
}

@Generated(hash = 1051627685)
public Phrase(String phrase, String translate, String sample, String tags,
        long createTime, boolean isLearned, int learnedCount) {
    this.phrase = phrase;
    this.translate = translate;
    this.sample = sample;
    this.tags = tags;
    this.createTime = createTime;
    this.isLearned = isLearned;
    this.learnedCount = learnedCount;
}

public String getPhrase() {
    return this.phrase;
}

public void setPhrase(String phrase) {
    this.phrase = phrase;
}

public String getTranslate() {
    return this.translate;
}

public void setTranslate(String translate) {
    this.translate = translate;
}

public String getSample() {
    return this.sample;
}

public void setSample(String sample) {
    this.sample = sample;
}

public String getTags() {
    return this.tags;
}

public void setTags(String tags) {
    this.tags = tags;
}

public long getCreateTime() {
    return this.createTime;
}

public void setCreateTime(long createTime) {
    this.createTime = createTime;
}

public boolean getIsLearned() {
    return this.isLearned;
}

public void setIsLearned(boolean isLearned) {
    this.isLearned = isLearned;
}

public int getLearnedCount() {
    return this.learnedCount;
}

public void setLearnedCount(int learnedCount) {
    this.learnedCount = learnedCount;
}
}
