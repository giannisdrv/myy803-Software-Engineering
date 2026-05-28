package myy803.project_2026.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.services.CrcService;
import myy803.project_2026.services.DiagramService;
import myy803.project_2026.services.UseCaseService;
import myy803.project_2026.services.ProjectService;

@Controller
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	@Autowired
	UseCaseService useCaseService;
	@Autowired
	CrcService crcService;
	@Autowired
	DiagramService diagramService;
	
	@RequestMapping("/project/dashboard")
	public String getProjectHome(Model model) {
		model.addAttribute("projects", projectService.getProjectsForCurrentUser());
	    return "project/dashboard"; 
	}
	
	@PostMapping("/project/delete/{id}")
	public String deleteProject(@PathVariable int id) {
	    projectService.deleteById(id);
	    return "redirect:/project/dashboard";
	}
	
	@GetMapping("/project/create_project")
	public String create(Model model) {
	    model.addAttribute("project", new Project());
	    return "project/create_project";
	}
	
	@PostMapping("/project/create_project")
	public String createProject(@ModelAttribute("project") Project projectForm) {
		projectService.createProject(projectForm);
		return "redirect:/project/dashboard";
	}
	
	@GetMapping("/project/view_project/{id}")
	public String viewProject(@PathVariable("id") int project_id, Model model) {
		Project project = projectService.findById(project_id);
		
        model.addAttribute("project", project);
        model.addAttribute("useCases", useCaseService.findByProjectId(project_id));
        model.addAttribute("crcCards", crcService.findByProjectId(project_id));
	    return "project/view_project";
	}
	
	@GetMapping("/project/diagram/{id}")
	public String viewDiagram(@PathVariable("id") int projectId, @RequestParam("type") String type, Model model) {
	    Project project = projectService.findById(projectId);
	    String script;
	    if ("usecase".equals(type)) {
	        script = diagramService.generateUseCaseDiagram(projectId);
	    } else {
	        script = diagramService.generateCrcDiagram(projectId);
	    }
	    model.addAttribute("project", project);
	    model.addAttribute("script", script);
	    model.addAttribute("diagramType", type);
	    return "project/view_diagram";
	}
	
}