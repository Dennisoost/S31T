/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sjoerd
 */

public class Row {
    private final SimpleStringProperty rank;
    private final SimpleStringProperty userName;
    private final SimpleStringProperty value;
    
    Row(String rank, String userName, String value){
        this.rank = new SimpleStringProperty(rank);
        this.userName = new SimpleStringProperty(userName);
        this.value = new SimpleStringProperty(value);
    }
    
    public String getRank(){
        return rank.get();
    }
    
    public String getUserName(){
        return userName.get();
    }
    
    public String getValue(){
        return value.get();
    }
}
