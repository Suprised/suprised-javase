package com.suprised.lucene.index;

import org.apache.lucene.util.Version;

public interface Costs {

    public static final String CONTENTS = "contents";
    
    public static final String TITLE = "title";
    
    public static final String MODIFYED = "modified";
    
    public static final String PATH = "path";
    
    public static final Version LUCENE_VERSION = Version.LUCENE_47;
    
    public static final String DATE_FULL_PATTERN = "yyyy-MM-dd hh:mm:ss";
}
