package example.com.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MealName {
    BREAKFAST,
    LUNCH,
    DINNER
}
