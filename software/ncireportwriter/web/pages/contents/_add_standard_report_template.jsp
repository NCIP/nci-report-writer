<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %> 

<f:view>
  <h:form id="SELECT_TASKForm">
    <table summary="" cellpadding="0" cellspacing="0" border="0" 
        width="100%" height="100%"> <!-- Table 1 (Begin) -->
        
      <tr>
        <td height="20" width="100%" class="mainMenu">
          <%@ include file="/pages/contents/menuBar.jsp" %>
        </td>
      </tr>

      <tr>
        <td width="100%" valign="top">
          <br>
          <!-- target of anchor to skip menus -->
          <a name="content" />
          <table summary="" cellpadding="0" cellspacing="0" border="0" 
              class="contentPage" width="600"> <!-- Table 2 (Begin) -->
            <tr>
              <td>
                <table summary="" cellpadding="0" cellspacing="0" border="0"> <!-- Table 3 (Begin) -->
                  <tr>
                    <td class="dataTablePrimaryLabel" height="20">CREATE REPORT TEMPLATE</td>
                  </tr>
                  <tr>
                    <td>
                      <table summary="Enter summary of data here" class="dataTable" 
                          cellpadding="3" cellspacing="0" border="0" width="100%">
                          <!-- Table 4 (Begin) -->

                        <tr class="dataRowDark">
                          <td class="dataCellText">Label</td>
                          <td class="dataCellText"><input type="text" name="label"></td>
                        </tr>
                        
                        <tr class="dataRowLight">
                          <td class="dataCellText">                                           
                            <h:outputText value="Coding Scheme:" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="ontologyId" 
                              value="#{ontologyBean.selectedOntology}" 
                              valueChangeListener="#{ontologyBean.ontologySelectionChanged}" >
                                <f:selectItems value="#{ontologyBean.ontologyList}" />
                            </h:selectOneMenu>                                          
                          </td>                                           
                        </tr>
                        
                        <tr class="dataRowDark">
                          <td class="dataCellText">Root Concept Code</td>
                          <td class="dataCellText"><input type="text" name="rootConceptCode"></td>
                        </tr>

                        <tr class="dataRowLight">
                          <td class="dataCellText">                                           
                            <h:outputText value="Association Name" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="associationId" 
                                value="#{ontologyBean.selectedAssociation}" 
                                valueChangeListener="#{ontologyBean.associationSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.associationList}" />
                            </h:selectOneMenu>                                          
                          </td>                                           
                        </tr>
                        
                        <!--
                        <tr class="dataRowDark">
                          <td class="dataCellText">Direction</td>
                          <td class="dataCellText">                                           
                            <h:selectOneRadio id="direction"
                                value="#{ontologyBean.selectedDirection}">
                              <f:selectItems value="#{ontologyBean.directionList}"/>
                            </h:selectOneRadio>
                          </td>
                        </tr>
                        -->
                        
                        <tr class="dataRowDark">
                          <td class="dataCellText">Direction</td>
                          <td class="dataCellText">
                            <input type="radio" name="direction" value="source" checked>Source&nbsp;
                            <input type="radio" name="direction" value="target">Target
                          </td>
                        </tr>                                       
                        
                        <tr class="dataRowLight">
                          <td class="dataCellText">
                            <h:outputText value="Level" />
                          </td>
                          <td class="dataCellText">
                            <h:selectOneMenu id="LevelId" value="#{ontologyBean.selectedLevel}" 
                                valueChangeListener="#{ontologyBean.levelSelectionChanged}" >
                              <f:selectItems value="#{ontologyBean.levelList}" />
                            </h:selectOneMenu>
                          </td>                                       
                        </tr>
                          
                      </table> <!-- Table 4 (End) -->
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="actionSection">
                      <table cellpadding="4" cellspacing="0" border="0">
                        <tr>
                          <td><h:commandButton id="save" value="Save" 
                              action="#{userSessionBean.saveTemplateAction}" /></td>
                          <td><input type="reset" value="Reset" /></td>
                          <td><h:commandButton id="back" value="Back" action="back" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table> <!-- Table 3 (End) -->
              </td>
            </tr>
          </table> <!-- Table 2 (End) -->
        </td>
      </tr>
            
    </table> <!-- Table 1 (End) -->
  </h:form>
</f:view>