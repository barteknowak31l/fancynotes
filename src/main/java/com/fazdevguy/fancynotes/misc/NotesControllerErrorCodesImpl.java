package com.fazdevguy.fancynotes.misc;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotesControllerErrorCodesImpl implements IErrorCodes{

    public enum ErrorCodes{
        EMPTY_NAME_ERROR,
        OK
    }

    private Map<ErrorCodes,String > errorCodesStringMap;


    public NotesControllerErrorCodesImpl(){

        errorCodesStringMap = new HashMap<ErrorCodes,String>();
        errorCodesStringMap.put(ErrorCodes.EMPTY_NAME_ERROR,"emptyNameError");
        errorCodesStringMap.put(ErrorCodes.OK,"ok");
    }


    @Override
    public <ErrorCodes> String getCode(ErrorCodes key) {
        return errorCodesStringMap.get(key);
    }


}
