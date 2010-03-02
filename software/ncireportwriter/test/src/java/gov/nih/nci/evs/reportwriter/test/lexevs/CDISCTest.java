package gov.nih.nci.evs.reportwriter.test.lexevs;

import java.util.*;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;

public class CDISCTest {
    private static Logger _logger = Logger.getLogger(CDISCTest.class);

    public static void main(String[] args) {
        try {
            args = SetupEnv.getInstance().parse(args);
            CDISCTest test = new CDISCTest();
            test.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "09.12d";
        String code = "C17998";
        String parentCode = "C66731";
        String value = getCdiscPt(codingScheme, version, code, parentCode);
        _logger.debug("Value: " + value);
    }

    private String getCdiscPt(String codingScheme, String version, String code,
        String parentCode) throws Exception {
        Concept concept =
            DataUtils.getConceptByCode(codingScheme, version, null, code);
        if (concept == null)
            throw new Exception("Can not retrieve concept from code " + code
                + ".");
        Concept parentConcept =
            DataUtils.getConceptByCode(codingScheme, version, null, parentCode);
        if (parentConcept == null)
            throw new Exception("Can not retrieve parent concept from code "
                + parentConcept + ".");

        String name = concept.getEntityDescription().getContent();
        String parentName = parentConcept.getEntityDescription().getContent();
        _logger.debug("* concept: " + name + "(" + code + ")");
        _logger
            .debug("* parentConcept: " + parentName + "(" + parentCode + ")");

        String nciABTerm = null;
        Vector<SynonymInfo> v = getSynonyms(parentConcept);
        ListUtils.debug(_logger, "getSynonyms(parentConcept)", v);
        int n = v.size();
        for (int i = 0; i < n; ++i) {
            SynonymInfo info = v.get(i);
            if (info.source.equalsIgnoreCase("NCI")
                && info.type.equalsIgnoreCase("AB")) {
                nciABTerm = info.name;
            }
        }
        if (nciABTerm == null)
            return "";

        v = getSynonyms(concept);
        ListUtils.debug(_logger, "getSynonyms(concept)", v);
        n = v.size();
        for (int i = 0; i < n; ++i) {
            SynonymInfo info = v.get(i);
            if (info.sourceCode.equals(nciABTerm))
                return info.name;
        }
        for (int i = 0; i < n; ++i) {
            SynonymInfo info = v.get(i);
            if (info.source.equalsIgnoreCase("CDISC")
                && info.source.equalsIgnoreCase("SY"))
                return info.name;
        }
        for (int i = 0; i < n; ++i) {
            SynonymInfo info = v.get(i);
            if (info.source.equalsIgnoreCase("CDISC")
                && info.source.equalsIgnoreCase("PT"))
                return info.name;
        }
        return "";
    }

    public class SynonymInfo {
        public String name = "";
        public String type = "";
        public String source = "";
        public String sourceCode = "";

        public String toString() {
            return "name=" + name + ", type=" + type + ", source=" + source
                + ", sourceCode=" + sourceCode;
        }
    }

    public Vector<SynonymInfo> getSynonyms(Concept concept) {
        Vector<SynonymInfo> v = new Vector<SynonymInfo>();
        if (concept == null)
            return v;

        Presentation[] properties = concept.getPresentation();
        for (int i = 0; i < properties.length; i++) {
            Presentation p = properties[i];
            SynonymInfo info = new SynonymInfo();

            info.name = p.getValue().getContent();
            int n = p.getPropertyQualifierCount();
            for (int j = 0; j < n; j++) {
                PropertyQualifier q = p.getPropertyQualifier(j);
                String qualifier_name = q.getPropertyQualifierName();
                String qualifier_value = q.getValue().getContent();
                if (qualifier_name.compareTo("source-code") == 0) {
                    info.sourceCode = qualifier_value;
                    break;
                }
            }

            info.type = p.getRepresentationalForm();
            Source[] sources = p.getSource();
            if (sources != null && sources.length > 0) {
                Source src = sources[0];
                info.source = src.getContent();
            }
            v.add(info);
        }
        SortUtils.quickSort(v);
        return v;
    }
}