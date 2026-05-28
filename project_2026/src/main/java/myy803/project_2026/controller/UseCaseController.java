package myy803.project_2026.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.services.UseCaseService;
import myy803.project_2026.services.AlternativeFlowService;
import myy803.project_2026.services.CrcService;

@Controller
public class UseCaseController{
	
	@Autowired 
	UseCaseService useCaseService;
	@Autowired
	AlternativeFlowService alternativeFlowService;
	@Autowired
	CrcService crcService;

	
	@PostMapping("/usecase/delete/{id}")
	public String deleteUseCase(@PathVariable("id") int usecaseId) {
		int projectId = useCaseService.deleteById(usecaseId);
		return "redirect:/project/view_project/"+ projectId;
	}
	
	@GetMapping("/usecase/create/{id}")
	public String createUseCase(@PathVariable("id") int projectId, Model model) {
		model.addAttribute("usecase", new UseCase());
        model.addAttribute("projectId", projectId);
		return "/usecase/create";
	}
	
	@PostMapping("/usecase/save/{projectId}")
	public String saveUseCase(@PathVariable("projectId") int projectId, @ModelAttribute UseCase usecase) {
		useCaseService.createUseCase(projectId, usecase);
		return "redirect:/project/view_project/"+ projectId;
	}
	
	@GetMapping("/usecase/view/{id}")
	public String viewUseCase(@PathVariable("id") int usecaseId, Model model) {
		UseCase usecase = useCaseService.findById(usecaseId);
	    model.addAttribute("usecase", usecase);
	    model.addAttribute("alternativeFlows", alternativeFlowService.findByUseCaseId(usecaseId));
	    model.addAttribute("linkedCrcs", crcService.getLinkedCrcs(usecaseId));
	    model.addAttribute("projectCrcs", crcService.findByProjectId(usecase.getProject().getId()));
	    return "usecase/view_usecase";
	}
	
	@PostMapping("/usecase/update/{id}")
    public String updateUseCaseField(@PathVariable("id") int usecaseId,  @RequestParam("field") String field, @RequestParam("value") String value) {
		useCaseService.updateField(usecaseId, field, value);
        return "redirect:/usecase/view/" + usecaseId;
    }
	
	@PostMapping("/alternativeflow/add/{id}")
	public String addAlternativeFlow(@PathVariable("id") int usecaseId, @RequestParam("description") String description) {
		alternativeFlowService.addAlternativeFlow(usecaseId, description);
	    return "redirect:/usecase/view/" + usecaseId;
	}
	
	@PostMapping("/alternativeflow/delete/{id}")
	public String deleteAlternativeFlow(@PathVariable("id") int alternativeFlowId){
		int usecaseId = alternativeFlowService.deleteById(alternativeFlowId);
		return "redirect:/usecase/view/"+ usecaseId;
	}
	
	@PostMapping("/usecase/link/{useCaseId}")
	public String linkCrc(@PathVariable("useCaseId") int useCaseId, @RequestParam("crcId") int crcId) {
	    crcService.linkUseCase(crcId, useCaseId);
	    return "redirect:/usecase/view/" + useCaseId;
	}

	@PostMapping("/usecase/unlink/{useCaseId}")
	public String unlinkCrc(@PathVariable("useCaseId") int useCaseId, @RequestParam("crcId") int crcId) {
	    crcService.unlinkUseCase(crcId, useCaseId);
	    return "redirect:/usecase/view/" + useCaseId;
	}
}