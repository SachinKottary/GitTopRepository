package com.sachin.gogit.utils;

import androidx.fragment.app.Fragment;

public class FragmentUtils {

    public static final int FRAGMENT_GO_GIT_HOME = 0;

    /**
     * Returns the fragment name to be instantiate
     *
     * @param type int identifier to differentiate the fragment
     * @return string fragment name
     */
    public static String getFragmentTag(int type) {
        switch (type) {
            case FRAGMENT_GO_GIT_HOME:
                return Fragment.class.getName();
        }
        return "";
    }

}