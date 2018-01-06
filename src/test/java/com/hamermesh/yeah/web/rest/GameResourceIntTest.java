package com.hamermesh.yeah.web.rest;

import com.hamermesh.yeah.GameTreeApp;

import com.hamermesh.yeah.domain.Game;
import com.hamermesh.yeah.repository.GameRepository;
import com.hamermesh.yeah.service.GameService;
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
 * Test class for the GameResource REST controller.
 *
 * @see GameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GameTreeApp.class)
public class GameResourceIntTest {

    private static final Integer DEFAULT_BRANCH_DEPTH = 1;
    private static final Integer UPDATED_BRANCH_DEPTH = 2;

    private static final LocalDate DEFAULT_TIME_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TIME_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CURRENT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_STATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ROOT = false;
    private static final Boolean UPDATED_IS_ROOT = true;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGameMockMvc;

    private Game game;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GameResource gameResource = new GameResource(gameService);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
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
    public static Game createEntity(EntityManager em) {
        Game game = new Game()
            .branchDepth(DEFAULT_BRANCH_DEPTH)
            .timeUpdated(DEFAULT_TIME_UPDATED)
            .timeCreated(DEFAULT_TIME_CREATED)
            .currentState(DEFAULT_CURRENT_STATE)
            .isRoot(DEFAULT_IS_ROOT);
        return game;
    }

    @Before
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game
        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getBranchDepth()).isEqualTo(DEFAULT_BRANCH_DEPTH);
        assertThat(testGame.getTimeUpdated()).isEqualTo(DEFAULT_TIME_UPDATED);
        assertThat(testGame.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
        assertThat(testGame.getCurrentState()).isEqualTo(DEFAULT_CURRENT_STATE);
        assertThat(testGame.isIsRoot()).isEqualTo(DEFAULT_IS_ROOT);
    }

    @Test
    @Transactional
    public void createGameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game with an existing ID
        game.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBranchDepthIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setBranchDepth(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setTimeUpdated(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setTimeCreated(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setCurrentState(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsRootIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setIsRoot(null);

        // Create the Game, which fails.

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchDepth").value(hasItem(DEFAULT_BRANCH_DEPTH)))
            .andExpect(jsonPath("$.[*].timeUpdated").value(hasItem(DEFAULT_TIME_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].currentState").value(hasItem(DEFAULT_CURRENT_STATE.toString())))
            .andExpect(jsonPath("$.[*].isRoot").value(hasItem(DEFAULT_IS_ROOT.booleanValue())));
    }

    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.branchDepth").value(DEFAULT_BRANCH_DEPTH))
            .andExpect(jsonPath("$.timeUpdated").value(DEFAULT_TIME_UPDATED.toString()))
            .andExpect(jsonPath("$.timeCreated").value(DEFAULT_TIME_CREATED.toString()))
            .andExpect(jsonPath("$.currentState").value(DEFAULT_CURRENT_STATE.toString()))
            .andExpect(jsonPath("$.isRoot").value(DEFAULT_IS_ROOT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findOne(game.getId());
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame
            .branchDepth(UPDATED_BRANCH_DEPTH)
            .timeUpdated(UPDATED_TIME_UPDATED)
            .timeCreated(UPDATED_TIME_CREATED)
            .currentState(UPDATED_CURRENT_STATE)
            .isRoot(UPDATED_IS_ROOT);

        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGame)))
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getBranchDepth()).isEqualTo(UPDATED_BRANCH_DEPTH);
        assertThat(testGame.getTimeUpdated()).isEqualTo(UPDATED_TIME_UPDATED);
        assertThat(testGame.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testGame.getCurrentState()).isEqualTo(UPDATED_CURRENT_STATE);
        assertThat(testGame.isIsRoot()).isEqualTo(UPDATED_IS_ROOT);
    }

    @Test
    @Transactional
    public void updateNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Create the Game

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(game)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        // Initialize the database
        gameService.save(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Get the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Game.class);
        Game game1 = new Game();
        game1.setId(1L);
        Game game2 = new Game();
        game2.setId(game1.getId());
        assertThat(game1).isEqualTo(game2);
        game2.setId(2L);
        assertThat(game1).isNotEqualTo(game2);
        game1.setId(null);
        assertThat(game1).isNotEqualTo(game2);
    }
}
