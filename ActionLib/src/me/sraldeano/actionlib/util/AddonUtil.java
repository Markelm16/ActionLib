/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.sraldeano.actionlib.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import me.sraldeano.actionlib.Action;
import me.sraldeano.actionlib.ActionLib;

public class AddonUtil {
    private Pattern JAR_PATTERN = Pattern.compile("(.+?)(\\.jar)");
    static File folder;
    
    public AddonUtil() {
        folder = new File(ActionLib.plugin.getDataFolder(), "addons");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    
    public List<Action> loadAddons() {
        
        ArrayList<Action> actionList = new ArrayList<>();
        for (File jar : folder.listFiles()) {
            
            if (jar.getPath().endsWith(".jar")) {
                try {
                    actionList.add(loadAddon(new JarFile(jar)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return actionList;
    }
    
    public Action loadAddon(JarFile jar) {
        try {
            AddonDescription description;
            
            description = getDescription(jar);
            
            File dataFolder = new File(folder, description.getName());
            
            if (!dataFolder.exists())
                dataFolder.mkdir();
            
            String[] jarNameSplited = jar.getName().split("/");
            String jarName = jarNameSplited[jarNameSplited.length - 1];
            
            return new AddonLoader(description, new File(folder, jarName), dataFolder).getAction();
        } catch (MalformedURLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public AddonDescription getDescription(JarFile jar) {
        ZipEntry entry = jar.getEntry("addon.yml");
        InputStream stream = null;
        try {
            stream = jar.getInputStream(entry);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new AddonDescription(stream);
    }
    
    public static File getFolder() {
        return folder;
    }
    
}
