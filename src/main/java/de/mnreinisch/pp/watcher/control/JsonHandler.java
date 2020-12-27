package de.mnreinisch.pp.watcher.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonHandler<T> {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static List<String> loadAllConfigs(File configFolder) {
        List<String> fileNames = new ArrayList<>();
        if (!configFolder.exists()) {
            configFolder.mkdir();
            return fileNames;
        }
        Collection<File> configFiles = FileUtils.listFiles(configFolder, new String[]{"json"}, true);

        for (File file : configFiles) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    public static boolean checkLastConfig(File configPath, String configName) {
        if (configName == null || configPath == null) return false;

        File lastConfig = new File(configPath, configName);
        return lastConfig.exists() && lastConfig.isFile();
    }

    public void save(T saveObject, File currentConfig) throws TechnicalException {
        newSafe(saveObject, currentConfig);

    }

    public T readSingle(Class<T> clazz, File config) throws CustomException, TechnicalException {
        try (Reader reader = new FileReader(config)) {
            return mapper.readValue(reader, clazz);
        } catch (FileNotFoundException e) {
            throw new CustomException("Config " + config.getName() + " doesn't exist.", e, true);
        } catch (IOException e) {
            throw new TechnicalException("Error while reading config " + config.getName() + "\n" + e.getMessage(), e);
        }
    }

    public List<T> readList(Class<T> clazz, File config) throws CustomException, TechnicalException {
        try (Reader reader = new FileReader(config)) {
            TypeFactory typeFactory = mapper.getTypeFactory();
            return mapper.readValue(reader, typeFactory.constructCollectionType(List.class, clazz));
        } catch (FileNotFoundException e) {
            throw new CustomException("Config " + config.getName() + " doesn't exist.", e, true);
        } catch (IOException e) {
            throw new TechnicalException("Error while reading config " + config.getName() + "\n" + e.getMessage(), e);
        }
    }

    private void newSafe(T clazz, File config) throws TechnicalException {
        try (FileWriter fileWriter = new FileWriter(config)) {
            if (!config.exists()) {
                config.createNewFile();
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, clazz);
        } catch (IOException e) {
            throw new TechnicalException("Error while saving.\n" + e.getMessage(), e);
        }
    }
}
