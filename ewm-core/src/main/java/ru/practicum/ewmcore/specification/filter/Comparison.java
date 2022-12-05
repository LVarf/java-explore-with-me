package ru.practicum.ewmcore.specification.filter;

public enum Comparison {
    EQ, //equals
    EQ_IGNORE_CASE, //equals ignore case
    NE, //not equals
    BETWEEN, //between
    LIKE,
    NOT_LIKE,
    LIKE_IGNORE_CASE,
    NOT_LIKE_IGNORE_CASE,
    STARTS_WITH,
    ENDS_IN,
    FTS,
    GT, //>
    GE, //=>
    LT, //<
    LTN, //< or null
    LE, //<=
    IS_NULL;
}
