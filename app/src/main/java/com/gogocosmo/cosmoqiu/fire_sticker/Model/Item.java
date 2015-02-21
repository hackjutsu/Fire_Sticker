package com.gogocosmo.cosmoqiu.fire_sticker.Model;

/**
 * Created by cosmoqiu on 1/29/15.
 */
public class Item {

    private String _question = null;
    private String _answer = null;
    private String _title = null;

    public Item(String question, String answer, String title) {
        _question = question;
        _answer = answer;
        _title = title;
    }

    public void setQuestion(String _question) {
        this._question = _question;
    }

    public void setAnswer(String _answer) {
        this._answer = _answer;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getQuestion() {
        return _question;
    }

    public String getAnswer() {
        return _answer;
    }

    public String getTitle() {
        return _title;
    }

    public String toString() {
        return "Title: " + _title + "," + "Question: " + _question + ", " + "Answer: " + _answer ;
    }
}
