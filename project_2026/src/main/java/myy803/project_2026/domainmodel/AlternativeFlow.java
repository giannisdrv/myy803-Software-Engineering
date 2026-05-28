package myy803.project_2026.domainmodel;

import jakarta.persistence.*;

@Entity
@Table(name = "AlternativeFlow")
public class AlternativeFlow{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private int id;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="use_case_id")
	private UseCase usecase;
	
	public AlternativeFlow() {
		super();
	}

	public AlternativeFlow(int id, String description, UseCase usecase) {
		super();
		this.id = id;
		this.description = description;
		this.usecase = usecase;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UseCase getUsecase() {
		return usecase;
	}

	public void setUsecase(UseCase usecase) {
		this.usecase = usecase;
	}

	@Override
	public String toString() {
		return "AlternativeFlow [id=" + id + ", description=" + description + ", usecase=" + usecase + "]";
	}
	
	
}