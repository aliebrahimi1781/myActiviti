package bpm;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import activiti.mgmt.util.ActivitiUtil;

public class BsfjTester {
	
	
	private String processDefinitionKey = "bfsj";
	private String pricessinstanceid = "3001";
	
	@Test
	public void t_start_process_instance_1() {
		
		ActivitiUtil.identityService.setAuthenticatedUserId("user1");
		
		ProcessInstance pi = ActivitiUtil.runtimeService.startProcessInstanceByKey(processDefinitionKey);
		
		
	}

	@Test
	public void t_start_process_instance_2() {
		
		String taskName = "claim";
		
		HistoricProcessInstance pi = ActivitiUtil.getProcessInsById(pricessinstanceid);
		
		Task task = ActivitiUtil.taskService.createTaskQuery().processInstanceId(pricessinstanceid).taskName(taskName).singleResult();
		
		ActivitiUtil.taskService.addCandidateGroup(task.getId(), "group1");
		ActivitiUtil.taskService.addCandidateUser(task.getId(), pi.getStartUserId());
		
		System.out.println("done");
	}
	
	@Test
	/**
	 * 取回的场合
	 */
	public void t_claim_1() {
		String taskName = "claim";
		
		HistoricProcessInstance pi = ActivitiUtil.getProcessInsById(pricessinstanceid);
		Task task = ActivitiUtil.taskService.createTaskQuery().processInstanceId(pricessinstanceid).taskName(taskName).singleResult();
		
		ActivitiUtil.taskService.claim(task.getId(), pi.getStartUserId());
		
	}
	
	@Test
	public void t_claim_2() {
		String taskName = "claim";
		
		HistoricProcessInstance pi = ActivitiUtil.getProcessInsById(pricessinstanceid);
		Task task = ActivitiUtil.taskService.createTaskQuery()
				.processInstanceId(pricessinstanceid)
				.taskAssignee("user1").singleResult();
		
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("input", "N");
		ActivitiUtil.taskService.complete(task.getId(), var);
		
		System.out.println("done");
		
		
	}
	
	@Test
	public void t_reEdit() {
		String taskName = "claim";
		
		HistoricProcessInstance pi = ActivitiUtil.getProcessInsById(pricessinstanceid);
		Task task = ActivitiUtil.taskService.createTaskQuery()
				.processInstanceId(pricessinstanceid)
				.taskAssignee("user1").singleResult();
		
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("input", "Y");
		ActivitiUtil.taskService.complete(task.getId(), var);
		
		System.out.println("done");
		
		
	}
}
