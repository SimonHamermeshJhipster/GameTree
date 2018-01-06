package com.hamermesh.yeah.service;

import com.hamermesh.yeah.domain.Move;
import com.hamermesh.yeah.repository.MoveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Move.
 */
@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final MoveRepository moveRepository;

    public MoveService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    /**
     * Save a move.
     *
     * @param move the entity to save
     * @return the persisted entity
     */
    public Move save(Move move) {
        log.debug("Request to save Move : {}", move);
        return moveRepository.save(move);
    }

    /**
     * Get all the moves.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Move> findAll() {
        log.debug("Request to get all Moves");
        return moveRepository.findAll();
    }

    /**
     * Get one move by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Move findOne(Long id) {
        log.debug("Request to get Move : {}", id);
        return moveRepository.findOne(id);
    }

    /**
     * Delete the move by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Move : {}", id);
        moveRepository.delete(id);
    }
}
