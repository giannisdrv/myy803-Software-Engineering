package myy803.project_2026.services;

import java.util.List;

import myy803.project_2026.domainmodel.AlternativeFlow;

public interface AlternativeFlowService{
	AlternativeFlow findById(int id);
    List<AlternativeFlow> findByUseCaseId(int usecaseId);
    void addAlternativeFlow(int useCaseId, String description);
    int deleteById(int id);
}