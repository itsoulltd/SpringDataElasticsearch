package com.infoworks.lab.domain.entities;

import com.infoworks.lab.domain.validation.constraint.Gender.IsValidGender;
import com.infoworks.lab.rest.validation.Email.EmailPattern;
import com.it.soul.lab.sql.entity.PrimaryKey;
import com.it.soul.lab.sql.entity.TableName;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@TableName(value = "User")
@Document(indexName = "users")
public class User extends Auditable<Integer, Long> {

	@Id @PrimaryKey(name="id", auto=true)
	private Integer id;

    @NotNull(message = "name must not be null.")
    private String name;

    @NotEmpty
    @EmailPattern(message = "Invalid Email Address!")
    private String email = "";

    @IsValidGender
    private String sex = Gender.NONE.name();

    @Min(value = 18, message = "age min Value is 18.")
	private int age = 18;


	//@NotNull(message = "dob Must Not Null")
	//@Past(message = "Date Of Birth Must Be Greater than Now")
    private Date dob = new java.sql.Date(new Date().getTime());

	private boolean active;

	public User() { /**/ }

    public User(@NotNull(message = "Name must not be null") String name
            , Gender sex
            , @Min(value = 18, message = "Min Value is 18.") int age) {
	    this.name = name;
        this.sex = sex.name();
        this.age = age;
        updateDOB(age, false);
    }

    private void updateDOB(@Min(value = 18, message = "Min Value is 18.") int age, boolean isPositive) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
        int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
        calendar.set(Calendar.YEAR, year);
        setDob(calendar.getTime());
    }

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = new java.sql.Date(dob.getTime());
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
