package com.github.fromi.chess.material;

import static com.github.fromi.chess.material.util.Squares.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class SquareTest {

    @Test
    public void test_squares_between_rank() {
        assertThat(D4.squaresInPathTo(H4).collect(toList()), hasSize(3));
        assertThat(D4.squaresInPathTo(H4).collect(toList()), containsInAnyOrder(E4, F4, G4));

        assertThat(A2.squaresInPathTo(F2).collect(toList()), hasSize(4));
        assertThat(A2.squaresInPathTo(F2).collect(toList()), containsInAnyOrder(B2, C2, D2, E2));
    }

    @Test
    public void test_squares_between_files() {
        assertThat(A1.squaresInPathTo(A8).collect(toList()), hasSize(6));
        assertThat(A1.squaresInPathTo(A8).collect(toList()), containsInAnyOrder(A2, A3, A4, A5, A6, A7));

        assertThat(F3.squaresInPathTo(F5).collect(toList()), hasSize(1));
        assertThat(F3.squaresInPathTo(F5).collect(toList()), hasItem(F4));
    }

    @Test
    public void test_squares_between_diagonal() {
        assertTrue(A2.squaresInPathTo(B3).count() == 0);

        assertThat(A2.squaresInPathTo(C4).collect(toList()), hasSize(1));
        assertThat(A2.squaresInPathTo(C4).collect(toList()), hasItem(B3));

        assertThat(A7.squaresInPathTo(G1).collect(toList()), hasSize(5));
        assertThat(A7.squaresInPathTo(G1).collect(toList()), containsInAnyOrder(B6, C5, D4, E3, F2));

        assertThat(G1.squaresInPathTo(C5).collect(toList()), hasSize(3));
        assertThat(G1.squaresInPathTo(C5).collect(toList()), containsInAnyOrder(F2, E3, D4));

        assertThat(H7.squaresInPathTo(C2).collect(toList()), hasSize(4));
        assertThat(H7.squaresInPathTo(C2).collect(toList()), containsInAnyOrder(D3, E4, F5, G6));
    }

    @Test
    public void if_not_a_line_or_diagonal_path_is_empty() {
        assertThat(B3.squaresInPathTo(F6).count(), equalTo(0L));
    }

    @Test
    public void adjacent_squares() {
        List<Square> adjacentSquares = B8.adjacentSquares().collect(toList());
        assertThat(adjacentSquares, hasSize(5));
        assertThat(adjacentSquares, containsInAnyOrder(A8, C8, A7, B7, C7));
    }

    @Test
    public void has_a_nice_label() {
        assertThat(F5.toString(), equalTo("f5"));
    }
}