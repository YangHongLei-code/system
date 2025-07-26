package com.example.system.action.util;


public class Result {
    private final int code;
    private final String msg;
    private final Object data;

    private Result(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }

    // 静态 builder() 方法
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    // Builder 类
    public static class Builder {
        private int code;
        private String msg;
        private Object data;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    // 使用示例
    public static void main(String[] args) {
        Result result = Result.builder()
                .code(200)
                .msg("操作成功")
                .data(new Object())
                .build();

        System.out.println(result.getCode());  // 输出: 200
        System.out.println(result.getMsg());   // 输出: "操作成功"
    }
}