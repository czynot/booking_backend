package com.booking.passwordHistory;

import com.booking.users.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "passwordhistory")
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @ApiModelProperty(name="id", value = "password history id", example = "0", position = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @JsonProperty
    @NotBlank(message = "Password name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "password", value = "Password of the user", required = true, example = "password", position = 2)
    private String password;


    @JsonProperty
    @Column(name = "createdat")
    private Timestamp createdAt;

    public PasswordHistory(){

    }

    public PasswordHistory(User user, String password){
        this.user = user;
        this.password = password;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHistory passwordHistory = (PasswordHistory) o;
        return Objects.equals(id, passwordHistory.id) &&
                Objects.equals(user, passwordHistory.user) &&
                Objects.equals(password, passwordHistory.password) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, password);
    }
}
