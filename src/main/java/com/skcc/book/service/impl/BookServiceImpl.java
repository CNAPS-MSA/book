package com.skcc.book.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.book.adaptor.BookProducer;
import com.skcc.book.domain.event.CatalogChanged;
import com.skcc.book.domain.BookReservation;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.service.BookService;
import com.skcc.book.domain.Book;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.web.rest.dto.BookInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final BookProducer bookProducer;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookServiceImpl(BookRepository bookRepository, BookProducer bookProducer) {
        this.bookRepository = bookRepository;
        this.bookProducer = bookProducer;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<BookInfoDTO> getBookInfo(List<Long> bookIds, Long userId) {
        List<BookInfoDTO> bookInfoDTOList = new ArrayList<>();
        for(Long bookId: bookIds){
            Book book = bookRepository.findById(bookId).get();
                if(book.getBookStatus().equals(BookStatus.AVAILABLE)){ //대여가능상태
                    if(book.getBookReservations().size()==0) { //예약자 없으면
                        bookInfoDTOList.add(new BookInfoDTO(bookId, bookRepository.findById(bookId).get().getTitle()));
                    }else{
                        if(book.checkReservationContains(userId)){ //예약자 리스트에 있으면
                            if(book.isFirstReservation(userId)){ //예약자 1번인 경우
                                bookInfoDTOList.add(new BookInfoDTO(bookId, bookRepository.findById(bookId).get().getTitle())); //예약
                                book=book.removeBookReservationByUserId(userId); // 예약자리스트에서 삭제
                                bookRepository.save(book);
                            }
                        }else{ //예약자 리스트에 없으면
                            book= makeReservation(book, userId, (long)book.getBookReservations().size());
                        }
                    }
                }
                else{ //대여 불가능 상태
                    if(book.getBookReservations().size()>0) {
                        if (!book.checkReservationContains(userId)) { //예약자 리스트에 없으면
                            log.debug("check Reservation", false);
                            book = makeReservation(book, userId, (long) book.getBookReservations().size()); //리스트에 추가
                        }
                    }else{
                        book = book.bookReservations(new HashSet<>());
                        book = makeReservation(book, userId, (long) book.getBookReservations().size());
                    }
                    System.out.println("book Reservation Size:"+ book.getBookReservations().size());
                    //log.debug("book Reservation Size:", book.getbookReservations().size());
                }
        }
        return bookInfoDTOList;
    }
    //책의 예약자 목록에 추가
    @Override
    @Transactional
    public Book makeReservation(Book book, Long userId, Long bookResCnt) {

        book=book.addBookReservation(new BookReservation(userId, bookResCnt+1));
        System.out.println("Make Reservation: ");
        book.getBookReservations().forEach(b-> System.out.println(b.getUserId()+"&&"+b.getReservedSeqNo()));
        book=bookRepository.save(book);
        return book;
    }

    @Override
    public Book getBooks(Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @Override
    public void sendBookCatalogEvent(String eventType,Long bookId) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book book = bookRepository.findById(bookId).get();
        CatalogChanged catalogChanged = new CatalogChanged();
        if(eventType.equals("NEW_BOOK") || eventType.equals("UPDATE_BOOK")) {
            catalogChanged.setBookId(book.getId());
            catalogChanged.setAuthor(book.getAuthor());
            catalogChanged.setClassification(book.getClassification().toString());
            catalogChanged.setDescription(book.getDescription());
            catalogChanged.setPublicationDate(book.getPublicationDate().format(fmt));
            catalogChanged.setTitle(book.getTitle());
            catalogChanged.setEventType(eventType);
            catalogChanged.setRented(!book.getBookStatus().equals(BookStatus.AVAILABLE));
            catalogChanged.setRentCnt((long) 0);
            bookProducer.sendBookCreateEvent(catalogChanged);
        }else if(eventType.equals("DELETE_BOOK")){
            catalogChanged.setEventType(eventType);
            catalogChanged.setBookId(book.getId());
            bookProducer.sendBookDeleteEvent(catalogChanged);
        }
    }


}
