package com.hamermesh.yeah.repository;

import com.hamermesh.yeah.domain.Move;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Move entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    @Query("select move from Move move where move.createdByUser.login = ?#{principal.username}")
    List<Move> findByCreatedByUserIsCurrentUser();

}
