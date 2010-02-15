package gov.nih.nci.evs.reportwriter.utils;

import java.util.*;
import java.util.regex.*;

import javax.faces.context.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

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
 * @author EVS Team (Kim Ong, David Yee, Wilberto Garcia)
 * @version 1.0
 */

public class HTTPUtils {
    private static Logger _logger = Logger.getLogger(HTTPUtils.class);

    public static String cleanXSS(String value) {

        if (value == null || value.length() < 1)
            return value;

        // Remove XSS attacks
        value = replaceAll(value, "<\\s*script\\s*>.*</\\s*script\\s*>", "");
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value =
            replaceAll(value, "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("\"", "&quot;");
        return value;
    }

    public static String replaceAll(String string, String regex,
        String replaceWith) {

        Pattern myPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        string = myPattern.matcher(string).replaceAll(replaceWith);
        return string;
    }

    public static void printRequestSessionAttributes(String text) {
        _logger.debug(" ");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Request Session Attribute(s): ");
        if (text != null && text.trim().length() > 0)
            _logger.debug(text);

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            HttpSession session = request.getSession();
            Enumeration<?> enumeration =
                SortUtils.sort(session.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = session.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }

    public static void printRequestAttributes(String text) {
        _logger.debug(" ");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Request Attribute(s):");
        if (text != null && text.trim().length() > 0)
            _logger.debug(text);

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            Enumeration<?> enumeration =
                SortUtils.sort(request.getAttributeNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }

    public static void printRequestParameters(String text) {
        _logger.debug(" ");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Request Parameter(s):");
        if (text != null && text.trim().length() > 0)
            _logger.debug(text);

        try {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            Enumeration<?> enumeration =
                SortUtils.sort(request.getParameterNames());
            int i = 0;
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getParameter(name);
                _logger.debug("  " + i + ") " + name + ": " + value);
                ++i;
            }
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }

    public static void printAttributes(String text) {
        printRequestSessionAttributes(text);
        printRequestAttributes(text);
        printRequestParameters(text);
        _logger.debug(" ");
    }

    public static String warningMsg(HttpServletRequest request, String msg) {
        request.setAttribute("warningMsg", msg);
        return "warningMsg";
    }

    public static String warningMsg(HttpServletRequest request,
        StringBuffer buffer) {
        return warningMsg(request, buffer.toString());
    }

    public static String warningMsg(HttpServletRequest request,
        StringBuffer buffer, Throwable throwable) {
        if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) != '\n')
            buffer.append("\n");
        buffer.append(throwable.getClass().getSimpleName() + ": "
            + throwable.getMessage());
        return warningMsg(request, buffer);
    }

    public static String getParameter(HttpServletRequest request,
        String parameterName) {
        String value = request.getParameter(parameterName);
        value = value.trim();
        request.setAttribute(parameterName, value);
        return value;
    }

    public static String getSessionAttributeStr(HttpServletRequest request,
        String attributeName) {
        String value =
            (String) request.getSession().getAttribute(attributeName);
        value = value.trim();
        request.setAttribute(attributeName, value);
        return value;
    }
    
    public static boolean getJspAttributeBoolean(HttpServletRequest request,
        String attributeName, boolean defaultValue) {
        Boolean value = (Boolean) request.getAttribute(attributeName);
        if (value == null)
            return defaultValue;
        return value.booleanValue();
    }
}