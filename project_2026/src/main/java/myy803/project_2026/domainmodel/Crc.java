package myy803.project_2026.domainmodel;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Crc")


public class Crc{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private int id;
	
	@Column(name = "class_name")
	private String class_name;
	
	@Column(name = "responsibilities")
	private String responsibilities;
	
	@Column(name = "collaborations")
	private String collaborations;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="project_id")
	private Project project;
	
	@ManyToMany
    @JoinTable(
        name = "Crc_Use_Case_Link", 
        joinColumns = @JoinColumn(name = "crc_id"), 
        inverseJoinColumns = @JoinColumn(name = "use_case_id") 
    )
    private List<UseCase> useCases = new ArrayList<>();
	
	public Crc() {
		super();
	}
	
	public Crc(int id, String class_name, String responsibilities, String collaborations, Project project) {
		super();
		this.id = id;
		this.class_name = class_name;
		this.responsibilities = responsibilities;
		this.collaborations = collaborations;
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return class_name;
	}

	public void setName(String class_name) {
		this.class_name = class_name;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilties) {
		this.responsibilities = responsibilties;
	}

	public String getCollaborations() {
		return collaborations;
	}

	public void setCollaborations(String collaborations) {
		this.collaborations = collaborations;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<UseCase> getUseCases() {
	    return useCases;
	}

	public void setUseCases(List<UseCase> useCases) {
	    this.useCases = useCases;
	}

	@Override
	public String toString() {
		return "Crc [id=" + id + ", class_name=" + class_name + ", responsibilties=" + responsibilities
				+ ", collaborations=" + collaborations + ", project=" + project + ", useCases=" + useCases + "]";
	}
	
	
	
}
	