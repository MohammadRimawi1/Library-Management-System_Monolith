//package com.exalt.library.services.factory;
//
//import com.exalt.library.models.libraryitems.LibraryItem;
//import com.exalt.library.models.libraryitems.onlineitems.BookOnline;
//import com.exalt.library.models.libraryitems.onlineitems.StoryOnline;
//import com.exalt.library.models.libraryitems.physicalitems.BookPhysical;
//import com.exalt.library.models.libraryitems.physicalitems.StoryPhysical;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertInstanceOf;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
///**
// * Unit tests for {@link LibraryItemFactory}.
// * @author Mohammad Rimawi
// */
//class LibraryItemFactoryTest {
//
//    /**
//     * create("BookPhysical") should return a BookPhysical instance.
//     */
//    @Test
//    void create_returnsBookPhysical_whenTypeIsBookPhysical() {
//        LibraryItem item = LibraryItemFactory.create("BookPhysical");
//        assertInstanceOf(BookPhysical.class, item);
//    }
//
//    /**
//     * create("StoryPhysical") should return a StoryPhysical instance.
//     */
//    @Test
//    void create_returnsStoryPhysical_whenTypeIsStoryPhysical() {
//        LibraryItem item = LibraryItemFactory.create("StoryPhysical");
//        assertInstanceOf(StoryPhysical.class, item);
//    }
//
//    /**
//     * create("BookOnline") should return a BookOnline instance.
//     */
//    @Test
//    void create_returnsBookOnline_whenTypeIsBookOnline() {
//        LibraryItem item = LibraryItemFactory.create("BookOnline");
//        assertInstanceOf(BookOnline.class, item);
//    }
//
//    /**
//     * create("StoryOnline") should return a StoryOnline instance.
//     */
//    @Test
//    void create_returnsStoryOnline_whenTypeIsStoryOnline() {
//        LibraryItem item = LibraryItemFactory.create("StoryOnline");
//        assertInstanceOf(StoryOnline.class, item);
//    }
//
//    /**
//     * create should throw for an unrecognized type name.
//     */
//    @Test
//    void create_throwsIllegalArgumentException_whenTypeIsUnknown() {
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
//                () -> LibraryItemFactory.create("Magazine"));
//        assertInstanceOf(String.class, ex.getMessage());
//    }
//
//    /**
//     * create should throw for a null type name (switch statement fails on null selector).
//     */
//    @Test
//    void create_throws_whenTypeIsNull() {
//        assertThrows(NullPointerException.class, () -> LibraryItemFactory.create(null));
//    }
//}