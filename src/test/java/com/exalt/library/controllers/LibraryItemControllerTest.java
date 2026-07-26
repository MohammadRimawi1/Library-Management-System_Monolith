package com.exalt.library.controllers;

import com.exalt.library.dto.AuthorDTO;
import com.exalt.library.dto.LibraryItemDTO;
import com.exalt.library.exceptions.ItemNotFoundException;
import com.exalt.library.exceptions.handler.GlobalExceptionHandler;
import com.exalt.library.models.libraryitems.physicalitems.BookPhysical;
import com.exalt.library.services.LibraryItemServices;
import com.exalt.library.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link LibraryItemController}.
 * @author Mohammad Rimawi
 */
@WebMvcTest(controllers = LibraryItemController.class)
@Import(GlobalExceptionHandler.class)
class LibraryItemControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibraryItemServices libraryItemServices;

    @MockitoBean
    private JwtService jwtService;

    /**
     * Retrieving all library items should return HTTP 200 and the item list.
     */
    @Test
    @WithMockUser
    void findAll_returns200WithList() throws Exception {
        BookPhysical book = new BookPhysical();
        book.setTitle("Clean Code");
        when(libraryItemServices.getAllItems()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/library-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Clean Code"));
    }

    /**
     * An existing library item should be returned with HTTP 200.
     */
    @Test
    @WithMockUser
    void findById_returns200_whenFound() throws Exception {
        BookPhysical book = new BookPhysical();
        book.setTitle("Clean Code");
        when(libraryItemServices.findItemById("123")).thenReturn(book);

        mockMvc.perform(get("/api/library-items/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }

    /**
     * A missing library item should return HTTP 404.
     */
    @Test
    @WithMockUser
    void findById_returns404_whenNotFound() throws Exception {
        when(libraryItemServices.findItemById("missing"))
                .thenThrow(new ItemNotFoundException("Item not found"));

        mockMvc.perform(get("/api/library-items/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Item not found"));
    }

    /**
     * Librarians should be able to create library items.
     */
    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void create_returns201_withCreatedItem() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("J. Doe", "American", null);
        LibraryItemDTO request = new LibraryItemDTO("BookPhysical", "Clean Code", 3, "A book about code", "English", authorDTO);

        BookPhysical created = new BookPhysical();
        created.setTitle("Clean Code");
        created.setNumOfCopies(3);
        when(libraryItemServices.createItem(request)).thenReturn(created);

        mockMvc.perform(post("/api/library-items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }

    /**
     * Creating an item with an unknown type should return HTTP 400.
     */
    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void create_returns400_whenTypeIsInvalid() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO("J. Doe", "American", null);
        LibraryItemDTO request = new LibraryItemDTO("UnknownType", "Clean Code", 3, "A book about code", "English", authorDTO);

        when(libraryItemServices.createItem(request))
                .thenThrow(new IllegalArgumentException("Unknown item type: UnknownType"));

        mockMvc.perform(post("/api/library-items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unknown item type: UnknownType"));
    }
}