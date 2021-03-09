package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ConversionDescriptor;

import java.lang.reflect.Type;
import java.util.Objects;

public class TypeConversionDescriptor implements ConversionDescriptor {
    private final Type source;
    private final Type target;

    private TypeConversionDescriptor(Type source, Type target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeConversionDescriptor that = (TypeConversionDescriptor) o;
        return source.equals(that.source) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    public static ConversionDescriptor from(Type source, Type target) {
        return new TypeConversionDescriptor(source, target);
    }
}
