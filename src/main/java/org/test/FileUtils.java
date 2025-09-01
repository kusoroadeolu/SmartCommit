package org.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jgit.lib.PersonIdent;
import org.test.exceptions.FileOperationsException;
import org.test.singletons.ObjectMapperCreator;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class FileUtils {

    private final static Logger log = Logger.getLogger(FileUtils.class.getName());
    private final static ObjectMapper OBJECT_MAPPER = ObjectMapperCreator.objectMapper();
    private final static Path CONFIG_PATH = Path.of(Paths.get("").toAbsolutePath().normalize().toString() ,"smartcommit-config.json");

    // Creates the config file and writes to it
    public static void createConfigFile(){
        try{
            if (Files.exists(CONFIG_PATH)){
                log.info(String.format("Config file %s already exists", CONFIG_PATH.getFileName()));
                return;
            }

            Files.createFile(CONFIG_PATH);
            log.info(String.format("Successfully created json config: %s", CONFIG_PATH.getFileName()));
            writeToFile();

        }catch (AccessDeniedException e){
            log.severe(String.format("Failed to create config file %s due to insufficient access", CONFIG_PATH.getFileName()));
            throw new FileOperationsException(String.format("Failed to create config file %s due to insufficient access", CONFIG_PATH.getFileName()), e);
        }catch (IOException e){
            log.severe(String.format("Failed to create config file %s due to an IO error", CONFIG_PATH.getFileName()));
            throw new FileOperationsException(String.format("Failed to create config file %s due to an IO error", CONFIG_PATH.getFileName()), e);
        }


    }

    //Writes the necessary info on a blank slate to the config file
    protected static void writeToFile(){
        try{
            String initData = """
                    {
                     "exclude": [],
                     "commit-mode": "summary",
                     "gemini-token": "",
                     "pat-token": "",
                     "person-ident": {
                        "name":"",
                        "email":""
                     }
                    }
                    """;
            byte[] toBytes = initData.getBytes();
            Files.write(CONFIG_PATH, toBytes);
            log.info("Successfully written init config properties to config file");
        }catch (IOException e){
            log.severe(String.format("Failed to write to config file %s due to an IO error", CONFIG_PATH.getFileName()));
            throw new FileOperationsException(String.format("Failed to create write to file %s due to an IO error", CONFIG_PATH.getFileName()));
        }
    }

    //Extracts json data from the file as a string
    protected static String extractJsonDataAsString(String jsonPath){
        try{
            byte[] bytes = Files.readAllBytes(CONFIG_PATH);
            JsonNode rootNode = OBJECT_MAPPER.readTree(bytes);
            return rootNode.path(jsonPath).asText();
        }catch (IOException e){
            log.severe("An IO error occurred while trying to extract json data from config file");
            throw new FileOperationsException("An IO error occurred while trying to extract json data from config file");
        }
    }

    //Extract the excluded extensions from the json file
    public static Set<String> extractExcludedExtensions(){
        if(!Files.exists(CONFIG_PATH)){
            log.warning("Failed to find config file. Returning empty set");
            return Set.of();
        }

        Set<String> cachedExclusions = new HashSet<>(5);
        try{

           byte[] jsonData = Files.readAllBytes(CONFIG_PATH);

            if(jsonData.length == 0){
                log.warning("Found no json data to read from config");
                return Set.of();
            }

            JsonNode rootNode = OBJECT_MAPPER.readTree(jsonData);
            Iterator<JsonNode> excludedFileExtensionNodes = rootNode.path("exclude").elements();
            excludedFileExtensionNodes.forEachRemaining(node -> cachedExclusions.add(node.asText().toLowerCase()));

            log.info(String.format("Found %s excluded file extensions to exclude", cachedExclusions.size()));
            return cachedExclusions;

        }catch (IOException e){
            log.severe("An IO error occurred while trying to extract excluded extensions from config file");
            throw new FileOperationsException("An IO error occurred while trying to extract excluded extensions from config file");
        }
    }

    //Extracts the PAT token from the file
    public static String extractPATToken(){
        String token = extractJsonDataAsString("pat-token");
        if(token == null || token.isEmpty()){
            log.severe("PAT Token cannot be null or empty");
            throw new FileOperationsException("PAT Token cannot be null or empty");
        }
        return token;

    }

    //Extracts the Gemini API Token from the file
    public static String extractGeminiToken(){
        String token = extractJsonDataAsString("gemini-token");

        if(token == null || token.isEmpty()){
            log.severe("Gemini Token cannot be null or empty");
            throw new FileOperationsException("Gemini Token cannot be null or empty");
        }

        return token;
    }

    //Extracts the commit mode from the file
    public static String extractCommitMode(){
            return extractJsonDataAsString("commit-mode").toLowerCase();
    }

    //Extracts the person ident from the file
    public static PersonIdentWrapper extractPersonIdent() {
        try{
            byte[] bytes = Files.readAllBytes(CONFIG_PATH);
            JsonNode node = OBJECT_MAPPER.readTree(bytes);
            JsonNode personIdentNode = node.get("person-ident");
            return OBJECT_MAPPER.convertValue(personIdentNode, PersonIdentWrapper.class);
        }catch (IOException e){
            log.severe("An IO error occurred while trying to extract person ident from config file");
            throw new FileOperationsException("An IO error occurred while trying to extract person ident from config file");
        }
    }
}
