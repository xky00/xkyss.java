package com.xkyss.quarkus.server.dto;

import com.xkyss.quarkus.server.error.ErrorCode;

import java.util.Objects;

public class Response<T> {
    private Integer code;

    private String message;

    private T data;

    private PageInfo page;

    public Response(Integer code) {
        this(code, null, null);
    }

    public Response(Integer code, String message) {
        this(code, message, null);
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(Integer code, String message, T data, PageInfo pageInfo) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.page = pageInfo;
    }

    public static <T> Response<T> success() {
        return new Response<>(ErrorCode.成功);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ErrorCode.成功, null, data);
    }

    public static <T> Response<T> success(T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.成功, null, data, pageInfo);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(ErrorCode.成功, message, data);
    }

    public static <T> Response<T> success(String message, T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.成功, message, data, pageInfo);
    }

    public static <T> Response<T> warn() {

        return new Response<>(ErrorCode.警告);
    }

    public static <T> Response<T> warn(T data) {
        return new Response<>(ErrorCode.警告, null, data);
    }

    public static <T> Response<T> warn(T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.警告, null, data, pageInfo);
    }

    public static <T> Response<T> warn(String message, T data) {
        return new Response<>(ErrorCode.警告, message, data);
    }


    public static <T> Response<T> warn(String message, T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.警告, message, data, pageInfo);
    }

    public static <T> Response<T> error() {
        return new Response<>(ErrorCode.错误);
    }

    public static <T> Response<T> error(T data) {
        return new Response<>(ErrorCode.错误, null, data);
    }

    public static <T> Response<T> error(T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.错误, null, data, pageInfo);
    }

    public static <T> Response<T> error(String message, T data) {
        return new Response<>(ErrorCode.错误, message, data);
    }

    public static <T> Response<T> error(String message, T data, PageInfo pageInfo) {
        return new Response<>(ErrorCode.错误, message, data, pageInfo);
    }

    public boolean isSuccess() {
        return Objects.equals(ErrorCode.成功, code);
    }

    public boolean isWarn() {
        return Objects.equals(ErrorCode.警告, code);
    }

    public boolean isError() {
        return Objects.equals(ErrorCode.错误, code);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}
