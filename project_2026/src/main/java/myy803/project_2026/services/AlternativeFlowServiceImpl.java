package myy803.project_2026.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.project_2026.domainmodel.AlternativeFlow;
import myy803.project_2026.domainmodel.UseCase;
import myy803.project_2026.repository.AlternativeFlowRepository;

@Service
public class AlternativeFlowServiceImpl implements AlternativeFlowService{
	
	@Autowired
	private AlternativeFlowRepository alternativeFlowRepository;
	@Autowired
    private UseCaseService useCaseService;
	
	@Override
    public AlternativeFlow findById(int id) {
        return alternativeFlowRepository.findById(id);
    }
	
	@Override
    public List<AlternativeFlow> findByUseCaseId(int usecaseId) {
        return alternativeFlowRepository.findByUsecase_Id(usecaseId);
    }

	@Override
    public void addAlternativeFlow(int useCaseId, String description) {
        UseCase usecase = useCaseService.findById(useCaseId); 
        AlternativeFlow alternativeFlow = new AlternativeFlow();
        alternativeFlow.setDescription(description);
        alternativeFlow.setUsecase(usecase);
        alternativeFlowRepository.save(alternativeFlow);
    }

    
	@Override
    public int deleteById(int id) {
        AlternativeFlow alternativeFlow = alternativeFlowRepository.findById(id);
        int useCaseId = alternativeFlow.getUsecase().getId();
        useCaseService.findById(useCaseId);
        alternativeFlowRepository.deleteById(id);
        return useCaseId;
    }
}