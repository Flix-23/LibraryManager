package com.felixon.loan_service.services;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;
import com.felixon.loan_service.repositories.BookLoanRepository;
import com.felixon.loan_service.services.consumer.BookEventConsumer;
import com.felixon.loan_service.services.consumer.UserEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookLoanServiceImpl implements BookLoanService{

    @Autowired
    private BookLoanRepository loanRepository;

    private ModelMapper model = new ModelMapper();


    @Override
    @Transactional
    public BookLoanResponse addLoan(BookLoanRequest bookLoanRequest) {
            var bookLoan = BookLoan.builder()
                    .bookTitle(BookEventConsumer.bookEventTitle)
                    .username(UserEventConsumer.userEventName)
                    .build();

        this.loanRepository.save(bookLoan);
        log.info("loan approved {}", bookLoan);

        return model.map(bookLoan, BookLoanResponse.class);

    }

    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponse> getAllLoan() {

        var bookLoan = loanRepository.findAll();
        return bookLoan.stream().map(this::mapToBookLoanResponse).toList();
    }

    private BookLoanResponse mapToBookLoanResponse(BookLoan bookLoan) {
        return model.map(bookLoan, BookLoanResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookLoanResponse findById(Long idLoan) {
        Optional<BookLoan> bookLoan = this.loanRepository.findById(idLoan);
        return model.map(bookLoan, BookLoanResponse.class);
    }

    @Override
    @Transactional
    public BookLoanResponse updateLoan(Long idLoan, BookLoanRequest bookLoanRequest) {
        Optional<BookLoan> optionalBookLoan = loanRepository.findById(idLoan);

        optionalBookLoan.ifPresent(loanDB -> {

            loanDB.setReturned(bookLoanRequest.isReturned());

            this.loanRepository.save(loanDB);
        });

        return model.map(optionalBookLoan, BookLoanResponse.class);
    }
}
