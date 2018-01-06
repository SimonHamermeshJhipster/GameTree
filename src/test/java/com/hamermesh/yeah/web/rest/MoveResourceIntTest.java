package com.hamermesh.yeah.web.rest;

import com.hamermesh.yeah.GameTreeApp;

import com.hamermesh.yeah.domain.Move;
import com.hamermesh.yeah.domain.Game;
import com.hamermesh.yeah.repository.MoveRepository;
import com.hamermesh.yeah.service.MoveService;
import com.hamermesh.yeah.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.hamermesh.yeah.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MoveResource REST controller.
 *
 * @see MoveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameTreeApp.class)
public class MoveResourceIntTest {

    private static final String DEFAULT_CURRENT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_PREV_STATE = "AAAAAAAAAA";
    private static final String UPDATED_PREV_STATE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TIME_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_CREATED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private MoveService moveService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMoveMockMvc;

    private Move move;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MoveResource moveResource = new MoveResource(moveService);
        this.restMoveMockMvc = MockMvcBuilders.standaloneSetup(moveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Move createEntity(EntityManager em) {
        Move move = new Move()
            .currentState(DEFAULT_CURRENT_STATE)
            .prevState(DEFAULT_PREV_STATE)
            .timeCreated(DEFAULT_TIME_CREATED);
        // Add required entity
        Game game = GameResourceIntTest.createEntity(em);
        em.persist(game);
        em.flush();
        move.setGame(game);
        return move;
    }

    @Before
    public void initTest() {
        move = createEntity(em);
    }

    @Test
    @Transactional
    public void createMove() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();

        // Create the Move
        restMoveMockMvc.perform(post("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isCreated());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate + 1);
        Move testMove = moveList.get(moveList.size() - 1);
        assertThat(testMove.getCurrentState()).isEqualTo(DEFAULT_CURRENT_STATE);
        assertThat(testMove.getPrevState()).isEqualTo(DEFAULT_PREV_STATE);
        assertThat(testMove.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
    }

    @Test
    @Transactional
    public void createMoveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();

        // Create the Move with an existing ID
        move.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoveMockMvc.perform(post("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isBadRequest());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCurrentStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moveRepository.findAll().size();
        // set the field null
        move.setCurrentState(null);

        // Create the Move, which fails.

        restMoveMockMvc.perform(post("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isBadRequest());

        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrevStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moveRepository.findAll().size();
        // set the field null
        move.setPrevState(null);

        // Create the Move, which fails.

        restMoveMockMvc.perform(post("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isBadRequest());

        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = moveRepository.findAll().size();
        // set the field null
        move.setTimeCreated(null);

        // Create the Move, which fails.

        restMoveMockMvc.perform(post("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isBadRequest());

        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMoves() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        // Get all the moveList
        restMoveMockMvc.perform(get("/api/moves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(move.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentState").value(hasItem(DEFAULT_CURRENT_STATE.toString())))
            .andExpect(jsonPath("$.[*].prevState").value(hasItem(DEFAULT_PREV_STATE.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getMove() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        // Get the move
        restMoveMockMvc.perform(get("/api/moves/{id}", move.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(move.getId().intValue()))
            .andExpect(jsonPath("$.currentState").value(DEFAULT_CURRENT_STATE.toString()))
            .andExpect(jsonPath("$.prevState").value(DEFAULT_PREV_STATE.toString()))
            .andExpect(jsonPath("$.timeCreated").value(DEFAULT_TIME_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMove() throws Exception {
        // Get the move
        restMoveMockMvc.perform(get("/api/moves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMove() throws Exception {
        // Initialize the database
        moveService.save(move);

        int databaseSizeBeforeUpdate = moveRepository.findAll().size();

        // Update the move
        Move updatedMove = moveRepository.findOne(move.getId());
        // Disconnect from session so that the updates on updatedMove are not directly saved in db
        em.detach(updatedMove);
        updatedMove
            .currentState(UPDATED_CURRENT_STATE)
            .prevState(UPDATED_PREV_STATE)
            .timeCreated(UPDATED_TIME_CREATED);

        restMoveMockMvc.perform(put("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMove)))
            .andExpect(status().isOk());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeUpdate);
        Move testMove = moveList.get(moveList.size() - 1);
        assertThat(testMove.getCurrentState()).isEqualTo(UPDATED_CURRENT_STATE);
        assertThat(testMove.getPrevState()).isEqualTo(UPDATED_PREV_STATE);
        assertThat(testMove.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingMove() throws Exception {
        int databaseSizeBeforeUpdate = moveRepository.findAll().size();

        // Create the Move

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMoveMockMvc.perform(put("/api/moves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(move)))
            .andExpect(status().isCreated());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMove() throws Exception {
        // Initialize the database
        moveService.save(move);

        int databaseSizeBeforeDelete = moveRepository.findAll().size();

        // Get the move
        restMoveMockMvc.perform(delete("/api/moves/{id}", move.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Move.class);
        Move move1 = new Move();
        move1.setId(1L);
        Move move2 = new Move();
        move2.setId(move1.getId());
        assertThat(move1).isEqualTo(move2);
        move2.setId(2L);
        assertThat(move1).isNotEqualTo(move2);
        move1.setId(null);
        assertThat(move1).isNotEqualTo(move2);
    }
}
