/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.utils;

import java.io.Serializable;

/**
 *
 * @author winiga
 */
public class Paginator implements Serializable{
    
    public static boolean hasNext(int currentPage, int totalPage) {
        
        if(currentPage >= 0 && totalPage >= 0 ) {
            return  !(totalPage == currentPage);
        }
        
        return false;
    }
    
    public static boolean hasPrevious(int currentPage) {
        
        if(currentPage >= 0 ) {
            return currentPage > 1;
        }
        
        return false;
    }
}
