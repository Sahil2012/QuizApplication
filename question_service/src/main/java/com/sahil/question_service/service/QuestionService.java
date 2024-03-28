package com.sahil.question_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sahil.question_service.dao.QuestionDao;
import com.sahil.question_service.model.Question;
import com.sahil.question_service.model.QuestionWrapper;
import com.sahil.question_service.model.Response;


@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionDao.findAll(); 
        return new ResponseEntity<List<Question>>(questions, HttpStatus.OK);   

    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        
        List<Question> questions = questionDao.findByCategory(category);
        return new ResponseEntity<List<Question>>(questions, HttpStatus.OK);   
    }

    public ResponseEntity<String> addQuestion(Question question) {
        Question quest = questionDao.save(question);
        return new ResponseEntity<>(quest.getId().toString(),HttpStatus.CREATED);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(categoryName,numQuestions);

        List<Integer> quizQuestions = new ArrayList<>();

        questions.forEach(question -> quizQuestions.add(question.getId()));

        return new ResponseEntity<>(quizQuestions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        // TODO Auto-generated method stub
        List<QuestionWrapper> questionWrappers = new ArrayList<>();

        for(int questionId : questionIds) {
            Question question = questionDao.findById(questionId).get();
            QuestionWrapper questionWrapper = new QuestionWrapper(question.getId(), question.getQuestionTitle(), question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4());
            questionWrappers.add(questionWrapper);
        }

        return new ResponseEntity<List<QuestionWrapper>>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        
        int score = 0;
        for(Response response : responses) {
            Question question = questionDao.findById(response.getId()).orElse(null);
            if(question != null && question.getRightAnswer().equals(response.getResponse())) 
                score ++;
        }

        return new ResponseEntity<>(score,HttpStatus.OK);
    }
    
}
