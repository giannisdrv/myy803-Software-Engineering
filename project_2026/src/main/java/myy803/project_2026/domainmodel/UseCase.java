package myy803.project_2026.domainmodel;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "UseCase")

public class UseCase{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private int id;
	
	@Column(name = "use_case_name")
	private String use_case_name;
	
	@Column(name = "actor")
	private String actor;
	
	@Column(name = "preconditions")
	private String preconditions;
	
	@Column(name = "main_flow")
	private String main_flow;
	
	@Column(name = "postconditions")
	private String postconditions;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="project_id")
	private Project project;
	
	@ManyToMany(mappedBy = "useCases") 
	private List<Crc> crcCards = new ArrayList<>();
	
	public UseCase(){
		super();
	}

	public UseCase(int id, String use_case_name, String actor, String preconditions, String main_flow,
			String postconditions, Project project) {
		super();
		this.id = id;
		this.use_case_name = use_case_name;
		this.actor = actor;
		this.preconditions = preconditions;
		this.main_flow = main_flow;
		this.postconditions = postconditions;
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return use_case_name;
	}

	public void setName(String use_case_name) {
		this.use_case_name = use_case_name;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getMain_flow() {
		return main_flow;
	}

	public void setMain_flow(String main_flow) {
		this.main_flow = main_flow;
	}

	public String getPostconditions() {
		return postconditions;
	}

	public void setPostconditions(String postconditions) {
		this.postconditions = postconditions;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<Crc> getCrcCards() {
	    return crcCards;
	}

	public void setCrcCards(List<Crc> crcCards) {
	    this.crcCards = crcCards;
	}

	@Override
	public String toString() {
		return "UseCase [id=" + id + ", use_case_name=" + use_case_name + ", actor=" + actor + ", preconditions="
				+ preconditions + ", main_flow=" + main_flow + ", postconditions=" + postconditions + ", project="
				+ project + ", crcCards=" + crcCards + "]";
	}

	 
	
	
}