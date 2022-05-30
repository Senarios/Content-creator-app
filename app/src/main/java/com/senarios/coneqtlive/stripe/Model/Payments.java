package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Payments {

    @SerializedName("statement_descriptor")
    @Expose
    private Object statementDescriptor;
    @SerializedName("statement_descriptor_kana")
    @Expose
    private Object statementDescriptorKana;
    @SerializedName("statement_descriptor_kanji")
    @Expose
    private Object statementDescriptorKanji;

    public Object getStatementDescriptor() {
        return statementDescriptor;
    }

    public void setStatementDescriptor(Object statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

    public Object getStatementDescriptorKana() {
        return statementDescriptorKana;
    }

    public void setStatementDescriptorKana(Object statementDescriptorKana) {
        this.statementDescriptorKana = statementDescriptorKana;
    }

    public Object getStatementDescriptorKanji() {
        return statementDescriptorKanji;
    }

    public void setStatementDescriptorKanji(Object statementDescriptorKanji) {
        this.statementDescriptorKanji = statementDescriptorKanji;
    }

}
