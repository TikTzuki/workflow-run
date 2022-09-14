// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.BpmnParseException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.impl.Condition;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.behavior.*;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.bpmn.listener.ClassDelegateExecutionListener;
import org.zik.bpm.engine.impl.bpmn.listener.DelegateExpressionExecutionListener;
import org.zik.bpm.engine.impl.bpmn.listener.ExpressionExecutionListener;
import org.zik.bpm.engine.impl.bpmn.listener.ScriptExecutionListener;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement.CallableElementBinding;
import org.zik.bpm.engine.impl.core.model.CallableElement;
import org.zik.bpm.engine.impl.core.model.CallableElementParameter;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ConstantValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.NullValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;
import org.zik.bpm.engine.impl.el.*;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.impl.form.handler.*;
import org.zik.bpm.engine.impl.jobexecutor.*;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.*;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.impl.scripting.ScriptCondition;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.task.listener.ClassDelegateTaskListener;
import org.zik.bpm.engine.impl.task.listener.DelegateExpressionTaskListener;
import org.zik.bpm.engine.impl.task.listener.ExpressionTaskListener;
import org.zik.bpm.engine.impl.task.listener.ScriptTaskListener;
import org.zik.bpm.engine.impl.util.*;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.impl.util.xml.Namespace;
import org.zik.bpm.engine.impl.util.xml.Parse;
import org.zik.bpm.engine.impl.variable.VariableDeclaration;
import org.zik.bpm.engine.repository.ProcessDefinition;

import java.io.InputStream;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.*;

public class BpmnParse extends Parse {
    public static final String MULTI_INSTANCE_BODY_ID_SUFFIX = "#multiInstanceBody";
    protected static final BpmnParseLogger LOG;
    public static final String PROPERTYNAME_DOCUMENTATION = "documentation";
    public static final String PROPERTYNAME_INITIATOR_VARIABLE_NAME = "initiatorVariableName";
    public static final String PROPERTYNAME_HAS_CONDITIONAL_EVENTS = "hasConditionalEvents";
    public static final String PROPERTYNAME_CONDITION = "condition";
    public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";
    public static final String PROPERTYNAME_VARIABLE_DECLARATIONS = "variableDeclarations";
    public static final String PROPERTYNAME_TIMER_DECLARATION = "timerDeclarations";
    public static final String PROPERTYNAME_MESSAGE_JOB_DECLARATION = "messageJobDeclaration";
    public static final String PROPERTYNAME_ISEXPANDED = "isExpanded";
    public static final String PROPERTYNAME_START_TIMER = "timerStart";
    public static final String PROPERTYNAME_COMPENSATION_HANDLER_ID = "compensationHandler";
    public static final String PROPERTYNAME_IS_FOR_COMPENSATION = "isForCompensation";
    public static final String PROPERTYNAME_EVENT_SUBSCRIPTION_JOB_DECLARATION = "eventJobDeclarations";
    public static final String PROPERTYNAME_THROWS_COMPENSATION = "throwsCompensation";
    public static final String PROPERTYNAME_CONSUMES_COMPENSATION = "consumesCompensation";
    public static final String PROPERTYNAME_JOB_PRIORITY = "jobPriority";
    public static final String PROPERTYNAME_TASK_PRIORITY = "taskPriority";
    public static final String PROPERTYNAME_EXTERNAL_TASK_TOPIC = "topic";
    public static final String PROPERTYNAME_CLASS = "class";
    public static final String PROPERTYNAME_EXPRESSION = "expression";
    public static final String PROPERTYNAME_DELEGATE_EXPRESSION = "delegateExpression";
    public static final String PROPERTYNAME_VARIABLE_MAPPING_CLASS = "variableMappingClass";
    public static final String PROPERTYNAME_VARIABLE_MAPPING_DELEGATE_EXPRESSION = "variableMappingDelegateExpression";
    public static final String PROPERTYNAME_RESOURCE = "resource";
    public static final String PROPERTYNAME_LANGUAGE = "language";
    public static final String TYPE = "type";
    public static final String TRUE = "true";
    public static final String INTERRUPTING = "isInterrupting";
    public static final String CONDITIONAL_EVENT_DEFINITION = "conditionalEventDefinition";
    public static final String ESCALATION_EVENT_DEFINITION = "escalationEventDefinition";
    public static final String COMPENSATE_EVENT_DEFINITION = "compensateEventDefinition";
    public static final String TIMER_EVENT_DEFINITION = "timerEventDefinition";
    public static final String SIGNAL_EVENT_DEFINITION = "signalEventDefinition";
    public static final String MESSAGE_EVENT_DEFINITION = "messageEventDefinition";
    public static final String ERROR_EVENT_DEFINITION = "errorEventDefinition";
    public static final String CANCEL_EVENT_DEFINITION = "cancelEventDefinition";
    public static final String LINK_EVENT_DEFINITION = "linkEventDefinition";
    public static final String CONDITION_EXPRESSION = "conditionExpression";
    public static final String CONDITION = "condition";
    public static final List<String> VARIABLE_EVENTS;
    /**
     * @deprecated
     */
    @Deprecated
    public static final String PROPERTYNAME_TYPE;
    /**
     * @deprecated
     */
    @Deprecated
    public static final String PROPERTYNAME_ERROR_EVENT_DEFINITIONS;
    protected static final String POTENTIAL_STARTER = "potentialStarter";
    protected static final String CANDIDATE_STARTER_USERS_EXTENSION = "candidateStarterUsers";
    protected static final String CANDIDATE_STARTER_GROUPS_EXTENSION = "candidateStarterGroups";
    protected static final String ATTRIBUTEVALUE_T_FORMAL_EXPRESSION = "http://www.omg.org/spec/BPMN/20100524/MODEL:tFormalExpression";
    public static final String PROPERTYNAME_IS_MULTI_INSTANCE = "isMultiInstance";
    public static final Namespace CAMUNDA_BPMN_EXTENSIONS_NS;
    public static final Namespace XSI_NS;
    public static final Namespace BPMN_DI_NS;
    public static final Namespace OMG_DI_NS;
    public static final Namespace BPMN_DC_NS;
    public static final String ALL = "all";
    protected DeploymentEntity deployment;
    protected List<ProcessDefinitionEntity> processDefinitions = new ArrayList();
    protected Map<String, Error> errors = new HashMap();
    protected Map<String, Escalation> escalations = new HashMap();
    protected Map<String, List<JobDeclaration<?, ?>>> jobDeclarations = new HashMap();
    protected Map<String, TransitionImpl> sequenceFlows;
    protected List<String> elementIds = new ArrayList();
    protected Map<String, String> participantProcesses = new HashMap();
    protected Map<String, MessageDefinition> messages = new HashMap();
    protected Map<String, SignalDefinition> signals = new HashMap();
    protected ExpressionManager expressionManager;
    protected List<BpmnParseListener> parseListeners;
    protected Map<String, XMLImporter> importers = new HashMap();
    protected Map<String, String> prefixs = new HashMap();
    protected String targetNamespace;
    private Map<String, String> eventLinkTargets = new HashMap();
    private Map<String, String> eventLinkSources = new HashMap();
    protected static final String HUMAN_PERFORMER = "humanPerformer";
    protected static final String POTENTIAL_OWNER = "potentialOwner";
    protected static final String RESOURCE_ASSIGNMENT_EXPR = "resourceAssignmentExpression";
    protected static final String FORMAL_EXPRESSION = "formalExpression";
    protected static final String USER_PREFIX = "user(";
    protected static final String GROUP_PREFIX = "group(";
    protected static final String ASSIGNEE_EXTENSION = "assignee";
    protected static final String CANDIDATE_USERS_EXTENSION = "candidateUsers";
    protected static final String CANDIDATE_GROUPS_EXTENSION = "candidateGroups";
    protected static final String DUE_DATE_EXTENSION = "dueDate";
    protected static final String FOLLOW_UP_DATE_EXTENSION = "followUpDate";
    protected static final String PRIORITY_EXTENSION = "priority";

    public BpmnParse(BpmnParser parser) {
        super(parser);
        this.expressionManager = parser.getExpressionManager();
        this.parseListeners = parser.getParseListeners();
        this.setSchemaResource(ReflectUtil.getResourceUrlAsString("org/camunda/bpm/engine/impl/bpmn/parser/BPMN20.xsd"));
    }

    public BpmnParse deployment(DeploymentEntity deployment) {
        this.deployment = deployment;
        return this;
    }

    public BpmnParse execute() {
        super.execute();

        try {
            this.parseRootElement();
        } catch (BpmnParseException var6) {
            this.addError(var6);
        } catch (Exception var7) {
            LOG.parsingFailure(var7);
            throw LOG.parsingProcessException(var7);
        } finally {
            if (this.hasWarnings()) {
                this.logWarnings();
            }

            if (this.hasErrors()) {
                this.throwExceptionForErrors();
            }

        }

        return this;
    }

    protected void parseRootElement() {
        this.collectElementIds();
        this.parseDefinitionsAttributes();
        this.parseImports();
        this.parseMessages();
        this.parseSignals();
        this.parseErrors();
        this.parseEscalations();
        this.parseProcessDefinitions();
        this.parseCollaboration();
        this.parseDiagramInterchangeElements();
        Iterator var1 = this.parseListeners.iterator();

        while (var1.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var1.next();
            parseListener.parseRootElement(this.rootElement, this.getProcessDefinitions());
        }

    }

    protected void collectElementIds() {
        this.rootElement.collectIds(this.elementIds);
    }

    protected void parseDefinitionsAttributes() {
        this.targetNamespace = this.rootElement.attribute("targetNamespace");
        Iterator var1 = this.rootElement.attributes().iterator();

        while (var1.hasNext()) {
            String attribute = (String) var1.next();
            if (attribute.startsWith("xmlns:")) {
                String prefixValue = this.rootElement.attribute(attribute);
                String prefixName = attribute.substring(6);
                this.prefixs.put(prefixName, prefixValue);
            }
        }

    }

    protected String resolveName(String name) {
        if (name == null) {
            return null;
        } else {
            int indexOfP = name.indexOf(58);
            if (indexOfP != -1) {
                String prefix = name.substring(0, indexOfP);
                String resolvedPrefix = (String) this.prefixs.get(prefix);
                return resolvedPrefix + ":" + name.substring(indexOfP + 1);
            } else {
                return this.targetNamespace + ":" + name;
            }
        }
    }

    protected void parseImports() {
        List<Element> imports = this.rootElement.elements("import");
        Iterator var2 = imports.iterator();

        while (var2.hasNext()) {
            Element theImport = (Element) var2.next();
            String importType = theImport.attribute("importType");
            XMLImporter importer = this.getImporter(importType, theImport);
            if (importer == null) {
                this.addError("Could not import item of type " + importType, theImport);
            } else {
                importer.importFrom(theImport, this);
            }
        }

    }

    protected XMLImporter getImporter(String importType, Element theImport) {
        if (this.importers.containsKey(importType)) {
            return (XMLImporter) this.importers.get(importType);
        } else {
            if (importType.equals("http://schemas.xmlsoap.org/wsdl/")) {
                try {
                    Class<?> wsdlImporterClass = Class.forName("org.camunda.bpm.engine.impl.webservice.CxfWSDLImporter", true, Thread.currentThread().getContextClassLoader());
                    XMLImporter newInstance = (XMLImporter) wsdlImporterClass.newInstance();
                    this.importers.put(importType, newInstance);
                    return newInstance;
                } catch (Exception var5) {
                    this.addError("Could not find importer for type " + importType, theImport);
                }
            }

            return null;
        }
    }

    public void parseMessages() {
        Iterator var1 = this.rootElement.elements("message").iterator();

        while (var1.hasNext()) {
            Element messageElement = (Element) var1.next();
            String id = messageElement.attribute("id");
            String messageName = messageElement.attribute("name");
            Expression messageExpression = null;
            if (messageName != null) {
                messageExpression = this.expressionManager.createExpression(messageName);
            }

            MessageDefinition messageDefinition = new MessageDefinition(this.targetNamespace + ":" + id, messageExpression);
            this.messages.put(messageDefinition.getId(), messageDefinition);
        }

    }

    protected void parseSignals() {
        Iterator var1 = this.rootElement.elements("signal").iterator();

        while (var1.hasNext()) {
            Element signalElement = (Element) var1.next();
            String id = signalElement.attribute("id");
            String signalName = signalElement.attribute("name");
            Iterator var5 = this.signals.values().iterator();

            SignalDefinition signal;
            while (var5.hasNext()) {
                signal = (SignalDefinition) var5.next();
                if (signal.getName().equals(signalName)) {
                    this.addError("duplicate signal name '" + signalName + "'.", signalElement);
                }
            }

            if (id == null) {
                this.addError("signal must have an id", signalElement);
            } else if (signalName == null) {
                this.addError("signal with id '" + id + "' has no name", signalElement);
            } else {
                Expression signalExpression = this.expressionManager.createExpression(signalName);
                signal = new SignalDefinition();
                signal.setId(this.targetNamespace + ":" + id);
                signal.setExpression(signalExpression);
                this.signals.put(signal.getId(), signal);
            }
        }

    }

    public void parseErrors() {
        Error error;
        String id;
        for (Iterator var1 = this.rootElement.elements("error").iterator(); var1.hasNext(); this.errors.put(id, error)) {
            Element errorElement = (Element) var1.next();
            error = new Error();
            id = errorElement.attribute("id");
            if (id == null) {
                this.addError("'id' is mandatory on error definition", errorElement);
            }

            error.setId(id);
            String errorCode = errorElement.attribute("errorCode");
            if (errorCode != null) {
                error.setErrorCode(errorCode);
            }

            String errorMessage = errorElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "errorMessage");
            if (errorMessage != null) {
                error.setErrorMessageExpression(this.createParameterValueProvider(errorMessage, this.expressionManager));
            }
        }

    }

    protected void parseEscalations() {
        Iterator var1 = this.rootElement.elements("escalation").iterator();

        while (var1.hasNext()) {
            Element element = (Element) var1.next();
            String id = element.attribute("id");
            if (id == null) {
                this.addError("escalation must have an id", element);
            } else {
                Escalation escalation = this.createEscalation(id, element);
                this.escalations.put(id, escalation);
            }
        }

    }

    protected Escalation createEscalation(String id, Element element) {
        Escalation escalation = new Escalation(id);
        String name = element.attribute("name");
        if (name != null) {
            escalation.setName(name);
        }

        String escalationCode = element.attribute("escalationCode");
        if (escalationCode != null && !escalationCode.isEmpty()) {
            escalation.setEscalationCode(escalationCode);
        }

        return escalation;
    }

    public void parseProcessDefinitions() {
        Iterator var1 = this.rootElement.elements("process").iterator();

        while (var1.hasNext()) {
            Element processElement = (Element) var1.next();
            boolean isExecutable = !this.deployment.isNew();
            String isExecutableStr = processElement.attribute("isExecutable");
            if (isExecutableStr != null) {
                isExecutable = Boolean.parseBoolean(isExecutableStr);
                if (!isExecutable) {
                    LOG.ignoringNonExecutableProcess(processElement.attribute("id"));
                }
            } else {
                LOG.missingIsExecutableAttribute(processElement.attribute("id"));
            }

            if (isExecutable) {
                this.processDefinitions.add(this.parseProcess(processElement));
            }
        }

    }

    public void parseCollaboration() {
        Element collaboration = this.rootElement.element("collaboration");
        if (collaboration != null) {
            Iterator var2 = collaboration.elements("participant").iterator();

            while (var2.hasNext()) {
                Element participant = (Element) var2.next();
                String processRef = participant.attribute("processRef");
                if (processRef != null) {
                    ProcessDefinitionImpl procDef = this.getProcessDefinition(processRef);
                    if (procDef != null) {
                        ParticipantProcess participantProcess = new ParticipantProcess();
                        participantProcess.setId(participant.attribute("id"));
                        participantProcess.setName(participant.attribute("name"));
                        procDef.setParticipantProcess(participantProcess);
                        this.participantProcesses.put(participantProcess.getId(), processRef);
                    }
                }
            }
        }

    }

    public ProcessDefinitionEntity parseProcess(Element processElement) {
        this.sequenceFlows = new HashMap();
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setKey(processElement.attribute("id"));
        processDefinition.setName(processElement.attribute("name"));
        processDefinition.setCategory(this.rootElement.attribute("targetNamespace"));
        processDefinition.setProperty("documentation", this.parseDocumentation(processElement));
        processDefinition.setTaskDefinitions(new HashMap());
        processDefinition.setDeploymentId(this.deployment.getId());
        processDefinition.setTenantId(this.deployment.getTenantId());
        processDefinition.setProperty("jobPriority", this.parsePriority(processElement, "jobPriority"));
        processDefinition.setProperty("taskPriority", this.parsePriority(processElement, "taskPriority"));
        processDefinition.setVersionTag(processElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "versionTag"));

        try {
            String historyTimeToLive = processElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "historyTimeToLive", Context.getProcessEngineConfiguration().getHistoryTimeToLive());
            processDefinition.setHistoryTimeToLive(ParseUtil.parseHistoryTimeToLive(historyTimeToLive));
        } catch (Exception var6) {
            this.addError(new BpmnParseException(var6.getMessage(), processElement, var6));
        }

        boolean isStartableInTasklist = this.isStartable(processElement);
        processDefinition.setStartableInTasklist(isStartableInTasklist);
        LOG.parsingElement("process", processDefinition.getKey());
        this.parseScope(processElement, processDefinition);
        this.parseLaneSets(processElement, processDefinition);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseProcess(processElement, processDefinition);
        }

        this.validateActivities(processDefinition.getActivities());
        var4 = processDefinition.getActivities().iterator();

        while (var4.hasNext()) {
            ActivityImpl activity = (ActivityImpl) var4.next();
            activity.setDelegateAsyncAfterUpdate((ActivityImpl.AsyncAfterUpdate) null);
            activity.setDelegateAsyncBeforeUpdate((ActivityImpl.AsyncBeforeUpdate) null);
        }

        return processDefinition;
    }

    protected void parseLaneSets(Element parentElement, ProcessDefinitionEntity processDefinition) {
        List<Element> laneSets = parentElement.elements("laneSet");
        if (laneSets != null && laneSets.size() > 0) {
            Iterator var4 = laneSets.iterator();

            while (var4.hasNext()) {
                Element laneSetElement = (Element) var4.next();
                LaneSet newLaneSet = new LaneSet();
                newLaneSet.setId(laneSetElement.attribute("id"));
                newLaneSet.setName(laneSetElement.attribute("name"));
                this.parseLanes(laneSetElement, newLaneSet);
                processDefinition.addLaneSet(newLaneSet);
            }
        }

    }

    protected void parseLanes(Element laneSetElement, LaneSet laneSet) {
        List<Element> lanes = laneSetElement.elements("lane");
        Lane lane;
        if (lanes != null && lanes.size() > 0) {
            for (Iterator var4 = lanes.iterator(); var4.hasNext(); laneSet.addLane(lane)) {
                Element laneElement = (Element) var4.next();
                lane = new Lane();
                lane.setId(laneElement.attribute("id"));
                lane.setName(laneElement.attribute("name"));
                List<Element> flowNodeElements = laneElement.elements("flowNodeRef");
                if (flowNodeElements != null && flowNodeElements.size() > 0) {
                    Iterator var8 = flowNodeElements.iterator();

                    while (var8.hasNext()) {
                        Element flowNodeElement = (Element) var8.next();
                        lane.getFlowNodeIds().add(flowNodeElement.getText());
                    }
                }
            }
        }

    }

    public void parseScope(Element scopeElement, ScopeImpl parentScope) {
        List<Element> activityElements = new ArrayList(scopeElement.elements());
        Map<String, Element> intermediateCatchEvents = this.filterIntermediateCatchEvents(activityElements);
        activityElements.removeAll(intermediateCatchEvents.values());
        Map<String, Element> compensationHandlers = this.filterCompensationHandlers(activityElements);
        activityElements.removeAll(compensationHandlers.values());
        this.parseStartEvents(scopeElement, parentScope);
        this.parseActivities(activityElements, scopeElement, parentScope);
        this.parseIntermediateCatchEvents(scopeElement, parentScope, intermediateCatchEvents);
        this.parseEndEvents(scopeElement, parentScope);
        this.parseBoundaryEvents(scopeElement, parentScope);
        this.parseSequenceFlow(scopeElement, parentScope, compensationHandlers);
        this.parseExecutionListenersOnScope(scopeElement, parentScope);
        this.parseAssociations(scopeElement, parentScope, compensationHandlers);
        this.parseCompensationHandlers(parentScope, compensationHandlers);
        Iterator var6 = parentScope.getBacklogErrorCallbacks().iterator();

        while (var6.hasNext()) {
            ScopeImpl.BacklogErrorCallback callback = (ScopeImpl.BacklogErrorCallback) var6.next();
            callback.callback();
        }

        if (parentScope instanceof ProcessDefinition) {
            this.parseProcessDefinitionCustomExtensions(scopeElement, (ProcessDefinition) parentScope);
        }

    }

    protected HashMap<String, Element> filterIntermediateCatchEvents(List<Element> activityElements) {
        HashMap<String, Element> intermediateCatchEvents = new HashMap();
        Iterator var3 = activityElements.iterator();

        while (var3.hasNext()) {
            Element activityElement = (Element) var3.next();
            if (activityElement.getTagName().equals("intermediateCatchEvent")) {
                intermediateCatchEvents.put(activityElement.attribute("id"), activityElement);
            }
        }

        return intermediateCatchEvents;
    }

    protected HashMap<String, Element> filterCompensationHandlers(List<Element> activityElements) {
        HashMap<String, Element> compensationHandlers = new HashMap();
        Iterator var3 = activityElements.iterator();

        while (var3.hasNext()) {
            Element activityElement = (Element) var3.next();
            if (this.isCompensationHandler(activityElement)) {
                compensationHandlers.put(activityElement.attribute("id"), activityElement);
            }
        }

        return compensationHandlers;
    }

    protected void parseIntermediateCatchEvents(Element scopeElement, ScopeImpl parentScope, Map<String, Element> intermediateCatchEventElements) {
        Iterator var4 = intermediateCatchEventElements.values().iterator();

        while (var4.hasNext()) {
            Element intermediateCatchEventElement = (Element) var4.next();
            if (parentScope.findActivity(intermediateCatchEventElement.attribute("id")) == null) {
                ActivityImpl activity = this.parseIntermediateCatchEvent(intermediateCatchEventElement, parentScope, (ActivityImpl) null);
                if (activity != null) {
                    this.parseActivityInputOutput(intermediateCatchEventElement, activity);
                }
            }
        }

        intermediateCatchEventElements.clear();
    }

    protected void parseProcessDefinitionCustomExtensions(Element scopeElement, ProcessDefinition definition) {
        this.parseStartAuthorization(scopeElement, definition);
    }

    protected void parseStartAuthorization(Element scopeElement, ProcessDefinition definition) {
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) definition;
        Element extentionsElement = scopeElement.element("extensionElements");
        if (extentionsElement != null) {
            List<Element> potentialStarterElements = extentionsElement.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "potentialStarter");
            Iterator var6 = potentialStarterElements.iterator();

            while (var6.hasNext()) {
                Element potentialStarterElement = (Element) var6.next();
                this.parsePotentialStarterResourceAssignment(potentialStarterElement, processDefinition);
            }
        }

        String candidateUsersString = scopeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "candidateStarterUsers");
        if (candidateUsersString != null) {
            List<String> candidateUsers = this.parseCommaSeparatedList(candidateUsersString);
            Iterator var13 = candidateUsers.iterator();

            while (var13.hasNext()) {
                String candidateUser = (String) var13.next();
                processDefinition.addCandidateStarterUserIdExpression(this.expressionManager.createExpression(candidateUser.trim()));
            }
        }

        String candidateGroupsString = scopeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "candidateStarterGroups");
        if (candidateGroupsString != null) {
            List<String> candidateGroups = this.parseCommaSeparatedList(candidateGroupsString);
            Iterator var15 = candidateGroups.iterator();

            while (var15.hasNext()) {
                String candidateGroup = (String) var15.next();
                processDefinition.addCandidateStarterGroupIdExpression(this.expressionManager.createExpression(candidateGroup.trim()));
            }
        }

    }

    protected void parsePotentialStarterResourceAssignment(Element performerElement, ProcessDefinitionEntity processDefinition) {
        Element raeElement = performerElement.element("resourceAssignmentExpression");
        if (raeElement != null) {
            Element feElement = raeElement.element("formalExpression");
            if (feElement != null) {
                List<String> assignmentExpressions = this.parseCommaSeparatedList(feElement.getText());
                Iterator var6 = assignmentExpressions.iterator();

                while (var6.hasNext()) {
                    String assignmentExpression = (String) var6.next();
                    assignmentExpression = assignmentExpression.trim();
                    String groupAssignementId;
                    if (assignmentExpression.startsWith("user(")) {
                        groupAssignementId = this.getAssignmentId(assignmentExpression, "user(");
                        processDefinition.addCandidateStarterUserIdExpression(this.expressionManager.createExpression(groupAssignementId));
                    } else if (assignmentExpression.startsWith("group(")) {
                        groupAssignementId = this.getAssignmentId(assignmentExpression, "group(");
                        processDefinition.addCandidateStarterGroupIdExpression(this.expressionManager.createExpression(groupAssignementId));
                    } else {
                        processDefinition.addCandidateStarterGroupIdExpression(this.expressionManager.createExpression(assignmentExpression));
                    }
                }
            }
        }

    }

    protected void parseAssociations(Element scopeElement, ScopeImpl parentScope, Map<String, Element> compensationHandlers) {
        Iterator var4 = scopeElement.elements("association").iterator();

        while (true) {
            while (var4.hasNext()) {
                Element associationElement = (Element) var4.next();
                String sourceRef = associationElement.attribute("sourceRef");
                if (sourceRef == null) {
                    this.addError("association element missing attribute 'sourceRef'", associationElement);
                }

                String targetRef = associationElement.attribute("targetRef");
                if (targetRef == null) {
                    this.addError("association element missing attribute 'targetRef'", associationElement);
                }

                ActivityImpl sourceActivity = parentScope.findActivity(sourceRef);
                ActivityImpl targetActivity = parentScope.findActivity(targetRef);
                if (sourceActivity == null && !this.elementIds.contains(sourceRef)) {
                    this.addError("Invalid reference sourceRef '" + sourceRef + "' of association element ", associationElement);
                } else if (targetActivity == null && !this.elementIds.contains(targetRef)) {
                    this.addError("Invalid reference targetRef '" + targetRef + "' of association element ", associationElement);
                } else if (sourceActivity != null && "compensationBoundaryCatch".equals(sourceActivity.getProperty(BpmnProperties.TYPE.getName()))) {
                    if (targetActivity == null && compensationHandlers.containsKey(targetRef)) {
                        targetActivity = this.parseCompensationHandlerForCompensationBoundaryEvent(parentScope, sourceActivity, targetRef, compensationHandlers);
                        compensationHandlers.remove(targetActivity.getId());
                    }

                    if (targetActivity != null) {
                        this.parseAssociationOfCompensationBoundaryEvent(associationElement, sourceActivity, targetActivity);
                    }
                }
            }

            return;
        }
    }

    protected ActivityImpl parseCompensationHandlerForCompensationBoundaryEvent(ScopeImpl parentScope, ActivityImpl sourceActivity, String targetRef, Map<String, Element> compensationHandlers) {
        Element compensationHandler = (Element) compensationHandlers.get(targetRef);
        ActivityImpl eventScope = (ActivityImpl) sourceActivity.getEventScope();
        ActivityImpl compensationHandlerActivity = null;
        if (eventScope.isMultiInstance()) {
            ScopeImpl miBody = eventScope.getFlowScope();
            compensationHandlerActivity = this.parseActivity(compensationHandler, (Element) null, miBody);
        } else {
            compensationHandlerActivity = this.parseActivity(compensationHandler, (Element) null, parentScope);
        }

        compensationHandlerActivity.getProperties().set(BpmnProperties.COMPENSATION_BOUNDARY_EVENT, sourceActivity);
        return compensationHandlerActivity;
    }

    protected void parseAssociationOfCompensationBoundaryEvent(Element associationElement, ActivityImpl sourceActivity, ActivityImpl targetActivity) {
        if (!targetActivity.isCompensationHandler()) {
            this.addError("compensation boundary catch must be connected to element with isForCompensation=true", associationElement, new String[]{sourceActivity.getId(), targetActivity.getId()});
        } else {
            ActivityImpl compensatedActivity = (ActivityImpl) sourceActivity.getEventScope();
            ActivityImpl compensationHandler = compensatedActivity.findCompensationHandler();
            if (compensationHandler != null && compensationHandler.isSubProcessScope()) {
                this.addError("compensation boundary event and event subprocess with compensation start event are not supported on the same scope", associationElement, new String[]{compensatedActivity.getId(), sourceActivity.getId()});
            } else {
                compensatedActivity.setProperty("compensationHandler", targetActivity.getId());
            }
        }

    }

    protected void parseCompensationHandlers(ScopeImpl parentScope, Map<String, Element> compensationHandlers) {
        Iterator var3 = (new HashSet(compensationHandlers.values())).iterator();

        while (var3.hasNext()) {
            Element compensationHandler = (Element) var3.next();
            this.parseActivity(compensationHandler, (Element) null, parentScope);
        }

        compensationHandlers.clear();
    }

    public void parseStartEvents(Element parentElement, ScopeImpl scope) {
        List<Element> startEventElements = parentElement.elements("startEvent");
        List<ActivityImpl> startEventActivities = new ArrayList();
        Iterator var5;
        Element startEventElement;
        ActivityImpl startEventActivity;
        if (startEventElements.size() > 0) {
            var5 = startEventElements.iterator();

            while (var5.hasNext()) {
                startEventElement = (Element) var5.next();
                startEventActivity = this.createActivityOnScope(startEventElement, scope);
                this.parseAsynchronousContinuationForActivity(startEventElement, startEventActivity);
                if (scope instanceof ProcessDefinitionEntity) {
                    this.parseProcessDefinitionStartEvent(startEventActivity, startEventElement, parentElement, scope);
                    startEventActivities.add(startEventActivity);
                } else {
                    this.parseScopeStartEvent(startEventActivity, startEventElement, parentElement, (ActivityImpl) scope);
                }

                this.ensureNoIoMappingDefined(startEventElement);
                this.parseExecutionListenersOnScope(startEventElement, startEventActivity);
            }
        } else if (Arrays.asList("process", "subProcess").contains(parentElement.getTagName())) {
            this.addError(parentElement.getTagName() + " must define a startEvent element", parentElement);
        }

        if (scope instanceof ProcessDefinitionEntity) {
            this.selectInitial(startEventActivities, (ProcessDefinitionEntity) scope, parentElement);
            this.parseStartFormHandlers(startEventElements, (ProcessDefinitionEntity) scope);
        }

        var5 = startEventElements.iterator();

        while (var5.hasNext()) {
            startEventElement = (Element) var5.next();
            startEventActivity = scope.getChildActivity(startEventElement.attribute("id"));
            Iterator var8 = this.parseListeners.iterator();

            while (var8.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var8.next();
                parseListener.parseStartEvent(startEventElement, scope, startEventActivity);
            }
        }

    }

    protected void selectInitial(List<ActivityImpl> startEventActivities, ProcessDefinitionEntity processDefinition, Element parentElement) {
        ActivityImpl initial = null;
        List<String> exclusiveStartEventTypes = Arrays.asList("startEvent", "startTimerEvent");
        Iterator var6 = startEventActivities.iterator();

        while (var6.hasNext()) {
            ActivityImpl activityImpl = (ActivityImpl) var6.next();
            if (exclusiveStartEventTypes.contains(activityImpl.getProperty(BpmnProperties.TYPE.getName()))) {
                if (initial == null) {
                    initial = activityImpl;
                } else {
                    this.addError("multiple none start events or timer start events not supported on process definition", parentElement, new String[]{activityImpl.getId()});
                }
            }
        }

        if (initial == null && startEventActivities.size() == 1) {
            initial = (ActivityImpl) startEventActivities.get(0);
        }

        processDefinition.setInitial(initial);
    }

    protected void parseProcessDefinitionStartEvent(ActivityImpl startEventActivity, Element startEventElement, Element parentElement, ScopeImpl scope) {
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) scope;
        String initiatorVariableName = startEventElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "initiator");
        if (initiatorVariableName != null) {
            processDefinition.setProperty("initiatorVariableName", initiatorVariableName);
        }

        startEventActivity.setActivityBehavior(new NoneStartEventActivityBehavior());
        Element timerEventDefinition = startEventElement.element("timerEventDefinition");
        Element messageEventDefinition = startEventElement.element("messageEventDefinition");
        Element signalEventDefinition = startEventElement.element("signalEventDefinition");
        Element conditionEventDefinition = startEventElement.element("conditionalEventDefinition");
        if (timerEventDefinition != null) {
            this.parseTimerStartEventDefinition(timerEventDefinition, startEventActivity, processDefinition);
        } else if (messageEventDefinition != null) {
            startEventActivity.getProperties().set(BpmnProperties.TYPE, "messageStartEvent");
            EventSubscriptionDeclaration messageStartEventSubscriptionDeclaration = this.parseMessageEventDefinition(messageEventDefinition, startEventElement.attribute("id"));
            messageStartEventSubscriptionDeclaration.setActivityId(startEventActivity.getId());
            messageStartEventSubscriptionDeclaration.setStartEvent(true);
            this.ensureNoExpressionInMessageStartEvent(messageEventDefinition, messageStartEventSubscriptionDeclaration, startEventElement.attribute("id"));
            this.addEventSubscriptionDeclaration(messageStartEventSubscriptionDeclaration, processDefinition, startEventElement);
        } else if (signalEventDefinition != null) {
            startEventActivity.getProperties().set(BpmnProperties.TYPE, "signalStartEvent");
            startEventActivity.setEventScope(scope);
            this.parseSignalCatchEventDefinition(signalEventDefinition, startEventActivity, true);
        } else if (conditionEventDefinition != null) {
            startEventActivity.getProperties().set(BpmnProperties.TYPE, "conditionalStartEvent");
            ConditionalEventDefinition conditionalEventDefinition = this.parseConditionalEventDefinition(conditionEventDefinition, startEventActivity);
            conditionalEventDefinition.setStartEvent(true);
            conditionalEventDefinition.setActivityId(startEventActivity.getId());
            startEventActivity.getProperties().set(BpmnProperties.CONDITIONAL_EVENT_DEFINITION, conditionalEventDefinition);
            this.addEventSubscriptionDeclaration(conditionalEventDefinition, processDefinition, startEventElement);
        }

    }

    protected void parseStartFormHandlers(List<Element> startEventElements, ProcessDefinitionEntity processDefinition) {
        if (processDefinition.getInitial() != null) {
            Iterator var3 = startEventElements.iterator();

            while (var3.hasNext()) {
                Element startEventElement = (Element) var3.next();
                if (startEventElement.attribute("id").equals(processDefinition.getInitial().getId())) {
                    String startFormHandlerClassName = startEventElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formHandlerClass");
                    Object startFormHandler;
                    if (startFormHandlerClassName != null) {
                        startFormHandler = (StartFormHandler) ReflectUtil.instantiate(startFormHandlerClassName);
                    } else {
                        startFormHandler = new DefaultStartFormHandler();
                    }

                    ((StartFormHandler) startFormHandler).parseConfiguration(startEventElement, this.deployment, processDefinition, this);
                    processDefinition.setStartFormHandler(new DelegateStartFormHandler((StartFormHandler) startFormHandler, this.deployment));
                    FormDefinition formDefinition = this.parseFormDefinition(startEventElement);
                    processDefinition.setStartFormDefinition(formDefinition);
                    processDefinition.setHasStartFormKey(formDefinition.getFormKey() != null);
                }
            }
        }

    }

    protected void parseScopeStartEvent(ActivityImpl startEventActivity, Element startEventElement, Element parentElement, ActivityImpl scopeActivity) {
        Properties scopeProperties = scopeActivity.getProperties();
        if (!scopeProperties.contains(BpmnProperties.INITIAL_ACTIVITY)) {
            scopeProperties.set(BpmnProperties.INITIAL_ACTIVITY, startEventActivity);
        } else {
            this.addError("multiple start events not supported for subprocess", parentElement, new String[]{startEventActivity.getId()});
        }

        Element errorEventDefinition = startEventElement.element("errorEventDefinition");
        Element messageEventDefinition = startEventElement.element("messageEventDefinition");
        Element signalEventDefinition = startEventElement.element("signalEventDefinition");
        Element timerEventDefinition = startEventElement.element("timerEventDefinition");
        Element compensateEventDefinition = startEventElement.element("compensateEventDefinition");
        Element escalationEventDefinitionElement = startEventElement.element("escalationEventDefinition");
        Element conditionalEventDefinitionElement = startEventElement.element("conditionalEventDefinition");
        if (scopeActivity.isTriggeredByEvent()) {
            EventSubProcessStartEventActivityBehavior behavior = new EventSubProcessStartEventActivityBehavior();
            String isInterruptingAttr = startEventElement.attribute("isInterrupting");
            boolean isInterrupting = isInterruptingAttr.equalsIgnoreCase("true");
            if (isInterrupting) {
                scopeActivity.setActivityStartBehavior(ActivityStartBehavior.INTERRUPT_EVENT_SCOPE);
            } else {
                scopeActivity.setActivityStartBehavior(ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE);
            }

            startEventActivity.setEventScope(scopeActivity.getFlowScope());
            if (errorEventDefinition != null) {
                if (!isInterrupting) {
                    this.addError("error start event of event subprocess must be interrupting", startEventElement);
                }

                this.parseErrorStartEventDefinition(errorEventDefinition, startEventActivity);
            } else {
                EventSubscriptionDeclaration eventSubscriptionDeclaration;
                if (messageEventDefinition != null) {
                    startEventActivity.getProperties().set(BpmnProperties.TYPE, "messageStartEvent");
                    eventSubscriptionDeclaration = this.parseMessageEventDefinition(messageEventDefinition, startEventActivity.getId());
                    this.parseEventDefinitionForSubprocess(eventSubscriptionDeclaration, startEventActivity, messageEventDefinition);
                } else if (signalEventDefinition != null) {
                    startEventActivity.getProperties().set(BpmnProperties.TYPE, "signalStartEvent");
                    eventSubscriptionDeclaration = this.parseSignalEventDefinition(signalEventDefinition, false, startEventActivity.getId());
                    this.parseEventDefinitionForSubprocess(eventSubscriptionDeclaration, startEventActivity, signalEventDefinition);
                } else if (timerEventDefinition != null) {
                    this.parseTimerStartEventDefinitionForEventSubprocess(timerEventDefinition, startEventActivity, isInterrupting);
                } else if (compensateEventDefinition != null) {
                    this.parseCompensationEventSubprocess(startEventActivity, startEventElement, scopeActivity, compensateEventDefinition);
                } else if (escalationEventDefinitionElement != null) {
                    startEventActivity.getProperties().set(BpmnProperties.TYPE, "escalationStartEvent");
                    EscalationEventDefinition escalationEventDefinition = this.createEscalationEventDefinitionForEscalationHandler(escalationEventDefinitionElement, scopeActivity, isInterrupting, startEventActivity.getId());
                    this.addEscalationEventDefinition(startEventActivity.getEventScope(), escalationEventDefinition, escalationEventDefinitionElement, startEventActivity.getId());
                } else if (conditionalEventDefinitionElement != null) {
                    ConditionalEventDefinition conditionalEventDef = this.parseConditionalStartEventForEventSubprocess(conditionalEventDefinitionElement, startEventActivity, isInterrupting);
                    behavior = new EventSubProcessStartConditionalEventActivityBehavior(conditionalEventDef);
                } else {
                    this.addError("start event of event subprocess must be of type 'error', 'message', 'timer', 'signal', 'compensation' or 'escalation'", startEventElement);
                }
            }

            startEventActivity.setActivityBehavior((ActivityBehavior) behavior);
        } else {
            Element conditionalEventDefinition = startEventElement.element("conditionalEventDefinition");
            if (conditionalEventDefinition != null) {
                this.addError("conditionalEventDefinition is not allowed on start event within a subprocess", conditionalEventDefinition, new String[]{startEventActivity.getId()});
            }

            if (timerEventDefinition != null) {
                this.addError("timerEventDefinition is not allowed on start event within a subprocess", timerEventDefinition, new String[]{startEventActivity.getId()});
            }

            if (escalationEventDefinitionElement != null) {
                this.addError("escalationEventDefinition is not allowed on start event within a subprocess", escalationEventDefinitionElement, new String[]{startEventActivity.getId()});
            }

            if (compensateEventDefinition != null) {
                this.addError("compensateEventDefinition is not allowed on start event within a subprocess", compensateEventDefinition, new String[]{startEventActivity.getId()});
            }

            if (errorEventDefinition != null) {
                this.addError("errorEventDefinition only allowed on start event if subprocess is an event subprocess", errorEventDefinition, new String[]{startEventActivity.getId()});
            }

            if (messageEventDefinition != null) {
                this.addError("messageEventDefinition only allowed on start event if subprocess is an event subprocess", messageEventDefinition, new String[]{startEventActivity.getId()});
            }

            if (signalEventDefinition != null) {
                this.addError("signalEventDefintion only allowed on start event if subprocess is an event subprocess", signalEventDefinition, new String[]{startEventActivity.getId()});
            }

            startEventActivity.setActivityBehavior(new NoneStartEventActivityBehavior());
        }

    }

    protected void parseCompensationEventSubprocess(ActivityImpl startEventActivity, Element startEventElement, ActivityImpl scopeActivity, Element compensateEventDefinition) {
        startEventActivity.getProperties().set(BpmnProperties.TYPE, "compensationStartEvent");
        scopeActivity.setProperty("isForCompensation", Boolean.TRUE);
        if (scopeActivity.getFlowScope() instanceof ProcessDefinitionEntity) {
            this.addError("event subprocess with compensation start event is only supported for embedded subprocess (since throwing compensation through a call activity-induced process hierarchy is not supported)", startEventElement);
        }

        ScopeImpl subprocess = scopeActivity.getFlowScope();
        ActivityImpl compensationHandler = ((ActivityImpl) subprocess).findCompensationHandler();
        if (compensationHandler == null) {
            subprocess.setProperty("compensationHandler", scopeActivity.getActivityId());
        } else if (compensationHandler.isSubProcessScope()) {
            this.addError("multiple event subprocesses with compensation start event are not supported on the same scope", startEventElement);
        } else {
            this.addError("compensation boundary event and event subprocess with compensation start event are not supported on the same scope", startEventElement);
        }

        this.validateCatchCompensateEventDefinition(compensateEventDefinition, startEventActivity.getId());
    }

    protected void parseErrorStartEventDefinition(Element errorEventDefinition, ActivityImpl startEventActivity) {
        startEventActivity.getProperties().set(BpmnProperties.TYPE, "errorStartEvent");
        String errorRef = errorEventDefinition.attribute("errorRef");
        Error error = null;
        String eventSubProcessActivity = startEventActivity.getFlowScope().getId();
        ErrorEventDefinition definition = new ErrorEventDefinition(eventSubProcessActivity);
        if (errorRef != null) {
            error = (Error) this.errors.get(errorRef);
            String errorCode = error == null ? errorRef : error.getErrorCode();
            definition.setErrorCode(errorCode);
        }

        definition.setPrecedence(10);
        this.setErrorCodeVariableOnErrorEventDefinition(errorEventDefinition, definition);
        this.setErrorMessageVariableOnErrorEventDefinition(errorEventDefinition, definition);
        this.addErrorEventDefinition(definition, startEventActivity.getEventScope());
    }

    protected void setErrorCodeVariableOnErrorEventDefinition(Element errorEventDefinition, ErrorEventDefinition definition) {
        String errorCodeVar = errorEventDefinition.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "errorCodeVariable");
        if (errorCodeVar != null) {
            definition.setErrorCodeVariable(errorCodeVar);
        }

    }

    protected void setErrorMessageVariableOnErrorEventDefinition(Element errorEventDefinition, ErrorEventDefinition definition) {
        String errorMessageVariable = errorEventDefinition.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "errorMessageVariable");
        if (errorMessageVariable != null) {
            definition.setErrorMessageVariable(errorMessageVariable);
        }

    }

    protected EventSubscriptionDeclaration parseMessageEventDefinition(Element messageEventDefinition, String messageElementId) {
        String messageRef = messageEventDefinition.attribute("messageRef");
        if (messageRef == null) {
            this.addError("attribute 'messageRef' is required", messageEventDefinition, new String[]{messageElementId});
        }

        MessageDefinition messageDefinition = (MessageDefinition) this.messages.get(this.resolveName(messageRef));
        if (messageDefinition == null) {
            this.addError("Invalid 'messageRef': no message with id '" + messageRef + "' found.", messageEventDefinition, new String[]{messageElementId});
        }

        return new EventSubscriptionDeclaration(messageDefinition.getExpression(), EventType.MESSAGE);
    }

    protected void addEventSubscriptionDeclaration(EventSubscriptionDeclaration subscription, ScopeImpl scope, Element element) {
        if (subscription.getEventType().equals(EventType.MESSAGE.name()) && !subscription.hasEventName()) {
            this.addError("Cannot have a message event subscription with an empty or missing name", element, new String[]{subscription.getActivityId()});
        }

        Map<String, EventSubscriptionDeclaration> eventDefinitions = scope.getProperties().get(BpmnProperties.EVENT_SUBSCRIPTION_DECLARATIONS);
        if (this.hasMultipleMessageEventDefinitionsWithSameName(subscription, eventDefinitions.values())) {
            this.addError("Cannot have more than one message event subscription with name '" + subscription.getUnresolvedEventName() + "' for scope '" + scope.getId() + "'", element, new String[]{subscription.getActivityId()});
        }

        if (this.hasMultipleSignalEventDefinitionsWithSameName(subscription, eventDefinitions.values())) {
            this.addError("Cannot have more than one signal event subscription with name '" + subscription.getUnresolvedEventName() + "' for scope '" + scope.getId() + "'", element, new String[]{subscription.getActivityId()});
        }

        if (subscription.isStartEvent() && this.hasMultipleConditionalEventDefinitionsWithSameCondition(subscription, eventDefinitions.values())) {
            this.addError("Cannot have more than one conditional event subscription with the same condition '" + ((ConditionalEventDefinition) subscription).getConditionAsString() + "'", element, new String[]{subscription.getActivityId()});
        }

        scope.getProperties().putMapEntry(BpmnProperties.EVENT_SUBSCRIPTION_DECLARATIONS, subscription.getActivityId(), subscription);
    }

    protected boolean hasMultipleMessageEventDefinitionsWithSameName(EventSubscriptionDeclaration subscription, Collection<EventSubscriptionDeclaration> eventDefinitions) {
        return this.hasMultipleEventDefinitionsWithSameName(subscription, eventDefinitions, EventType.MESSAGE.name());
    }

    protected boolean hasMultipleSignalEventDefinitionsWithSameName(EventSubscriptionDeclaration subscription, Collection<EventSubscriptionDeclaration> eventDefinitions) {
        return this.hasMultipleEventDefinitionsWithSameName(subscription, eventDefinitions, EventType.SIGNAL.name());
    }

    protected boolean hasMultipleConditionalEventDefinitionsWithSameCondition(EventSubscriptionDeclaration subscription, Collection<EventSubscriptionDeclaration> eventDefinitions) {
        if (subscription.getEventType().equals(EventType.CONDITONAL.name())) {
            Iterator var3 = eventDefinitions.iterator();

            while (var3.hasNext()) {
                EventSubscriptionDeclaration eventDefinition = (EventSubscriptionDeclaration) var3.next();
                if (eventDefinition.getEventType().equals(EventType.CONDITONAL.name()) && eventDefinition.isStartEvent() == subscription.isStartEvent() && ((ConditionalEventDefinition) eventDefinition).getConditionAsString().equals(((ConditionalEventDefinition) subscription).getConditionAsString())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasMultipleEventDefinitionsWithSameName(EventSubscriptionDeclaration subscription, Collection<EventSubscriptionDeclaration> eventDefinitions, String eventType) {
        if (subscription.getEventType().equals(eventType)) {
            Iterator var4 = eventDefinitions.iterator();

            while (var4.hasNext()) {
                EventSubscriptionDeclaration eventDefinition = (EventSubscriptionDeclaration) var4.next();
                if (eventDefinition.getEventType().equals(eventType) && eventDefinition.getUnresolvedEventName().equals(subscription.getUnresolvedEventName()) && eventDefinition.isStartEvent() == subscription.isStartEvent()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void addEventSubscriptionJobDeclaration(EventSubscriptionJobDeclaration jobDeclaration, ActivityImpl activity, Element element) {
        List<EventSubscriptionJobDeclaration> jobDeclarationsForActivity = (List) activity.getProperty("eventJobDeclarations");
        if (jobDeclarationsForActivity == null) {
            jobDeclarationsForActivity = new ArrayList();
            activity.setProperty("eventJobDeclarations", jobDeclarationsForActivity);
        }

        if (this.activityAlreadyContainsJobDeclarationEventType((List) jobDeclarationsForActivity, jobDeclaration)) {
            this.addError("Activity contains already job declaration with type " + jobDeclaration.getEventType(), element, new String[]{activity.getId()});
        }

        ((List) jobDeclarationsForActivity).add(jobDeclaration);
    }

    protected boolean activityAlreadyContainsJobDeclarationEventType(List<EventSubscriptionJobDeclaration> jobDeclarationsForActivity, EventSubscriptionJobDeclaration jobDeclaration) {
        Iterator var3 = jobDeclarationsForActivity.iterator();

        EventSubscriptionJobDeclaration declaration;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            declaration = (EventSubscriptionJobDeclaration) var3.next();
        } while (!declaration.getEventType().equals(jobDeclaration.getEventType()));

        return true;
    }

    public void parseActivities(List<Element> activityElements, Element parentElement, ScopeImpl scopeElement) {
        Iterator var4 = activityElements.iterator();

        while (var4.hasNext()) {
            Element activityElement = (Element) var4.next();
            this.parseActivity(activityElement, parentElement, scopeElement);
        }

    }

    protected ActivityImpl parseActivity(Element activityElement, Element parentElement, ScopeImpl scopeElement) {
        ActivityImpl activity = null;
        boolean isMultiInstance = false;
        ScopeImpl miBody = this.parseMultiInstanceLoopCharacteristics(activityElement, scopeElement);
        if (miBody != null) {
            scopeElement = miBody;
            isMultiInstance = true;
        }

        if (activityElement.getTagName().equals("exclusiveGateway")) {
            activity = this.parseExclusiveGateway(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("inclusiveGateway")) {
            activity = this.parseInclusiveGateway(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("parallelGateway")) {
            activity = this.parseParallelGateway(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("scriptTask")) {
            activity = this.parseScriptTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("serviceTask")) {
            activity = this.parseServiceTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("businessRuleTask")) {
            activity = this.parseBusinessRuleTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("task")) {
            activity = this.parseTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("manualTask")) {
            activity = this.parseManualTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("userTask")) {
            activity = this.parseUserTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("sendTask")) {
            activity = this.parseSendTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("receiveTask")) {
            activity = this.parseReceiveTask(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("subProcess")) {
            activity = this.parseSubProcess(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("callActivity")) {
            activity = this.parseCallActivity(activityElement, scopeElement, isMultiInstance);
        } else if (activityElement.getTagName().equals("intermediateThrowEvent")) {
            activity = this.parseIntermediateThrowEvent(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("eventBasedGateway")) {
            activity = this.parseEventBasedGateway(activityElement, parentElement, scopeElement);
        } else if (activityElement.getTagName().equals("transaction")) {
            activity = this.parseTransaction(activityElement, scopeElement);
        } else if (activityElement.getTagName().equals("adHocSubProcess") || activityElement.getTagName().equals("complexGateway")) {
            this.addWarning("Ignoring unsupported activity type", activityElement);
        }

        if (isMultiInstance) {
            activity.setProperty("isMultiInstance", true);
        }

        if (activity != null) {
            activity.setName(activityElement.attribute("name"));
            this.parseActivityInputOutput(activityElement, activity);
        }

        return activity;
    }

    public void validateActivities(List<ActivityImpl> activities) {
        Iterator var2 = activities.iterator();

        while (var2.hasNext()) {
            ActivityImpl activity = (ActivityImpl) var2.next();
            this.validateActivity(activity);
            if (activity.getActivities().size() > 0) {
                this.validateActivities(activity.getActivities());
            }
        }

    }

    protected void validateActivity(ActivityImpl activity) {
        if (activity.getActivityBehavior() instanceof ExclusiveGatewayActivityBehavior) {
            this.validateExclusiveGateway(activity);
        }

        this.validateOutgoingFlows(activity);
    }

    protected void validateOutgoingFlows(ActivityImpl activity) {
        if (activity.isAsyncAfter()) {
            Iterator var2 = activity.getOutgoingTransitions().iterator();

            while (var2.hasNext()) {
                PvmTransition transition = (PvmTransition) var2.next();
                if (transition.getId() == null) {
                    this.addError("Sequence flow with sourceRef='" + activity.getId() + "' must have an id, activity with id '" + activity.getId() + "' uses 'asyncAfter'.", (Element) null, new String[]{activity.getId()});
                }
            }
        }

    }

    public void validateExclusiveGateway(ActivityImpl activity) {
        if (activity.getOutgoingTransitions().size() == 0) {
            this.addError("Exclusive Gateway '" + activity.getId() + "' has no outgoing sequence flows.", (Element) null, new String[]{activity.getId()});
        } else if (activity.getOutgoingTransitions().size() == 1) {
            PvmTransition flow = (PvmTransition) activity.getOutgoingTransitions().get(0);
            Condition condition = (Condition) flow.getProperty("condition");
            if (condition != null) {
                this.addError("Exclusive Gateway '" + activity.getId() + "' has only one outgoing sequence flow ('" + flow.getId() + "'). This is not allowed to have a condition.", (Element) null, new String[]{activity.getId(), flow.getId()});
            }
        } else {
            String defaultSequenceFlow = (String) activity.getProperty("default");
            boolean hasDefaultFlow = defaultSequenceFlow != null && defaultSequenceFlow.length() > 0;
            ArrayList<PvmTransition> flowsWithoutCondition = new ArrayList();
            Iterator var5 = activity.getOutgoingTransitions().iterator();

            PvmTransition flow;
            while (var5.hasNext()) {
                flow = (PvmTransition) var5.next();
                Condition condition = (Condition) flow.getProperty("condition");
                boolean isDefaultFlow = flow.getId() != null && flow.getId().equals(defaultSequenceFlow);
                boolean hasConditon = condition != null;
                if (!hasConditon && !isDefaultFlow) {
                    flowsWithoutCondition.add(flow);
                }

                if (hasConditon && isDefaultFlow) {
                    this.addError("Exclusive Gateway '" + activity.getId() + "' has outgoing sequence flow '" + flow.getId() + "' which is the default flow but has a condition too.", (Element) null, new String[]{activity.getId(), flow.getId()});
                }
            }

            if (!hasDefaultFlow && flowsWithoutCondition.size() <= 1) {
                if (flowsWithoutCondition.size() == 1) {
                    flow = (PvmTransition) flowsWithoutCondition.get(0);
                    this.addWarning("Exclusive Gateway '" + activity.getId() + "' has outgoing sequence flow '" + flow.getId() + "' without condition which is not the default flow. We assume it to be the default flow, but it is bad modeling practice, better set the default flow in your gateway.", (Element) null, new String[]{activity.getId(), flow.getId()});
                }
            } else {
                var5 = flowsWithoutCondition.iterator();

                while (var5.hasNext()) {
                    flow = (PvmTransition) var5.next();
                    this.addError("Exclusive Gateway '" + activity.getId() + "' has outgoing sequence flow '" + flow.getId() + "' without condition which is not the default flow.", (Element) null, new String[]{activity.getId(), flow.getId()});
                }
            }
        }

    }

    public ActivityImpl parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scopeElement, ActivityImpl eventBasedGateway) {
        ActivityImpl nestedActivity = this.createActivityOnScope(intermediateEventElement, scopeElement);
        Element timerEventDefinition = intermediateEventElement.element("timerEventDefinition");
        Element signalEventDefinition = intermediateEventElement.element("signalEventDefinition");
        Element messageEventDefinition = intermediateEventElement.element("messageEventDefinition");
        Element linkEventDefinitionElement = intermediateEventElement.element("linkEventDefinition");
        Element conditionalEventDefinitionElement = intermediateEventElement.element("conditionalEventDefinition");
        IntermediateCatchEventActivityBehavior defaultCatchBehaviour = new IntermediateCatchEventActivityBehavior(eventBasedGateway != null);
        this.parseAsynchronousContinuationForActivity(intermediateEventElement, nestedActivity);
        boolean isEventBaseGatewayPresent = eventBasedGateway != null;
        if (isEventBaseGatewayPresent) {
            nestedActivity.setEventScope(eventBasedGateway);
            nestedActivity.setActivityStartBehavior(ActivityStartBehavior.CANCEL_EVENT_SCOPE);
        } else {
            nestedActivity.setEventScope(nestedActivity);
            nestedActivity.setScope(true);
        }

        nestedActivity.setActivityBehavior(defaultCatchBehaviour);
        if (timerEventDefinition != null) {
            this.parseIntermediateTimerEventDefinition(timerEventDefinition, nestedActivity);
        } else if (signalEventDefinition != null) {
            this.parseIntermediateSignalEventDefinition(signalEventDefinition, nestedActivity);
        } else if (messageEventDefinition != null) {
            this.parseIntermediateMessageEventDefinition(messageEventDefinition, nestedActivity);
        } else if (linkEventDefinitionElement != null) {
            if (isEventBaseGatewayPresent) {
                this.addError("IntermediateCatchLinkEvent is not allowed after an EventBasedGateway.", intermediateEventElement);
            }

            nestedActivity.setActivityBehavior(new IntermediateCatchLinkEventActivityBehavior());
            this.parseIntermediateLinkEventCatchBehavior(intermediateEventElement, nestedActivity, linkEventDefinitionElement);
        } else if (conditionalEventDefinitionElement != null) {
            ConditionalEventDefinition conditionalEvent = this.parseIntermediateConditionalEventDefinition(conditionalEventDefinitionElement, nestedActivity);
            nestedActivity.setActivityBehavior(new IntermediateConditionalEventBehavior(conditionalEvent, isEventBaseGatewayPresent));
        } else {
            this.addError("Unsupported intermediate catch event type", intermediateEventElement);
        }

        this.parseExecutionListenersOnScope(intermediateEventElement, nestedActivity);
        Iterator var14 = this.parseListeners.iterator();

        while (var14.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var14.next();
            parseListener.parseIntermediateCatchEvent(intermediateEventElement, scopeElement, nestedActivity);
        }

        return nestedActivity;
    }

    protected void parseIntermediateLinkEventCatchBehavior(Element intermediateEventElement, ActivityImpl activity, Element linkEventDefinitionElement) {
        activity.getProperties().set(BpmnProperties.TYPE, "intermediateLinkCatch");
        String linkName = linkEventDefinitionElement.attribute("name");
        String elementName = intermediateEventElement.attribute("name");
        String elementId = intermediateEventElement.attribute("id");
        if (this.eventLinkTargets.containsKey(linkName)) {
            this.addError("Multiple Intermediate Catch Events with the same link event name ('" + linkName + "') are not allowed.", intermediateEventElement);
        } else {
            if (!linkName.equals(elementName)) {
                this.addWarning("Link Event named '" + elementName + "' contains link event definition with name '" + linkName + "' - it is recommended to use the same name for both.", intermediateEventElement);
            }

            this.eventLinkTargets.put(linkName, elementId);
        }

    }

    protected void parseIntermediateMessageEventDefinition(Element messageEventDefinition, ActivityImpl nestedActivity) {
        nestedActivity.getProperties().set(BpmnProperties.TYPE, "intermediateMessageCatch");
        EventSubscriptionDeclaration messageDefinition = this.parseMessageEventDefinition(messageEventDefinition, nestedActivity.getId());
        messageDefinition.setActivityId(nestedActivity.getId());
        this.addEventSubscriptionDeclaration(messageDefinition, nestedActivity.getEventScope(), messageEventDefinition);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseIntermediateMessageCatchEventDefinition(messageEventDefinition, nestedActivity);
        }

    }

    public ActivityImpl parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scopeElement) {
        Element signalEventDefinitionElement = intermediateEventElement.element("signalEventDefinition");
        Element compensateEventDefinitionElement = intermediateEventElement.element("compensateEventDefinition");
        Element linkEventDefinitionElement = intermediateEventElement.element("linkEventDefinition");
        Element messageEventDefinitionElement = intermediateEventElement.element("messageEventDefinition");
        Element escalationEventDefinition = intermediateEventElement.element("escalationEventDefinition");
        String elementId = intermediateEventElement.attribute("id");
        if (linkEventDefinitionElement != null) {
            String linkName = linkEventDefinitionElement.attribute("name");
            this.eventLinkSources.put(elementId, linkName);
            return null;
        } else {
            ActivityImpl nestedActivityImpl = this.createActivityOnScope(intermediateEventElement, scopeElement);
            ActivityBehavior activityBehavior = null;
            this.parseAsynchronousContinuationForActivity(intermediateEventElement, nestedActivityImpl);
            boolean isServiceTaskLike = this.isServiceTaskLike(messageEventDefinitionElement);
            if (signalEventDefinitionElement != null) {
                nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateSignalThrow");
                EventSubscriptionDeclaration signalDefinition = this.parseSignalEventDefinition(signalEventDefinitionElement, true, nestedActivityImpl.getId());
                activityBehavior = new ThrowSignalEventActivityBehavior(signalDefinition);
            } else if (compensateEventDefinitionElement != null) {
                nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateCompensationThrowEvent");
                CompensateEventDefinition compensateEventDefinition = this.parseThrowCompensateEventDefinition(compensateEventDefinitionElement, scopeElement, elementId);
                activityBehavior = new CompensationEventActivityBehavior(compensateEventDefinition);
                nestedActivityImpl.setProperty("throwsCompensation", true);
                nestedActivityImpl.setScope(true);
            } else if (messageEventDefinitionElement != null) {
                if (isServiceTaskLike) {
                    nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateMessageThrowEvent");
                    this.parseServiceTaskLike(nestedActivityImpl, "intermediateMessageThrowEvent", messageEventDefinitionElement, intermediateEventElement, scopeElement);
                } else {
                    nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateNoneThrowEvent");
                    activityBehavior = new IntermediateThrowNoneEventActivityBehavior();
                }
            } else if (escalationEventDefinition != null) {
                nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateEscalationThrowEvent");
                Escalation escalation = this.findEscalationForEscalationEventDefinition(escalationEventDefinition, nestedActivityImpl.getId());
                if (escalation != null && escalation.getEscalationCode() == null) {
                    this.addError("throwing escalation event must have an 'escalationCode'", escalationEventDefinition, new String[]{nestedActivityImpl.getId()});
                }

                activityBehavior = new ThrowEscalationEventActivityBehavior(escalation);
            } else {
                nestedActivityImpl.getProperties().set(BpmnProperties.TYPE, "intermediateNoneThrowEvent");
                activityBehavior = new IntermediateThrowNoneEventActivityBehavior();
            }

            if (activityBehavior != null) {
                nestedActivityImpl.setActivityBehavior((ActivityBehavior) activityBehavior);
            }

            this.parseExecutionListenersOnScope(intermediateEventElement, nestedActivityImpl);
            Iterator var17 = this.parseListeners.iterator();

            while (var17.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var17.next();
                parseListener.parseIntermediateThrowEvent(intermediateEventElement, scopeElement, nestedActivityImpl);
            }

            if (isServiceTaskLike) {
                this.validateServiceTaskLike(nestedActivityImpl, "intermediateMessageThrowEvent", messageEventDefinitionElement);
            }

            return nestedActivityImpl;
        }
    }

    protected CompensateEventDefinition parseThrowCompensateEventDefinition(final Element compensateEventDefinitionElement, ScopeImpl scopeElement, final String parentElementId) {
        final String activityRef = compensateEventDefinitionElement.attribute("activityRef");
        boolean waitForCompletion = "true".equals(compensateEventDefinitionElement.attribute("waitForCompletion", "true"));
        if (activityRef != null && scopeElement.findActivityAtLevelOfSubprocess(activityRef) == null) {
            Boolean isTriggeredByEvent = (Boolean) scopeElement.getProperties().get(BpmnProperties.TRIGGERED_BY_EVENT);
            String type = (String) scopeElement.getProperty(PROPERTYNAME_TYPE);
            if (Boolean.TRUE == isTriggeredByEvent && "subProcess".equals(type)) {
                scopeElement = scopeElement.getFlowScope();
            }

            if (scopeElement.findActivityAtLevelOfSubprocess(activityRef) == null) {
                final String scopeId = scopeElement.getId();
                scopeElement.addToBacklog(activityRef, new ScopeImpl.BacklogErrorCallback() {
                    public void callback() {
                        BpmnParse.this.addError("Invalid attribute value for 'activityRef': no activity with id '" + activityRef + "' in scope '" + scopeId + "'", compensateEventDefinitionElement, new String[]{parentElementId});
                    }
                });
            }
        }

        CompensateEventDefinition compensateEventDefinition = new CompensateEventDefinition();
        compensateEventDefinition.setActivityRef(activityRef);
        compensateEventDefinition.setWaitForCompletion(waitForCompletion);
        if (!waitForCompletion) {
            this.addWarning("Unsupported attribute value for 'waitForCompletion': 'waitForCompletion=false' is not supported. Compensation event will wait for compensation to join.", compensateEventDefinitionElement, new String[]{parentElementId});
        }

        return compensateEventDefinition;
    }

    protected void validateCatchCompensateEventDefinition(Element compensateEventDefinitionElement, String parentElementId) {
        String activityRef = compensateEventDefinitionElement.attribute("activityRef");
        if (activityRef != null) {
            this.addWarning("attribute 'activityRef' is not supported on catching compensation event. attribute will be ignored", compensateEventDefinitionElement, new String[]{parentElementId});
        }

        String waitForCompletion = compensateEventDefinitionElement.attribute("waitForCompletion");
        if (waitForCompletion != null) {
            this.addWarning("attribute 'waitForCompletion' is not supported on catching compensation event. attribute will be ignored", compensateEventDefinitionElement, new String[]{parentElementId});
        }

    }

    protected void parseBoundaryCompensateEventDefinition(Element compensateEventDefinition, ActivityImpl activity) {
        activity.getProperties().set(BpmnProperties.TYPE, "compensationBoundaryCatch");
        ScopeImpl hostActivity = activity.getEventScope();
        Iterator var4 = activity.getFlowScope().getActivities().iterator();

        while (var4.hasNext()) {
            ActivityImpl sibling = (ActivityImpl) var4.next();
            if (sibling.getProperty(BpmnProperties.TYPE.getName()).equals("compensationBoundaryCatch") && sibling.getEventScope().equals(hostActivity) && sibling != activity) {
                this.addError("multiple boundary events with compensateEventDefinition not supported on same activity", compensateEventDefinition, new String[]{activity.getId()});
            }
        }

        this.validateCatchCompensateEventDefinition(compensateEventDefinition, activity.getId());
    }

    protected ActivityBehavior parseBoundaryCancelEventDefinition(Element cancelEventDefinition, ActivityImpl activity) {
        activity.getProperties().set(BpmnProperties.TYPE, "cancelBoundaryCatch");
        LegacyBehavior.parseCancelBoundaryEvent(activity);
        ActivityImpl transaction = (ActivityImpl) activity.getEventScope();
        if (transaction.getActivityBehavior() != null && transaction.getActivityBehavior() instanceof MultiInstanceActivityBehavior) {
            transaction = (ActivityImpl) transaction.getActivities().get(0);
        }

        if (!"transaction".equals(transaction.getProperty(BpmnProperties.TYPE.getName()))) {
            this.addError("boundary event with cancelEventDefinition only supported on transaction subprocesses", cancelEventDefinition, new String[]{activity.getId()});
        }

        Iterator var4 = activity.getFlowScope().getActivities().iterator();

        ActivityImpl childActivity;
        while (var4.hasNext()) {
            childActivity = (ActivityImpl) var4.next();
            if ("cancelBoundaryCatch".equals(childActivity.getProperty(BpmnProperties.TYPE.getName())) && childActivity != activity && childActivity.getEventScope() == transaction) {
                this.addError("multiple boundary events with cancelEventDefinition not supported on same transaction subprocess", cancelEventDefinition, new String[]{activity.getId()});
            }
        }

        var4 = transaction.getActivities().iterator();

        while (var4.hasNext()) {
            childActivity = (ActivityImpl) var4.next();
            ActivityBehavior activityBehavior = childActivity.getActivityBehavior();
            if (activityBehavior != null && activityBehavior instanceof CancelEndEventActivityBehavior) {
                ((CancelEndEventActivityBehavior) activityBehavior).setCancelBoundaryEvent(activity);
            }
        }

        return new CancelBoundaryEventActivityBehavior();
    }

    public ScopeImpl parseMultiInstanceLoopCharacteristics(Element activityElement, ScopeImpl scope) {
        Element miLoopCharacteristics = activityElement.element("multiInstanceLoopCharacteristics");
        if (miLoopCharacteristics == null) {
            return null;
        } else {
            String id = activityElement.attribute("id");
            LOG.parsingElement("mi body for activity", id);
            id = getIdForMiBody(id);
            ActivityImpl miBodyScope = scope.createActivity(id);
            this.setActivityAsyncDelegates(miBodyScope);
            miBodyScope.setProperty(PROPERTYNAME_TYPE, "multiInstanceBody");
            miBodyScope.setScope(true);
            boolean isSequential = this.parseBooleanAttribute(miLoopCharacteristics.attribute("isSequential"), false);
            MultiInstanceActivityBehavior behavior = null;
            if (isSequential) {
                behavior = new SequentialMultiInstanceActivityBehavior();
            } else {
                behavior = new ParallelMultiInstanceActivityBehavior();
            }

            miBodyScope.setActivityBehavior((ActivityBehavior) behavior);
            Element loopCardinality = miLoopCharacteristics.element("loopCardinality");
            if (loopCardinality != null) {
                String loopCardinalityText = loopCardinality.getText();
                if (loopCardinalityText == null || "".equals(loopCardinalityText)) {
                    this.addError("loopCardinality must be defined for a multiInstanceLoopCharacteristics definition ", miLoopCharacteristics, new String[]{id});
                }

                ((MultiInstanceActivityBehavior) behavior).setLoopCardinalityExpression(this.expressionManager.createExpression(loopCardinalityText));
            }

            Element completionCondition = miLoopCharacteristics.element("completionCondition");
            String collection;
            if (completionCondition != null) {
                collection = completionCondition.getText();
                ((MultiInstanceActivityBehavior) behavior).setCompletionConditionExpression(this.expressionManager.createExpression(collection));
            }

            collection = miLoopCharacteristics.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "collection");
            if (collection != null) {
                if (collection.contains("{")) {
                    ((MultiInstanceActivityBehavior) behavior).setCollectionExpression(this.expressionManager.createExpression(collection));
                } else {
                    ((MultiInstanceActivityBehavior) behavior).setCollectionVariable(collection);
                }
            }

            Element loopDataInputRef = miLoopCharacteristics.element("loopDataInputRef");
            String loopDataInputRefText;
            if (loopDataInputRef != null) {
                loopDataInputRefText = loopDataInputRef.getText();
                if (loopDataInputRefText != null) {
                    if (loopDataInputRefText.contains("{")) {
                        ((MultiInstanceActivityBehavior) behavior).setCollectionExpression(this.expressionManager.createExpression(loopDataInputRefText));
                    } else {
                        ((MultiInstanceActivityBehavior) behavior).setCollectionVariable(loopDataInputRefText);
                    }
                }
            }

            loopDataInputRefText = miLoopCharacteristics.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "elementVariable");
            if (loopDataInputRefText != null) {
                ((MultiInstanceActivityBehavior) behavior).setCollectionElementVariable(loopDataInputRefText);
            }

            Element inputDataItem = miLoopCharacteristics.element("inputDataItem");
            if (inputDataItem != null) {
                String inputDataItemName = inputDataItem.attribute("name");
                ((MultiInstanceActivityBehavior) behavior).setCollectionElementVariable(inputDataItemName);
            }

            if (((MultiInstanceActivityBehavior) behavior).getLoopCardinalityExpression() == null && ((MultiInstanceActivityBehavior) behavior).getCollectionExpression() == null && ((MultiInstanceActivityBehavior) behavior).getCollectionVariable() == null) {
                this.addError("Either loopCardinality or loopDataInputRef/activiti:collection must been set", miLoopCharacteristics, new String[]{id});
            }

            if (((MultiInstanceActivityBehavior) behavior).getCollectionExpression() == null && ((MultiInstanceActivityBehavior) behavior).getCollectionVariable() == null && ((MultiInstanceActivityBehavior) behavior).getCollectionElementVariable() != null) {
                this.addError("LoopDataInputRef/activiti:collection must be set when using inputDataItem or activiti:elementVariable", miLoopCharacteristics, new String[]{id});
            }

            Iterator var17 = this.parseListeners.iterator();

            while (var17.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var17.next();
                parseListener.parseMultiInstanceLoopCharacteristics(activityElement, miLoopCharacteristics, miBodyScope);
            }

            return miBodyScope;
        }
    }

    public static String getIdForMiBody(String id) {
        return id + "#multiInstanceBody";
    }

    public ActivityImpl createActivityOnScope(Element activityElement, ScopeImpl scopeElement) {
        String id = activityElement.attribute("id");
        LOG.parsingElement("activity", id);
        ActivityImpl activity = scopeElement.createActivity(id);
        activity.setProperty("name", activityElement.attribute("name"));
        activity.setProperty("documentation", this.parseDocumentation(activityElement));
        activity.setProperty("default", activityElement.attribute("default"));
        activity.getProperties().set(BpmnProperties.TYPE, activityElement.getTagName());
        activity.setProperty("line", activityElement.getLine());
        this.setActivityAsyncDelegates(activity);
        activity.setProperty("jobPriority", this.parsePriority(activityElement, "jobPriority"));
        if (this.isCompensationHandler(activityElement)) {
            activity.setProperty("isForCompensation", true);
        }

        return activity;
    }

    protected void setActivityAsyncDelegates(final ActivityImpl activity) {
        activity.setDelegateAsyncAfterUpdate(new ActivityImpl.AsyncAfterUpdate() {
            public void updateAsyncAfter(boolean asyncAfter, boolean exclusive) {
                if (asyncAfter) {
                    BpmnParse.this.addMessageJobDeclaration(new AsyncAfterMessageJobDeclaration(), activity, exclusive);
                } else {
                    BpmnParse.this.removeMessageJobDeclarationWithJobConfiguration(activity, "async-after");
                }

            }
        });
        activity.setDelegateAsyncBeforeUpdate(new ActivityImpl.AsyncBeforeUpdate() {
            public void updateAsyncBefore(boolean asyncBefore, boolean exclusive) {
                if (asyncBefore) {
                    BpmnParse.this.addMessageJobDeclaration(new AsyncBeforeMessageJobDeclaration(), activity, exclusive);
                } else {
                    BpmnParse.this.removeMessageJobDeclarationWithJobConfiguration(activity, "async-before");
                }

            }
        });
    }

    protected void addMessageJobDeclaration(MessageJobDeclaration messageJobDeclaration, ActivityImpl activity, boolean exclusive) {
        ProcessDefinition procDef = (ProcessDefinition) activity.getProcessDefinition();
        if (!this.exists(messageJobDeclaration, procDef.getKey(), activity.getActivityId())) {
            messageJobDeclaration.setExclusive(exclusive);
            messageJobDeclaration.setActivity(activity);
            messageJobDeclaration.setJobPriorityProvider((ParameterValueProvider) activity.getProperty("jobPriority"));
            this.addMessageJobDeclarationToActivity(messageJobDeclaration, activity);
            this.addJobDeclarationToProcessDefinition(messageJobDeclaration, procDef);
        }

    }

    protected boolean exists(MessageJobDeclaration msgJobdecl, String procDefKey, String activityId) {
        boolean exist = false;
        List<JobDeclaration<?, ?>> declarations = (List) this.jobDeclarations.get(procDefKey);
        if (declarations != null) {
            for (int i = 0; i < declarations.size() && !exist; ++i) {
                JobDeclaration<?, ?> decl = (JobDeclaration) declarations.get(i);
                if (decl.getActivityId().equals(activityId) && decl.getJobConfiguration().equalsIgnoreCase(msgJobdecl.getJobConfiguration())) {
                    exist = true;
                }
            }
        }

        return exist;
    }

    protected void removeMessageJobDeclarationWithJobConfiguration(ActivityImpl activity, String jobConfiguration) {
        List<MessageJobDeclaration> messageJobDeclarations = (List) activity.getProperty("messageJobDeclaration");
        if (messageJobDeclarations != null) {
            Iterator<MessageJobDeclaration> iter = messageJobDeclarations.iterator();

            while (iter.hasNext()) {
                MessageJobDeclaration msgDecl = (MessageJobDeclaration) iter.next();
                if (msgDecl.getJobConfiguration().equalsIgnoreCase(jobConfiguration) && msgDecl.getActivityId().equalsIgnoreCase(activity.getActivityId())) {
                    iter.remove();
                }
            }
        }

        ProcessDefinition procDef = (ProcessDefinition) activity.getProcessDefinition();
        List<JobDeclaration<?, ?>> declarations = (List) this.jobDeclarations.get(procDef.getKey());
        if (declarations != null) {
            Iterator<JobDeclaration<?, ?>> iter = declarations.iterator();

            while (iter.hasNext()) {
                JobDeclaration<?, ?> jobDcl = (JobDeclaration) iter.next();
                if (jobDcl.getJobConfiguration().equalsIgnoreCase(jobConfiguration) && jobDcl.getActivityId().equalsIgnoreCase(activity.getActivityId())) {
                    iter.remove();
                }
            }
        }

    }

    public String parseDocumentation(Element element) {
        List<Element> docElements = element.elements("documentation");
        List<String> docStrings = new ArrayList();
        Iterator var4 = docElements.iterator();

        while (var4.hasNext()) {
            Element e = (Element) var4.next();
            docStrings.add(e.getText());
        }

        return parseDocumentation((List) docStrings);
    }

    public static String parseDocumentation(List<String> docStrings) {
        if (docStrings.isEmpty()) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();

            String e;
            for (Iterator var2 = docStrings.iterator(); var2.hasNext(); builder.append(e.trim())) {
                e = (String) var2.next();
                if (builder.length() != 0) {
                    builder.append("\n\n");
                }
            }

            return builder.toString();
        }
    }

    protected boolean isCompensationHandler(Element activityElement) {
        String isForCompensation = activityElement.attribute("isForCompensation");
        return isForCompensation != null && isForCompensation.equalsIgnoreCase("true");
    }

    public ActivityImpl parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(exclusiveGwElement, scope);
        activity.setActivityBehavior(new ExclusiveGatewayActivityBehavior());
        this.parseAsynchronousContinuationForActivity(exclusiveGwElement, activity);
        this.parseExecutionListenersOnScope(exclusiveGwElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseExclusiveGateway(exclusiveGwElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(inclusiveGwElement, scope);
        activity.setActivityBehavior(new InclusiveGatewayActivityBehavior());
        this.parseAsynchronousContinuationForActivity(inclusiveGwElement, activity);
        this.parseExecutionListenersOnScope(inclusiveGwElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseInclusiveGateway(inclusiveGwElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseEventBasedGateway(Element eventBasedGwElement, Element parentElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(eventBasedGwElement, scope);
        activity.setActivityBehavior(new EventBasedGatewayActivityBehavior());
        activity.setScope(true);
        this.parseAsynchronousContinuationForActivity(eventBasedGwElement, activity);
        if (activity.isAsyncAfter()) {
            this.addError("'asyncAfter' not supported for " + eventBasedGwElement.getTagName() + " elements.", eventBasedGwElement);
        }

        this.parseExecutionListenersOnScope(eventBasedGwElement, activity);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseEventBasedGateway(eventBasedGwElement, scope, activity);
        }

        List<Element> sequenceFlows = parentElement.elements("sequenceFlow");
        Map<String, Element> siblingsMap = new HashMap();
        List<Element> siblings = parentElement.elements();
        Iterator var8 = siblings.iterator();

        Element sequenceFlow;
        while (var8.hasNext()) {
            sequenceFlow = (Element) var8.next();
            siblingsMap.put(sequenceFlow.attribute("id"), sequenceFlow);
        }

        var8 = sequenceFlows.iterator();

        while (var8.hasNext()) {
            sequenceFlow = (Element) var8.next();
            String sourceRef = sequenceFlow.attribute("sourceRef");
            String targetRef = sequenceFlow.attribute("targetRef");
            if (activity.getId().equals(sourceRef)) {
                Element sibling = (Element) siblingsMap.get(targetRef);
                if (sibling != null) {
                    if (sibling.getTagName().equals("intermediateCatchEvent")) {
                        ActivityImpl catchEventActivity = this.parseIntermediateCatchEvent(sibling, scope, activity);
                        if (catchEventActivity != null) {
                            this.parseActivityInputOutput(sibling, catchEventActivity);
                        }
                    } else {
                        this.addError("Event based gateway can only be connected to elements of type intermediateCatchEvent", eventBasedGwElement);
                    }
                }
            }
        }

        return activity;
    }

    public ActivityImpl parseParallelGateway(Element parallelGwElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(parallelGwElement, scope);
        activity.setActivityBehavior(new ParallelGatewayActivityBehavior());
        this.parseAsynchronousContinuationForActivity(parallelGwElement, activity);
        this.parseExecutionListenersOnScope(parallelGwElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseParallelGateway(parallelGwElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseScriptTask(Element scriptTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(scriptTaskElement, scope);
        ScriptTaskActivityBehavior activityBehavior = this.parseScriptTaskElement(scriptTaskElement);
        if (activityBehavior != null) {
            this.parseAsynchronousContinuationForActivity(scriptTaskElement, activity);
            activity.setActivityBehavior(activityBehavior);
            this.parseExecutionListenersOnScope(scriptTaskElement, activity);
            Iterator var5 = this.parseListeners.iterator();

            while (var5.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var5.next();
                parseListener.parseScriptTask(scriptTaskElement, scope, activity);
            }
        }

        return activity;
    }

    protected ScriptTaskActivityBehavior parseScriptTaskElement(Element scriptTaskElement) {
        String language = scriptTaskElement.attribute("scriptFormat");
        if (language == null) {
            language = "juel";
        }

        String resultVariableName = this.parseResultVariable(scriptTaskElement);
        String scriptSource = null;
        Element scriptElement = scriptTaskElement.element("script");
        if (scriptElement != null) {
            scriptSource = scriptElement.getText();
        }

        String scriptResource = scriptTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "resource");

        try {
            ExecutableScript script = ScriptUtil.getScript(language, scriptSource, scriptResource, this.expressionManager);
            return new ScriptTaskActivityBehavior(script, resultVariableName);
        } catch (ProcessEngineException var8) {
            this.addError("Unable to process ScriptTask: " + var8.getMessage(), scriptElement);
            return null;
        }
    }

    protected String parseResultVariable(Element element) {
        String resultVariableName = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "resultVariable");
        if (resultVariableName == null) {
            resultVariableName = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "resultVariableName");
        }

        return resultVariableName;
    }

    public ActivityImpl parseServiceTask(Element serviceTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(serviceTaskElement, scope);
        this.parseAsynchronousContinuationForActivity(serviceTaskElement, activity);
        String elementName = "serviceTask";
        this.parseServiceTaskLike(activity, elementName, serviceTaskElement, serviceTaskElement, scope);
        this.parseExecutionListenersOnScope(serviceTaskElement, activity);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseServiceTask(serviceTaskElement, scope, activity);
        }

        this.validateServiceTaskLike(activity, elementName, serviceTaskElement);
        return activity;
    }

    public void parseServiceTaskLike(ActivityImpl activity, String elementName, Element serviceTaskElement, Element camundaPropertiesElement, ScopeImpl scope) {
        String type = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "type");
        String className = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "class");
        String expression = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "expression");
        String delegateExpression = serviceTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "delegateExpression");
        String resultVariableName = this.parseResultVariable(serviceTaskElement);
        if (type != null) {
            if (type.equalsIgnoreCase("mail")) {
                this.parseEmailServiceTask(activity, serviceTaskElement, this.parseFieldDeclarations(serviceTaskElement));
            } else if (type.equalsIgnoreCase("shell")) {
                this.parseShellServiceTask(activity, serviceTaskElement, this.parseFieldDeclarations(serviceTaskElement));
            } else if (type.equalsIgnoreCase("external")) {
                this.parseExternalServiceTask(activity, serviceTaskElement, camundaPropertiesElement);
            } else {
                this.addError("Invalid usage of type attribute on " + elementName + ": '" + type + "'", serviceTaskElement);
            }
        } else if (className != null && className.trim().length() > 0) {
            if (resultVariableName != null) {
                this.addError("'resultVariableName' not supported for " + elementName + " elements using 'class'", serviceTaskElement);
            }

            activity.setActivityBehavior(new ClassDelegateActivityBehavior(className, this.parseFieldDeclarations(serviceTaskElement)));
        } else if (delegateExpression != null) {
            if (resultVariableName != null) {
                this.addError("'resultVariableName' not supported for " + elementName + " elements using 'delegateExpression'", serviceTaskElement);
            }

            activity.setActivityBehavior(new ServiceTaskDelegateExpressionActivityBehavior(this.expressionManager.createExpression(delegateExpression), this.parseFieldDeclarations(serviceTaskElement)));
        } else if (expression != null && expression.trim().length() > 0) {
            activity.setActivityBehavior(new ServiceTaskExpressionActivityBehavior(this.expressionManager.createExpression(expression), resultVariableName));
        }

    }

    protected void validateServiceTaskLike(ActivityImpl activity, String elementName, Element serviceTaskElement) {
        if (activity.getActivityBehavior() == null) {
            this.addError("One of the attributes 'class', 'delegateExpression', 'type', or 'expression' is mandatory on " + elementName + ". If you are using a connector, make sure theconnect process engine plugin is registered with the process engine.", serviceTaskElement);
        }

    }

    public ActivityImpl parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope) {
        String decisionRef = businessRuleTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "decisionRef");
        if (decisionRef != null) {
            return this.parseDmnBusinessRuleTask(businessRuleTaskElement, scope);
        } else {
            ActivityImpl activity = this.createActivityOnScope(businessRuleTaskElement, scope);
            this.parseAsynchronousContinuationForActivity(businessRuleTaskElement, activity);
            String elementName = "businessRuleTask";
            this.parseServiceTaskLike(activity, elementName, businessRuleTaskElement, businessRuleTaskElement, scope);
            this.parseExecutionListenersOnScope(businessRuleTaskElement, activity);
            Iterator var6 = this.parseListeners.iterator();

            while (var6.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var6.next();
                parseListener.parseBusinessRuleTask(businessRuleTaskElement, scope, activity);
            }

            this.validateServiceTaskLike(activity, elementName, businessRuleTaskElement);
            return activity;
        }
    }

    protected ActivityImpl parseDmnBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(businessRuleTaskElement, scope);
        activity.setScope(true);
        this.parseAsynchronousContinuationForActivity(businessRuleTaskElement, activity);
        String decisionRef = businessRuleTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "decisionRef");
        BaseCallableElement callableElement = new BaseCallableElement();
        callableElement.setDeploymentId(this.deployment.getId());
        ParameterValueProvider definitionKeyProvider = this.createParameterValueProvider(decisionRef, this.expressionManager);
        callableElement.setDefinitionKeyValueProvider(definitionKeyProvider);
        this.parseBinding(businessRuleTaskElement, activity, callableElement, "decisionRefBinding");
        this.parseVersion(businessRuleTaskElement, activity, callableElement, "decisionRefBinding", "decisionRefVersion");
        this.parseVersionTag(businessRuleTaskElement, activity, callableElement, "decisionRefBinding", "decisionRefVersionTag");
        this.parseTenantId(businessRuleTaskElement, activity, callableElement, "decisionRefTenantId");
        String resultVariable = this.parseResultVariable(businessRuleTaskElement);
        DecisionResultMapper decisionResultMapper = this.parseDecisionResultMapper(businessRuleTaskElement);
        DmnBusinessRuleTaskActivityBehavior behavior = new DmnBusinessRuleTaskActivityBehavior(callableElement, resultVariable, decisionResultMapper);
        activity.setActivityBehavior(behavior);
        this.parseExecutionListenersOnScope(businessRuleTaskElement, activity);
        Iterator var10 = this.parseListeners.iterator();

        while (var10.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var10.next();
            parseListener.parseBusinessRuleTask(businessRuleTaskElement, scope, activity);
        }

        return activity;
    }

    protected DecisionResultMapper parseDecisionResultMapper(Element businessRuleTaskElement) {
        String decisionResultMapper = businessRuleTaskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "mapDecisionResult");
        DecisionResultMapper mapper = DecisionEvaluationUtil.getDecisionResultMapperForName(decisionResultMapper);
        if (mapper == null) {
            this.addError("No decision result mapper found for name '" + decisionResultMapper + "'. Supported mappers are 'singleEntry', 'singleResult', 'collectEntries' and 'resultList'.", businessRuleTaskElement);
        }

        return mapper;
    }

    protected void parseAsynchronousContinuationForActivity(Element activityElement, ActivityImpl activity) {
        ActivityImpl parentFlowScopeActivity = activity.getParentFlowScopeActivity();
        if (parentFlowScopeActivity != null && parentFlowScopeActivity.getActivityBehavior() instanceof MultiInstanceActivityBehavior && !activity.isCompensationHandler()) {
            this.parseAsynchronousContinuation(activityElement, parentFlowScopeActivity);
            Element miLoopCharacteristics = activityElement.element("multiInstanceLoopCharacteristics");
            this.parseAsynchronousContinuation(miLoopCharacteristics, activity);
        } else {
            this.parseAsynchronousContinuation(activityElement, activity);
        }

    }

    protected void parseAsynchronousContinuation(Element element, ActivityImpl activity) {
        boolean isAsyncBefore = this.isAsyncBefore(element);
        boolean isAsyncAfter = this.isAsyncAfter(element);
        boolean exclusive = this.isExclusive(element);
        activity.setAsyncBefore(isAsyncBefore, exclusive);
        activity.setAsyncAfter(isAsyncAfter, exclusive);
    }

    protected ParameterValueProvider parsePriority(Element element, String priorityAttribute) {
        String priorityAttributeValue = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, priorityAttribute);
        if (priorityAttributeValue == null) {
            return null;
        } else {
            Object value = priorityAttributeValue;
            if (!StringUtil.isExpression(priorityAttributeValue)) {
                try {
                    value = Integer.parseInt(priorityAttributeValue);
                } catch (NumberFormatException var6) {
                    this.addError("Value '" + priorityAttributeValue + "' for attribute '" + priorityAttribute + "' is not a valid number", element);
                }
            }

            return this.createParameterValueProvider(value, this.expressionManager);
        }
    }

    protected ParameterValueProvider parseTopic(Element element, String topicAttribute) {
        String topicAttributeValue = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, topicAttribute);
        if (topicAttributeValue == null) {
            this.addError("External tasks must specify a 'topic' attribute in the camunda namespace", element);
            return null;
        } else {
            return this.createParameterValueProvider(topicAttributeValue, this.expressionManager);
        }
    }

    protected void addMessageJobDeclarationToActivity(MessageJobDeclaration messageJobDeclaration, ActivityImpl activity) {
        List<MessageJobDeclaration> messageJobDeclarations = (List) activity.getProperty("messageJobDeclaration");
        if (messageJobDeclarations == null) {
            messageJobDeclarations = new ArrayList();
            activity.setProperty("messageJobDeclaration", messageJobDeclarations);
        }

        ((List) messageJobDeclarations).add(messageJobDeclaration);
    }

    protected void addJobDeclarationToProcessDefinition(JobDeclaration<?, ?> jobDeclaration, ProcessDefinition processDefinition) {
        String key = processDefinition.getKey();
        List<JobDeclaration<?, ?>> containingJobDeclarations = (List) this.jobDeclarations.get(key);
        if (containingJobDeclarations == null) {
            containingJobDeclarations = new ArrayList();
            this.jobDeclarations.put(key, containingJobDeclarations);
        }

        ((List) containingJobDeclarations).add(jobDeclaration);
    }

    public ActivityImpl parseSendTask(Element sendTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(sendTaskElement, scope);
        if (this.isServiceTaskLike(sendTaskElement)) {
            String elementName = "sendTask";
            this.parseAsynchronousContinuationForActivity(sendTaskElement, activity);
            this.parseServiceTaskLike(activity, elementName, sendTaskElement, sendTaskElement, scope);
            this.parseExecutionListenersOnScope(sendTaskElement, activity);
            Iterator var5 = this.parseListeners.iterator();

            while (var5.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var5.next();
                parseListener.parseSendTask(sendTaskElement, scope, activity);
            }

            this.validateServiceTaskLike(activity, elementName, sendTaskElement);
        } else {
            this.parseAsynchronousContinuationForActivity(sendTaskElement, activity);
            this.parseExecutionListenersOnScope(sendTaskElement, activity);
            Iterator var8 = this.parseListeners.iterator();

            while (var8.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var8.next();
                parseListener.parseSendTask(sendTaskElement, scope, activity);
            }

            if (activity.getActivityBehavior() == null) {
                this.addError("One of the attributes 'class', 'delegateExpression', 'type', or 'expression' is mandatory on sendTask.", sendTaskElement);
            }
        }

        return activity;
    }

    protected void parseEmailServiceTask(ActivityImpl activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
        this.validateFieldDeclarationsForEmail(serviceTaskElement, fieldDeclarations);
        activity.setActivityBehavior((MailActivityBehavior) ClassDelegateUtil.instantiateDelegate(MailActivityBehavior.class, fieldDeclarations));
    }

    protected void parseShellServiceTask(ActivityImpl activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
        this.validateFieldDeclarationsForShell(serviceTaskElement, fieldDeclarations);
        activity.setActivityBehavior((ActivityBehavior) ClassDelegateUtil.instantiateDelegate(ShellActivityBehavior.class, fieldDeclarations));
    }

    protected void parseExternalServiceTask(ActivityImpl activity, Element serviceTaskElement, Element camundaPropertiesElement) {
        activity.setScope(true);
        ParameterValueProvider topicNameProvider = this.parseTopic(serviceTaskElement, "topic");
        ParameterValueProvider priorityProvider = this.parsePriority(serviceTaskElement, "taskPriority");
        Map<String, String> properties = BpmnParseUtil.parseCamundaExtensionProperties(camundaPropertiesElement);
        activity.getProperties().set(BpmnProperties.EXTENSION_PROPERTIES, properties);
        List<CamundaErrorEventDefinition> camundaErrorEventDefinitions = this.parseCamundaErrorEventDefinitions(activity, serviceTaskElement);
        activity.getProperties().set(BpmnProperties.CAMUNDA_ERROR_EVENT_DEFINITION, camundaErrorEventDefinitions);
        activity.setActivityBehavior(new ExternalTaskActivityBehavior(topicNameProvider, priorityProvider));
    }

    protected void validateFieldDeclarationsForEmail(Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
        boolean toDefined = false;
        boolean textOrHtmlDefined = false;
        Iterator var5 = fieldDeclarations.iterator();

        while (var5.hasNext()) {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) var5.next();
            if (fieldDeclaration.getName().equals("to")) {
                toDefined = true;
            }

            if (fieldDeclaration.getName().equals("html")) {
                textOrHtmlDefined = true;
            }

            if (fieldDeclaration.getName().equals("text")) {
                textOrHtmlDefined = true;
            }
        }

        if (!toDefined) {
            this.addError("No recipient is defined on the mail activity", serviceTaskElement);
        }

        if (!textOrHtmlDefined) {
            this.addError("Text or html field should be provided", serviceTaskElement);
        }

    }

    protected void validateFieldDeclarationsForShell(Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
        boolean shellCommandDefined = false;
        Iterator var4 = fieldDeclarations.iterator();

        while (true) {
            String fieldName;
            String fieldValue;
            do {
                if (!var4.hasNext()) {
                    if (!shellCommandDefined) {
                        this.addError("No shell command is defined on the shell activity", serviceTaskElement);
                    }

                    return;
                }

                FieldDeclaration fieldDeclaration = (FieldDeclaration) var4.next();
                fieldName = fieldDeclaration.getName();
                FixedValue fieldFixedValue = (FixedValue) fieldDeclaration.getValue();
                fieldValue = fieldFixedValue.getExpressionText();
                shellCommandDefined |= fieldName.equals("command");
            } while (!fieldName.equals("wait") && !fieldName.equals("redirectError") && !fieldName.equals("cleanEnv"));

            if (!fieldValue.toLowerCase().equals("true") && !fieldValue.toLowerCase().equals("false")) {
                this.addError("undefined value for shell " + fieldName + " parameter :" + fieldValue.toString(), serviceTaskElement);
            }
        }
    }

    public List<FieldDeclaration> parseFieldDeclarations(Element element) {
        List<FieldDeclaration> fieldDeclarations = new ArrayList();
        Element elementWithFieldInjections = element.element("extensionElements");
        if (elementWithFieldInjections == null) {
            elementWithFieldInjections = element;
        }

        List<Element> fieldDeclarationElements = elementWithFieldInjections.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "field");
        if (fieldDeclarationElements != null && !fieldDeclarationElements.isEmpty()) {
            Iterator var5 = fieldDeclarationElements.iterator();

            while (var5.hasNext()) {
                Element fieldDeclarationElement = (Element) var5.next();
                FieldDeclaration fieldDeclaration = this.parseFieldDeclaration(element, fieldDeclarationElement);
                if (fieldDeclaration != null) {
                    fieldDeclarations.add(fieldDeclaration);
                }
            }
        }

        return fieldDeclarations;
    }

    protected FieldDeclaration parseFieldDeclaration(Element serviceTaskElement, Element fieldDeclarationElement) {
        String fieldName = fieldDeclarationElement.attribute("name");
        FieldDeclaration fieldDeclaration = this.parseStringFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
        if (fieldDeclaration == null) {
            fieldDeclaration = this.parseExpressionFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
        }

        if (fieldDeclaration == null) {
            this.addError("One of the following is mandatory on a field declaration: one of attributes stringValue|expression or one of child elements string|expression", serviceTaskElement);
        }

        return fieldDeclaration;
    }

    protected FieldDeclaration parseStringFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
        try {
            String fieldValue = this.getStringValueFromAttributeOrElement("stringValue", "string", fieldDeclarationElement, serviceTaskElement.attribute("id"));
            if (fieldValue != null) {
                return new FieldDeclaration(fieldName, Expression.class.getName(), new FixedValue(fieldValue));
            }
        } catch (ProcessEngineException var5) {
            if (var5.getMessage().contains("multiple elements with tag name")) {
                this.addError("Multiple string field declarations found", serviceTaskElement);
            } else {
                this.addError("Error when paring field declarations: " + var5.getMessage(), serviceTaskElement);
            }
        }

        return null;
    }

    protected FieldDeclaration parseExpressionFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
        try {
            String expression = this.getStringValueFromAttributeOrElement("expression", "expression", fieldDeclarationElement, serviceTaskElement.attribute("id"));
            if (expression != null && expression.trim().length() > 0) {
                return new FieldDeclaration(fieldName, Expression.class.getName(), this.expressionManager.createExpression(expression));
            }
        } catch (ProcessEngineException var5) {
            if (var5.getMessage().contains("multiple elements with tag name")) {
                this.addError("Multiple expression field declarations found", serviceTaskElement);
            } else {
                this.addError("Error when paring field declarations: " + var5.getMessage(), serviceTaskElement);
            }
        }

        return null;
    }

    protected String getStringValueFromAttributeOrElement(String attributeName, String elementName, Element element, String ancestorElementId) {
        String value = null;
        String attributeValue = element.attribute(attributeName);
        Element childElement = element.elementNS(CAMUNDA_BPMN_EXTENSIONS_NS, elementName);
        String stringElementText = null;
        if (attributeValue != null && childElement != null) {
            this.addError("Can't use attribute '" + attributeName + "' and element '" + elementName + "' together, only use one", element, new String[]{ancestorElementId});
        } else if (childElement != null) {
            stringElementText = childElement.getText();
            if (stringElementText != null && stringElementText.length() != 0) {
                value = stringElementText;
            } else {
                this.addError("No valid value found in attribute '" + attributeName + "' nor element '" + elementName + "'", element, new String[]{ancestorElementId});
            }
        } else if (attributeValue != null && attributeValue.length() > 0) {
            value = attributeValue;
        }

        return value;
    }

    public ActivityImpl parseTask(Element taskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(taskElement, scope);
        activity.setActivityBehavior(new TaskActivityBehavior());
        this.parseAsynchronousContinuationForActivity(taskElement, activity);
        this.parseExecutionListenersOnScope(taskElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseTask(taskElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseManualTask(Element manualTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(manualTaskElement, scope);
        activity.setActivityBehavior(new ManualTaskActivityBehavior());
        this.parseAsynchronousContinuationForActivity(manualTaskElement, activity);
        this.parseExecutionListenersOnScope(manualTaskElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseManualTask(manualTaskElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseReceiveTask(Element receiveTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(receiveTaskElement, scope);
        activity.setActivityBehavior(new ReceiveTaskActivityBehavior());
        this.parseAsynchronousContinuationForActivity(receiveTaskElement, activity);
        this.parseExecutionListenersOnScope(receiveTaskElement, activity);
        if (receiveTaskElement.attribute("messageRef") != null) {
            activity.setScope(true);
            activity.setEventScope(activity);
            EventSubscriptionDeclaration declaration = this.parseMessageEventDefinition(receiveTaskElement, activity.getId());
            declaration.setActivityId(activity.getActivityId());
            declaration.setEventScopeActivityId(activity.getActivityId());
            this.addEventSubscriptionDeclaration(declaration, activity, receiveTaskElement);
        }

        Iterator var6 = this.parseListeners.iterator();

        while (var6.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var6.next();
            parseListener.parseReceiveTask(receiveTaskElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseUserTask(Element userTaskElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(userTaskElement, scope);
        this.parseAsynchronousContinuationForActivity(userTaskElement, activity);
        TaskDefinition taskDefinition = this.parseTaskDefinition(userTaskElement, activity.getId(), activity, (ProcessDefinitionEntity) scope.getProcessDefinition());
        TaskDecorator taskDecorator = new TaskDecorator(taskDefinition, this.expressionManager);
        UserTaskActivityBehavior userTaskActivity = new UserTaskActivityBehavior(taskDecorator);
        activity.setActivityBehavior(userTaskActivity);
        this.parseProperties(userTaskElement, activity);
        this.parseExecutionListenersOnScope(userTaskElement, activity);
        Iterator var7 = this.parseListeners.iterator();

        while (var7.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var7.next();
            parseListener.parseUserTask(userTaskElement, scope, activity);
        }

        return activity;
    }

    public TaskDefinition parseTaskDefinition(Element taskElement, String taskDefinitionKey, ActivityImpl activity, ProcessDefinitionEntity processDefinition) {
        String taskFormHandlerClassName = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formHandlerClass");
        Object taskFormHandler;
        if (taskFormHandlerClassName != null) {
            taskFormHandler = (TaskFormHandler) ReflectUtil.instantiate(taskFormHandlerClassName);
        } else {
            taskFormHandler = new DefaultTaskFormHandler();
        }

        ((TaskFormHandler) taskFormHandler).parseConfiguration(taskElement, this.deployment, processDefinition, this);
        TaskDefinition taskDefinition = new TaskDefinition(new DelegateTaskFormHandler((TaskFormHandler) taskFormHandler, this.deployment));
        taskDefinition.setKey(taskDefinitionKey);
        processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
        FormDefinition formDefinition = this.parseFormDefinition(taskElement);
        taskDefinition.setFormDefinition(formDefinition);
        String name = taskElement.attribute("name");
        if (name != null) {
            taskDefinition.setNameExpression(this.expressionManager.createExpression(name));
        }

        String descriptionStr = this.parseDocumentation(taskElement);
        if (descriptionStr != null) {
            taskDefinition.setDescriptionExpression(this.expressionManager.createExpression(descriptionStr));
        }

        this.parseHumanPerformer(taskElement, taskDefinition);
        this.parsePotentialOwner(taskElement, taskDefinition);
        this.parseUserTaskCustomExtensions(taskElement, activity, taskDefinition);
        return taskDefinition;
    }

    protected FormDefinition parseFormDefinition(Element flowNodeElement) {
        FormDefinition formDefinition = new FormDefinition();
        String formKeyAttribute = flowNodeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formKey");
        String formRefAttribute = flowNodeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formRef");
        if (formKeyAttribute != null && formRefAttribute != null) {
            this.addError("Invalid element definition: only one of the attributes formKey and formRef is allowed.", flowNodeElement);
        }

        if (formKeyAttribute != null) {
            formDefinition.setFormKey(this.expressionManager.createExpression(formKeyAttribute));
        }

        if (formRefAttribute != null) {
            formDefinition.setCamundaFormDefinitionKey(this.expressionManager.createExpression(formRefAttribute));
            String formRefBindingAttribute = flowNodeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formRefBinding");
            if (formRefBindingAttribute == null || !DefaultTaskFormHandler.ALLOWED_FORM_REF_BINDINGS.contains(formRefBindingAttribute)) {
                this.addError("Invalid element definition: value for formRefBinding attribute has to be one of " + DefaultTaskFormHandler.ALLOWED_FORM_REF_BINDINGS + " but was " + formRefBindingAttribute, flowNodeElement);
            }

            if (formRefBindingAttribute != null) {
                formDefinition.setCamundaFormDefinitionBinding(formRefBindingAttribute);
            }

            if ("version".equals(formRefBindingAttribute)) {
                String formRefVersionAttribute = flowNodeElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "formRefVersion");
                Expression camundaFormDefinitionVersion = this.expressionManager.createExpression(formRefVersionAttribute);
                if (formRefVersionAttribute != null) {
                    formDefinition.setCamundaFormDefinitionVersion(camundaFormDefinitionVersion);
                }
            }
        }

        return formDefinition;
    }

    protected void parseHumanPerformer(Element taskElement, TaskDefinition taskDefinition) {
        List<Element> humanPerformerElements = taskElement.elements("humanPerformer");
        if (humanPerformerElements.size() > 1) {
            this.addError("Invalid task definition: multiple humanPerformer sub elements defined for " + taskDefinition.getNameExpression(), taskElement);
        } else if (humanPerformerElements.size() == 1) {
            Element humanPerformerElement = (Element) humanPerformerElements.get(0);
            if (humanPerformerElement != null) {
                this.parseHumanPerformerResourceAssignment(humanPerformerElement, taskDefinition);
            }
        }

    }

    protected void parsePotentialOwner(Element taskElement, TaskDefinition taskDefinition) {
        List<Element> potentialOwnerElements = taskElement.elements("potentialOwner");
        Iterator var4 = potentialOwnerElements.iterator();

        while (var4.hasNext()) {
            Element potentialOwnerElement = (Element) var4.next();
            this.parsePotentialOwnerResourceAssignment(potentialOwnerElement, taskDefinition);
        }

    }

    protected void parseHumanPerformerResourceAssignment(Element performerElement, TaskDefinition taskDefinition) {
        Element raeElement = performerElement.element("resourceAssignmentExpression");
        if (raeElement != null) {
            Element feElement = raeElement.element("formalExpression");
            if (feElement != null) {
                taskDefinition.setAssigneeExpression(this.expressionManager.createExpression(feElement.getText()));
            }
        }

    }

    protected void parsePotentialOwnerResourceAssignment(Element performerElement, TaskDefinition taskDefinition) {
        Element raeElement = performerElement.element("resourceAssignmentExpression");
        if (raeElement != null) {
            Element feElement = raeElement.element("formalExpression");
            if (feElement != null) {
                List<String> assignmentExpressions = this.parseCommaSeparatedList(feElement.getText());
                Iterator var6 = assignmentExpressions.iterator();

                while (var6.hasNext()) {
                    String assignmentExpression = (String) var6.next();
                    assignmentExpression = assignmentExpression.trim();
                    String groupAssignementId;
                    if (assignmentExpression.startsWith("user(")) {
                        groupAssignementId = this.getAssignmentId(assignmentExpression, "user(");
                        taskDefinition.addCandidateUserIdExpression(this.expressionManager.createExpression(groupAssignementId));
                    } else if (assignmentExpression.startsWith("group(")) {
                        groupAssignementId = this.getAssignmentId(assignmentExpression, "group(");
                        taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(groupAssignementId));
                    } else {
                        taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(assignmentExpression));
                    }
                }
            }
        }

    }

    protected String getAssignmentId(String expression, String prefix) {
        return expression.substring(prefix.length(), expression.length() - 1).trim();
    }

    protected void parseUserTaskCustomExtensions(Element taskElement, ActivityImpl activity, TaskDefinition taskDefinition) {
        String assignee = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "assignee");
        if (assignee != null) {
            if (taskDefinition.getAssigneeExpression() == null) {
                taskDefinition.setAssigneeExpression(this.expressionManager.createExpression(assignee));
            } else {
                this.addError("Invalid usage: duplicate assignee declaration for task " + taskDefinition.getNameExpression(), taskElement);
            }
        }

        String candidateUsersString = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "candidateUsers");
        String followUpDateExpression;
        if (candidateUsersString != null) {
            List<String> candidateUsers = this.parseCommaSeparatedList(candidateUsersString);
            Iterator var7 = candidateUsers.iterator();

            while (var7.hasNext()) {
                followUpDateExpression = (String) var7.next();
                taskDefinition.addCandidateUserIdExpression(this.expressionManager.createExpression(followUpDateExpression.trim()));
            }
        }

        String candidateGroupsString = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "candidateGroups");
        String priorityExpression;
        if (candidateGroupsString != null) {
            List<String> candidateGroups = this.parseCommaSeparatedList(candidateGroupsString);
            Iterator var13 = candidateGroups.iterator();

            while (var13.hasNext()) {
                priorityExpression = (String) var13.next();
                taskDefinition.addCandidateGroupIdExpression(this.expressionManager.createExpression(priorityExpression.trim()));
            }
        }

        this.parseTaskListeners(taskElement, activity, taskDefinition);
        String dueDateExpression = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "dueDate");
        if (dueDateExpression != null) {
            taskDefinition.setDueDateExpression(this.expressionManager.createExpression(dueDateExpression));
        }

        followUpDateExpression = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "followUpDate");
        if (followUpDateExpression != null) {
            taskDefinition.setFollowUpDateExpression(this.expressionManager.createExpression(followUpDateExpression));
        }

        priorityExpression = taskElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "priority");
        if (priorityExpression != null) {
            taskDefinition.setPriorityExpression(this.expressionManager.createExpression(priorityExpression));
        }

    }

    protected List<String> parseCommaSeparatedList(String s) {
        List<String> result = new ArrayList();
        if (s != null && !"".equals(s)) {
            StringCharacterIterator iterator = new StringCharacterIterator(s);
            char c = iterator.first();
            StringBuilder strb = new StringBuilder();

            for (boolean insideExpression = false; c != '\uffff'; c = iterator.next()) {
                if (c != '{' && c != '$') {
                    if (c == '}') {
                        insideExpression = false;
                    } else if (c == ',' && !insideExpression) {
                        result.add(strb.toString().trim());
                        strb.delete(0, strb.length());
                    }
                } else {
                    insideExpression = true;
                }

                if (c != ',' || insideExpression) {
                    strb.append(c);
                }
            }

            if (strb.length() > 0) {
                result.add(strb.toString().trim());
            }
        }

        return result;
    }

    protected void parseTaskListeners(Element userTaskElement, ActivityImpl activity, TaskDefinition taskDefinition) {
        Element extentionsElement = userTaskElement.element("extensionElements");
        if (extentionsElement != null) {
            List<Element> taskListenerElements = extentionsElement.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "taskListener");
            Iterator var6 = taskListenerElements.iterator();

            while (true) {
                while (true) {
                    while (var6.hasNext()) {
                        Element taskListenerElement = (Element) var6.next();
                        String eventName = taskListenerElement.attribute("event");
                        if (eventName != null) {
                            TaskListener taskListener;
                            if (!"create".equals(eventName) && !"assignment".equals(eventName) && !"complete".equals(eventName) && !"update".equals(eventName) && !"delete".equals(eventName)) {
                                if ("timeout".equals(eventName)) {
                                    taskListener = this.parseTimeoutTaskListener(taskListenerElement, activity, taskDefinition);
                                    taskDefinition.addTimeoutTaskListener(taskListenerElement.attribute("id"), taskListener);
                                } else {
                                    this.addError("Attribute 'event' must be one of {create|assignment|complete|update|delete|timeout}", userTaskElement);
                                }
                            } else {
                                taskListener = this.parseTaskListener(taskListenerElement, activity.getId());
                                taskDefinition.addTaskListener(eventName, taskListener);
                            }
                        } else {
                            this.addError("Attribute 'event' is mandatory on taskListener", userTaskElement);
                        }
                    }

                    return;
                }
            }
        }
    }

    protected TaskListener parseTaskListener(Element taskListenerElement, String taskElementId) {
        TaskListener taskListener = null;
        String className = taskListenerElement.attribute("class");
        String expression = taskListenerElement.attribute("expression");
        String delegateExpression = taskListenerElement.attribute("delegateExpression");
        Element scriptElement = taskListenerElement.elementNS(CAMUNDA_BPMN_EXTENSIONS_NS, "script");
        if (className != null) {
            taskListener = new ClassDelegateTaskListener(className, this.parseFieldDeclarations(taskListenerElement));
        } else if (expression != null) {
            taskListener = new ExpressionTaskListener(this.expressionManager.createExpression(expression));
        } else if (delegateExpression != null) {
            taskListener = new DelegateExpressionTaskListener(this.expressionManager.createExpression(delegateExpression), this.parseFieldDeclarations(taskListenerElement));
        } else if (scriptElement != null) {
            try {
                ExecutableScript executableScript = BpmnParseUtil.parseCamundaScript(scriptElement);
                if (executableScript != null) {
                    taskListener = new ScriptTaskListener(executableScript);
                }
            } catch (BpmnParseException var9) {
                this.addError(var9, taskElementId);
            }
        } else {
            this.addError("Element 'class', 'expression', 'delegateExpression' or 'script' is mandatory on taskListener", taskListenerElement, new String[]{taskElementId});
        }

        return (TaskListener) taskListener;
    }

    protected TaskListener parseTimeoutTaskListener(Element taskListenerElement, ActivityImpl timerActivity, TaskDefinition taskDefinition) {
        String listenerId = taskListenerElement.attribute("id");
        String timerActivityId = timerActivity.getId();
        if (listenerId == null) {
            this.addError("Element 'id' is mandatory on taskListener of type 'timeout'", taskListenerElement, new String[]{timerActivityId});
        }

        Element timerEventDefinition = taskListenerElement.element("timerEventDefinition");
        if (timerEventDefinition == null) {
            this.addError("Element 'timerEventDefinition' is mandatory on taskListener of type 'timeout'", taskListenerElement, new String[]{timerActivityId});
        }

        timerActivity.setScope(true);
        timerActivity.setEventScope(timerActivity);
        TimerDeclarationImpl timerDeclaration = this.parseTimer(timerEventDefinition, timerActivity, "timer-task-listener");
        timerDeclaration.setRawJobHandlerConfiguration(timerActivityId + "$" + "taskListener~" + listenerId);
        this.addTimerListenerDeclaration(listenerId, timerActivity, timerDeclaration);
        return this.parseTaskListener(taskListenerElement, timerActivityId);
    }

    public void parseEndEvents(Element parentElement, ScopeImpl scope) {
        Iterator var3 = parentElement.elements("endEvent").iterator();

        while (var3.hasNext()) {
            Element endEventElement = (Element) var3.next();
            ActivityImpl activity = this.createActivityOnScope(endEventElement, scope);
            Element errorEventDefinition = endEventElement.element("errorEventDefinition");
            Element cancelEventDefinition = endEventElement.element("cancelEventDefinition");
            Element terminateEventDefinition = endEventElement.element("terminateEventDefinition");
            Element messageEventDefinitionElement = endEventElement.element("messageEventDefinition");
            Element signalEventDefinition = endEventElement.element("signalEventDefinition");
            Element compensateEventDefinitionElement = endEventElement.element("compensateEventDefinition");
            Element escalationEventDefinition = endEventElement.element("escalationEventDefinition");
            boolean isServiceTaskLike = this.isServiceTaskLike(messageEventDefinitionElement);
            String activityId = activity.getId();
            if (errorEventDefinition == null) {
                if (cancelEventDefinition != null) {
                    if (scope.getProperty(BpmnProperties.TYPE.getName()) != null && scope.getProperty(BpmnProperties.TYPE.getName()).equals("transaction")) {
                        activity.getProperties().set(BpmnProperties.TYPE, "cancelEndEvent");
                        activity.setActivityBehavior(new CancelEndEventActivityBehavior());
                        activity.setActivityStartBehavior(ActivityStartBehavior.INTERRUPT_FLOW_SCOPE);
                        activity.setProperty("throwsCompensation", true);
                        activity.setScope(true);
                    } else {
                        this.addError("end event with cancelEventDefinition only supported inside transaction subprocess", cancelEventDefinition, new String[]{activityId});
                    }
                } else if (terminateEventDefinition != null) {
                    activity.getProperties().set(BpmnProperties.TYPE, "terminateEndEvent");
                    activity.setActivityBehavior(new TerminateEndEventActivityBehavior());
                    activity.setActivityStartBehavior(ActivityStartBehavior.INTERRUPT_FLOW_SCOPE);
                } else if (messageEventDefinitionElement != null) {
                    if (isServiceTaskLike) {
                        this.parseServiceTaskLike(activity, "messageEndEvent", messageEventDefinitionElement, endEventElement, scope);
                        activity.getProperties().set(BpmnProperties.TYPE, "messageEndEvent");
                    } else {
                        activity.setActivityBehavior(new IntermediateThrowNoneEventActivityBehavior());
                    }
                } else if (signalEventDefinition != null) {
                    activity.getProperties().set(BpmnProperties.TYPE, "signalEndEvent");
                    EventSubscriptionDeclaration signalDefinition = this.parseSignalEventDefinition(signalEventDefinition, true, activityId);
                    activity.setActivityBehavior(new ThrowSignalEventActivityBehavior(signalDefinition));
                } else if (compensateEventDefinitionElement != null) {
                    activity.getProperties().set(BpmnProperties.TYPE, "compensationEndEvent");
                    CompensateEventDefinition compensateEventDefinition = this.parseThrowCompensateEventDefinition(compensateEventDefinitionElement, scope, endEventElement.attribute("id"));
                    activity.setActivityBehavior(new CompensationEventActivityBehavior(compensateEventDefinition));
                    activity.setProperty("throwsCompensation", true);
                    activity.setScope(true);
                } else if (escalationEventDefinition != null) {
                    activity.getProperties().set(BpmnProperties.TYPE, "escalationEndEvent");
                    Escalation escalation = this.findEscalationForEscalationEventDefinition(escalationEventDefinition, activityId);
                    if (escalation != null && escalation.getEscalationCode() == null) {
                        this.addError("escalation end event must have an 'escalationCode'", escalationEventDefinition, new String[]{activityId});
                    }

                    activity.setActivityBehavior(new ThrowEscalationEventActivityBehavior(escalation));
                } else {
                    activity.getProperties().set(BpmnProperties.TYPE, "noneEndEvent");
                    activity.setActivityBehavior(new NoneEndEventActivityBehavior());
                }
            } else {
                String errorRef = errorEventDefinition.attribute("errorRef");
                if (errorRef != null && !"".equals(errorRef)) {
                    Error error = (Error) this.errors.get(errorRef);
                    if (error != null && (error.getErrorCode() == null || "".equals(error.getErrorCode()))) {
                        this.addError("'errorCode' is mandatory on errors referenced by throwing error event definitions, but the error '" + error.getId() + "' does not define one.", errorEventDefinition, new String[]{activityId});
                    }

                    activity.getProperties().set(BpmnProperties.TYPE, "errorEndEvent");
                    if (error != null) {
                        activity.setActivityBehavior(new ErrorEndEventActivityBehavior(error.getErrorCode(), error.getErrorMessageExpression()));
                    } else {
                        activity.setActivityBehavior(new ErrorEndEventActivityBehavior(errorRef, (ParameterValueProvider) null));
                    }
                } else {
                    this.addError("'errorRef' attribute is mandatory on error end event", errorEventDefinition, new String[]{activityId});
                }
            }

            if (activity != null) {
                this.parseActivityInputOutput(endEventElement, activity);
            }

            this.parseAsynchronousContinuationForActivity(endEventElement, activity);
            this.parseExecutionListenersOnScope(endEventElement, activity);
            Iterator var20 = this.parseListeners.iterator();

            while (var20.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var20.next();
                parseListener.parseEndEvent(endEventElement, scope, activity);
            }

            if (isServiceTaskLike) {
                this.validateServiceTaskLike(activity, "messageEndEvent", messageEventDefinitionElement);
            }
        }

    }

    public void parseBoundaryEvents(Element parentElement, ScopeImpl flowScope) {
        Iterator var3 = parentElement.elements("boundaryEvent").iterator();

        while (var3.hasNext()) {
            Element boundaryEventElement = (Element) var3.next();
            String attachedToRef = boundaryEventElement.attribute("attachedToRef");
            if (attachedToRef == null || attachedToRef.equals("")) {
                this.addError("AttachedToRef is required when using a timerEventDefinition", boundaryEventElement);
            }

            String id = boundaryEventElement.attribute("id");
            LOG.parsingElement("boundary event", id);
            Element timerEventDefinition = boundaryEventElement.element("timerEventDefinition");
            Element errorEventDefinition = boundaryEventElement.element("errorEventDefinition");
            Element signalEventDefinition = boundaryEventElement.element("signalEventDefinition");
            Element cancelEventDefinition = boundaryEventElement.element("cancelEventDefinition");
            Element compensateEventDefinition = boundaryEventElement.element("compensateEventDefinition");
            Element messageEventDefinition = boundaryEventElement.element("messageEventDefinition");
            Element escalationEventDefinition = boundaryEventElement.element("escalationEventDefinition");
            Element conditionalEventDefinition = boundaryEventElement.element("conditionalEventDefinition");
            ActivityImpl boundaryEventActivity = this.createActivityOnScope(boundaryEventElement, flowScope);
            this.parseAsynchronousContinuation(boundaryEventElement, boundaryEventActivity);
            ActivityImpl attachedActivity = flowScope.findActivityAtLevelOfSubprocess(attachedToRef);
            if (attachedActivity == null) {
                this.addError("Invalid reference in boundary event. Make sure that the referenced activity is defined in the same scope as the boundary event", boundaryEventElement);
            }

            if (compensateEventDefinition == null) {
                ActivityImpl multiInstanceScope = this.getMultiInstanceScope(attachedActivity);
                if (multiInstanceScope != null) {
                    boundaryEventActivity.setEventScope(multiInstanceScope);
                } else {
                    attachedActivity.setScope(true);
                    boundaryEventActivity.setEventScope(attachedActivity);
                }
            } else {
                boundaryEventActivity.setEventScope(attachedActivity);
            }

            String cancelActivityAttr = boundaryEventElement.attribute("cancelActivity", "true");
            boolean isCancelActivity = Boolean.valueOf(cancelActivityAttr);
            if (isCancelActivity) {
                boundaryEventActivity.setActivityStartBehavior(ActivityStartBehavior.CANCEL_EVENT_SCOPE);
            } else {
                boundaryEventActivity.setActivityStartBehavior(ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE);
            }

            ActivityBehavior behavior = new BoundaryEventActivityBehavior();
            if (timerEventDefinition != null) {
                this.parseBoundaryTimerEventDefinition(timerEventDefinition, isCancelActivity, boundaryEventActivity);
            } else if (errorEventDefinition != null) {
                this.parseBoundaryErrorEventDefinition(errorEventDefinition, boundaryEventActivity);
            } else if (signalEventDefinition != null) {
                this.parseBoundarySignalEventDefinition(signalEventDefinition, isCancelActivity, boundaryEventActivity);
            } else if (cancelEventDefinition != null) {
                behavior = this.parseBoundaryCancelEventDefinition(cancelEventDefinition, boundaryEventActivity);
            } else if (compensateEventDefinition != null) {
                this.parseBoundaryCompensateEventDefinition(compensateEventDefinition, boundaryEventActivity);
            } else if (messageEventDefinition != null) {
                this.parseBoundaryMessageEventDefinition(messageEventDefinition, isCancelActivity, boundaryEventActivity);
            } else if (escalationEventDefinition != null) {
                if (!attachedActivity.isSubProcessScope() && !(attachedActivity.getActivityBehavior() instanceof CallActivityBehavior) && !(attachedActivity.getActivityBehavior() instanceof UserTaskActivityBehavior)) {
                    this.addError("An escalation boundary event should only be attached to a subprocess, a call activity or an user task", boundaryEventElement);
                } else {
                    this.parseBoundaryEscalationEventDefinition(escalationEventDefinition, isCancelActivity, boundaryEventActivity);
                }
            } else if (conditionalEventDefinition != null) {
                behavior = this.parseBoundaryConditionalEventDefinition(conditionalEventDefinition, isCancelActivity, boundaryEventActivity);
            } else {
                this.addError("Unsupported boundary event type", boundaryEventElement);
            }

            this.ensureNoIoMappingDefined(boundaryEventElement);
            boundaryEventActivity.setActivityBehavior((ActivityBehavior) behavior);
            this.parseExecutionListenersOnScope(boundaryEventElement, boundaryEventActivity);
            Iterator var20 = this.parseListeners.iterator();

            while (var20.hasNext()) {
                BpmnParseListener parseListener = (BpmnParseListener) var20.next();
                parseListener.parseBoundaryEvent(boundaryEventElement, flowScope, boundaryEventActivity);
            }
        }

    }

    public List<CamundaErrorEventDefinition> parseCamundaErrorEventDefinitions(ActivityImpl activity, Element scopeElement) {
        List<CamundaErrorEventDefinition> errorEventDefinitions = new ArrayList();
        Element extensionElements = scopeElement.element("extensionElements");
        if (extensionElements != null) {
            List<Element> errorEventDefinitionElements = extensionElements.elements("errorEventDefinition");
            Iterator var6 = errorEventDefinitionElements.iterator();

            while (var6.hasNext()) {
                Element errorEventDefinitionElement = (Element) var6.next();
                String errorRef = errorEventDefinitionElement.attribute("errorRef");
                Error error = null;
                if (errorRef != null) {
                    String camundaExpression = errorEventDefinitionElement.attribute("expression");
                    error = (Error) this.errors.get(errorRef);
                    CamundaErrorEventDefinition definition = new CamundaErrorEventDefinition(activity.getId(), this.expressionManager.createExpression(camundaExpression));
                    definition.setErrorCode(error == null ? errorRef : error.getErrorCode());
                    this.setErrorCodeVariableOnErrorEventDefinition(errorEventDefinitionElement, definition);
                    this.setErrorMessageVariableOnErrorEventDefinition(errorEventDefinitionElement, definition);
                    errorEventDefinitions.add(definition);
                }
            }
        }

        return errorEventDefinitions;
    }

    protected ActivityImpl getMultiInstanceScope(ActivityImpl activity) {
        return activity.isMultiInstance() ? activity.getParentFlowScopeActivity() : null;
    }

    public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl boundaryActivity) {
        boundaryActivity.getProperties().set(BpmnProperties.TYPE, "boundaryTimer");
        TimerDeclarationImpl timerDeclaration = this.parseTimer(timerEventDefinition, boundaryActivity, "timer-transition");
        if (interrupting) {
            timerDeclaration.setInterruptingTimer(true);
            Element timeCycleElement = timerEventDefinition.element("timeCycle");
            if (timeCycleElement != null) {
                this.addTimeCycleWarning(timeCycleElement, "cancelling boundary", boundaryActivity.getId());
            }
        }

        this.addTimerDeclaration(boundaryActivity.getEventScope(), timerDeclaration);
        Iterator var7 = this.parseListeners.iterator();

        while (var7.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var7.next();
            parseListener.parseBoundaryTimerEventDefinition(timerEventDefinition, interrupting, boundaryActivity);
        }

    }

    public void parseBoundarySignalEventDefinition(Element element, boolean interrupting, ActivityImpl signalActivity) {
        signalActivity.getProperties().set(BpmnProperties.TYPE, "boundarySignal");
        EventSubscriptionDeclaration signalDefinition = this.parseSignalEventDefinition(element, false, signalActivity.getId());
        if (signalActivity.getId() == null) {
            this.addError("boundary event has no id", element);
        }

        signalDefinition.setActivityId(signalActivity.getId());
        this.addEventSubscriptionDeclaration(signalDefinition, signalActivity.getEventScope(), element);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseBoundarySignalEventDefinition(element, interrupting, signalActivity);
        }

    }

    public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl messageActivity) {
        messageActivity.getProperties().set(BpmnProperties.TYPE, "boundaryMessage");
        EventSubscriptionDeclaration messageEventDefinition = this.parseMessageEventDefinition(element, messageActivity.getId());
        if (messageActivity.getId() == null) {
            this.addError("boundary event has no id", element);
        }

        messageEventDefinition.setActivityId(messageActivity.getId());
        this.addEventSubscriptionDeclaration(messageEventDefinition, messageActivity.getEventScope(), element);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseBoundaryMessageEventDefinition(element, interrupting, messageActivity);
        }

    }

    protected void parseTimerStartEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity, ProcessDefinitionEntity processDefinition) {
        timerActivity.getProperties().set(BpmnProperties.TYPE, "startTimerEvent");
        TimerDeclarationImpl timerDeclaration = this.parseTimer(timerEventDefinition, timerActivity, "timer-start-event");
        timerDeclaration.setRawJobHandlerConfiguration(processDefinition.getKey());
        List<TimerDeclarationImpl> timerDeclarations = (List) processDefinition.getProperty("timerStart");
        if (timerDeclarations == null) {
            timerDeclarations = new ArrayList();
            processDefinition.setProperty("timerStart", timerDeclarations);
        }

        ((List) timerDeclarations).add(timerDeclaration);
    }

    protected void parseTimerStartEventDefinitionForEventSubprocess(Element timerEventDefinition, ActivityImpl timerActivity, boolean interrupting) {
        timerActivity.getProperties().set(BpmnProperties.TYPE, "startTimerEvent");
        TimerDeclarationImpl timerDeclaration = this.parseTimer(timerEventDefinition, timerActivity, "timer-start-event-subprocess");
        timerDeclaration.setActivity(timerActivity);
        timerDeclaration.setEventScopeActivityId(timerActivity.getEventScope().getId());
        timerDeclaration.setRawJobHandlerConfiguration(timerActivity.getFlowScope().getId());
        timerDeclaration.setInterruptingTimer(interrupting);
        if (interrupting) {
            Element timeCycleElement = timerEventDefinition.element("timeCycle");
            if (timeCycleElement != null) {
                this.addTimeCycleWarning(timeCycleElement, "interrupting start", timerActivity.getId());
            }
        }

        this.addTimerDeclaration(timerActivity.getEventScope(), timerDeclaration);
    }

    protected void parseEventDefinitionForSubprocess(EventSubscriptionDeclaration subscriptionDeclaration, ActivityImpl activity, Element element) {
        subscriptionDeclaration.setActivityId(activity.getId());
        subscriptionDeclaration.setEventScopeActivityId(activity.getEventScope().getId());
        subscriptionDeclaration.setStartEvent(false);
        this.addEventSubscriptionDeclaration(subscriptionDeclaration, activity.getEventScope(), element);
    }

    protected void parseIntermediateSignalEventDefinition(Element element, ActivityImpl signalActivity) {
        signalActivity.getProperties().set(BpmnProperties.TYPE, "intermediateSignalCatch");
        this.parseSignalCatchEventDefinition(element, signalActivity, false);
        Iterator var3 = this.parseListeners.iterator();

        while (var3.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var3.next();
            parseListener.parseIntermediateSignalCatchEventDefinition(element, signalActivity);
        }

    }

    protected void parseSignalCatchEventDefinition(Element element, ActivityImpl signalActivity, boolean isStartEvent) {
        EventSubscriptionDeclaration signalDefinition = this.parseSignalEventDefinition(element, false, signalActivity.getId());
        signalDefinition.setActivityId(signalActivity.getId());
        signalDefinition.setStartEvent(isStartEvent);
        this.addEventSubscriptionDeclaration(signalDefinition, signalActivity.getEventScope(), element);
        EventSubscriptionJobDeclaration catchingAsyncDeclaration = new EventSubscriptionJobDeclaration(signalDefinition);
        catchingAsyncDeclaration.setJobPriorityProvider((ParameterValueProvider) signalActivity.getProperty("jobPriority"));
        catchingAsyncDeclaration.setActivity(signalActivity);
        signalDefinition.setJobDeclaration(catchingAsyncDeclaration);
        this.addEventSubscriptionJobDeclaration(catchingAsyncDeclaration, signalActivity, element);
    }

    protected EventSubscriptionDeclaration parseSignalEventDefinition(Element signalEventDefinitionElement, boolean isThrowing, String signalElementId) {
        String signalRef = signalEventDefinitionElement.attribute("signalRef");
        if (signalRef == null) {
            this.addError("signalEventDefinition does not have required property 'signalRef'", signalEventDefinitionElement, new String[]{signalElementId});
            return null;
        } else {
            SignalDefinition signalDefinition = (SignalDefinition) this.signals.get(this.resolveName(signalRef));
            if (signalDefinition == null) {
                this.addError("Could not find signal with id '" + signalRef + "'", signalEventDefinitionElement, new String[]{signalElementId});
            }

            EventSubscriptionDeclaration signalEventDefinition;
            if (isThrowing) {
                CallableElement payload = new CallableElement();
                this.parseInputParameter(signalEventDefinitionElement, payload);
                signalEventDefinition = new EventSubscriptionDeclaration(signalDefinition.getExpression(), EventType.SIGNAL, payload);
            } else {
                signalEventDefinition = new EventSubscriptionDeclaration(signalDefinition.getExpression(), EventType.SIGNAL);
            }

            boolean throwingAsync = "true".equals(signalEventDefinitionElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "async", "false"));
            signalEventDefinition.setAsync(throwingAsync);
            return signalEventDefinition;
        }
    }

    protected void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl timerActivity) {
        timerActivity.getProperties().set(BpmnProperties.TYPE, "intermediateTimer");
        TimerDeclarationImpl timerDeclaration = this.parseTimer(timerEventDefinition, timerActivity, "timer-intermediate-transition");
        Element timeCycleElement = timerEventDefinition.element("timeCycle");
        if (timeCycleElement != null) {
            this.addTimeCycleWarning(timeCycleElement, "intermediate catch", timerActivity.getId());
        }

        this.addTimerDeclaration(timerActivity.getEventScope(), timerDeclaration);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseIntermediateTimerEventDefinition(timerEventDefinition, timerActivity);
        }

    }

    protected TimerDeclarationImpl parseTimer(Element timerEventDefinition, ActivityImpl timerActivity, String jobHandlerType) {
        TimerDeclarationType type = TimerDeclarationType.DATE;
        Expression expression = this.parseExpression(timerEventDefinition, "timeDate");
        if (expression == null) {
            type = TimerDeclarationType.CYCLE;
            expression = this.parseExpression(timerEventDefinition, "timeCycle");
        }

        if (expression == null) {
            type = TimerDeclarationType.DURATION;
            expression = this.parseExpression(timerEventDefinition, "timeDuration");
        }

        if (expression == null) {
            this.addError("Timer needs configuration (either timeDate, timeCycle or timeDuration is needed).", timerEventDefinition, new String[]{timerActivity.getId()});
        }

        TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(expression, type, jobHandlerType);
        timerDeclaration.setRawJobHandlerConfiguration(timerActivity.getId());
        timerDeclaration.setExclusive("true".equals(timerEventDefinition.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "exclusive", String.valueOf(true))));
        if (timerActivity.getId() == null) {
            this.addError("Attribute \"id\" is required!", timerEventDefinition);
        }

        timerDeclaration.setActivity(timerActivity);
        timerDeclaration.setJobConfiguration(type.toString() + ": " + expression.getExpressionText());
        this.addJobDeclarationToProcessDefinition(timerDeclaration, (ProcessDefinition) timerActivity.getProcessDefinition());
        timerDeclaration.setJobPriorityProvider((ParameterValueProvider) timerActivity.getProperty("jobPriority"));
        return timerDeclaration;
    }

    protected Expression parseExpression(Element parent, String name) {
        Element value = parent.element(name);
        if (value != null) {
            String expressionText = value.getText().trim();
            return this.expressionManager.createExpression(expressionText);
        } else {
            return null;
        }
    }

    public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, ActivityImpl boundaryEventActivity) {
        boundaryEventActivity.getProperties().set(BpmnProperties.TYPE, "boundaryError");
        String errorRef = errorEventDefinition.attribute("errorRef");
        Error error = null;
        ErrorEventDefinition definition = new ErrorEventDefinition(boundaryEventActivity.getId());
        if (errorRef != null) {
            error = (Error) this.errors.get(errorRef);
            definition.setErrorCode(error == null ? errorRef : error.getErrorCode());
        }

        this.setErrorCodeVariableOnErrorEventDefinition(errorEventDefinition, definition);
        this.setErrorMessageVariableOnErrorEventDefinition(errorEventDefinition, definition);
        this.addErrorEventDefinition(definition, boundaryEventActivity.getEventScope());
        Iterator var6 = this.parseListeners.iterator();

        while (var6.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var6.next();
            parseListener.parseBoundaryErrorEventDefinition(errorEventDefinition, true, (ActivityImpl) boundaryEventActivity.getEventScope(), boundaryEventActivity);
        }

    }

    protected void addErrorEventDefinition(ErrorEventDefinition errorEventDefinition, ScopeImpl catchingScope) {
        catchingScope.getProperties().addListItem(BpmnProperties.ERROR_EVENT_DEFINITIONS, errorEventDefinition);
        List<ErrorEventDefinition> errorEventDefinitions = catchingScope.getProperties().get(BpmnProperties.ERROR_EVENT_DEFINITIONS);
        Collections.sort(errorEventDefinitions, ErrorEventDefinition.comparator);
    }

    protected void parseBoundaryEscalationEventDefinition(Element escalationEventDefinitionElement, boolean cancelActivity, ActivityImpl boundaryEventActivity) {
        boundaryEventActivity.getProperties().set(BpmnProperties.TYPE, "boundaryEscalation");
        EscalationEventDefinition escalationEventDefinition = this.createEscalationEventDefinitionForEscalationHandler(escalationEventDefinitionElement, boundaryEventActivity, cancelActivity, boundaryEventActivity.getId());
        this.addEscalationEventDefinition(boundaryEventActivity.getEventScope(), escalationEventDefinition, escalationEventDefinitionElement, boundaryEventActivity.getId());
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseBoundaryEscalationEventDefinition(escalationEventDefinitionElement, cancelActivity, boundaryEventActivity);
        }

    }

    protected Escalation findEscalationForEscalationEventDefinition(Element escalationEventDefinition, String escalationElementId) {
        String escalationRef = escalationEventDefinition.attribute("escalationRef");
        if (escalationRef == null) {
            this.addError("escalationEventDefinition does not have required attribute 'escalationRef'", escalationEventDefinition, new String[]{escalationElementId});
        } else {
            if (this.escalations.containsKey(escalationRef)) {
                return (Escalation) this.escalations.get(escalationRef);
            }

            this.addError("could not find escalation with id '" + escalationRef + "'", escalationEventDefinition, new String[]{escalationElementId});
        }

        return null;
    }

    protected EscalationEventDefinition createEscalationEventDefinitionForEscalationHandler(Element escalationEventDefinitionElement, ActivityImpl escalationHandler, boolean cancelActivity, String parentElementId) {
        EscalationEventDefinition escalationEventDefinition = new EscalationEventDefinition(escalationHandler, cancelActivity);
        String escalationRef = escalationEventDefinitionElement.attribute("escalationRef");
        if (escalationRef != null) {
            if (!this.escalations.containsKey(escalationRef)) {
                this.addError("could not find escalation with id '" + escalationRef + "'", escalationEventDefinitionElement, new String[]{parentElementId});
            } else {
                Escalation escalation = (Escalation) this.escalations.get(escalationRef);
                escalationEventDefinition.setEscalationCode(escalation.getEscalationCode());
            }
        }

        String escalationCodeVariable = escalationEventDefinitionElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "escalationCodeVariable");
        if (escalationCodeVariable != null) {
            escalationEventDefinition.setEscalationCodeVariable(escalationCodeVariable);
        }

        return escalationEventDefinition;
    }

    protected void addEscalationEventDefinition(ScopeImpl catchingScope, EscalationEventDefinition escalationEventDefinition, Element element, String escalationElementId) {
        Iterator var5 = catchingScope.getProperties().get(BpmnProperties.ESCALATION_EVENT_DEFINITIONS).iterator();

        while (true) {
            while (true) {
                while (var5.hasNext()) {
                    EscalationEventDefinition existingEscalationEventDefinition = (EscalationEventDefinition) var5.next();
                    if (existingEscalationEventDefinition.getEscalationHandler().isSubProcessScope() && escalationEventDefinition.getEscalationHandler().isSubProcessScope()) {
                        if (existingEscalationEventDefinition.getEscalationCode() == null && escalationEventDefinition.getEscalationCode() == null) {
                            this.addError("The same scope can not contains more than one escalation event subprocess without escalation code. An escalation event subprocess without escalation code catch all escalation events.", element, new String[]{escalationElementId});
                        } else if (existingEscalationEventDefinition.getEscalationCode() != null && escalationEventDefinition.getEscalationCode() != null) {
                            if (existingEscalationEventDefinition.getEscalationCode().equals(escalationEventDefinition.getEscalationCode())) {
                                this.addError("multiple escalation event subprocesses with the same escalationCode '" + escalationEventDefinition.getEscalationCode() + "' are not supported on same scope", element, new String[]{escalationElementId});
                            }
                        } else {
                            this.addError("The same scope can not contains an escalation event subprocess without escalation code and another one with escalation code. The escalation event subprocess without escalation code catch all escalation events.", element, new String[]{escalationElementId});
                        }
                    } else if (!existingEscalationEventDefinition.getEscalationHandler().isSubProcessScope() && !escalationEventDefinition.getEscalationHandler().isSubProcessScope()) {
                        if (existingEscalationEventDefinition.getEscalationCode() == null && escalationEventDefinition.getEscalationCode() == null) {
                            this.addError("The same scope can not contains more than one escalation boundary event without escalation code. An escalation boundary event without escalation code catch all escalation events.", element, new String[]{escalationElementId});
                        } else if (existingEscalationEventDefinition.getEscalationCode() != null && escalationEventDefinition.getEscalationCode() != null) {
                            if (existingEscalationEventDefinition.getEscalationCode().equals(escalationEventDefinition.getEscalationCode())) {
                                this.addError("multiple escalation boundary events with the same escalationCode '" + escalationEventDefinition.getEscalationCode() + "' are not supported on same scope", element, new String[]{escalationElementId});
                            }
                        } else {
                            this.addError("The same scope can not contains an escalation boundary event without escalation code and another one with escalation code. The escalation boundary event without escalation code catch all escalation events.", element, new String[]{escalationElementId});
                        }
                    }
                }

                catchingScope.getProperties().addListItem(BpmnProperties.ESCALATION_EVENT_DEFINITIONS, escalationEventDefinition);
                return;
            }
        }
    }

    protected void addTimerDeclaration(ScopeImpl scope, TimerDeclarationImpl timerDeclaration) {
        scope.getProperties().putMapEntry(BpmnProperties.TIMER_DECLARATIONS, timerDeclaration.getActivityId(), timerDeclaration);
    }

    protected void addTimerListenerDeclaration(String listenerId, ScopeImpl scope, TimerDeclarationImpl timerDeclaration) {
        if (scope.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS) != null && scope.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS).get(timerDeclaration.getActivityId()) != null) {
            ((Map) scope.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS).get(timerDeclaration.getActivityId())).put(listenerId, timerDeclaration);
        } else {
            Map<String, TimerDeclarationImpl> activityDeclarations = new HashMap();
            activityDeclarations.put(listenerId, timerDeclaration);
            scope.getProperties().putMapEntry(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS, timerDeclaration.getActivityId(), activityDeclarations);
        }

    }

    protected void addVariableDeclaration(ScopeImpl scope, VariableDeclaration variableDeclaration) {
        List<VariableDeclaration> variableDeclarations = (List) scope.getProperty("variableDeclarations");
        if (variableDeclarations == null) {
            variableDeclarations = new ArrayList();
            scope.setProperty("variableDeclarations", variableDeclarations);
        }

        ((List) variableDeclarations).add(variableDeclaration);
    }

    public BoundaryConditionalEventActivityBehavior parseBoundaryConditionalEventDefinition(Element element, boolean interrupting, ActivityImpl conditionalActivity) {
        conditionalActivity.getProperties().set(BpmnProperties.TYPE, "boundaryConditional");
        ConditionalEventDefinition conditionalEventDefinition = this.parseConditionalEventDefinition(element, conditionalActivity);
        conditionalEventDefinition.setInterrupting(interrupting);
        this.addEventSubscriptionDeclaration(conditionalEventDefinition, conditionalActivity.getEventScope(), element);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseBoundaryConditionalEventDefinition(element, interrupting, conditionalActivity);
        }

        return new BoundaryConditionalEventActivityBehavior(conditionalEventDefinition);
    }

    public ConditionalEventDefinition parseIntermediateConditionalEventDefinition(Element element, ActivityImpl conditionalActivity) {
        conditionalActivity.getProperties().set(BpmnProperties.TYPE, "intermediateConditional");
        ConditionalEventDefinition conditionalEventDefinition = this.parseConditionalEventDefinition(element, conditionalActivity);
        this.addEventSubscriptionDeclaration(conditionalEventDefinition, conditionalActivity.getEventScope(), element);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseIntermediateConditionalEventDefinition(element, conditionalActivity);
        }

        return conditionalEventDefinition;
    }

    public ConditionalEventDefinition parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl conditionalActivity, boolean interrupting) {
        conditionalActivity.getProperties().set(BpmnProperties.TYPE, "conditionalStartEvent");
        ConditionalEventDefinition conditionalEventDefinition = this.parseConditionalEventDefinition(element, conditionalActivity);
        conditionalEventDefinition.setInterrupting(interrupting);
        this.addEventSubscriptionDeclaration(conditionalEventDefinition, conditionalActivity.getEventScope(), element);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseConditionalStartEventForEventSubprocess(element, conditionalActivity, interrupting);
        }

        return conditionalEventDefinition;
    }

    protected ConditionalEventDefinition parseConditionalEventDefinition(Element element, ActivityImpl conditionalActivity) {
        ConditionalEventDefinition conditionalEventDefinition = null;
        Element conditionExprElement = element.element("condition");
        String conditionalActivityId = conditionalActivity.getId();
        if (conditionExprElement != null) {
            Condition condition = this.parseConditionExpression(conditionExprElement, conditionalActivityId);
            conditionalEventDefinition = new ConditionalEventDefinition(condition, conditionalActivity);
            String expression = conditionExprElement.getText().trim();
            conditionalEventDefinition.setConditionAsString(expression);
            conditionalActivity.getProcessDefinition().getProperties().set(BpmnProperties.HAS_CONDITIONAL_EVENTS, true);
            String variableName = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "variableName");
            conditionalEventDefinition.setVariableName(variableName);
            String variableEvents = element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "variableEvents");
            List<String> variableEventsList = this.parseCommaSeparatedList(variableEvents);
            conditionalEventDefinition.setVariableEvents(new HashSet(variableEventsList));
            Iterator var11 = variableEventsList.iterator();

            while (var11.hasNext()) {
                String variableEvent = (String) var11.next();
                if (!VARIABLE_EVENTS.contains(variableEvent)) {
                    this.addWarning("Variable event: " + variableEvent + " is not valid. Possible variable change events are: " + Arrays.toString(VARIABLE_EVENTS.toArray()), element, new String[]{conditionalActivityId});
                }
            }
        } else {
            this.addError("Conditional event must contain an expression for evaluation.", element, new String[]{conditionalActivityId});
        }

        return conditionalEventDefinition;
    }

    public ActivityImpl parseSubProcess(Element subProcessElement, ScopeImpl scope) {
        ActivityImpl subProcessActivity = this.createActivityOnScope(subProcessElement, scope);
        subProcessActivity.setSubProcessScope(true);
        this.parseAsynchronousContinuationForActivity(subProcessElement, subProcessActivity);
        Boolean isTriggeredByEvent = this.parseBooleanAttribute(subProcessElement.attribute("triggeredByEvent"), false);
        subProcessActivity.getProperties().set(BpmnProperties.TRIGGERED_BY_EVENT, isTriggeredByEvent);
        subProcessActivity.setProperty("consumesCompensation", !isTriggeredByEvent);
        subProcessActivity.setScope(true);
        if (isTriggeredByEvent) {
            subProcessActivity.setActivityBehavior(new EventSubProcessActivityBehavior());
            subProcessActivity.setEventScope(scope);
        } else {
            subProcessActivity.setActivityBehavior(new SubProcessActivityBehavior());
        }

        this.parseScope(subProcessElement, subProcessActivity);
        Iterator var5 = this.parseListeners.iterator();

        while (var5.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var5.next();
            parseListener.parseSubProcess(subProcessElement, scope, subProcessActivity);
        }

        return subProcessActivity;
    }

    protected ActivityImpl parseTransaction(Element transactionElement, ScopeImpl scope) {
        ActivityImpl activity = this.createActivityOnScope(transactionElement, scope);
        this.parseAsynchronousContinuationForActivity(transactionElement, activity);
        activity.setScope(true);
        activity.setSubProcessScope(true);
        activity.setActivityBehavior(new SubProcessActivityBehavior());
        activity.getProperties().set(BpmnProperties.TRIGGERED_BY_EVENT, false);
        this.parseScope(transactionElement, activity);
        Iterator var4 = this.parseListeners.iterator();

        while (var4.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var4.next();
            parseListener.parseTransaction(transactionElement, scope, activity);
        }

        return activity;
    }

    public ActivityImpl parseCallActivity(Element callActivityElement, ScopeImpl scope, boolean isMultiInstance) {
        ActivityImpl activity = this.createActivityOnScope(callActivityElement, scope);
        this.parseAsynchronousContinuationForActivity(callActivityElement, activity);
        String calledElement = callActivityElement.attribute("calledElement");
        String caseRef = callActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "caseRef");
        String className = callActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "variableMappingClass");
        String delegateExpression = callActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "variableMappingDelegateExpression");
        if (calledElement == null && caseRef == null) {
            this.addError("Missing attribute 'calledElement' or 'caseRef'", callActivityElement);
        } else if (calledElement != null && caseRef != null) {
            this.addError("The attributes 'calledElement' or 'caseRef' cannot be used together: Use either 'calledElement' or 'caseRef'", callActivityElement);
        }

        String bindingAttributeName = "calledElementBinding";
        String versionAttributeName = "calledElementVersion";
        String versionTagAttributeName = "calledElementVersionTag";
        String tenantIdAttributeName = "calledElementTenantId";
        String deploymentId = this.deployment.getId();
        CallableElement callableElement = new CallableElement();
        callableElement.setDeploymentId(deploymentId);
        CallableElementActivityBehavior behavior = null;
        ParameterValueProvider definitionKeyProvider;
        if (calledElement != null) {
            if (className != null) {
                behavior = new CallActivityBehavior(className);
            } else if (delegateExpression != null) {
                Expression exp = this.expressionManager.createExpression(delegateExpression);
                behavior = new CallActivityBehavior(exp);
            } else {
                behavior = new CallActivityBehavior();
            }

            definitionKeyProvider = this.createParameterValueProvider(calledElement, this.expressionManager);
            callableElement.setDefinitionKeyValueProvider(definitionKeyProvider);
        } else {
            behavior = new CaseCallActivityBehavior();
            definitionKeyProvider = this.createParameterValueProvider(caseRef, this.expressionManager);
            callableElement.setDefinitionKeyValueProvider(definitionKeyProvider);
            bindingAttributeName = "caseBinding";
            versionAttributeName = "caseVersion";
            tenantIdAttributeName = "caseTenantId";
        }

        ((CallableElementActivityBehavior) behavior).setCallableElement(callableElement);
        this.parseBinding(callActivityElement, activity, callableElement, bindingAttributeName);
        this.parseVersion(callActivityElement, activity, callableElement, bindingAttributeName, versionAttributeName);
        this.parseVersionTag(callActivityElement, activity, callableElement, bindingAttributeName, versionTagAttributeName);
        this.parseTenantId(callActivityElement, activity, callableElement, tenantIdAttributeName);
        this.parseInputParameter(callActivityElement, callableElement);
        this.parseOutputParameter(callActivityElement, activity, callableElement);
        if (!isMultiInstance) {
            activity.setScope(true);
        }

        activity.setActivityBehavior((ActivityBehavior) behavior);
        this.parseExecutionListenersOnScope(callActivityElement, activity);
        Iterator var19 = this.parseListeners.iterator();

        while (var19.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var19.next();
            parseListener.parseCallActivity(callActivityElement, scope, activity);
        }

        return activity;
    }

    protected void parseBinding(Element callActivityElement, ActivityImpl activity, BaseCallableElement callableElement, String bindingAttributeName) {
        String binding = callActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, bindingAttributeName);
        if (CallableElementBinding.DEPLOYMENT.getValue().equals(binding)) {
            callableElement.setBinding(CallableElementBinding.DEPLOYMENT);
        } else if (CallableElementBinding.LATEST.getValue().equals(binding)) {
            callableElement.setBinding(CallableElementBinding.LATEST);
        } else if (CallableElementBinding.VERSION.getValue().equals(binding)) {
            callableElement.setBinding(CallableElementBinding.VERSION);
        } else if (CallableElementBinding.VERSION_TAG.getValue().equals(binding)) {
            callableElement.setBinding(CallableElementBinding.VERSION_TAG);
        }

    }

    protected void parseTenantId(Element callingActivityElement, ActivityImpl activity, BaseCallableElement callableElement, String attrName) {
        ParameterValueProvider tenantIdValueProvider = null;
        String tenantId = callingActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, attrName);
        if (tenantId != null && tenantId.length() > 0) {
            tenantIdValueProvider = this.createParameterValueProvider(tenantId, this.expressionManager);
        }

        callableElement.setTenantIdProvider(tenantIdValueProvider);
    }

    protected void parseVersion(Element callingActivityElement, ActivityImpl activity, BaseCallableElement callableElement, String bindingAttributeName, String versionAttributeName) {
        String version = null;
        BaseCallableElement.CallableElementBinding binding = callableElement.getBinding();
        version = callingActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, versionAttributeName);
        if (binding != null && binding.equals(CallableElementBinding.VERSION) && version == null) {
            this.addError("Missing attribute '" + versionAttributeName + "' when '" + bindingAttributeName + "' has value '" + CallableElementBinding.VERSION.getValue() + "'", callingActivityElement);
        }

        ParameterValueProvider versionProvider = this.createParameterValueProvider(version, this.expressionManager);
        callableElement.setVersionValueProvider(versionProvider);
    }

    protected void parseVersionTag(Element callingActivityElement, ActivityImpl activity, BaseCallableElement callableElement, String bindingAttributeName, String versionTagAttributeName) {
        String versionTag = null;
        BaseCallableElement.CallableElementBinding binding = callableElement.getBinding();
        versionTag = callingActivityElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, versionTagAttributeName);
        if (binding != null && binding.equals(CallableElementBinding.VERSION_TAG) && versionTag == null) {
            this.addError("Missing attribute '" + versionTagAttributeName + "' when '" + bindingAttributeName + "' has value '" + CallableElementBinding.VERSION_TAG.getValue() + "'", callingActivityElement);
        }

        ParameterValueProvider versionTagProvider = this.createParameterValueProvider(versionTag, this.expressionManager);
        callableElement.setVersionTagValueProvider(versionTagProvider);
    }

    protected void parseInputParameter(Element elementWithParameters, CallableElement callableElement) {
        Element extensionsElement = elementWithParameters.element("extensionElements");
        if (extensionsElement != null) {
            Iterator var4 = extensionsElement.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "in").iterator();

            while (true) {
                while (var4.hasNext()) {
                    Element inElement = (Element) var4.next();
                    String businessKey = inElement.attribute("businessKey");
                    if (businessKey != null && !businessKey.isEmpty()) {
                        ParameterValueProvider businessKeyValueProvider = this.createParameterValueProvider(businessKey, this.expressionManager);
                        callableElement.setBusinessKeyValueProvider(businessKeyValueProvider);
                    } else {
                        CallableElementParameter parameter = this.parseCallableElementProvider(inElement, elementWithParameters.attribute("id"));
                        if (this.attributeValueEquals(inElement, "local", "true")) {
                            parameter.setReadLocal(true);
                        }

                        callableElement.addInput(parameter);
                    }
                }

                return;
            }
        }
    }

    protected void parseOutputParameter(Element callActivityElement, ActivityImpl activity, CallableElement callableElement) {
        Element extensionsElement = callActivityElement.element("extensionElements");
        if (extensionsElement != null) {
            Iterator var5 = extensionsElement.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "out").iterator();

            while (var5.hasNext()) {
                Element outElement = (Element) var5.next();
                CallableElementParameter parameter = this.parseCallableElementProvider(outElement, callActivityElement.attribute("id"));
                if (this.attributeValueEquals(outElement, "local", "true")) {
                    callableElement.addOutputLocal(parameter);
                } else {
                    callableElement.addOutput(parameter);
                }
            }
        }

    }

    protected boolean attributeValueEquals(Element element, String attribute, String comparisonValue) {
        String value = element.attribute(attribute);
        return comparisonValue.equals(value);
    }

    protected CallableElementParameter parseCallableElementProvider(Element parameterElement, String ancestorElementId) {
        CallableElementParameter parameter = new CallableElementParameter();
        String variables = parameterElement.attribute("variables");
        if ("all".equals(variables)) {
            parameter.setAllVariables(true);
        } else {
            boolean strictValidation = !Context.getProcessEngineConfiguration().getDisableStrictCallActivityValidation();
            ParameterValueProvider sourceValueProvider = new NullValueProvider();
            String source = parameterElement.attribute("source");
            if (source != null) {
                if (!source.isEmpty()) {
                    sourceValueProvider = new ConstantValueProvider(source);
                } else if (strictValidation) {
                    this.addError("Empty attribute 'source' when passing variables", parameterElement, new String[]{ancestorElementId});
                } else {
                    source = null;
                }
            }

            if (source == null) {
                source = parameterElement.attribute("sourceExpression");
                if (source != null) {
                    if (!source.isEmpty()) {
                        Expression expression = this.expressionManager.createExpression(source);
                        sourceValueProvider = new ElValueProvider(expression);
                    } else if (strictValidation) {
                        this.addError("Empty attribute 'sourceExpression' when passing variables", parameterElement, new String[]{ancestorElementId});
                    }
                }
            }

            if (strictValidation && source == null) {
                this.addError("Missing parameter 'source' or 'sourceExpression' when passing variables", parameterElement, new String[]{ancestorElementId});
            }

            parameter.setSourceValueProvider((ParameterValueProvider) sourceValueProvider);
            String target = parameterElement.attribute("target");
            if ((strictValidation || source != null && !source.isEmpty()) && target == null) {
                this.addError("Missing attribute 'target' when attribute 'source' or 'sourceExpression' is set", parameterElement, new String[]{ancestorElementId});
            } else if (strictValidation && target != null && target.isEmpty()) {
                this.addError("Empty attribute 'target' when attribute 'source' or 'sourceExpression' is set", parameterElement, new String[]{ancestorElementId});
            }

            parameter.setTarget(target);
        }

        return parameter;
    }

    public void parseProperties(Element element, ActivityImpl activity) {
        List<Element> propertyElements = element.elements("property");
        Iterator var4 = propertyElements.iterator();

        while (var4.hasNext()) {
            Element propertyElement = (Element) var4.next();
            this.parseProperty(propertyElement, activity);
        }

    }

    public void parseProperty(Element propertyElement, ActivityImpl activity) {
        String id = propertyElement.attribute("id");
        String name = propertyElement.attribute("name");
        if (name == null) {
            if (id == null) {
                this.addError("Invalid property usage on line " + propertyElement.getLine() + ": no id or name specified.", propertyElement, new String[]{activity.getId()});
            } else {
                name = id;
            }
        }

        String type = null;
        this.parsePropertyCustomExtensions(activity, propertyElement, name, (String) type);
    }

    public void parsePropertyCustomExtensions(ActivityImpl activity, Element propertyElement, String propertyName, String propertyType) {
        if (propertyType == null) {
            String type = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "type");
            propertyType = type != null ? type : "string";
        }

        VariableDeclaration variableDeclaration = new VariableDeclaration(propertyName, propertyType);
        this.addVariableDeclaration(activity, variableDeclaration);
        activity.setScope(true);
        String src = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "src");
        if (src != null) {
            variableDeclaration.setSourceVariableName(src);
        }

        String srcExpr = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "srcExpr");
        if (srcExpr != null) {
            Expression sourceExpression = this.expressionManager.createExpression(srcExpr);
            variableDeclaration.setSourceExpression(sourceExpression);
        }

        String dst = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "dst");
        if (dst != null) {
            variableDeclaration.setDestinationVariableName(dst);
        }

        String destExpr = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "dstExpr");
        if (destExpr != null) {
            Expression destinationExpression = this.expressionManager.createExpression(destExpr);
            variableDeclaration.setDestinationExpression(destinationExpression);
        }

        String link = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "link");
        if (link != null) {
            variableDeclaration.setLink(link);
        }

        String linkExpr = propertyElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "linkExpr");
        if (linkExpr != null) {
            Expression linkExpression = this.expressionManager.createExpression(linkExpr);
            variableDeclaration.setLinkExpression(linkExpression);
        }

        Iterator var15 = this.parseListeners.iterator();

        while (var15.hasNext()) {
            BpmnParseListener parseListener = (BpmnParseListener) var15.next();
            parseListener.parseProperty(propertyElement, variableDeclaration, activity);
        }

    }

    public void parseSequenceFlow(Element processElement, ScopeImpl scope, Map<String, Element> compensationHandlers) {
        Iterator var4 = processElement.elements("sequenceFlow").iterator();

        while (true) {
            while (var4.hasNext()) {
                Element sequenceFlowElement = (Element) var4.next();
                String id = sequenceFlowElement.attribute("id");
                String sourceRef = sequenceFlowElement.attribute("sourceRef");
                String destinationRef = sequenceFlowElement.attribute("targetRef");
                if (this.eventLinkSources.containsKey(destinationRef)) {
                    String linkName = (String) this.eventLinkSources.get(destinationRef);
                    destinationRef = (String) this.eventLinkTargets.get(linkName);
                    if (destinationRef == null) {
                        this.addError("sequence flow points to link event source with name '" + linkName + "' but no event target with that name exists. Most probably your link events are not configured correctly.", sequenceFlowElement);
                        return;
                    }
                }

                ActivityImpl sourceActivity = scope.findActivityAtLevelOfSubprocess(sourceRef);
                ActivityImpl destinationActivity = scope.findActivityAtLevelOfSubprocess(destinationRef);
                if (sourceActivity == null && compensationHandlers.containsKey(sourceRef) || sourceActivity != null && sourceActivity.isCompensationHandler()) {
                    this.addError("Invalid outgoing sequence flow of compensation activity '" + sourceRef + "'. A compensation activity should not have an incoming or outgoing sequence flow.", sequenceFlowElement, new String[]{sourceRef, id});
                } else if (destinationActivity == null && compensationHandlers.containsKey(destinationRef) || destinationActivity != null && destinationActivity.isCompensationHandler()) {
                    this.addError("Invalid incoming sequence flow of compensation activity '" + destinationRef + "'. A compensation activity should not have an incoming or outgoing sequence flow.", sequenceFlowElement, new String[]{destinationRef, id});
                } else if (sourceActivity == null) {
                    this.addError("Invalid source '" + sourceRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
                } else if (destinationActivity == null) {
                    this.addError("Invalid destination '" + destinationRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
                } else if (!(sourceActivity.getActivityBehavior() instanceof EventBasedGatewayActivityBehavior)) {
                    if (destinationActivity.getActivityBehavior() instanceof IntermediateCatchEventActivityBehavior && destinationActivity.getEventScope() != null && destinationActivity.getEventScope().getActivityBehavior() instanceof EventBasedGatewayActivityBehavior) {
                        this.addError("Invalid incoming sequenceflow for intermediateCatchEvent with id '" + destinationActivity.getId() + "' connected to an event-based gateway.", sequenceFlowElement);
                    } else if (sourceActivity.getActivityBehavior() instanceof SubProcessActivityBehavior && sourceActivity.isTriggeredByEvent()) {
                        this.addError("Invalid outgoing sequence flow of event subprocess", sequenceFlowElement);
                    } else if (destinationActivity.getActivityBehavior() instanceof SubProcessActivityBehavior && destinationActivity.isTriggeredByEvent()) {
                        this.addError("Invalid incoming sequence flow of event subprocess", sequenceFlowElement);
                    } else {
                        if (this.getMultiInstanceScope(sourceActivity) != null) {
                            sourceActivity = this.getMultiInstanceScope(sourceActivity);
                        }

                        if (this.getMultiInstanceScope(destinationActivity) != null) {
                            destinationActivity = this.getMultiInstanceScope(destinationActivity);
                        }

                        TransitionImpl transition = sourceActivity.createOutgoingTransition(id);
                        this.sequenceFlows.put(id, transition);
                        transition.setProperty("name", sequenceFlowElement.attribute("name"));
                        transition.setProperty("documentation", this.parseDocumentation(sequenceFlowElement));
                        transition.setDestination(destinationActivity);
                        this.parseSequenceFlowConditionExpression(sequenceFlowElement, transition);
                        this.parseExecutionListenersOnTransition(sequenceFlowElement, transition);
                        Iterator var12 = this.parseListeners.iterator();

                        while (var12.hasNext()) {
                            BpmnParseListener parseListener = (BpmnParseListener) var12.next();
                            parseListener.parseSequenceFlow(sequenceFlowElement, scope, transition);
                        }
                    }
                }
            }

            return;
        }
    }

    public void parseSequenceFlowConditionExpression(Element seqFlowElement, TransitionImpl seqFlow) {
        Element conditionExprElement = seqFlowElement.element("conditionExpression");
        if (conditionExprElement != null) {
            Condition condition = this.parseConditionExpression(conditionExprElement, seqFlow.getId());
            seqFlow.setProperty("conditionText", conditionExprElement.getText().trim());
            seqFlow.setProperty("condition", condition);
        }

    }

    protected Condition parseConditionExpression(Element conditionExprElement, String ancestorElementId) {
        String expression = conditionExprElement.getText().trim();
        String type = conditionExprElement.attributeNS(XSI_NS, "type");
        String language = conditionExprElement.attribute("language");
        String resource = conditionExprElement.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "resource");
        if (type != null) {
            String value = type.contains(":") ? this.resolveName(type) : "http://www.omg.org/spec/BPMN/20100524/MODEL:" + type;
            if (!value.equals("http://www.omg.org/spec/BPMN/20100524/MODEL:tFormalExpression")) {
                this.addError("Invalid type, only tFormalExpression is currently supported", conditionExprElement, new String[]{ancestorElementId});
            }
        }

        Condition condition = null;
        if (language == null) {
            condition = new UelExpressionCondition(this.expressionManager.createExpression(expression));
        } else {
            try {
                ExecutableScript script = ScriptUtil.getScript(language, expression, resource, this.expressionManager);
                condition = new ScriptCondition(script);
            } catch (ProcessEngineException var9) {
                this.addError("Unable to process condition expression:" + var9.getMessage(), conditionExprElement, new String[]{ancestorElementId});
            }
        }

        return (Condition) condition;
    }

    public void parseExecutionListenersOnScope(Element scopeElement, ScopeImpl scope) {
        Element extentionsElement = scopeElement.element("extensionElements");
        String scopeElementId = scopeElement.attribute("id");
        if (extentionsElement != null) {
            List<Element> listenerElements = extentionsElement.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "executionListener");
            Iterator var6 = listenerElements.iterator();

            while (var6.hasNext()) {
                Element listenerElement = (Element) var6.next();
                String eventName = listenerElement.attribute("event");
                if (this.isValidEventNameForScope(eventName, listenerElement, scopeElementId)) {
                    ExecutionListener listener = this.parseExecutionListener(listenerElement, scopeElementId);
                    if (listener != null) {
                        scope.addExecutionListener(eventName, listener);
                    }
                }
            }
        }

    }

    protected boolean isValidEventNameForScope(String eventName, Element listenerElement, String ancestorElementId) {
        if (eventName != null && eventName.trim().length() > 0) {
            if ("start".equals(eventName) || "end".equals(eventName)) {
                return true;
            }

            this.addError("Attribute 'event' must be one of {start|end}", listenerElement, new String[]{ancestorElementId});
        } else {
            this.addError("Attribute 'event' is mandatory on listener", listenerElement, new String[]{ancestorElementId});
        }

        return false;
    }

    public void parseExecutionListenersOnTransition(Element activitiElement, TransitionImpl activity) {
        Element extensionElements = activitiElement.element("extensionElements");
        if (extensionElements != null) {
            List<Element> listenerElements = extensionElements.elementsNS(CAMUNDA_BPMN_EXTENSIONS_NS, "executionListener");
            Iterator var5 = listenerElements.iterator();

            while (var5.hasNext()) {
                Element listenerElement = (Element) var5.next();
                ExecutionListener listener = this.parseExecutionListener(listenerElement, activity.getId());
                if (listener != null) {
                    activity.addExecutionListener(listener);
                }
            }
        }

    }

    public ExecutionListener parseExecutionListener(Element executionListenerElement, String ancestorElementId) {
        ExecutionListener executionListener = null;
        String className = executionListenerElement.attribute("class");
        String expression = executionListenerElement.attribute("expression");
        String delegateExpression = executionListenerElement.attribute("delegateExpression");
        Element scriptElement = executionListenerElement.elementNS(CAMUNDA_BPMN_EXTENSIONS_NS, "script");
        if (className != null) {
            if (className.isEmpty()) {
                this.addError("Attribute 'class' cannot be empty", executionListenerElement, new String[]{ancestorElementId});
            } else {
                executionListener = new ClassDelegateExecutionListener(className, this.parseFieldDeclarations(executionListenerElement));
            }
        } else if (expression != null) {
            executionListener = new ExpressionExecutionListener(this.expressionManager.createExpression(expression));
        } else if (delegateExpression != null) {
            if (delegateExpression.isEmpty()) {
                this.addError("Attribute 'delegateExpression' cannot be empty", executionListenerElement, new String[]{ancestorElementId});
            } else {
                executionListener = new DelegateExpressionExecutionListener(this.expressionManager.createExpression(delegateExpression), this.parseFieldDeclarations(executionListenerElement));
            }
        } else if (scriptElement != null) {
            try {
                ExecutableScript executableScript = BpmnParseUtil.parseCamundaScript(scriptElement);
                if (executableScript != null) {
                    executionListener = new ScriptExecutionListener(executableScript);
                }
            } catch (BpmnParseException var9) {
                this.addError(var9, ancestorElementId);
            }
        } else {
            this.addError("Element 'class', 'expression', 'delegateExpression' or 'script' is mandatory on executionListener", executionListenerElement, new String[]{ancestorElementId});
        }

        return (ExecutionListener) executionListener;
    }

    public void parseDiagramInterchangeElements() {
        List<Element> diagrams = this.rootElement.elementsNS(BPMN_DI_NS, "BPMNDiagram");
        if (!diagrams.isEmpty()) {
            Iterator var2 = diagrams.iterator();

            while (var2.hasNext()) {
                Element diagramElement = (Element) var2.next();
                this.parseBPMNDiagram(diagramElement);
            }
        }

    }

    public void parseBPMNDiagram(Element bpmndiagramElement) {
        Element bpmnPlane = bpmndiagramElement.elementNS(BPMN_DI_NS, "BPMNPlane");
        if (bpmnPlane != null) {
            this.parseBPMNPlane(bpmnPlane);
        }

    }

    public void parseBPMNPlane(Element bpmnPlaneElement) {
        String bpmnElement = bpmnPlaneElement.attribute("bpmnElement");
        if (bpmnElement != null && !"".equals(bpmnElement)) {
            if (this.getProcessDefinition(bpmnElement) != null) {
                this.getProcessDefinition(bpmnElement).setGraphicalNotationDefined(true);
            }

            List<Element> shapes = bpmnPlaneElement.elementsNS(BPMN_DI_NS, "BPMNShape");
            Iterator var4 = shapes.iterator();

            while (var4.hasNext()) {
                Element shape = (Element) var4.next();
                this.parseBPMNShape(shape);
            }

            List<Element> edges = bpmnPlaneElement.elementsNS(BPMN_DI_NS, "BPMNEdge");
            Iterator var8 = edges.iterator();

            while (var8.hasNext()) {
                Element edge = (Element) var8.next();
                this.parseBPMNEdge(edge);
            }
        } else {
            this.addError("'bpmnElement' attribute is required on BPMNPlane ", bpmnPlaneElement);
        }

    }

    public void parseBPMNShape(Element bpmnShapeElement) {
        String bpmnElement = bpmnShapeElement.attribute("bpmnElement");
        if (bpmnElement != null && !"".equals(bpmnElement)) {
            if (this.participantProcesses.get(bpmnElement) != null) {
                ProcessDefinitionEntity procDef = this.getProcessDefinition((String) this.participantProcesses.get(bpmnElement));
                procDef.setGraphicalNotationDefined(true);
                this.parseDIBounds(bpmnShapeElement, procDef.getParticipantProcess());
                return;
            }

            Iterator var3 = this.getProcessDefinitions().iterator();

            while (var3.hasNext()) {
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) var3.next();
                ActivityImpl activity = processDefinition.findActivity(bpmnElement);
                if (activity != null) {
                    this.parseDIBounds(bpmnShapeElement, activity);
                    String isExpanded = bpmnShapeElement.attribute("isExpanded");
                    if (isExpanded != null) {
                        activity.setProperty("isExpanded", this.parseBooleanAttribute(isExpanded));
                    }
                } else {
                    Lane lane = processDefinition.getLaneForId(bpmnElement);
                    if (lane != null) {
                        this.parseDIBounds(bpmnShapeElement, lane);
                    } else if (!this.elementIds.contains(bpmnElement)) {
                        this.addError("Invalid reference in 'bpmnElement' attribute, activity " + bpmnElement + " not found", bpmnShapeElement);
                    }
                }
            }
        } else {
            this.addError("'bpmnElement' attribute is required on BPMNShape", bpmnShapeElement);
        }

    }

    protected void parseDIBounds(Element bpmnShapeElement, HasDIBounds target) {
        Element bounds = bpmnShapeElement.elementNS(BPMN_DC_NS, "Bounds");
        if (bounds != null) {
            target.setX(this.parseDoubleAttribute(bpmnShapeElement, "x", bounds.attribute("x"), true).intValue());
            target.setY(this.parseDoubleAttribute(bpmnShapeElement, "y", bounds.attribute("y"), true).intValue());
            target.setWidth(this.parseDoubleAttribute(bpmnShapeElement, "width", bounds.attribute("width"), true).intValue());
            target.setHeight(this.parseDoubleAttribute(bpmnShapeElement, "height", bounds.attribute("height"), true).intValue());
        } else {
            this.addError("'Bounds' element is required", bpmnShapeElement);
        }

    }

    public void parseBPMNEdge(Element bpmnEdgeElement) {
        String sequenceFlowId = bpmnEdgeElement.attribute("bpmnElement");
        if (sequenceFlowId != null && !"".equals(sequenceFlowId)) {
            if (this.sequenceFlows != null && this.sequenceFlows.containsKey(sequenceFlowId)) {
                TransitionImpl sequenceFlow = (TransitionImpl) this.sequenceFlows.get(sequenceFlowId);
                List<Element> waypointElements = bpmnEdgeElement.elementsNS(OMG_DI_NS, "waypoint");
                if (waypointElements.size() >= 2) {
                    List<Integer> waypoints = new ArrayList();
                    Iterator var6 = waypointElements.iterator();

                    while (var6.hasNext()) {
                        Element waypointElement = (Element) var6.next();
                        waypoints.add(this.parseDoubleAttribute(waypointElement, "x", waypointElement.attribute("x"), true).intValue());
                        waypoints.add(this.parseDoubleAttribute(waypointElement, "y", waypointElement.attribute("y"), true).intValue());
                    }

                    sequenceFlow.setWaypoints(waypoints);
                } else {
                    this.addError("Minimum 2 waypoint elements must be definted for a 'BPMNEdge'", bpmnEdgeElement);
                }
            } else if (!this.elementIds.contains(sequenceFlowId)) {
                this.addError("Invalid reference in 'bpmnElement' attribute, sequenceFlow " + sequenceFlowId + "not found", bpmnEdgeElement);
            }
        } else {
            this.addError("'bpmnElement' attribute is required on BPMNEdge", bpmnEdgeElement);
        }

    }

    public List<ProcessDefinitionEntity> getProcessDefinitions() {
        return this.processDefinitions;
    }

    public ProcessDefinitionEntity getProcessDefinition(String processDefinitionKey) {
        Iterator var2 = this.processDefinitions.iterator();

        ProcessDefinitionEntity processDefinition;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            processDefinition = (ProcessDefinitionEntity) var2.next();
        } while (!processDefinition.getKey().equals(processDefinitionKey));

        return processDefinition;
    }

    public BpmnParse name(String name) {
        super.name(name);
        return this;
    }

    public BpmnParse sourceInputStream(InputStream inputStream) {
        super.sourceInputStream(inputStream);
        return this;
    }

    public BpmnParse sourceResource(String resource, ClassLoader classLoader) {
        super.sourceResource(resource, classLoader);
        return this;
    }

    public BpmnParse sourceResource(String resource) {
        super.sourceResource(resource);
        return this;
    }

    public BpmnParse sourceString(String string) {
        super.sourceString(string);
        return this;
    }

    public BpmnParse sourceUrl(String url) {
        super.sourceUrl(url);
        return this;
    }

    public BpmnParse sourceUrl(URL url) {
        super.sourceUrl(url);
        return this;
    }

    public Boolean parseBooleanAttribute(String booleanText, boolean defaultValue) {
        return booleanText == null ? defaultValue : this.parseBooleanAttribute(booleanText);
    }

    public Boolean parseBooleanAttribute(String booleanText) {
        if (!"true".equals(booleanText) && !"enabled".equals(booleanText) && !"on".equals(booleanText) && !"active".equals(booleanText) && !"yes".equals(booleanText)) {
            return !"false".equals(booleanText) && !"disabled".equals(booleanText) && !"off".equals(booleanText) && !"inactive".equals(booleanText) && !"no".equals(booleanText) ? null : Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Double parseDoubleAttribute(Element element, String attributeName, String doubleText, boolean required) {
        if (!required || doubleText != null && !"".equals(doubleText)) {
            try {
                return Double.parseDouble(doubleText);
            } catch (NumberFormatException var6) {
                this.addError("Cannot parse " + attributeName + ": " + var6.getMessage(), element);
            }
        } else {
            this.addError(attributeName + " is required", element);
        }

        return -1.0;
    }

    protected boolean isStartable(Element element) {
        return "true".equalsIgnoreCase(element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "isStartableInTasklist", "true"));
    }

    protected boolean isExclusive(Element element) {
        return "true".equals(element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "exclusive", String.valueOf(true)));
    }

    protected boolean isAsyncBefore(Element element) {
        return "true".equals(element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "async")) || "true".equals(element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "asyncBefore"));
    }

    protected boolean isAsyncAfter(Element element) {
        return "true".equals(element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "asyncAfter"));
    }

    protected boolean isServiceTaskLike(Element element) {
        return element != null && (element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "class") != null || element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "expression") != null || element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "delegateExpression") != null || element.attributeNS(CAMUNDA_BPMN_EXTENSIONS_NS, "type") != null || this.hasConnector(element));
    }

    protected boolean hasConnector(Element element) {
        Element extensionElements = element.element("extensionElements");
        return extensionElements != null && extensionElements.element("connector") != null;
    }

    public Map<String, List<JobDeclaration<?, ?>>> getJobDeclarations() {
        return this.jobDeclarations;
    }

    public List<JobDeclaration<?, ?>> getJobDeclarationsByKey(String processDefinitionKey) {
        return (List) this.jobDeclarations.get(processDefinitionKey);
    }

    protected void parseActivityInputOutput(Element activityElement, ActivityImpl activity) {
        Element extensionElements = activityElement.element("extensionElements");
        if (extensionElements != null) {
            IoMapping inputOutput = null;

            try {
                inputOutput = BpmnParseUtil.parseInputOutput(extensionElements);
            } catch (BpmnParseException var6) {
                this.addError(var6, activity.getId());
            }

            if (inputOutput != null && this.checkActivityInputOutputSupported(activityElement, activity, inputOutput)) {
                activity.setIoMapping(inputOutput);
                if (this.getMultiInstanceScope(activity) == null) {
                    activity.setScope(true);
                }
            }
        }

    }

    protected boolean checkActivityInputOutputSupported(Element activityElement, ActivityImpl activity, IoMapping inputOutput) {
        String tagName = activityElement.getTagName();
        if (!tagName.toLowerCase().contains("task") && !tagName.contains("Event") && !tagName.equals("transaction") && !tagName.equals("subProcess") && !tagName.equals("callActivity")) {
            this.addError("camunda:inputOutput mapping unsupported for element type '" + tagName + "'.", activityElement);
            return false;
        } else if (tagName.equals("subProcess") && "true".equals(activityElement.attribute("triggeredByEvent"))) {
            this.addError("camunda:inputOutput mapping unsupported for element type '" + tagName + "' with attribute 'triggeredByEvent = true'.", activityElement);
            return false;
        } else {
            return !inputOutput.getOutputParameters().isEmpty() ? this.checkActivityOutputParameterSupported(activityElement, activity) : true;
        }
    }

    protected boolean checkActivityOutputParameterSupported(Element activityElement, ActivityImpl activity) {
        String tagName = activityElement.getTagName();
        if (tagName.equals("endEvent")) {
            this.addError("camunda:outputParameter not allowed for element type '" + tagName + "'.", activityElement);
            return true;
        } else if (this.getMultiInstanceScope(activity) != null) {
            this.addError("camunda:outputParameter not allowed for multi-instance constructs", activityElement);
            return false;
        } else {
            return true;
        }
    }

    protected void ensureNoIoMappingDefined(Element element) {
        Element inputOutput = BpmnParseUtil.findCamundaExtensionElement(element, "inputOutput");
        if (inputOutput != null) {
            this.addError("camunda:inputOutput mapping unsupported for element type '" + element.getTagName() + "'.", element);
        }

    }

    protected ParameterValueProvider createParameterValueProvider(Object value, ExpressionManager expressionManager) {
        if (value == null) {
            return new NullValueProvider();
        } else if (value instanceof String) {
            Expression expression = expressionManager.createExpression((String) value);
            return new ElValueProvider(expression);
        } else {
            return new ConstantValueProvider(value);
        }
    }

    protected void addTimeCycleWarning(Element timeCycleElement, String type, String timerElementId) {
        String warning = "It is not recommended to use a " + type + " timer event with a time cycle.";
        this.addWarning(warning, timeCycleElement, new String[]{timerElementId});
    }

    protected void ensureNoExpressionInMessageStartEvent(Element element, EventSubscriptionDeclaration messageStartEventSubscriptionDeclaration, String parentElementId) {
        boolean eventNameContainsExpression = false;
        if (messageStartEventSubscriptionDeclaration.hasEventName()) {
            eventNameContainsExpression = !messageStartEventSubscriptionDeclaration.isEventNameLiteralText();
        }

        if (eventNameContainsExpression) {
            String messageStartName = messageStartEventSubscriptionDeclaration.getUnresolvedEventName();
            this.addError("Invalid message name '" + messageStartName + "' for element '" + element.getTagName() + "': expressions in the message start event name are not allowed!", element, new String[]{parentElementId});
        }

    }

    static {
        LOG = ProcessEngineLogger.BPMN_PARSE_LOGGER;
        VARIABLE_EVENTS = Arrays.asList("create", "delete", "update");
        PROPERTYNAME_TYPE = BpmnProperties.TYPE.getName();
        PROPERTYNAME_ERROR_EVENT_DEFINITIONS = BpmnProperties.ERROR_EVENT_DEFINITIONS.getName();
        CAMUNDA_BPMN_EXTENSIONS_NS = new Namespace("http://camunda.org/schema/1.0/bpmn", "http://activiti.org/bpmn");
        XSI_NS = new Namespace("http://www.w3.org/2001/XMLSchema-instance");
        BPMN_DI_NS = new Namespace("http://www.omg.org/spec/BPMN/20100524/DI");
        OMG_DI_NS = new Namespace("http://www.omg.org/spec/DD/20100524/DI");
        BPMN_DC_NS = new Namespace("http://www.omg.org/spec/DD/20100524/DC");
    }
}
