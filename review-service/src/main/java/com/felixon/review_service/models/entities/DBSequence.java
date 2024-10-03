package com.felixon.review_service.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBSequence {

    @Id
    private String id;

    private long seq;
}
