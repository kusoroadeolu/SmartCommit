package org.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.test.exceptions.FileOperationsException;
import org.test.singletons.ObjectMapperCreator;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class FileUtils {

    private final static Logger log = Logger.getLogger(FileUtils.class.getName());
    private final static ObjectMapper OBJECT_MAPPER = ObjectMapperCreator.objectMapper();
    private final static String CONFIG_PATH = "smartcommit-config.json";

    //Just creates the config file
    public static void createConfigFile(){
        Path path = Path.of(CONFIG_PATH);

        try{
            if (Files.exists(path)){
                log.info(String.format("Config file %s already exists", path.getFileName()));
                return;
            }

            Files.createFile(path);
            log.info(String.format("Successfully created json config: %s", path.getFileName()));
            writeToFile(path);

        }catch (AccessDeniedException e){
            log.severe(String.format("Failed to create config file %s due to insufficient access", path.getFileName()));
            throw new FileOperationsException(String.format("Failed to create config file %s due to insufficient access", path.getFileName()), e);
        }catch (IOException e){
            log.severe(String.format("Failed to create config file %s due to an IO error", path.getFileName()));
            throw new FileOperationsException(String.format("Failed to create config file %s due to an IO error", path.getFileName()), e);
        }


    }

    protected static void writeToFile(Path path){
        try{
            String initData = "{\n \"exclude\": []\n}";
            byte[] toBytes = initData.getBytes();
            Files.write(path, toBytes);
            log.info("Successfully written init config properties to config file");
        }catch (IOException e){
            log.severe(String.format("Failed to write to config file %s due to an IO error", path.getFileName()));
            throw new FileOperationsException(String.format("Failed to create write to file %s due to an IO error", path.getFileName()));
        }
    }

    //Extract the excluded extensions from the json file
    public static Set<String> extractExcludedExtensions(){
        Path path = Path.of(CONFIG_PATH);

        if(!Files.exists(path)){
            log.warning("Failed to find config file. Returning empty set");
            return Set.of();
        }

        Set<String> cachedExclusions = new HashSet<>(5);
        try{

           byte[] jsonData = Files.readAllBytes(path);

            if(jsonData.length == 0){
                log.warning("Found no json data to read from config");
                return Set.of();
            }

            JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonData);
            JsonNode excludeNode = jsonNode.path("exclude");

            Iterator<JsonNode> excludedFileExtensionNodes = excludeNode.elements();
            excludedFileExtensionNodes.forEachRemaining(node -> cachedExclusions.add(node.asText().toLowerCase()));

            log.info(String.format("Found %s excluded file extensions to exclude", cachedExclusions.size()));
            return cachedExclusions;

        }catch (IOException e){
            log.severe("An IO error occurred while trying to extract excluded extensions from config file");
            throw new FileOperationsException("An IO error occurred while trying to extract excluded extensions from config file");
        }
    }



}
