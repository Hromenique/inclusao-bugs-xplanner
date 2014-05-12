package br.com.hrom.xplanner.modelo.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe que representa a entidade (tabela) story do banco de dados
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Entity
@Table(name = "story")
public class UserStory implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private long id;
	
	@Column(name = "last_update")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	private String name;
	
	@Column(length=4000)
	private String description;
	
	@Column(name = "iteration_id")
	private int iterationId;
	
	@Column(name = "tracker_id")
	private int trackerId;
	
	@Column(name = "estimated_hours")
	private double estimatedHours;
	
	private int priority;	
	
	@Column(name = "status", columnDefinition = "char(1)")
	private char status;
	
	@Column(name = "original_estimated_hours")
	private Double estimatedOriginalHours;
	
	private char disposition;
	
	private double postponedHours;

	@Column(name = "it_start_estimated_hours")
	private double iterationStartEstimatedHours;
	
	private int orderNo;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Person customer;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIterationId() {
		return iterationId;
	}
	public void setIterationId(int iterationId) {
		this.iterationId = iterationId;
	}
	public int getTrackerId() {
		return trackerId;
	}
	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}
	public double getEstimatedHours() {
		return estimatedHours;
	}
	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Person getCustomer() {
		return customer;
	}
	public void setCustomer(Person customer) {
		this.customer = customer;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Double getEstimatedOriginalHours() {
		return estimatedOriginalHours;
	}
	public void setEstimatedOriginalHours(Double estimatedOriginalHours) {
		this.estimatedOriginalHours = estimatedOriginalHours;
	}
	public char getDisposition() {
		return disposition;
	}
	public void setDisposition(char disposition) {
		this.disposition = disposition;
	}
	public double getPostponedHours() {
		return postponedHours;
	}
	public void setPostponedHours(double postponedHours) {
		this.postponedHours = postponedHours;
	}
	public double getIterationStartEstimatedHours() {
		return iterationStartEstimatedHours;
	}
	public void setIterationStartEstimatedHours(double iterationStartEstimatedHours) {
		this.iterationStartEstimatedHours = iterationStartEstimatedHours;
	}
	public int getOrderNo() {
		return orderNo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + disposition;
		long temp;
		temp = Double.doubleToLongBits(estimatedHours);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((estimatedOriginalHours == null) ? 0
						: estimatedOriginalHours.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + iterationId;
		temp = Double.doubleToLongBits(iterationStartEstimatedHours);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + orderNo;
		temp = Double.doubleToLongBits(postponedHours);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + priority;
		result = prime * result + status;
		result = prime * result + trackerId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserStory other = (UserStory) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (disposition != other.disposition)
			return false;
		if (Double.doubleToLongBits(estimatedHours) != Double
				.doubleToLongBits(other.estimatedHours))
			return false;
		if (estimatedOriginalHours == null) {
			if (other.estimatedOriginalHours != null)
				return false;
		} else if (!estimatedOriginalHours.equals(other.estimatedOriginalHours))
			return false;
		if (id != other.id)
			return false;
		if (iterationId != other.iterationId)
			return false;
		if (Double.doubleToLongBits(iterationStartEstimatedHours) != Double
				.doubleToLongBits(other.iterationStartEstimatedHours))
			return false;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orderNo != other.orderNo)
			return false;
		if (Double.doubleToLongBits(postponedHours) != Double
				.doubleToLongBits(other.postponedHours))
			return false;
		if (priority != other.priority)
			return false;
		if (status != other.status)
			return false;
		if (trackerId != other.trackerId)
			return false;
		return true;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}
