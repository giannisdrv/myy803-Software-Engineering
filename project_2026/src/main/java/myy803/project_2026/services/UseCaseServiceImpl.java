package myy803.project_2026.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.repository.UseCaseRepository;

@Service
public class UseCaseServiceImpl implements UseCaseService{
	
	@Autowired
    private UseCaseRepository useCaseRepository;
    @Autowired
    private ProjectService projectService;

	
    @Override
    public UseCase findById(int id){
        UseCase usecase = useCaseRepository.findById(id);
        projectService.findById(usecase.getProject().getId());
        return usecase;
    }

    @Override
    public List<UseCase> findByProjectId(int projectId){
        return useCaseRepository.findByProjectId(projectId);
    }
    
    @Override
    public void createUseCase(int projectId, UseCase usecase){
        Project project = projectService.findById(projectId); 
        List<UseCase> existingUseCases = useCaseRepository.findByProjectId(projectId);
        for (UseCase uc : existingUseCases) {
            if (uc.getName().equals(usecase.getName())) {
                throw new IllegalArgumentException("A use case with this name already exists in the project");
            }
        }
        usecase.setProject(project);
        useCaseRepository.save(usecase);
    }
    
    @Override
    public int deleteById(int id){
        UseCase usecase = useCaseRepository.findById(id);
        int projectId = usecase.getProject().getId();
        useCaseRepository.deleteById(id);
        return projectId;
    }
    
    @Override
    public void updateField(int useCaseId, String field, String value){
        UseCase usecase = useCaseRepository.findById(useCaseId);
        switch (field) {
            case "actor":          usecase.setActor(value);          
            break;
            case "preconditions":  usecase.setPreconditions(value);  
            break;
            case "main_flow":      usecase.setMain_flow(value);      
            break;
            case "postconditions": usecase.setPostconditions(value); 
            break;
        }
        useCaseRepository.save(usecase);
    }

}