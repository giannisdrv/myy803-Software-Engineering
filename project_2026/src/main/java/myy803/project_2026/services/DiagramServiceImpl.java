package myy803.project_2026.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.String;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myy803.project_2026.domainmodel.Crc;
import myy803.project_2026.domainmodel.Project;
import myy803.project_2026.domainmodel.UseCase;

@Service
public class DiagramServiceImpl implements DiagramService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UseCaseService useCaseService;
    @Autowired
    private CrcService crcService;

    @Override
    public String generateUseCaseDiagram(int projectId) {
        Project project = projectService.findById(projectId);
        List<UseCase> useCases = useCaseService.findByProjectId(projectId);
        StringBuilder sb = new StringBuilder();
        
        beginDiagram(sb, "Use Case Diagram - " + project.getName());
        buildUseCaseContent(sb, project, useCases);
        
        return sb.toString();
    }

    @Override
    public String generateCrcDiagram(int projectId) {
        Project project = projectService.findById(projectId);
        List<Crc> crcs = crcService.findByProjectId(projectId);
        StringBuilder sb = new StringBuilder();
        
        beginDiagram(sb, "CRC Diagram - " + project.getName());
        buildCrcContent(sb, crcs);
        
        return sb.toString();
    }
    
    @Override
    public void beginDiagram(StringBuilder sb, String title) {
        sb.append("#title: ").append(title).append("\n\n");
    }
    
    @Override
    public void buildUseCaseContent(StringBuilder sb, Project project, List<UseCase> useCases) {
        for (UseCase uc : useCases) {
            if (uc.getActor() != null && !uc.getActor().isEmpty()) {
                sb.append("[<actor> ").append(uc.getActor()).append("] -> [<usecase> ").append(uc.getName()).append("]\n");
            } else {
                sb.append("[<usecase> ").append(uc.getName()).append("]\n");
            }
        }
    }
    
    @Override
    public void buildCrcContent(StringBuilder sb, List<Crc> crcs) {
        for (Crc crc : crcs) {
            sb.append("[").append(crc.getName());
            if (crc.getResponsibilities() != null && !crc.getResponsibilities().isEmpty()) {
                sb.append("|\n  ").append(crc.getResponsibilities());
            }
            sb.append("]\n");
        }
        sb.append("\n");

        Set<String> classNames = new HashSet<>();
        for (Crc crc : crcs) {
            classNames.add(crc.getName());
        }
        for (Crc crc : crcs) {
            if (crc.getCollaborations() != null) {
                for (String className : classNames) {
                    if (!className.equals(crc.getName()) && crc.getCollaborations().contains(className)) {
                        sb.append("[").append(crc.getName()).append("] -> [").append(className).append("]\n");
                    }
                }
            }
        }
    }
}