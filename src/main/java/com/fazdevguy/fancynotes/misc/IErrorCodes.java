package com.fazdevguy.fancynotes.misc;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public interface IErrorCodes {

   <T> String getCode(T key);
}
