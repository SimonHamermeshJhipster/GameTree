package com.hamermesh.yeah.repository;

import com.hamermesh.yeah.domain.Game;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByPlayerBlackLogin(String login);

}
