package com.felixon.loan_service.models.entities;

import com.felixon.loan_service.LoanServiceApplication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "book_loan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(name = "book_title")
    private String bookTitle;
    @Column(name = "loan_date")
    private LocalDate loanDate;
    private boolean returned = false;

    @PrePersist
    public void prePersistLoan(){
        this.loanDate = LocalDate.now();

    }

    @Override
    public String toString() {
        return "BookLoan{" +
                "id=" + id +
                ", user_id=" + username +
                ", book_id='" + bookTitle + '\'' +
                ", loanDate=" + loanDate +
                ", returned=" + returned +
                '}';
    }
}
