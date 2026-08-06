//package com.exalt.library.services.factory;
//
//import com.exalt.library.models.libraryitems.LibraryItem;
//import com.exalt.library.models.libraryitems.onlineitems.BookOnline;
//import com.exalt.library.models.libraryitems.physicalitems.BookPhysical;
//import com.exalt.library.services.borrowtype.InHandBorrowStrategyService;
//import com.exalt.library.services.borrowtype.OnlineBorrowStrategyService;
//import com.exalt.library.services.strategies.BorrowStrategy;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.mock;
//
///**
// * Unit tests for {@link BorrowStrategyFactory}.
// * @author Mohammad Rimawi
// */
//class BorrowStrategyFactoryTest {
//
//    private InHandBorrowStrategyService inHandStrategy;
//    private OnlineBorrowStrategyService onlineStrategy;
//    private BorrowStrategyFactory factory;
//
//    /**
//     * sets up the factory with mocked strategy implementations before each test.
//     */
//    @BeforeEach
//    void setUp() {
//        inHandStrategy = mock(InHandBorrowStrategyService.class);
//        onlineStrategy = mock(OnlineBorrowStrategyService.class);
//        factory = new BorrowStrategyFactory(inHandStrategy, onlineStrategy);
//    }
//
//    /**
//     * resolve should return the online strategy when the item is an OnlineItem subtype.
//     */
//    @Test
//    void resolve_returnsOnlineStrategy_whenItemIsOnline() {
//        LibraryItem item = new BookOnline();
//
//        BorrowStrategy resolved = factory.resolve(item);
//
//        assertSame(onlineStrategy, resolved);
//    }
//
//    /**
//     * resolve should return the in-hand strategy when the item is a physical item.
//     */
//    @Test
//    void resolve_returnsInHandStrategy_whenItemIsPhysical() {
//        LibraryItem item = new BookPhysical();
//
//        BorrowStrategy resolved = factory.resolve(item);
//
//        assertSame(inHandStrategy, resolved);
//    }
//}