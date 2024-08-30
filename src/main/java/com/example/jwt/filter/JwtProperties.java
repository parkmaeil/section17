package com.example.jwt.filter;

public interface JwtProperties {
    public String SECRET="cosin";
    public int EXPIRATION_TIME=60000*10;
    public String TOKEN_PREFIX="Bearer ";
    public String HEADER_STRING="Authorization";
}
