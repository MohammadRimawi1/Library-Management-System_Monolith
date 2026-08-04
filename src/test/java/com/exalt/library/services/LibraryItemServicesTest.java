//package com.exalt.library.services;
//
//import com.exalt.library.dto.AuthorDTO;
//import com.exalt.library.dto.LibraryItemDTO;
//import com.exalt.library.exceptions.notfound.ItemNotFoundException;
//import com.exalt.library.models.libraryitems.LibraryItem;
//import com.exalt.library.repositories.LibraryItemRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link LibraryItemServices}.
// * @author Mohammad Rimawi
// */
//@ExtendWith(MockitoExtension.class)
//class LibraryItemServicesTest {
//
//    @Mock
//    private LibraryItemRepository libraryItemRepository;
//
//    @InjectMocks
//    private LibraryItemServices libraryItemServices;
//
//    /**
//     * An existing library item should be returned by ID.
//     */
//    @Test
//    void findItemById_returnsItem_whenExists() {
//        LibraryItem item = mock(LibraryItem.class);
//        when(libraryItemRepository.findById("123")).thenReturn(Optional.of(item));
//
//        LibraryItem result = libraryItemServices.findItemById("123");
//
//        assertEquals(item, result);
//    }
//
//    /**
//     * A missing library item ID should throw an exception.
//     */
//    @Test
//    void findItemById_throws_whenNotFound() {
//        when(libraryItemRepository.findById("missing")).thenReturn(Optional.empty());
//
//        assertThrows(ItemNotFoundException.class,
//                () -> libraryItemServices.findItemById("missing"));
//    }
//
//    /**
//     * Existing library item IDs should return {@code true}.
//     */
//    @Test
//    void itemExists_returnsTrue_whenExists() {
//        when(libraryItemRepository.existsById("123")).thenReturn(true);
//
//        assertTrue(libraryItemServices.itemExists("123"));
//    }
//
//    /**
//     * All stored library items should be returned.
//     */
//    @Test
//    void getAllItems_returnsFullList() {
//        LibraryItem item1 = mock(LibraryItem.class);
//        LibraryItem item2 = mock(LibraryItem.class);
//        when(libraryItemRepository.findAll()).thenReturn(List.of(item1, item2));
//
//        List<LibraryItem> result = libraryItemServices.getAllItems();
//
//        assertEquals(2, result.size());
//    }
//
//    /**
//     * The total library item count should be returned.
//     */
//    @Test
//    void getItemCount_returnsCorrectCount() {
//        when(libraryItemRepository.count()).thenReturn(3L);
//
//        assertEquals(3, libraryItemServices.getItemCount());
//    }
//
//    /**
//     * A valid library item should be saved and returned.
//     */
//    @Test
//    void createItem_savesAndReturnsItem_whenValid() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Computer Networks",
//                2, "A book about networks", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        ArgumentCaptor<LibraryItem> captor = ArgumentCaptor.forClass(LibraryItem.class);
//        when(libraryItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        LibraryItem result = libraryItemServices.createItem(dto);
//
//        verify(libraryItemRepository).save(captor.capture());
//        assertEquals("Computer Networks", captor.getValue().getTitle());
//        assertEquals("Computer Networks", result.getTitle());
//    }
//
//    /**
//     * Invalid library items should not be saved.
//     */
//    @Test
//    void createItem_throws_whenValidationFails() {
//        LibraryItemDTO invalidDto = new LibraryItemDTO("", "Computer Networks",
//                2, "A book about networks", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class,
//                () -> libraryItemServices.createItem(invalidDto));
//
//        verify(libraryItemRepository, never()).save(any());
//    }
//}