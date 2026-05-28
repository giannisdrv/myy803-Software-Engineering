package myy803.project_2026.services;

import java.util.List;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;

public interface DiagramService {
	String generateUseCaseDiagram(int projectId);
	String generateCrcDiagram(int projectId);
    void beginDiagram(StringBuilder sb, String title);
    void buildUseCaseContent(StringBuilder sb, Project project, List<UseCase> useCases);
    void buildCrcContent(StringBuilder sb, List<Crc> crcs);
}