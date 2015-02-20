package com.gogocosmo.cosmoqiu.fire_sticker.Model;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class Item {

    private String _question = null;
    private String _answer = null;

    public Item(String question, String answer) {
        _question = question;
        _answer = answer;
    }

    public void setQuestion(String _question) {
        this._question = _question;
    }

    public void setAnswer(String _answer) {
        this._answer = _answer;
    }

    public String getQuestion() {
        return _question;
    }

    public String getAnswer() {
        return _answer;
    }

    public String toString() {
        return "Question: " + _question + ", " + "Answer: " + _answer ;
    }
}
