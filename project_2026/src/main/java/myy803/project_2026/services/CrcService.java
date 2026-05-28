package myy803.project_2026.services;
import java.util.List;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.UseCase;

public interface CrcService{
	Crc findById(int id);
    List<Crc> findByProjectId(int projectId);
    void createCrc(int projectId, Crc crc);
    int deleteById(int id); 
    void updateField(int crcId, String field, String value);
    void linkUseCase(int crcId, int useCaseId);
    void unlinkUseCase(int crcId, int useCaseId);
    List<UseCase> getLinkedUseCases(int crcId);
    List<Crc> getLinkedCrcs(int useCaseId);
}