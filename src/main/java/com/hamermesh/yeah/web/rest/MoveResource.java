package com.hamermesh.yeah.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hamermesh.yeah.domain.Move;
import com.hamermesh.yeah.service.MoveService;
import com.hamermesh.yeah.web.rest.errors.BadRequestAlertException;
import com.hamermesh.yeah.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Move.
 */
@RestController
@RequestMapping("/api")
public class MoveResource {

    private final Logger log = LoggerFactory.getLogger(MoveResource.class);

    private static final String ENTITY_NAME = "move";

    private final MoveService moveService;

    public MoveResource(MoveService moveService) {
        this.moveService = moveService;
    }

    /**
     * POST  /moves : Create a new move.
     *
     * @param move the move to create
     * @return the ResponseEntity with status 201 (Created) and with body the new move, or with status 400 (Bad Request) if the move has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/moves")
    @Timed
    public ResponseEntity<Move> createMove(@Valid @RequestBody Move move) throws URISyntaxException {
        log.debug("REST request to save Move : {}", move);
        if (move.getId() != null) {
            throw new BadRequestAlertException("A new move cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Move result = moveService.save(move);
        return ResponseEntity.created(new URI("/api/moves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /moves : Updates an existing move.
     *
     * @param move the move to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated move,
     * or with status 400 (Bad Request) if the move is not valid,
     * or with status 500 (Internal Server Error) if the move couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/moves")
    @Timed
    public ResponseEntity<Move> updateMove(@Valid @RequestBody Move move) throws URISyntaxException {
        log.debug("REST request to update Move : {}", move);
        if (move.getId() == null) {
            return createMove(move);
        }
        Move result = moveService.save(move);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, move.getId().toString()))
            .body(result);
    }

    /**
     * GET  /moves : get all the moves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of moves in body
     */
    @GetMapping("/moves")
    @Timed
    public List<Move> getAllMoves() {
        log.debug("REST request to get all Moves");
        return moveService.findAll();
        }

    /**
     * GET  /moves/:id : get the "id" move.
     *
     * @param id the id of the move to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the move, or with status 404 (Not Found)
     */
    @GetMapping("/moves/{id}")
    @Timed
    public ResponseEntity<Move> getMove(@PathVariable Long id) {
        log.debug("REST request to get Move : {}", id);
        Move move = moveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(move));
    }

    /**
     * DELETE  /moves/:id : delete the "id" move.
     *
     * @param id the id of the move to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/moves/{id}")
    @Timed
    public ResponseEntity<Void> deleteMove(@PathVariable Long id) {
        log.debug("REST request to delete Move : {}", id);
        moveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
