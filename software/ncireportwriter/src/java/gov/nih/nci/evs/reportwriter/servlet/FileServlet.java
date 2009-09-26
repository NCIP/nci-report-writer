package gov.nih.nci.evs.reportwriter.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.bean.*;

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
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public final class FileServlet extends HttpServlet {
    private final Logger _logger = Logger.getLogger(FileServlet.class);

    /**
     * Route the user to the execute method
     * 
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * 
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     * 
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * 
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * 
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     * 
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */

    public void sendErrorResponse(HttpServletRequest request,
            HttpServletResponse response, String message) throws IOException,
            ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("message", message);

            String nextJSP = "/pages/message.jsf";
            RequestDispatcher dispatcher =
                getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(request, response);
        }
    }

    public void sendResponse(HttpServletResponse response,
            StandardReport standardReport) throws IOException, ServletException {
        // processing download

        String fullPathName = standardReport.getPathName();
        ReportFormat rf = standardReport.getFormat();
        String format = rf.getDescription();
        response.setContentType("text/html");
        // String line_br = "\n";
        String line_br = "\r\n";

        response.setHeader("Cache-Control", "no-cache");

        if (format.indexOf("Excel") != -1) {
            String contentType = "application/vnd.ms-excel";
            response.setContentType(contentType);
            line_br = "";
        }

        File file = new File(fullPathName);
        String filename = file.getName();
        // HKR, GForge# 19467 - wrong placement of '\' in header.
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + filename + "\"");

        _logger.debug("Full path name =" + fullPathName);
        _logger.debug("File name =" + filename);

        if (format.indexOf("Excel") == -1) {
            try {
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                DataInputStream dis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    dis = new DataInputStream(bis);
                    String s = "";
                    while (dis.available() != 0) {
                        String line = dis.readLine();
                        s = s + line + line_br;
                    }
                    PrintWriter out = response.getWriter();
                    out.write(s);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                }

            } catch (Exception e) {
            }
        } else if (format.indexOf("Excel") != -1) {
            try {
                InputStream myxls = new FileInputStream(fullPathName);
                HSSFWorkbook wb = new HSSFWorkbook(myxls);

                try {
                    wb.write(response.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } catch (Exception e) {
                // LogUtils.log(logger, Level.ERROR, e);
            }
        }
    }

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // <td class="dataCellText"><a
        // href="<%=request.getContextPath() %>/fileServlet?template=<%=templateId%>&format=<%=formatId%>"
        // ><%=template%> (<%=formatDescription%>)</a></td>

        // Get ReportFormat from database
        String formatId = request.getParameter("format");
        String templateId = request.getParameter("template");
        ReportFormat reportFormat = null;
        StandardReportTemplate standardReportTemplate = null;
        String message = "Format ID " + formatId + " not found.";
        try {
            reportFormat = null;
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            String methodName = "setId";
            int format_id = Integer.parseInt(formatId);
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            Object reportFormat_obj =
                sdkclientutil.search(FQName, methodName, format_id);
            if (reportFormat_obj == null) {
                sendErrorResponse(request, response, message);
                return;
            } else {
                reportFormat = (ReportFormat) reportFormat_obj;
            }
            // Get StandardReportTemplate from database
            message = "Template ID " + templateId + " not found.";
            try {
                // Get all
                // gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate
                // Find ov.nih.nci.evs.reportwriter.bean.StandardReport with
                // matched template and format
                // Find the fullpath of the StandardReport
                // Check ReportStatus (send not available or pending approval
                // message as needed)

                FQName =
                    "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
                methodName = "setId";
                int template_id = Integer.parseInt(templateId);
                Object standardReportTemplate_obj =
                    sdkclientutil.search(FQName, methodName, template_id);
                if (standardReportTemplate_obj == null) {
                    sendErrorResponse(request, response, message);
                }

                standardReportTemplate =
                    (StandardReportTemplate) standardReportTemplate_obj;

                // Search for the matched StandardReport in the database (by
                // template and format)
                FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
                message = "The selected report is not available for download.";
                Object[] objs = sdkclientutil.search(FQName);

                if (objs == null || objs.length == 0) {
                    sendErrorResponse(request, response, message);
                } else {
                    for (int i = 0; i < objs.length; i++) {
                        StandardReport standardReport =
                            (StandardReport) objs[i];
                        StandardReportTemplate srt =
                            standardReport.getTemplate();
                        int i1 = srt.getId().intValue();
                        int i2 = standardReportTemplate.getId().intValue();

                        if (i1 == i2) {
                            ReportFormat rf = standardReport.getFormat();
                            if (rf != null && rf.getId() == format_id) {
                                HttpSession session = request.getSession(false);
                                Boolean isAdmin =
                                    (Boolean) session.getAttribute("isAdmin");
                                if (isAdmin != null
                                        && isAdmin.equals(Boolean.TRUE)) {
                                    sendResponse(response, standardReport);
                                } else {
                                    // Check if the report has been approved:
                                    ReportStatus rs =
                                        standardReport.getStatus();
                                    if (rs != null) {
                                        String approved = "APPROVED";
                                        if (approved.equals(rs.getLabel())) {
                                            sendResponse(response,
                                                    standardReport);
                                        } else {
                                            sendErrorResponse(request,
                                                    response,
                                                    "The selected report has not yet been approved.");
                                        }
                                    } else {
                                        sendErrorResponse(request, response,
                                                "The selected report has not yet been approved.");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                sendErrorResponse(request, response, message);
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            sendErrorResponse(request, response, message);
            ex.printStackTrace();
        }
    }
}
