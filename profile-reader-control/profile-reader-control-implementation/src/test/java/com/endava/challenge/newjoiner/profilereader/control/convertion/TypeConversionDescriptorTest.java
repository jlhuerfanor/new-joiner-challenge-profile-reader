package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ConversionDescriptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeConversionDescriptorTest {

    private ConversionDescriptor descriptorA;
    private ConversionDescriptor descriptorB;
    private boolean areTheSame;
    private int hashOfA;
    private int hashOfB;

    @Test
    void testEqualsSuccess() {
        givenTwoTypeConversionDescriptorWithTheSameTypes();
        whenICheckEqualityBetweenBothTypes();
        thenBothDescriptorsAreTheSame();
    }

    @Test
    void equalDescriptorHaveTheSameHash() {
        givenTwoTypeConversionDescriptorWithTheSameTypes();
        whenIGetTheHashOfBothTypes();
        thenBothDescriptorsHaveTheSameHash();
    }

    @Test
    void typeDescriptorsAreNotTheSame() {
        givenTwoTypeConversionDescriptorWithDifferentTypes();
        whenICheckEqualityBetweenBothTypes();
        thenBothDescriptorsAreNotTheSame();
    }

    @Test
    void differentDescriptorsHaveNotTheSameHash() {
        givenTwoTypeConversionDescriptorWithDifferentTypes();
        whenIGetTheHashOfBothTypes();
        thenBothDescriptorsHaveNotTheSameHash();
    }

    private void thenBothDescriptorsHaveNotTheSameHash() {
        assertNotEquals(this.hashOfA, this.hashOfB);
    }

    private void whenIGetTheHashOfBothTypes() {
        this.hashOfA = this.descriptorA.hashCode();
        this.hashOfB = this.descriptorB.hashCode();
    }

    private void thenBothDescriptorsHaveTheSameHash() {
        assertEquals(this.hashOfA, this.hashOfB);
    }

    private void givenTwoTypeConversionDescriptorWithDifferentTypes() {
        this.descriptorA = TypeConversionDescriptor.from(Integer.class, Double.class);
        this.descriptorB = TypeConversionDescriptor.from(Integer.class, Long.class);
    }

    private void thenBothDescriptorsAreNotTheSame() {
        assertFalse(this.areTheSame);
    }

    private void givenTwoTypeConversionDescriptorWithTheSameTypes() {
        this.descriptorA = TypeConversionDescriptor.from(Integer.class, Double.class);
        this.descriptorB = TypeConversionDescriptor.from(Integer.class, Double.class);
    }

    private void whenICheckEqualityBetweenBothTypes() {
        this.areTheSame = descriptorA.equals(descriptorB);
    }

    private void thenBothDescriptorsAreTheSame() {
        assertTrue(this.areTheSame);
    }
}