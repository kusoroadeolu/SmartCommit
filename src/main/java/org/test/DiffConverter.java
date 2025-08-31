package org.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.test.diffsummary.DiffSummary;
import org.test.exceptions.GitDiffException;
import org.test.singletons.ObjectMapperCreator;

import java.util.List;
import java.util.logging.Logger;

public class DiffConverter {

    private final static ObjectMapper OBJECT_MAPPER = ObjectMapperCreator.objectMapper();
    private final static Logger log = Logger.getLogger(DiffConverter.class.getName());

    public static String diffSummariesToJson(List<DiffSummary> summaries){
            try{
                return OBJECT_MAPPER.writeValueAsString(summaries);
            }catch (JsonProcessingException e){
                log.severe("An unexpected error occurred while trying to write diff summary objects to JSON");
                throw new GitDiffException("An unexpected error occurred while trying to write diff summary objects to JSON");
            }
        }

    public static String convertDiffPatches(List<String> patches){
        StringBuilder sb = new StringBuilder();
        for (String s: patches){
            sb.append(s);
        }
        return sb.toString();
    }
}




