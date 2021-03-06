package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

public final class DateTypeAdapters {
    private static final ZoneId Z_ZONE = ZoneId.of("Z");

    private DateTypeAdapters() { }

    public static class JSONZoneDateTimeTypeAdapter<T extends TemporalAccessor>
        implements JsonSerializer<T>, JsonDeserializer<T>{
        private final DateTimeFormatter formatter;
        private final Function<ZonedDateTime, T> fromZonedDateTimeParser;
        private final Function<T, ZonedDateTime> toZoneDateTimeParser;

        public JSONZoneDateTimeTypeAdapter(
                DateTimeFormatter formatter,
                Function<ZonedDateTime, T> fromZonedDateTimeParser,
                Function<T, ZonedDateTime> toZoneDateTimeParser) {
            this.formatter = formatter;
            this.fromZonedDateTimeParser = fromZonedDateTimeParser;
            this.toZoneDateTimeParser = toZoneDateTimeParser;
        }

        @Override
        public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            ZonedDateTime dateTime = ZonedDateTime.parse(jsonElement.getAsString(), formatter);

            return fromZonedDateTimeParser.apply(dateTime);
        }

        @Override
        public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
            ZonedDateTime dateTime = this.toZoneDateTimeParser.apply(t)
                    .withZoneSameInstant(Z_ZONE);
            return new JsonPrimitive(formatter.format(dateTime));
        }
    }


    public static class LocalDateTypeAdapter extends JSONZoneDateTimeTypeAdapter<LocalDate> {

        public LocalDateTypeAdapter(ZoneId zoneId, String pattern) {
            super(DateTimeFormatter.ofPattern(pattern),
                    DateTypeAdapters.convertZonedDateTimeToLocalDate(zoneId),
                    DateTypeAdapters.convertLocalDatetoZoneDateTime(zoneId));
        }
    }

    public static class LocalDateTimeTypeAdapter extends JSONZoneDateTimeTypeAdapter<LocalDateTime> {
        public LocalDateTimeTypeAdapter(ZoneId zoneId, String pattern) {
            super(DateTimeFormatter.ofPattern(pattern),
                    DateTypeAdapters.convertZonedDateTimeToLocalDateTime(zoneId),
                    DateTypeAdapters.convertLocalDateTimetoZoneDateTime(zoneId));
        }
    }

    public static class LocalTimeTypeAdapter
            implements JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
        private final DateTimeFormatter formatter;

        public LocalTimeTypeAdapter(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        @Override
        public LocalTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalTime.parse(jsonElement.getAsString(), formatter);
        }

        @Override
        public JsonElement serialize(LocalTime localDate, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(formatter.format(localDate));
        }
    }

    private static Function<LocalDateTime, ZonedDateTime> convertLocalDateTimetoZoneDateTime(
            ZoneId zoneId) {
        return localDateTime -> localDateTime.atZone(zoneId)
                .withZoneSameInstant(Z_ZONE);
    }

    private static Function<ZonedDateTime, LocalDateTime> convertZonedDateTimeToLocalDateTime(ZoneId zoneId) {
        return zonedDateTime -> zonedDateTime.withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }

    private static Function<LocalDate, ZonedDateTime> convertLocalDatetoZoneDateTime(ZoneId zoneId) {
        return localDate -> localDate.atStartOfDay(zoneId)
                .withZoneSameInstant(Z_ZONE);
    }

    private static Function<ZonedDateTime, LocalDate> convertZonedDateTimeToLocalDate(ZoneId zoneId) {
        return zonedDateTime -> zonedDateTime.withZoneSameInstant(zoneId)
                .toLocalDate();
    }
}
