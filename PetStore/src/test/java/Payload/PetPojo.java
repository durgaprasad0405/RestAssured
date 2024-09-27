package Payload;

import java.util.List;

public class PetPojo {
	
	public int id;
	public category category;
	public String name;
	public List<String> photoUrls;
	public List<tag> tags;
	public String status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public category getCategory() {
		return category;
	}
	public void setCategory(category category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPhotoUrls() {
		return photoUrls;
	}
	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}
	public List<tag> getTags() {
		return tags;
	}
	public void setTags(List<tag> tags) {
		this.tags = tags;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	

}
