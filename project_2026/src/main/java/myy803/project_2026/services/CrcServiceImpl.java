package myy803.project_2026.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.repository.CrcRepository;
import myy803.project_2026.repository.UseCaseRepository;

@Service
public class CrcServiceImpl implements CrcService{
	
	@Autowired
	private CrcRepository crcRepository;
	@Autowired
    private ProjectService projectService;
	@Autowired
	private UseCaseRepository useCaseRepository;
	
	 @Override
	 public Crc findById(int id) {
	 	 Crc crc = crcRepository.findById(id);
	     projectService.findById(crc.getProject().getId());
	     return crc;
	 }

	 @Override
	 public List<Crc> findByProjectId(int projectId) {
		 return crcRepository.findByProjectId(projectId);
	 }
	
	 @Override
	 public void createCrc(int projectId, Crc crc) {
		 Project project = projectService.findById(projectId);
		 List<Crc> existingCrcs = crcRepository.findByProjectId(projectId);
		 for (Crc c : existingCrcs) {
		        if (c.getName().equals(crc.getName())) {
		            throw new IllegalArgumentException("A CRC card with this name already exists in the project");
		        }
		    }
	     crc.setProject(project);
	     crcRepository.save(crc);
	 }
	
	 @Override
	 public int deleteById(int id) {
		 Crc crc = crcRepository.findById(id);
	     projectService.findById(crc.getProject().getId());
	     int projectId = crc.getProject().getId();
	     crcRepository.deleteById(id);
	     return projectId;
	 }
	 
	 @Override
	 public void updateField(int crcId, String field, String value) {
		 Crc crc = crcRepository.findById(crcId);
	     projectService.findById(crc.getProject().getId());
	     switch (field) {
	     	case "name":	crc.setName(value);             
	     	break;
	        case "responsibilities":	crc.setResponsibilities(value);
	        break;
	        case "collaborations":	crc.setCollaborations(value);
	        break;
	     }
	     crcRepository.save(crc);
	 }
	 
	 @Override
	 @Transactional
	 public void linkUseCase(int crcId, int useCaseId) {
	     Crc crc = crcRepository.findById(crcId);
	     projectService.findById(crc.getProject().getId());
	     UseCase useCase = useCaseRepository.findById(useCaseId);
	     if (!crc.getUseCases().contains(useCase)) {
	         crc.getUseCases().add(useCase);
	         crcRepository.save(crc);
	     }
	 }
	 
	 @Override
	 @Transactional
	 public void unlinkUseCase(int crcId, int useCaseId) {
	     Crc crc = crcRepository.findById(crcId);
	     projectService.findById(crc.getProject().getId());
	     UseCase useCase = useCaseRepository.findById(useCaseId);
	     crc.getUseCases().remove(useCase);
	     crcRepository.save(crc);
	 }
	 
	 @Override
	 @Transactional
	 public List<UseCase> getLinkedUseCases(int crcId) {
	     Crc crc = crcRepository.findById(crcId);
	     return new ArrayList<>(crc.getUseCases());
	 }
	 
	 @Override
	 @Transactional
	 public List<Crc> getLinkedCrcs(int useCaseId) {
	     UseCase useCase = useCaseRepository.findById(useCaseId);
	     return new ArrayList<>(useCase.getCrcCards());
	 }

}