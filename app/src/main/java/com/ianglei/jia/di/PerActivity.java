package com.ianglei.jia.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ianglei on 2018/1/5.
 */

@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
