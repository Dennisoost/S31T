/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

/**
 *
 * @author Dennis
 */
public interface IChatClient {
    public void showMessage(String message);
    public void connectionFailed();  
}