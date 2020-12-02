package com.ibm.ta.sdk.core.detector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.ta.sdk.core.assessment.GenericIssue;
import com.ibm.ta.sdk.core.assessment.IssueMatchCriteria;
import com.ibm.ta.sdk.core.assessment.IssueRule;
import com.ibm.ta.sdk.core.util.GenericUtil;
import com.ibm.ta.sdk.spi.collect.AssessmentUnit;
import com.ibm.ta.sdk.spi.recommendation.Target;
import com.ibm.ta.sdk.spi.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomizedRuleTypeProvider implements IssueRuleTypeProvider {

    private static Logger logger = LogManager.getLogger(CustomizedRuleTypeProvider.class.getName());
    private final String RULE_TYPE_DETECTOR_NAME = "customizedDiff";
    private final String CRETERIA_KEYNAME = "criteria";
    @Override
    public String getName() {
        return RULE_TYPE_DETECTOR_NAME;
    }

    @Override
    public IssueMatchCriteria getIssueMatchCriteria(JsonObject matchCriteriaJson) {
        return new IssueMatchCriteria(matchCriteriaJson, CRETERIA_KEYNAME);
    }

    @Override
    public GenericIssue getIssue(Target target, AssessmentUnit assessmentUnit, IssueRule issueRule) {
        GenericIssue issue = new GenericIssue(issueRule);
        logger.debug(" issueRule="+issueRule.getMatchCriteriaJson());
        logger.debug(" assessmentUnit config file=" + assessmentUnit.getConfigFiles());

        if (assessmentUnit.getConfigFiles() != null) {
            List<Path> custDataFiles = assessmentUnit.getConfigFiles()
                    .stream()
                    .filter(path ->path.toString().contains("CustIssueDetectData"))
                    .collect(Collectors.toList());
            if (custDataFiles.size()>1) {
                issue.addOccurences(getOcurrence(custDataFiles, issueRule));
            }
        }

        return issue;
    }

    private List<Map<String, String>> getOcurrence(List<Path> custDataFiles, IssueRule issueRule) {
        List<Map<String, String>> ocMapList = new ArrayList<Map<String, String>>();;
        Map<String, JsonElement> creteria = getIssueMatchCriteria(issueRule.getMatchCriteriaJson()).getQueryPaths();
        logger.debug("zzzzzzz matching creteria: "+creteria);
        Path dataFile1 = custDataFiles.get(0);
        Path dataFile2 = custDataFiles.get(1);
        boolean matches = false;
        String missingItemName = null;
        try {
            JsonArray source = GenericUtil.getJson(dataFile1).getAsJsonObject().getAsJsonArray("source");
            JsonArray target = GenericUtil.getJson(dataFile2).getAsJsonObject().getAsJsonArray("target");
            for (JsonElement targetJe: target) {
                String targetStr = targetJe.getAsString();
                if (!source.toString().contains(targetStr)){
                    logger.debug("zzzzzzz find missing target "+targetStr);
                    matches = true;
                    missingItemName = targetStr;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (matches){
            JsonObject occurrenceAttr = issueRule.getMatchCriteria().getOccurrenceAttr();
            logger.debug(" occurrenceAttr="+occurrenceAttr);
            if (occurrenceAttr != null) {
                for (String attrKey : occurrenceAttr.keySet()) {
                    JsonObject oneAttribute = occurrenceAttr.get(attrKey).getAsJsonObject();
                    Map<String, String> newOccurence = new HashMap<>();
                    newOccurence.put(attrKey, missingItemName);
                    ocMapList.add(newOccurence);
                }
            }
        }
        return ocMapList;
    }
}

