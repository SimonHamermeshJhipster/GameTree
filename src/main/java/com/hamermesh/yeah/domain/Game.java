package com.hamermesh.yeah.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "branch_depth", nullable = false)
    private Integer branchDepth;

    @NotNull
    @Column(name = "time_updated", nullable = false)
    private LocalDate timeUpdated;

    @NotNull
    @Column(name = "time_created", nullable = false)
    private LocalDate timeCreated;

    @NotNull
    @Column(name = "current_state", nullable = false)
    private String currentState;

    @NotNull
    @Column(name = "is_root", nullable = false)
    private Boolean isRoot;

    @OneToMany(mappedBy = "parentGame")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Game> childGames = new HashSet<>();

    @OneToMany(mappedBy = "game")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Move> gameMoves = new HashSet<>();

    @OneToOne
    private User playerBlack;

    @OneToOne
    private User playerWhite;

    @OneToOne
    private User updatedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "PARENT_GAME_ID")
    private Game parentGame;

    @Column(name = "PARENT_GAME_ID", insertable = false, updatable = false)
    private Long parentGameId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBranchDepth() {
        return branchDepth;
    }

    public Game branchDepth(Integer branchDepth) {
        this.branchDepth = branchDepth;
        return this;
    }

    public void setBranchDepth(Integer branchDepth) {
        this.branchDepth = branchDepth;
    }

    public LocalDate getTimeUpdated() {
        return timeUpdated;
    }

    public Game timeUpdated(LocalDate timeUpdated) {
        this.timeUpdated = timeUpdated;
        return this;
    }

    public void setTimeUpdated(LocalDate timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public LocalDate getTimeCreated() {
        return timeCreated;
    }

    public Game timeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }

    public void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getCurrentState() {
        return currentState;
    }

    public Game currentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Boolean isIsRoot() {
        return isRoot;
    }

    public Game isRoot(Boolean isRoot) {
        this.isRoot = isRoot;
        return this;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }

    public Set<Game> getChildGames() {
        return childGames;
    }

    public Game childGames(Set<Game> games) {
        this.childGames = games;
        return this;
    }

    public Game addChildGames(Game game) {
        this.childGames.add(game);
        game.setParentGame(this);
        return this;
    }

    public Game removeChildGames(Game game) {
        this.childGames.remove(game);
        game.setParentGame(null);
        return this;
    }

    public void setChildGames(Set<Game> games) {
        this.childGames = games;
    }

    public Set<Move> getGameMoves() {
        return gameMoves;
    }

    public Game gameMoves(Set<Move> moves) {
        this.gameMoves = moves;
        return this;
    }

    public Game addGameMoves(Move move) {
        this.gameMoves.add(move);
        move.setGame(this);
        return this;
    }

    public Game removeGameMoves(Move move) {
        this.gameMoves.remove(move);
        move.setGame(null);
        return this;
    }

    public void setGameMoves(Set<Move> moves) {
        this.gameMoves = moves;
    }

    public User getPlayerBlack() {
        return playerBlack;
    }

    public Game playerBlack(User user) {
        this.playerBlack = user;
        return this;
    }

    public void setPlayerBlack(User user) {
        this.playerBlack = user;
    }

    public User getPlayerWhite() {
        return playerWhite;
    }

    public Game playerWhite(User user) {
        this.playerWhite = user;
        return this;
    }

    public void setPlayerWhite(User user) {
        this.playerWhite = user;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public Game updatedByUser(User user) {
        this.updatedByUser = user;
        return this;
    }

    public void setUpdatedByUser(User user) {
        this.updatedByUser = user;
    }

    public Game getParentGame() {
        return parentGame;
    }

    public Game parentGame(Game game) {
        this.parentGame = game;
        return this;
    }

    public void setParentGame(Game game) {
        this.parentGame = game;
    }

    public Long getParentGameId() {
        return parentGameId;
    }

    public void setParentGameId(Long parentGameId) {
        this.parentGameId = parentGameId;
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
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", branchDepth=" + getBranchDepth() +
            ", timeUpdated='" + getTimeUpdated() + "'" +
            ", timeCreated='" + getTimeCreated() + "'" +
            ", currentState='" + getCurrentState() + "'" +
            ", isRoot='" + isIsRoot() + "'" +
            "}";
    }
}
