package de.mnreinisch.pp.watcher.control;


import de.mnreinisch.pp.watcher.domain.Setting;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;

import java.io.File;

public class Settings {
    public static final int LIGHT_THEME = 0;
    public static final int DARK_THEME = 1;

    private File configFile = new File(LogInit.globalConfigPath, "settings.json");

    private Setting setting = new Setting();
    private static Settings _instance;

    public static Settings getInstance(){
        if(_instance == null){
            _instance = new Settings();
        }
        return _instance;
    }

    private Settings() {

    }

    public void checkSettings() throws TechnicalException, CustomException {
        if (configFile.exists()) {
            loadConfiguration();
        } else {
            createNewConfig();
        }
    }

    public void safeConfiguration() throws CustomException, TechnicalException {
        JsonHandler<Setting> jsonHandler = new JsonHandler<>();
        jsonHandler.save(setting, configFile);
    }


    private void loadConfiguration() throws TechnicalException, CustomException {
        JsonHandler<Setting> jsonHandler = new JsonHandler<>();
        setting = jsonHandler.readSingle(Setting.class, configFile);
    }

    private void createNewConfig() throws TechnicalException, CustomException {
        setting.setTheme(LIGHT_THEME);

        JsonHandler<Setting> jsonHandler = new JsonHandler<>();
        jsonHandler.save(setting, configFile);
    }

    public Setting getSetting() {
        return setting;
    }
}
