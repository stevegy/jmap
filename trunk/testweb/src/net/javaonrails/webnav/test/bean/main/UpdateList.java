/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.test.bean.main;

import java.util.ArrayList;
import net.javaonrails.webnav.jmap.controller.BasicBean;

/**
 *
 * @author Steve
 */
public class UpdateList extends BasicBean {

    private ArrayList<String> options;
    
    @Override
    public void execute() {
        this.setOptions(new ArrayList<String>());
        for(int i=0; i<5; i++) {
            this.options.add("option " + i + " " 
                    + this.getController().getUrlHint());
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
