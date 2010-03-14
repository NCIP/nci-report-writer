package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import java.util.*;

import javax.servlet.http.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class ReportStatusRequest {
    public String addAction() {
        return "report_status";
    }

    public String saveAction(String selectedStandardReportTemplate) {
        HttpServletRequest request = SessionUtil.getRequest();
        request.getSession().setAttribute("selectedStandardReportTemplate",
                selectedStandardReportTemplate);

        UserSessionBean userSessionBean = BeanUtils.getUserSessionBean();
        StandardReportTemplate standardReportTemplate =
            userSessionBean
                    .getStandardReportTemplate(selectedStandardReportTemplate);

        return HTTPUtils.sessionMsg(request, "The status of the "
                + standardReportTemplate.getLabel()
                + " has been updated successfully.");
    }

    public String assignAction() {
        HttpServletRequest request = SessionUtil.getRequest();
        // save to database
        String reportTemplate =
            (String) request.getSession().getAttribute(
                    "selectedStandardReportTemplate_draft");
        String statusValue =
            (String) request.getSession().getAttribute("selectedReportStatus");

        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            StandardReportTemplate standardReportTemplate = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
            Object[] objs = sdkclientutil.search(FQName);
            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    StandardReport standardReport = (StandardReport) objs[i];
                    standardReportTemplate = standardReport.getTemplate();
                    if (standardReportTemplate != null) {
                        if (reportTemplate.compareTo(standardReportTemplate
                                .getLabel()) == 0) {
                            FQName =
                                "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
                            String methodName = "setLabel";
                            String key = statusValue;

                            Object status_obj =
                                sdkclientutil.search(FQName, methodName, key);
                            if (status_obj != null) {
                                standardReport
                                        .setStatus((ReportStatus) status_obj);
                                java.util.Date lastModified = new Date(); // system
                                // date
                                standardReport.setLastModified(lastModified);
                                sdkclientutil
                                        .updateStandardReport(standardReport);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserSessionBean userSessionBean = BeanUtils.getUserSessionBean();
        userSessionBean.getStandardReportTemplateList_draft();

        return "assign_report_status";
    }
}