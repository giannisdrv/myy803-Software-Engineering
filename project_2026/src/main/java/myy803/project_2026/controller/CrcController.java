package myy803.project_2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.services.CrcService;
import myy803.project_2026.services.UseCaseService;

@Controller
public class CrcController{
	
	@Autowired
	CrcService crcService;
	@Autowired
	UseCaseService useCaseService;
	
	
	@PostMapping("/crc/delete/{id}")
	public String deleteCrc(@PathVariable("id") int crcId) {
		int projectId = crcService.deleteById(crcId);
		return "redirect:/project/view_project/" + projectId;
	}
	
	@GetMapping("/crc/create/{id}")
	public String createCrc(@PathVariable("id") int project_id, Model model) {
		model.addAttribute("crc", new Crc());	
		model.addAttribute("projectId", project_id);
		return "/crc/create";
	}
	
	@PostMapping("/crc/save/{projectId}")
	public String saveCrc(@PathVariable("projectId") int projectId, @ModelAttribute Crc crc) {
		crcService.createCrc(projectId, crc);
		return "redirect:/project/view_project/" + projectId;
	}
	
	@GetMapping("/crc/view/{id}")
	public String viewCrc(@PathVariable("id") int crcId, Model model) {
		Crc crc = crcService.findById(crcId);
	    model.addAttribute("crc", crc);
	    model.addAttribute("linkedUseCases", crcService.getLinkedUseCases(crcId));
	    model.addAttribute("projectUseCases", useCaseService.findByProjectId(crc.getProject().getId()));
	    return "crc/view_crc";
	}
	
	@PostMapping("/crc/update/{id}")
    public String updateCrcField(@PathVariable("id") int crcId,  @RequestParam("field") String field, @RequestParam("value") String value) {
		crcService.updateField(crcId, field, value);
        return "redirect:/crc/view/" + crcId;
    }
	
	@PostMapping("/crc/link/{crcId}")
	public String linkUseCase(@PathVariable("crcId") int crcId, @RequestParam("useCaseId") int useCaseId) {
	    crcService.linkUseCase(crcId, useCaseId);
	    return "redirect:/crc/view/" + crcId;
	}

	@PostMapping("/crc/unlink/{crcId}") 
	public String unlinkUseCase(@PathVariable("crcId") int crcId, @RequestParam("useCaseId") int useCaseId) {
	    crcService.unlinkUseCase(crcId, useCaseId);
	    return "redirect:/crc/view/" + crcId;
	}
}