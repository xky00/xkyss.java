package com.xkyss.mocky.contant;

public class MockConsts {
    public static final String DICT_PATH = "dicts";

    public static final String INPUT_PARAMETER_NOT_NULL = "Input parameter: '%s' should not be NULL.";

    public static final String LOWER_BOUND_BIGGER_THAN_ZERO = "The input parameter 'lowerBound' should be >= 0.0.";

    public static final String UPPER_BOUND_BIGGER_THAN_ZERO = "The input parameter 'upperBound' should be > 0.0.";

    public static final String UPPER_BOUND_BIGGER_LOWER_BOUND = "The input parameter 'upperBound' > 'lowerBound'.";

    public static final String SIZE_BIGGER_THAN_ZERO_STRICT = "The size needs to be bigger than 0 (>).";

    public static final String INPUT_PARAMETER_NOT_EMPTY_OR_NULL = "Input parameter: '{}' should not be empty or NULL.";

    public static final String IMPOSSIBLE_TO_SEQ_OVER_EMPTY_COLLECTION = "Impossible to create a Seq from an empty Iterable<T>.";

    public static final String SIZE_BIGGER_THAN_ZERO = "The size needs to be bigger than 0 (>=).";

    public static final String SEQ_INVALID_RANGE = "The min value '{}' should be lower than the maximum '{}' value of the sequence.";

    public static final String SEQ_OVERFLOW = "Seq overflow. Values are generated inside the interval: [{}, {}]. Cannot increment any further.";

    public static final String INVALID_REGEX_PATTERN = "Invalid regex pattern ('{}'): ";

    public static final String LOWER_DATE_SMALLER_THAN_UPPER_DATE = "lowerDate '{}' should be < than upperDate '{}'.";

    public static final String MAX_DATE_NOT_BIGGER_THAN = "maxDate '{}'  should be < than '{}' (LocalDates.MAX)";

    public static final String MIN_DATE_BIGGER_THAN = "minDate '{}' should be > than '{}' (LocalDate.MIN)";

    public static final String MAX_DATE_DIFFERENT_THAN_NOW = "maxDate '{}' should be > than now() ({}).";

    public static final String MIN_DATE_DIFFERENT_THAN_NOW = "minDate '{}' should be < than now() ({}).";

}
