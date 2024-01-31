package com.shop.backend.dtos;
public record SignUpDto (String firstName, String lastName, String login, char[] password, String role, boolean verified,  boolean oauth_provider) { }

