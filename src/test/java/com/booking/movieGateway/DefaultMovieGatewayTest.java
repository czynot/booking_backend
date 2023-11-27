package com.booking.movieGateway;

import com.booking.config.AppConfig;
import com.booking.movieGateway.DefaultMovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMovieGatewayTest {

    private static final String SINGLE_MOVIE_RESPONSE = "{\"imdbID\":\"id\",\"Title\":\"title\",\"Runtime\":\"120 min\",\"Plot\":\"plot\"}";
    private static final String MULTIPLE_MOVIES_RESPONSE = "[{\"imdbID\":\"id1\",\"Title\":\"title1\",\"Runtime\":\"90 min\",\"Plot\":\"plot1\"}," +
            "{\"imdbID\":\"id2\",\"Title\":\"title2\",\"Runtime\":\"100 min\",\"Plot\":\"plot2\"}]";

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void should_get_movie_from_service() throws IOException, FormatException {
        mockWebServer.enqueue(new MockResponse().setBody(SINGLE_MOVIE_RESPONSE));

        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(mockWebServer.url("/").toString());

        final var defaultMovieGateway = new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(), new ObjectMapper());

        final var actualMovie = defaultMovieGateway.getMovieFromId("id");

        final var expectedMovie = new Movie("id", "title", Duration.ofMinutes(120), "plot");
        assertThat(actualMovie, is(equalTo(expectedMovie)));
    }

    @Test
    void should_get_list_of_all_movies_from_service() throws IOException, FormatException {
        mockWebServer.enqueue(new MockResponse().setBody(MULTIPLE_MOVIES_RESPONSE));

        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(mockWebServer.url("/").toString());

        final var defaultMovieGateway = new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(), new ObjectMapper());

        final List<Movie> movieList = defaultMovieGateway.getAllMovies();

        final List<Movie> expectedMovies = Arrays.asList(
                new Movie("id1", "title1", Duration.ofMinutes(90), "plot1"),
                new Movie("id2", "title2", Duration.ofMinutes(100), "plot2")
        );

        assertThat(movieList, is(equalTo(expectedMovies)));
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
