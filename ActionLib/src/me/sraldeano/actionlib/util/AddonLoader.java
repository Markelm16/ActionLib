package me.sraldeano.actionlib.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sraldeano.actionlib.Action;
import me.sraldeano.actionlib.ActionLib;
import me.sraldeano.actionlib.ActionManager;

/**
 *
 * @author markelm
 */
public class AddonLoader {
    
    private Action action;
    
    public AddonLoader(ClassLoader parent, AddonDescription description, File file, File dataFolder) throws MalformedURLException, ClassNotFoundException {
        
        ActionLib.testMsg(file.toURI().toURL().toString());
        ClassLoader loader = URLClassLoader.newInstance(
            new URL[]{ file.toURI().toURL() },
            AddonLoader.class.getClassLoader()
        );
        
        

        Class<?> jarClass;
            
//            jarClass = Class.forName(description.getMain(), true, this);
            //jarClass = Class.forName(description.getMain(), true, loader);

//            Class<? extends Action> addonClazz;
//            
//            addonClazz = jarClass.asSubclass(Action.class);
        Class<?> javaClass = null;
        try {
            javaClass = Class.forName(description.getMain(), true, loader);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        Class<? extends Action> actionClass = null;
        try {
            actionClass = javaClass.asSubclass(Action.class);
//            Constructor constructor = actionLibClass.getConstructor();
            this.action =  actionClass.newInstance();
            ActionManager.registerAction(action, true);
        } catch (ClassCastException | IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(AddonLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public Action getAction() {
        return action;
    }
}
