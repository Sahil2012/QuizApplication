package com.sahil.quiz_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sahil.quiz_service.dao.QuizDao;
import com.sahil.quiz_service.feign.QuestionClient;
import com.sahil.quiz_service.model.QuestionWrapper;
import com.sahil.quiz_service.model.Quiz;
import com.sahil.quiz_service.model.Response;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;


    @Autowired
    QuestionClient questionClient;

    public ResponseEntity<String> createQuiz(String categoryName, Integer numQuestions, String title) {
        
        List<Integer> questions = questionClient.getQuestionsForQuiz(categoryName, numQuestions).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        
        quizDao.save(quiz);

        return new ResponseEntity<>("SUCESS",HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
       
        Quiz quiz = quizDao.findById(id).get();

        List<QuestionWrapper> questionWrappers = questionClient.getQuestionsFromId(quiz.getQuestionIds()).getBody();

        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        
        int score = questionClient.getScore(responses).getBody();

        return new ResponseEntity<>(score,HttpStatus.OK);
    }
    
}
