package activiti.util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.DiagramNode;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import common.AppConfig;

public class ActivitiUtil {
	
	
	private static ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(AppConfig.context);
	
	
	public static ProcessEngine processEngine = ctx.getBean(ProcessEngine.class);
	public static RuntimeService runtimeService = processEngine.getRuntimeService();
	public static RepositoryService repositoryService = processEngine.getRepositoryService();
	public static TaskService taskService = processEngine.getTaskService();
	public static ManagementService managementService = processEngine.getManagementService();
	public static IdentityService identityService = processEngine.getIdentityService();
	public static HistoryService historyService = processEngine.getHistoryService();
	public static FormService formService = processEngine.getFormService();
	
	public static List<ProcessDefinition> listProcdef(boolean latest) {
		
		List<ProcessDefinition> list = null;
		if(latest) {
			list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
		} else {
			list = repositoryService.createProcessDefinitionQuery().list();
		}
		
		return list;
	}
	
	public static ProcessDefinition getProcDef(String procDefId) {
		ProcessDefinition def = null;
		def = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		return def;
	}
	
	public static ProcessDefinition getProcDefByInst(String procInstId){
		String procDefId = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult().getProcessDefinitionId();
		return getProcDef(procDefId);
	}
	
	public static InputStream getProcDefImg(String procDefId) {
		
		ProcessDefinition procDef = getProcDef(procDefId);
		InputStream in = repositoryService.getResourceAsStream(procDef.getDeploymentId(), procDef.getDiagramResourceName());
		return in;
		
	}

	public static InputStream getProcDefXml(String procDefId) {
		ProcessDefinition procDef = getProcDef(procDefId);
		InputStream in = repositoryService.getResourceAsStream(procDef.getDeploymentId(), procDef.getResourceName());
		return in;
	}
	
	public static void deploy(String resourceName, InputStream inputStream) {
		repositoryService.createDeployment().addInputStream(resourceName, inputStream).deploy();
	}
	
	
	
	
	
	
	public static HistoricProcessInstance getProcessInsById(String processInsId) {
		return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInsId).singleResult();
	}
	
	public static List<HistoricTaskInstance> queryTaskByProcessInsId(String processInsId) {
		
		return historyService.createHistoricTaskInstanceQuery().processInstanceId(processInsId).list();
	}
	
	public static DiagramNode getNodeInProcessDefinition(String processDefinitionId, String id){
		DiagramNode node = repositoryService.getProcessDiagramLayout(processDefinitionId).getNode(id);
		return node;
	}



	public static void completeTask(String id, Map<String, Object> variables) {
		taskService.complete(id, variables);
		
	}
	

}
