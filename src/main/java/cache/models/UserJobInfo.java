package cache.models;

import cache.annotations.Column;
import cache.annotations.Id;
import cache.annotations.Table;

@Table(name = "user_job_info")
public class UserJobInfo {
    @Id
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "job_name")
    private String jobName;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "title")
    private String title;

    public UserJobInfo() {
    }

    public UserJobInfo(long id, long userId, String jobName, String country, String city, String title) {
        this.id = id;
        this.userId = userId;
        this.jobName = jobName;
        this.country = country;
        this.city = city;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UserJobInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", jobName='" + jobName + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
