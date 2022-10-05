package kz.optimabank.optima24.utility.crypt;

import java.util.Enumeration;
import java.util.Vector;

/**
  Created by Timur on 13.01.2017.
 */

abstract class Disposable {
    private static final Object SYNCH = new Object();
    private static Vector allInstances;

    @SuppressWarnings("unchecked")
    public Disposable() {
        synchronized (SYNCH){
            if (allInstances == null){
                allInstances = new Vector();
            }

            if (!allInstances.contains(this)){
                allInstances.addElement(this);
            }
        }
    }

    public abstract void dispose();

    public static void disposeAll(){
        synchronized (SYNCH){
            for (Enumeration enumeration = allInstances.elements(); enumeration.hasMoreElements();){
                Disposable disposable = (Disposable) enumeration.nextElement();
                disposable.dispose();
            }
            allInstances.removeAllElements();
            allInstances = null;
        }
    }
}
