package org.camunda.bpm.run.stuff;

public class NaturalNumber<T extends CoreVariableInstance> {


    public NaturalNumber() {
    }

    class Hihi implements CoreVariableInstance {

    }

    public static void main(String[] args) {
        NaturalNumber<? extends CoreVariableInstance> myStore = new NaturalNumber<NaturalNumber.Hihi>();

    }
}