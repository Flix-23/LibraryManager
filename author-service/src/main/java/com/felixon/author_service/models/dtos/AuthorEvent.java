package com.felixon.author_service.models.dtos;

public record AuthorEvent (String name, String email, String biography, String nationality) {
}
