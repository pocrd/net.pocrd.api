package net.pocrd.api;

import net.pocrd.entity.BaseFunctionFlag;

public class FunctionFlags extends BaseFunctionFlag {

    public static final BaseFunctionFlag EPUB_BOOK = new FunctionFlags("EB"); 
    
    public FunctionFlags(String name) {
        super(name);
    }

}
