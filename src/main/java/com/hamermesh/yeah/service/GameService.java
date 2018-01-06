package com.hamermesh.yeah.service;

import com.hamermesh.yeah.domain.Game;
import com.hamermesh.yeah.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Save a game.
     *
     * @param game the entity to save
     * @return the persisted entity
     */
    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);
        return gameRepository.save(game);
    }

    /**
     * Get all the games.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Game> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        return gameRepository.findAll(pageable);
    }

    /**
     * Get one game by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Game findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findOne(id);
    }


    /**
     * Get User's Black Games
     *
     * @param id the id of the User
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Game> findUsersBlackGames() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Request to get User {}'s Black Games", username);
        return  gameRepository.findByPlayerBlackLogin(username);
    }

    /**
     * Delete the game by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.delete(id);
    }
}
