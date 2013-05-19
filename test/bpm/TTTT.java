package bpm;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

public class TTTT {
	@Test
	public void t1() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("bpm/vacationRequest.bpmn20.xml").deploy();
		
		
		System.out.println("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
	}

}
