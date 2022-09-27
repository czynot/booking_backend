package com.booking.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonProperty
    @NotBlank(message = "User name must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "username", value = "Name of user (must be unique)", required = true, example = "user_name", position = 1)
    private String username;

    @JsonProperty
    @NotBlank(message = "Name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "name", value = "Name of user", required = true, example = "name", position = 2)
    private String name;

    @JsonProperty
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "counterNo", value = "Admin user counter number", required = true, example = "1", position = 3)
    private Integer counterNo;

    public Admin(User user, String name, Integer counterNo) {
        this.username = user.getUsername();
        this.name = name;
        this.counterNo = counterNo;
        this.user = user;
    }

    public Admin() {
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Integer getCounterNo() {
        return counterNo;
    }

}
