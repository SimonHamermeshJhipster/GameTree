package com.hamermesh.yeah.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Move.
 */
@Entity
@Table(name = "move")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Move implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "current_state", nullable = false)
    private String currentState;

    @NotNull
    @Column(name = "prev_state", nullable = false)
    private String prevState;

    @NotNull
    @Column(name = "time_created", nullable = false)
    private LocalDate timeCreated;

    @ManyToOne
    private User createdByUser;

    @ManyToOne(optional = false)
    @NotNull
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentState() {
        return currentState;
    }

    public Move currentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getPrevState() {
        return prevState;
    }

    public Move prevState(String prevState) {
        this.prevState = prevState;
        return this;
    }

    public void setPrevState(String prevState) {
        this.prevState = prevState;
    }

    public LocalDate getTimeCreated() {
        return timeCreated;
    }

    public Move timeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public Move createdByUser(User user) {
        this.createdByUser = user;
        return this;
    }

    public void setCreatedByUser(User user) {
        this.createdByUser = user;
    }

    public Game getGame() {
        return game;
    }

    public Move game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        if (move.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), move.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Move{" +
            "id=" + getId() +
            ", currentState='" + getCurrentState() + "'" +
            ", prevState='" + getPrevState() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            "}";
    }
}
