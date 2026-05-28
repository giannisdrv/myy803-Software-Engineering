package myy803.project_2026.services;
import java.util.List;

import myy803.project_2026.domainmodel.UseCase;

public interface UseCaseService{
	UseCase findById(int id);
    List<UseCase> findByProjectId(int projectId);
    void createUseCase(int projectId, UseCase usecase);
    int deleteById(int id);
    void updateField(int useCaseId, String field, String value);
}