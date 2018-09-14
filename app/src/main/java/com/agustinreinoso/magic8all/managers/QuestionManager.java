package com.agustinreinoso.magic8all.managers;

import android.graphics.Color;

import com.agustinreinoso.magic8all.models.FutureGuess;

import java.util.Random;

public class QuestionManager {
    private String[] answers = {"Yes - definitely",
            "Yes", "Cannot predict now","Try Asking again",
            "Outlook not so good"
            , "Very doubtful"
    };

    public  QuestionManager()
    {

    }

    public FutureGuess guessFuture()
    {
        int guess= (int) (0 + (answers.length - 0) * Math.random());

        FutureGuess futurePrediction= new FutureGuess();
        futurePrediction.setGuess(answers[guess]);

        if(guess<=1)
        {
            futurePrediction.setDangerLevel(Color.GREEN);
        }
        else if(guess<=3){
            futurePrediction.setDangerLevel(Color.YELLOW);
        }
        else
        {
            futurePrediction.setDangerLevel(Color.RED);

        }
        return  futurePrediction;
    }

}

